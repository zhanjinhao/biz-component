package cn.addenda.component.idgenerator.idgenerator.snowflake;

import cn.addenda.component.jdk.util.sql.ConnectionUtils;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static java.sql.Connection.TRANSACTION_SERIALIZABLE;

/**
 * @author addenda
 * @since 2023/6/4 19:25
 */
public class DbWorkerIdGenerator implements SnowflakeWorkerIdGenerator {

  private final DataSource dataSource;

  private final String appName;

  public DbWorkerIdGenerator(String appName, DataSource dataSource) {
    this.appName = appName;
    this.dataSource = dataSource;
  }

  @Override
  @SneakyThrows
  public long workerId() {
    Connection connection = dataSource.getConnection();
    boolean originalAutoCommit = false;
    int originalTi = -1;
    try {
      originalAutoCommit = ConnectionUtils.setAutoCommitFalse(connection);
      originalTi = ConnectionUtils.setTransactionIsolation(connection, TRANSACTION_SERIALIZABLE);

      boolean b = checkAppNameExists(connection);
      if (!b) {
        insertAppName(connection);
      }

      long workerId = queryNextId(connection);
      incrementNextId(connection, workerId);
      connection.commit();
      return (workerId % (1 << SnowflakeIdGenerator.WORKER_ID_BITS)) - 1L;
    } finally {
      ConnectionUtils.setTransactionIsolation(connection, originalTi);
      ConnectionUtils.setAutoCommitAndClose(connection, originalAutoCommit);
    }
  }

  private static final String APP_NAME_COUNT_SQL = "select count(*) as count from t_snow_flake_worker_id where app_name = ? for update";

  @SneakyThrows
  private boolean checkAppNameExists(Connection connection) {
    PreparedStatement preparedStatement = connection.prepareStatement(APP_NAME_COUNT_SQL);
    try {
      preparedStatement.setString(1, appName);
      ResultSet resultSet = preparedStatement.executeQuery();
      long count = -1;
      while (resultSet.next()) {
        count = resultSet.getLong("count");
      }
      return count == 1;
    } finally {
      ConnectionUtils.close(preparedStatement);
    }
  }

  private static final String INSERT_APP_NAME_SQL = "insert into t_snow_flake_worker_id(next_id, app_name) values(1, ?)";

  @SneakyThrows
  private void insertAppName(Connection connection) {
    PreparedStatement preparedStatement = connection.prepareStatement(INSERT_APP_NAME_SQL);
    try {
      preparedStatement.setString(1, appName);
      preparedStatement.executeUpdate();
    } finally {
      ConnectionUtils.close(preparedStatement);
    }
  }

  private static final String QUERY_NEXT_ID_SQL = "select next_id from t_snow_flake_worker_id where app_name = ? for update";

  @SneakyThrows
  private long queryNextId(Connection connection) {
    PreparedStatement preparedStatement = connection.prepareStatement(QUERY_NEXT_ID_SQL);
    try {
      preparedStatement.setString(1, appName);
      ResultSet resultSet = preparedStatement.executeQuery();
      long nextId = -1;
      while (resultSet.next()) {
        nextId = resultSet.getLong("next_id");
      }
      return nextId;
    } finally {
      ConnectionUtils.close(preparedStatement);
    }
  }

  private static final String INCREMENT_NEXT_ID_SQL = "update t_snow_flake_worker_id set next_id = ? where app_name = ?";

  @SneakyThrows
  private void incrementNextId(Connection connection, long curId) {
    PreparedStatement preparedStatement = connection.prepareStatement(INCREMENT_NEXT_ID_SQL);
    try {
      preparedStatement.setLong(1, curId + 1);
      preparedStatement.setString(2, appName);
      preparedStatement.executeUpdate();
    } finally {
      ConnectionUtils.close(preparedStatement);
    }
  }

}
