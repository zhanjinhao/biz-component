package cn.addenda.component.basemybatis.typehandler;

import cn.addenda.component.jdk.util.my.MyDateUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.springframework.util.Assert;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * 时间戳 《--》 LocalDateTime
 *
 * @author addenda
 * @since 2022/2/5 12:33
 */
@MappedJdbcTypes(value = JdbcType.BIGINT)
public class TsLocalDateTimeHandler extends BaseTypeHandler<LocalDateTime> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType) throws SQLException {
    Assert.notNull(parameter, "unexpected exception!");
    ps.setLong(i, MyDateUtils.localDateTimeToTimestamp(parameter));
  }

  @Override
  public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
    long time = rs.getLong(columnName);
    return MyDateUtils.timestampToLocalDateTime(time);
  }

  @Override
  public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    long time = rs.getLong(columnIndex);
    return MyDateUtils.timestampToLocalDateTime(time);
  }

  @Override
  public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    long time = cs.getLong(columnIndex);
    return MyDateUtils.timestampToLocalDateTime(time);
  }

}
