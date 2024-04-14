package cn.addenda.component.jaxrsfeign;

import feign.Request;
import feign.RequestTemplate;
import feign.Target;

import static feign.Util.checkNotNull;

/**
 * copy from HardCodedTarget. remove some validation.
 *
 * @author addenda
 * @since 2023/7/24 14:24
 */
public class SimpleHardCodedTarget<T> implements Target<T> {

  private final Class<T> type;
  private final String name;
  private final String url;

  public SimpleHardCodedTarget(Class<T> type, String url) {
    this(type, url, url);
  }

  public SimpleHardCodedTarget(Class<T> type, String name, String url) {
    this.type = checkNotNull(type, "type");
    this.name = name;
    this.url = url;
  }

  @Override
  public Class<T> type() {
    return type;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public String url() {
    return url;
  }

  /* no authentication or other special activity. just insert the url. */
  @Override
  public Request apply(RequestTemplate input) {
    if (input.url().indexOf("http") != 0) {
      input.target(url());
    }
    return input.request();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof SimpleHardCodedTarget) {
      SimpleHardCodedTarget<?> other = (SimpleHardCodedTarget) obj;
      return type.equals(other.type)
              && name.equals(other.name)
              && url.equals(other.url);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + type.hashCode();
    result = 31 * result + name.hashCode();
    result = 31 * result + url.hashCode();
    return result;
  }

  @Override
  public String toString() {
    if (name.equals(url)) {
      return "MyHardCodedTarget(type=" + type.getSimpleName() + ", url=" + url + ")";
    }
    return "MyHardCodedTarget(type=" + type.getSimpleName() + ", name=" + name + ", url=" + url
            + ")";
  }
}
