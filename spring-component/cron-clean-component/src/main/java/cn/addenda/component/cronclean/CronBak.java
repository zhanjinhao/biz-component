package cn.addenda.component.cronclean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.sql.DataSource;

import cn.addenda.component.jdk.util.IterableUtils;
import cn.addenda.component.jdk.util.my.MyArrayUtils;
import cn.addenda.component.jdk.util.sql.ConnectionUtils;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * @author addenda
 * @since 2024/3/27 19:43
 */
@Slf4j
public class CronBak extends CronClean implements InitializingBean {

  private static final String VALID_MESSAGE = "`querySql`只支持单表查询SQL，例[select id, namespace, prefix from t_idempotence where expire_time < now()]，当前是[%s]！";

  private final DataSource dataSource;

  private final String querySql;

  // 拼装limit
  private final Integer oneBatch;

  private Set<String> primaryKeyColumnSet;

  private Set<String> columnSet;

  private String tableName;

  private final String bakTableName;

  private boolean init = false;

  private ThreadPoolTaskScheduler threadPoolTaskScheduler;

  public CronBak(DataSource dataSource, String querySql, Integer oneBatch, String primaryKeyColumn, String bakTableName, String cron) {
    this(dataSource, querySql, oneBatch, MyArrayUtils.asHashSet(primaryKeyColumn), bakTableName, cron);
    Assert.notNull(primaryKeyColumn, "`primaryKeyColumn` can not be null!");
  }

  public CronBak(DataSource dataSource, String querySql, Integer oneBatch, Set<String> primaryKeyColumnSet, String bakTableName, String cron) {
    super(cron);
    Assert.notNull(dataSource, "`dataSource` can not be null!");
    Assert.notNull(querySql, "`querySql` can not be null!");
    Assert.notNull(oneBatch, "`oneBatch` can not be null!");
    Assert.isTrue(oneBatch != 0, "`oneBatch` can not be 0!");
    Assert.isTrue(!CollectionUtils.isEmpty(primaryKeyColumnSet), "`primaryKeyColumnSet` can not be empty!");
    Assert.notNull(bakTableName, "`bakTableName` can not be null!");
    this.dataSource = dataSource;
    this.querySql = querySql;
    this.oneBatch = oneBatch;
    this.primaryKeyColumnSet = primaryKeyColumnSet;
    this.bakTableName = bakTableName;
    init();
  }

  private void init() {
    SQLStatement sqlStatement = IterableUtils.oneOrNull(querySql, s -> SQLUtils.parseStatements(s, "mysql"));
    Assert.notNull(sqlStatement, String.format(VALID_MESSAGE, querySql));
    Assert.isTrue(sqlStatement instanceof SQLSelectStatement, String.format(VALID_MESSAGE, querySql));
    SQLSelectStatement sqlSelectStatement = (SQLSelectStatement) sqlStatement;
    SQLSelect select = sqlSelectStatement.getSelect();
    SQLSelectQuery query = select.getQuery();
    Assert.isTrue(query instanceof SQLSelectQueryBlock, String.format(VALID_MESSAGE, querySql));
    MySqlSelectQueryBlock mySqlSelectQueryBlock = (MySqlSelectQueryBlock) query;
    List<SQLSelectItem> selectList = mySqlSelectQueryBlock.getSelectList();
    columnSet = selectList.stream()
            .map(a -> Optional.ofNullable(a.getAlias()).orElse(removeGrave(a.getExpr().toString())))
            .collect(Collectors.toCollection(LinkedHashSet::new));
    initPrimaryKey();
    SQLExpr where = mySqlSelectQueryBlock.getWhere();
    Assert.notNull(where, "querySql中必须存在where语法。");
    SQLLimit limit = mySqlSelectQueryBlock.getLimit();
    Assert.isNull(limit, "querySql中不能存在limit语法。");
    SQLSelectGroupByClause groupBy = mySqlSelectQueryBlock.getGroupBy();
    Assert.isNull(groupBy, "querySql中不能存在groupBy语法。");
    SQLExprTableSource into = mySqlSelectQueryBlock.getInto();
    Assert.isNull(into, "querySql中不能存在into语法。");
    Assert.isTrue(!mySqlSelectQueryBlock.isForUpdate(), "querySql中不能存在for update语法。");
    SQLExpr offset = mySqlSelectQueryBlock.getOffset();
    Assert.isNull(offset, "querySql中不能存在offset语法。");

    SQLTableSource from = mySqlSelectQueryBlock.getFrom();
    Assert.isTrue(from instanceof SQLExprTableSource, String.format(VALID_MESSAGE, querySql));
    SQLExprTableSource sqlExprTableSource = (SQLExprTableSource) from;
    SQLExpr expr = sqlExprTableSource.getExpr();
    tableName = removeGrave(expr.toString());

    log.info("tableName: {}", tableName);
    log.info("columnSet: {}", String.join(",", columnSet));
    log.info("primaryKeyColumnSet: {}", String.join(",", primaryKeyColumnSet));
    log.info("cron description: {}", cronDescription());
    log.info("GENERATE_QUERY_SQL: {}", GENERATE_QUERY_SQL());
    log.info("GENERATE_DELETE_SQL: {}", GENERATE_DELETE_SQL());
    log.info("GENERATE_SAVE_HIS_SQL: {}", GENERATE_SAVE_HIS_SQL());
  }

