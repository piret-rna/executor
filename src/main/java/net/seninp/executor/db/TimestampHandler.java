package net.seninp.executor.db;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * handles the LONG-to-DATETIME conversion.
 * 
 * @author psenin
 *
 */
public class TimestampHandler extends BaseTypeHandler<Long> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, Long parameter, JdbcType jdbcType)
      throws SQLException {
    Timestamp t = new Timestamp(parameter);
    ps.setTimestamp(i, t);

  }

  @Override
  public Long getNullableResult(ResultSet rs, String columnName) throws SQLException {
    Timestamp timestamp = rs.getTimestamp(columnName);
    if (null == timestamp) {
      return null;
    }
    return timestamp.getTime();
  }

  @Override
  public Long getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    Timestamp timestamp = rs.getTimestamp(columnIndex);
    if (null == timestamp) {
      return null;
    }
    return timestamp.getTime();
  }

  @Override
  public Long getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    Timestamp timestamp = cs.getTimestamp(columnIndex);
    if (null == timestamp) {
      return null;
    }
    return timestamp.getTime();
  }

}
