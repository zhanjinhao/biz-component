package cn.addenda.component.jdk.util.sql;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author addenda
 * @since 2022/4/28
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConnectionUtils {

  public static boolean setAutoCommitFalse(Connection connection) throws SQLException {
    boolean originalAutoCommit = connection.getAutoCommit();
    if (originalAutoCommit) {
      connection.setAutoCommit(false);
    }
    return originalAutoCommit;
  }

  /**
   * 此方法是配合{@link ConnectionUtils#setAutoCommitFalse(Connection)} 使用
   */
  public static void setAutoCommitAndClose(Connection connection, boolean originalAutoCommit) throws SQLException {
    if (connection != null) {
      if (originalAutoCommit) {
        connection.setAutoCommit(true);
      }
      connection.close();
    }
  }

  public static void close(AutoCloseable connection) {
    if (connection == null) {
      return;
    }
    try {
      connection.close();
    } catch (Exception e) {
      String msg = String.format("关闭 Connection 失败，connection: [%s]。", connection);
      log.error(msg, e);
      throw new ConnectionException(msg, e);
    }
  }

  public static Connection openConnection(DataSource dataSource) {
    try {
      return dataSource.getConnection();
    } catch (SQLException e) {
      String msg = String.format("无法从DataSource中获取connection，dataSource: [%s]。", dataSource);
      log.error(msg, e);
      throw new ConnectionException(msg, e);
    }
  }

  public static void rollback(Connection connection) {
    try {
      connection.rollback();
    } catch (SQLException e) {
      String msg = String.format("rollback 失败，connection: [%s]。", connection);
      log.error(msg, e);
      throw new ConnectionException(msg, e);
    }
  }

  public static void commit(Connection connection) {
    try {
      connection.commit();
    } catch (SQLException e) {
      String msg = String.format("commit 失败，connection: [%s]。", connection);
      log.error(msg, e);
      throw new ConnectionException(msg, e);
    }
  }

  /**
   * @return 此方法执行之前，事务的隔离级别
   */
  public static int setTransactionIsolation(Connection connection, int expect) {
    try {
      int transactionIsolation = connection.getTransactionIsolation();
      connection.setTransactionIsolation(expect);
      return transactionIsolation;
    } catch (SQLException e) {
      String msg = String.format("设置 connection [%s] 的 事务隔离级别为 [%s] 失败。", connection, expect);
      log.error(msg, e);
      throw new ConnectionException(msg, e);
    }
  }

}