  private void initPrimaryKey() {
    // 移除`并转为小写
    for (String primaryKeyColumn : primaryKeyColumnSet) {
      Assert.notNull(primaryKeyColumn, "primaryKeyColumnSet中不能存在null！");
    }
    primaryKeyColumnSet = primaryKeyColumnSet.stream().map(this::removeGrave).collect(Collectors.toSet());
    for (String primaryKeyColumn : primaryKeyColumnSet) {
      Assert.isTrue(columnSet.contains(primaryKeyColumn),
              String.format("primaryKeyColumn[%s]在querySql[%s]中不存在！", primaryKeyColumn, querySql));
    }
  }

  private String GENERATE_DELETE_SQL() {
    StringBuilder sql = new StringBuilder();
    sql.append("delete from ").append(tableName).append(" where ");
    for (String primaryKeyColumn : primaryKeyColumnSet) {
      sql.append("`").append(primaryKeyColumn).append("`=? ,");
    }
    sql = new StringBuilder(sql.substring(0, sql.length() - 1));
    return sql.toString();
  }

  private String GENERATE_SAVE_HIS_SQL() {
    StringBuilder sql = new StringBuilder();
    sql.append("insert into ").append(bakTableName);
    sql.append("(");
    for (String column : columnSet) {
      sql.append("`").append(column).append("`,");
    }
    sql = new StringBuilder(sql.substring(0, sql.length() - 1));
    sql.append(") values (");
    for (int i = 0; i < columnSet.size(); i++) {
      sql.append(" ?,");
    }
    sql = new StringBuilder(sql.substring(0, sql.length() - 1));
    sql.append(")");
    return sql.toString();
  }

  private String GENERATE_QUERY_SQL() {
    String trimQuerySql = querySql.trim();
    if (trimQuerySql.endsWith(";")) {
      trimQuerySql = trimQuerySql.substring(0, trimQuerySql.length() - 1);
    }
    return trimQuerySql + " limit " + oneBatch;
  }

  @SneakyThrows
  private void bak() {
    try (Connection connection = dataSource.getConnection()) {
      boolean originalAutoCommit = ConnectionUtils.setAutoCommitFalse(connection);
      try {
        List<Map<String, Object>> expiredList = queryExpired(connection);
        if (!expiredList.isEmpty()) {
          int[] deleteResults = delete(connection, expiredList);
          saveHis(connection, expiredList, deleteResults);
        }
        connection.commit();
      } finally {
        ConnectionUtils.setAutoCommitAndClose(connection, originalAutoCommit);
      }
    }
  }

