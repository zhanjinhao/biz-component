package cn.addenda.component.stacktrace;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author addenda
 * @since 2023/2/17 9:51
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StackTraceUtils {

  /**
   * 默认排除集合，首次使用时加载，见 {@link #getDefaultExcludeSet()}。
   */
  private static volatile Set<IdentifierMatcher> defaultExcludeSet;

  /**
   * 实际用于发现排除清单的类加载器（{@code defaultExcludeSet} 由它加载），见 {@link #validate()}。
   * <p>要先于 {@code defaultExcludeSet} 赋值：后者是 volatile 写，读线程看到非空集合时也能看到本字段。
   */
  private static volatile ClassLoader discoveryClassLoader;

  private static final String EXCLUDED_PATH = "META-INF/biz-component-stacktrace.excluded";

  private static final char BOM = 0xFEFF;

  /**
   * 注释行前缀。类名、方法名都取不到 {@code --}，所以用它做注释不会和标识冲突。
   */
  private static final String COMMENT_PREFIX = "--";

  /**
   * javac 为 lambda 方法体生成的名称前缀，形如 {@code lambda$test2$6}、{@code lambda$new$0}。
   * <p>这是三类合成方法里<b>唯一会出现在"直接调用方"位置</b>的：用户在 lambda 体里调用工具方法时，
   * 该 lambda 体就是最内层的候选帧，跳过它才能拿到外层方法。
   */
  private static final String LAMBDA_METHOD_PREFIX = "lambda$";

  /**
   * javac 为"内部类访问外部类私有成员"生成的合成访问器前缀，形如 {@code access$000}。
   * <p>它永远不会成为返回的帧：访问器下面才是调用它的方法，上面是被访问的私有成员（用户代码），
   * 两者都比它更靠内层。覆盖它只是为了 {@code ifExcludeSyntheticMethod} 名副其实，实际近乎空操作。
   */
  private static final String ACCESSOR_METHOD_PREFIX = "access$";

  /**
   * javac 为可序列化 lambda 生成的反序列化入口，方法名就是 {@code $deserializeLambda$}。
   */
  private static final String DESERIALIZE_LAMBDA_METHOD = "$deserializeLambda$";

  /**
   * 内置排除项：无论排除清单是否存在、是否可读、是否写得对，都要排除 {@link StackTraceUtils} 自己，
   * 否则 {@code getCallerInfo} 返回的是工具类的内部栈帧（例如 {@code StackTraceUtils#determineStackTraceElement}）。
   */
  private static final Set<IdentifierMatcher> BUILT_IN_EXCLUDE_SET = Collections.singleton(
          IdentifierMatcherFactory.classMatcher(StackTraceUtils.class));

  /**
   * 获取默认排除集合：内置排除项 + classpath 上所有排除清单。
   * <p>首次调用时加载，之后复用。加载失败（清单读不到、存在非法行）抛 {@link StackTraceException}，
   * 并且<b>不会缓存失败结果</b>：下次调用会重新加载并再次抛出，不会让类"坏掉"。
   * <p>因此运行期第一次调用 {@code getCallerInfo} 时如果配置有问题，得到的是消息完整的
   * {@link StackTraceException}，而不是 {@code ExceptionInInitializerError} /
   * {@code NoClassDefFoundError} 这种定位困难、且会让类永久不可用的失败。
   *
   * @return 默认排除集合
   * @throws StackTraceException 排除清单读不到或存在非法行时抛出
   */
  public static Set<IdentifierMatcher> getDefaultExcludeSet() {
    Set<IdentifierMatcher> excludeSet = defaultExcludeSet;
    if (excludeSet == null) {
      synchronized (StackTraceUtils.class) {
        excludeSet = defaultExcludeSet;
        if (excludeSet == null) {
          ClassLoader classLoader = resolveClassLoader();
          Set<IdentifierMatcher> loaded = Collections.unmodifiableSet(loadDefaultExcludeSet(classLoader));
          discoveryClassLoader = classLoader;
          defaultExcludeSet = excludeSet = loaded;
        }
      }
    }
    return excludeSet;
  }

  /**
   * 一行式启动校验：在服务启动阶段调用，排除清单有问题就直接中断启动。
   * <p>排除清单是首次使用时加载的，如果首次使用发生在运行期（例如某个请求里才创建线程工厂），
   * 配置错误就会拖到运行期才暴露。在 bootstrap 里调用本方法可以把暴露时机提前到启动阶段：
   * <pre>
   *   StackTraceUtils.validate();
   * </pre>
   * <p>校验通过后清单已缓存，后续调用不会重复读取；并把结果打印到 {@code System.out}：
   * 用的哪个类加载器发现的清单、当前生效的排除项有哪些。排查"清单没生效"时先看这几行——
   * 尤其 Spring Boot devtools 这种清单可能在子加载器里的场景。
   * <pre>
   *   StackTraceUtils exclude set. thisClassLoader:[...LaunchedURLClassLoader@...], discoveryClassLoader:[...RestartClassLoader@...], size:[2].
   *     ClassNameAndMethodNameMatcher{className='cn.addenda.myapp.MyService', methodName='doSomething'}
   *     ClassNameMatcher{className='cn.addenda.component.stacktrace.StackTraceUtils'}
   * </pre>
   * <p>每次调用都会输出一次；加载失败时不输出（先抛异常，避免打出与实际不符的内容）。
   *
   * @throws StackTraceException 排除清单读不到或存在非法行时抛出，消息里带文件、行号和非法内容
   */
  public static void validate() {
    Set<IdentifierMatcher> excludeSet = getDefaultExcludeSet();

    System.out.printf(
            "StackTraceUtils exclude set. thisClassLoader:[%s], discoveryClassLoader:[%s], size:[%d].%n",
            StackTraceUtils.class.getClassLoader(), discoveryClassLoader, excludeSet.size());
    for (String identifierMatcher : sortedToStrings(excludeSet)) {
      System.out.printf("  %s%n", identifierMatcher);
    }
  }

  /**
   * 排除集合的字符串形式，排序后返回：{@code HashSet} 的遍历顺序不稳定，排一下便于比对两次启动的输出。
   */
  private static List<String> sortedToStrings(Set<IdentifierMatcher> excludeSet) {
    List<String> identifierMatchers = new ArrayList<>(excludeSet.size());
    for (IdentifierMatcher identifierMatcher : excludeSet) {
      identifierMatchers.add(identifierMatcher.toString());
    }
    Collections.sort(identifierMatchers);
    return identifierMatchers;
  }

  /**
   * 选择用于发现排除清单的类加载器：优先当前线程的上下文类加载器（TCCL），退回到加载本类的加载器，
   * 再退回到系统类加载器。
   * <p><b>为什么优先 TCCL</b>：classloader 只能看到自己和祖先的资源，看不到子加载器的。
   * <ul>
   *   <li>Spring Boot fat jar：{@code BOOT-INF/classes} 和 {@code BOOT-INF/lib} 都在同一个
   *       {@code LaunchedURLClassLoader} 下，TCCL 与"加载本类的加载器"等价；</li>
   *   <li>devtools：应用自己的 classpath 在 {@code RestartClassLoader}（本类加载器的<b>子</b>加载器）里，
   *       只有 TCCL 才能看到应用自己声明的清单，用"加载本类的加载器"会静默漏掉它。</li>
   * </ul>
   * <p>与 Spring 的 {@code ClassUtils.getDefaultClassLoader()} 的策略一致。
   * <p>注意：TCCL 是线程相关的，而排除集合在首次加载后就缓存了，因此"首次调用发生在哪个线程"决定了用哪个
   * TCCL —— 这也是把 {@link #validate()} 放在主线程 bootstrap 里调用的另一个好处。
   */
  private static ClassLoader resolveClassLoader() {
    ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
    if (contextClassLoader != null) {
      return contextClassLoader;
    }
    ClassLoader classLoader = StackTraceUtils.class.getClassLoader();
    return classLoader != null ? classLoader : ClassLoader.getSystemClassLoader();
  }

  /**
   * 加载 classpath 上所有 {@code META-INF/biz-component-stacktrace.excluded}，并合并内置排除项。
   * <p><b>加载是严格的</b>：清单读不到、或者其中任何一行不是合法标识，都会抛出
   * {@link StackTraceException}（消息里带文件、行号、非法内容，cause 里带具体原因）。
   * 配置错误就是要中断启动，不允许静默降级成"少排除几条"。
   * <p>{@link #getDefaultExcludeSet()} 就是用它加载的；把它放到启动阶段调用（见 {@link #validate()}），
   * 可以让配置错误在启动时暴露，而不是拖到运行期第一次 {@code getCallerInfo}。
   * <p>（历史行为：异常只有一句 "Can not open file"，没有行号也没有非法内容，定位基本靠猜。）
   *
   * @param classLoader 用于查找排除清单的类加载器，不能为 null
   * @return 内置排除项 + 所有排除清单的并集
   * @throws StackTraceException 清单无法读取或存在非法行时抛出
   */
  public static Set<IdentifierMatcher> loadDefaultExcludeSet(ClassLoader classLoader) {
    Set<IdentifierMatcher> identifierMatchers = new HashSet<>(BUILT_IN_EXCLUDE_SET);

    Enumeration<URL> urls;
    try {
      urls = classLoader.getResources(EXCLUDED_PATH);
    } catch (IOException e) {
      throw new StackTraceException(String.format("Can not list exclude files: [%s].", EXCLUDED_PATH), e);
    }

    while (urls.hasMoreElements()) {
      URL url = urls.nextElement();
      try (InputStream inputStream = openInputStream(url)) {
        identifierMatchers.addAll(readExcludeSet(inputStream, url.toString()));
      } catch (IOException e) {
        throw new StackTraceException(String.format("Can not read exclude file: [%s].", url), e);
      }
    }
    return identifierMatchers;
  }

  private static InputStream openInputStream(URL url) throws IOException {
    URLConnection urlConnection = url.openConnection();
    urlConnection.setUseCaches(false);
    return urlConnection.getInputStream();
  }

  /**
   * 读取排除清单，一行一个标识（格式见 {@link IdentifierMatcherFactory#getIdentifierMatcher(String)}）。
   * <p>下面是排除清单的示例，{@code --} 开头的行为注释：
   * <pre>
   *   -- 跳过 NamedFunction 内部的栈帧，让调用方拿到业务方法
   *   #cn.addenda.component.common.lambda.named.NamedFunction
   *   #cn.addenda.component.common.util.RetryUtils#retry
   * </pre>
   * <p>按 UTF-8 解码，并且会做归一化处理，避免生成"永远匹配不上"的 matcher：
   * <ul>
   *   <li>剔除行首的 UTF-8 BOM（U+FEFF），例如 Windows 记事本另存 UTF-8 时会写入 BOM；</li>
   *   <li>剔除行首行尾的空白字符，空行（含纯空白行）直接跳过；</li>
   *   <li>{@code --} 开头的行是注释，直接跳过。</li>
   * </ul>
   * <p>非法行（例如 {@code #}、{@code ##}、{@code #a#}、{@code #a#b#c}）会直接抛出
   * {@link StackTraceException}，消息里带行号和非法内容：配置错误要中断启动，不能静默跳过。
   * <p>注意：本方法不关闭入参 {@code inputStream}，由调用方负责关闭。
   *
   * @param inputStream 排除清单输入流，不能为 null
   * @return 排除清单对应的 matcher 集合，注释行与空行不在其中
   * @throws IOException         读取流失败时抛出
   * @throws StackTraceException 存在非法行时抛出
   */
  public static Set<IdentifierMatcher> readExcludeSet(InputStream inputStream) throws IOException {
    return readExcludeSet(inputStream, EXCLUDED_PATH);
  }

  static Set<IdentifierMatcher> readExcludeSet(InputStream inputStream, String source) throws IOException {
    Set<IdentifierMatcher> identifierMatchers = new HashSet<>();
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    String line;
    int lineNumber = 0;
    while ((line = bufferedReader.readLine()) != null) {
      lineNumber++;
      String identifier = normalize(line);
      if (identifier.isEmpty() || identifier.startsWith(COMMENT_PREFIX)) {
        continue;
      }
      try {
        identifierMatchers.add(IdentifierMatcherFactory.getIdentifierMatcher(identifier));
      } catch (Exception e) {
        throw new StackTraceException(String.format(
                "Invalid identifier: [%s], at line %d of [%s].", identifier, lineNumber, source), e);
      }
    }
    return identifierMatchers;
  }

  private static String normalize(String line) {
    String identifier = line.trim();
    if (!identifier.isEmpty() && identifier.charAt(0) == BOM) {
      identifier = identifier.substring(1).trim();
    }
    return identifier;
  }

  /**
   * 获取调用者信息，返回格式：{@code ClassName#MethodName}
   * <p>如果 {@code useSimpleClassName} 为 {@code true}，则类名不含包名，只输出简写的类名。
   *
   * @param useSimpleClassName             true 表示简写类名（仅类名，不含包路径），false 表示全限定名
   * @param ifExcludeSyntheticMethod       true 则跳过 javac 生成的合成方法栈帧
   *                                       （lambda 方法体 {@code lambda$foo$0}、合成访问器 {@code access$000}、
   *                                       {@code $deserializeLambda$}，见 {@link #isSyntheticMethodName(String)}）
   * @param ifExcludeAnonymousOrLocalClass true 则跳过匿名内部类<b>和局部类</b>的栈帧
   *                                       （两者都是编译器生成的类名：{@code Outer$1} / {@code Outer$1Local}）
   * @param excludes                       额外需要排除的类/方法标识（格式见 {@link IdentifierMatcherFactory}）
   * @return 调用者信息，格式：{@code ClassName#MethodName}
   * @see #getDetailedCallerInfo(boolean, boolean, boolean, String...)
   */
  public static String getCallerInfo(
          boolean useSimpleClassName, boolean ifExcludeSyntheticMethod, boolean ifExcludeAnonymousOrLocalClass, String... excludes) {
    StackTraceElement stackTraceElement = determineStackTraceElement(ifExcludeSyntheticMethod, ifExcludeAnonymousOrLocalClass, excludes);
    String className = stackTraceElement.getClassName();
    String methodName = stackTraceElement.getMethodName();
    if (useSimpleClassName) {
      className = extractSimpleClassName(className);
    }
    return className + "#" + methodName;
  }

  public static String getCallerInfo(boolean useSimpleClassName, boolean ifExcludeSyntheticMethod, boolean ifExcludeAnonymousOrLocalClass) {
    return getCallerInfo(useSimpleClassName, ifExcludeSyntheticMethod, ifExcludeAnonymousOrLocalClass, (String[]) null);
  }

  public static String getCallerInfo() {
    return getCallerInfo(true, false, false);
  }

  public static String getCallerInfo(String... excludes) {
    return getCallerInfo(true, false, false, excludes);
  }

  /**
   * 获取详细的调用者信息，返回格式：{@code ClassName#MethodName of FileName:LineNumber}
   * <p>相比 {@link #getCallerInfo}，额外包含文件名和行号，可精确到代码行。
   * <p>如果 {@code useSimpleClassName} 为 {@code true}，则类名不含包名，只输出简写的类名。
   *
   * @param useSimpleClassName             true 表示简写类名（仅类名，不含包路径），false 表示全限定名
   * @param ifExcludeSyntheticMethod       true 则跳过 javac 生成的合成方法栈帧
   *                                       （lambda 方法体 {@code lambda$foo$0}、合成访问器 {@code access$000}、
   *                                       {@code $deserializeLambda$}，见 {@link #isSyntheticMethodName(String)}）
   * @param ifExcludeAnonymousOrLocalClass true 则跳过匿名内部类<b>和局部类</b>的栈帧
   *                                       （两者都是编译器生成的类名：{@code Outer$1} / {@code Outer$1Local}）
   * @param excludes                       额外需要排除的类/方法标识（格式见 {@link IdentifierMatcherFactory}）
   * @return 详细的调用者信息，格式：{@code ClassName#MethodName of FileName:LineNumber}
   * @see #getCallerInfo(boolean, boolean, boolean, String...)
   */
  public static String getDetailedCallerInfo(
          boolean useSimpleClassName, boolean ifExcludeSyntheticMethod, boolean ifExcludeAnonymousOrLocalClass, String... excludes) {
    StackTraceElement stackTraceElement = determineStackTraceElement(ifExcludeSyntheticMethod, ifExcludeAnonymousOrLocalClass, excludes);
    String className = stackTraceElement.getClassName();
    String fileName = stackTraceElement.getFileName();
    int lineNumber = stackTraceElement.getLineNumber();
    String methodName = stackTraceElement.getMethodName();
    if (useSimpleClassName) {
      className = extractSimpleClassName(className);
    }
    // 热路径，别用 String.format：实测比拼接贵约 400ns/次（占整个调用约 13%）
    return className + "#" + methodName + " of " + fileName + ":" + lineNumber;
  }

  public static String getDetailedCallerInfo(
          boolean useSimpleClassName, boolean ifExcludeSyntheticMethod, boolean ifExcludeAnonymousOrLocalClass) {
    return getDetailedCallerInfo(useSimpleClassName, ifExcludeSyntheticMethod, ifExcludeAnonymousOrLocalClass, (String[]) null);
  }

  public static String getDetailedCallerInfo() {
    return getDetailedCallerInfo(true, false, false);
  }

  public static String getDetailedCallerInfo(String... excludes) {
    return getDetailedCallerInfo(true, false, false, excludes);
  }

  private static StackTraceElement determineStackTraceElement(
          boolean ifExcludeSyntheticMethod, boolean ifExcludeAnonymousOrLocalClass, String... excludes) {
    Set<IdentifierMatcher> excludeSet = getDefaultExcludeSet();
    // 空数组（getCallerInfo() 直接调 varargs 重载时会走到）不必拷贝默认集合
    if (excludes != null && excludes.length > 0) {
      excludeSet = new HashSet<>(excludeSet);
      for (String exclude : excludes) {
        if (exclude != null) {
          excludeSet.add(IdentifierMatcherFactory.getIdentifierMatcher(exclude));
        }
      }
    }
    return determineStackTraceElement(new Throwable().getStackTrace(), excludeSet, ifExcludeSyntheticMethod, ifExcludeAnonymousOrLocalClass);
  }

  private static StackTraceElement determineStackTraceElement(
          StackTraceElement[] stackTraceElements, Set<IdentifierMatcher> excludeSet,
          boolean ifExcludeSyntheticMethod, boolean ifExcludeAnonymousOrLocalClass) {
    if (stackTraceElements == null || stackTraceElements.length == 0) {
      throw new IllegalArgumentException("The arg `stackTraceElements` can not null and can not be empty.");
    }
    for (StackTraceElement stackTraceElement : stackTraceElements) {
      String methodName = stackTraceElement.getMethodName();
      if (ifExcludeSyntheticMethod && isSyntheticMethodName(methodName)) {
        continue;
      }
      String className = stackTraceElement.getClassName();
      if (ifExcludeAnonymousOrLocalClass && isAnonymousOrLocalClass(className)) {
        continue;
      }
      boolean flag = IdentifierMatcherFactory.match(excludeSet, stackTraceElement);
      if (!flag) {
        return stackTraceElement;
      }
    }
    String a = "All elements of `stackTraceElements` are excluded. The `stackTraceElements` are [%s]. The `excludeSet` are [%s].";
    throw new StackTraceException(String.format(a, toString(stackTraceElements), toString(excludeSet)));
  }

  private static String toString(StackTraceElement[] stackTraceElements) {
    StringBuilder sb = new StringBuilder("[");
    for (int i = 0; i < stackTraceElements.length; i++) {
      if (i > 0) {
        sb.append(",");
      }
      sb.append(stackTraceElements[i].toString());
    }
    sb.append("]");
    return sb.toString();
  }

  private static String toString(Set<IdentifierMatcher> excludeSet) {
    StringBuilder sb = new StringBuilder("[");
    boolean first = true;
    for (IdentifierMatcher matcher : excludeSet) {
      if (first) {
        first = false;
      } else {
        sb.append(",");
      }
      sb.append(matcher.toString());
    }
    sb.append("]");
    return sb.toString();
  }

  private static String extractSimpleClassName(String className) {
    return className.substring(className.lastIndexOf('.') + 1);
  }

  /**
   * 判断方法名是不是 javac 生成的合成方法。合成方法的栈帧对"谁调用了工具方法"没有信息量，应当跳过。
   * <p>覆盖三类：
   * <ul>
   *   <li>{@code lambda$<外层方法名>$<序号>} —— lambda 方法体，如 {@code lambda$test2$6}；</li>
   *   <li>{@code access$<数字>} —— 内部类访问外部类私有成员的合成访问器，如 {@code access$000}；</li>
   *   <li>{@code $deserializeLambda$} —— 可序列化 lambda 的反序列化入口。</li>
   * </ul>
   * 判定按 javac 的命名规则来，而不是简单的包含关系：{@code $} 是合法的 Java 标识符字符，
   * 用户完全可以写出 {@code notlambda$method}、{@code access$control} 这类方法名，它们必须按正常方法对待。
   *
   * @param methodName 栈帧的方法名
   * @return javac 生成的合成方法返回 true
   */
  public static boolean isSyntheticMethodName(String methodName) {
    if (methodName.startsWith(LAMBDA_METHOD_PREFIX) || methodName.equals(DESERIALIZE_LAMBDA_METHOD)) {
      return true;
    }
    // access$000：前缀后面必须至少有一位数字且全是数字，避免误伤 access$、access$control 这类用户方法
    if (methodName.startsWith(ACCESSOR_METHOD_PREFIX)) {
      int digitsStart = ACCESSOR_METHOD_PREFIX.length();
      if (digitsStart == methodName.length()) {
        return false;
      }
      for (int i = digitsStart; i < methodName.length(); i++) {
        if (!Character.isDigit(methodName.charAt(i))) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  /**
   * 判断这个类名是不是编译器为"局部类 / 匿名类"生成的。
   * <p>javac 的命名规则：类名里出现 {@code $} + 数字，这一段的数字是编译器给的序号。
   * <ul>
   *   <li>匿名类：{@code Outer$1}；</li>
   *   <li>局部类（方法体里声明的类）：{@code Outer$1Local}，数字后面才是类名；</li>
   *   <li>具名内部类：{@code Outer$Inner}，不含数字，<b>不算</b>。</li>
   * </ul>
   * 所以本方法判定的是"局部类 + 匿名类"这个集合，{@code ifExcludeAnonymousOrLocalClass} 用的就是这个语义：
   * 两者都是编译器生成的类名，它们的栈帧对"谁调用了工具方法"没有信息量，调用方真正想要的是外层方法。
   */
  private static boolean isAnonymousOrLocalClass(String className) {
    for (int i = 0; i < className.length() - 1; i++) {
      if (className.charAt(i) == '$' && Character.isDigit(className.charAt(i + 1))) {
        return true;
      }
    }
    return false;
  }

}
