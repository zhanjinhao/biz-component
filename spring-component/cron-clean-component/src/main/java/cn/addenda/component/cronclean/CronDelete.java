package cn.addenda.component.cronclean;

import cn.addenda.component.jdk.util.collection.IterableUtils;
import cn.addenda.component.jdk.util.sql.ConnectionUtils;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDeleteStatement;

import java.sql.Connection;
import java.sql.Statement;
import javax.sql.DataSource;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.Assert;

/**
 * @author addenda
 * @since 2024/3/27 19:40
 */
@Slf4j
public class CronDelete extends CronClean implements InitializingBean {

  private static final String VALID_MESSAGE = "`deleteSql`只支持单表删除SQL，例[delete from t_footprints_test3 where id > 1000]，当前是[%s]！";

  private final DataSource dataSource;

  private final String deleteSql;

  private final Integer oneBatch;

  private String tableName;

  private ThreadPoolTaskScheduler threadPoolTaskScheduler;

  private boolean init = false;

  public CronDelete(DataSource dataSource, String deleteSql, Integer oneBatch, String cron) {
    super(cron);
    this.dataSource = dataSource;
    this.deleteSql = deleteSql;
    this.oneBatch = oneBatch;
    init();
  }

  private void init() {
    Assert.notNull(dataSource, "`dataSource` can not be null!");
    Assert.notNull(deleteSql, "`deleteSql` can not be null!");
    Assert.notNull(oneBatch, "`oneBatch` can not be null!");
    Assert.isTrue(oneBatch != 0, "`oneBatch` can not be 0!");
    SQLStatement sqlStatement = IterableUtils.oneOrNull(deleteSql, s -> SQLUtils.parseStatements(s, "mysql"));
    Assert.notNull(sqlStatement, String.format(VALID_MESSAGE, this.deleteSql));
    Assert.isTrue(sqlStatement instanceof MySqlDeleteStatement, String.format(VALID_MESSAGE, deleteSql));
    MySqlDeleteStatement mySqlDeleteStatement = (MySqlDeleteStatement) sqlStatement;
    SQLTableSource tableSource = mySqlDeleteStatement.getTableSource();
    Assert.isTrue(tableSource instanceof SQLExprTableSource, String.format(VALID_MESSAGE, deleteSql));
    SQLExprTableSource sqlExprTableSource = (SQLExprTableSource) tableSource;
    SQLExpr expr = sqlExprTableSource.getExpr();
    tableName = removeGrave(expr.toString());

    SQLTableSource from = mySqlDeleteStatement.getFrom();
    Assert.isNull(from, "deleteSql中不能存在from语法。");
    SQLExpr where = mySqlDeleteStatement.getWhere();
    Assert.notNull(where, "deleteSql中必须存在where语法。");
    SQLLimit limit = mySqlDeleteStatement.getLimit();
    Assert.isNull(limit, "deleteSql中不能存在limit语法。");
    SQLTableSource using = mySqlDeleteStatement.getUsing();
    Assert.isNull(using, "deleteSql中不能存在using语法。");

    log.info("tableName: {}", tableName);
    log.info("GENERATE_DELETE_SQL: {}", GENERATE_DELETE_SQL());
    log.info("cron description: {}", cronDescription());
  }

  private String GENERATE_DELETE_SQL() {
    String trimQuerySql = deleteSql.trim();
    if (trimQuerySql.endsWith(";")) {
      trimQuerySql = trimQuerySql.substring(0, trimQuerySql.length() - 1);
    }
    return trimQuerySql + " limit " + oneBatch;
  }

  @SneakyThrows
  private void delete() {
    try (Connection connection = dataSource.getConnection()) {
      boolean originalAutoCommit = ConnectionUtils.setAutoCommitFalse(connection);
      try {
        try (Statement statement = connection.createStatement()) {
          String generatedDeleteSql = GENERATE_DELETE_SQL();
          log.debug("clean {}, execute generatedDeleteSql: {}.", tableName, generatedDeleteSql);
          int i = statement.executeUpdate(generatedDeleteSql);
          log.debug("删除表[{}][{}]条数据。", tableName, i);
        }
        connection.commit();
      } finally {
        ConnectionUtils.setAutoCommitAndClose(connection, originalAutoCommit);
      }
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    cronClean();
  }

  @Override
  public void clean() {
    this.delete();
  }

  @Override
  public void cronClean() {
    if (!init) {
      log.info("删除表[{}]的定时任务开始启动！", tableName);
      init = true;
      threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
      threadPoolTaskScheduler.initialize();
      threadPoolTaskScheduler.schedule(() -> {
        try {
          delete();
        } catch (Exception e) {
          log.error("删除表[{}]数据的过程中，出现了异常！", tableName, e);
        }
      }, new CronTrigger(getCron()));
      log.info("删除表[{}]的定时任务启动成功！", tableName);
    } else {
      log.info("删除表[{}]的定时任务已启动，本次不再启动！", tableName);
    }
  }

  @Override
  public void close() {
    if (init && threadPoolTaskScheduler != null) {
      log.info("删除表[{}]的定时任务开始关闭！", tableName);
      threadPoolTaskScheduler.destroy();
      log.info("删除表[{}]的定时任务关闭成功！", tableName);
    }
  }
}