  @SneakyThrows
  private List<Map<String, Object>> queryExpired(Connection connection) {
    String generatedQuerySql = GENERATE_QUERY_SQL();
    log.debug("bak {}, execute generatedQuerySql: {}.", tableName, generatedQuerySql);
    try (PreparedStatement preparedStatement = connection.prepareStatement(generatedQuerySql)) {
      ResultSet resultSet = preparedStatement.executeQuery();
      List<Map<String, Object>> result = new ArrayList<>();
      while (resultSet.next()) {
        Map<String, Object> map = new HashMap<>();
        for (String column : columnSet) {
          map.put(column, resultSet.getObject(column));
        }
        result.add(map);
      }
      return result;
    }
  }

  @SneakyThrows
  private int[] delete(Connection connection, List<Map<String, Object>> expiredList) {
    String generatedDeleteSql = GENERATE_DELETE_SQL();
    log.debug("bak {}, execute generatedDeleteSql: {}.", tableName, generatedDeleteSql);
    try (PreparedStatement ps = connection.prepareStatement(generatedDeleteSql)) {
      for (Map<String, Object> expired : expiredList) {
        int i = 1;
        for (String primaryKeyColumn : primaryKeyColumnSet) {
          ps.setObject(i, expired.get(primaryKeyColumn));
          i++;
        }
        ps.addBatch();
      }
      return ps.executeBatch();
    }
  }

  @SneakyThrows
  private void saveHis(Connection connection, List<Map<String, Object>> expiredList, int[] deleteResults) {
    String generatedSaveHisSql = GENERATE_SAVE_HIS_SQL();
    log.debug("bak {}, execute generatedSaveHisSql: {}.", tableName, generatedSaveHisSql);
    try (PreparedStatement ps = connection.prepareStatement(generatedSaveHisSql)) {
      List<String> deleteKey = new ArrayList<>();
      for (int i = 0; i < expiredList.size(); i++) {
        int deleteResult = deleteResults[i];
        if (deleteResult == 1) {
          Map<String, Object> expired = expiredList.get(i);
          deleteKey.add(generateKey(expired));
          int j = 1;
          for (String column : columnSet) {
            ps.setObject(j, expired.get(column));
            j++;
          }
          ps.addBatch();
        } else if (deleteResult != 0) {
          // 正常情况下是不会进入这里的
          log.error("删除操作的结果异常，当前索引[{}]的结果是[{}]，主键集合[{}]，结果集合[{}]。", i, deleteResult,
                  expiredList.stream().map(this::generateKey).collect(Collectors.joining(",")),
                  Arrays.stream(deleteResults).mapToObj(String::valueOf).collect(Collectors.joining(",")));
        }
      }
      if (!CollectionUtils.isEmpty(deleteKey)) {
        ps.executeBatch();
        log.debug("备份表[{}]主键为[{}]的数据。", tableName, String.join(",", deleteKey));
      }
    }
  }

  private String generateKey(Map<String, Object> expired) {
    return primaryKeyColumnSet.stream()
            .map(expired::get)
            .map(String::valueOf)
            .collect(Collectors.joining(","));
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    // 作为Spring Bean存在时，自动开启cronClean()
    cronClean();
  }

  @Override
  public void clean() {
    this.bak();
  }

  @Override
  public synchronized void cronClean() {
    if (!init) {
      log.info("备份表[{}]至[{}]的定时任务开始启动！", tableName, bakTableName);
      init = true;
      threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
      threadPoolTaskScheduler.initialize();
      threadPoolTaskScheduler.schedule(() -> {
        try {
          this.bak();
        } catch (Exception e) {
          log.error("备份表[{}]至[{}]的过程中，出现了异常！", tableName, bakTableName, e);
        }
      }, new CronTrigger(getCron()));
      log.info("备份表[{}]至[{}]的定时任务启动成功！", tableName, bakTableName);
    } else {
      log.error("备份表[{}]至[{}]的定时任务已启动，本次不再启动！", tableName, bakTableName);
    }
  }

  @Override
  public void close() {
    if (init && threadPoolTaskScheduler != null) {
      log.info("备份表[{}]至[{}]的定时任务开始关闭！", tableName, bakTableName);
      threadPoolTaskScheduler.destroy();
      log.info("备份表[{}]至[{}]的定时任务关闭成功！", tableName, bakTableName);
    }
  }

}
