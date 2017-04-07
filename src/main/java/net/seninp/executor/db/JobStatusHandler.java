package net.seninp.executor.db;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import net.seninp.executor.resource.JobCompletionStatus;

public class JobStatusHandler extends BaseTypeHandler<JobCompletionStatus> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, JobCompletionStatus parameter,
      JdbcType jdbcType) throws SQLException {
    String status = parameter.toString();
    ps.setString(i, status);
  }

  @Override
  public JobCompletionStatus getNullableResult(ResultSet rs, String columnName)
      throws SQLException {
    String status = rs.getString(columnName);
    if (null == status) {
      return null;
    }
    return JobCompletionStatus.fromValue(status);
  }

  @Override
  public JobCompletionStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    String status = rs.getString(columnIndex);
    if (null == status) {
      return null;
    }
    return JobCompletionStatus.fromValue(status);
  }

  @Override
  public JobCompletionStatus getNullableResult(CallableStatement cs, int columnIndex)
      throws SQLException {
    String status = cs.getString(columnIndex);
    if (null == status) {
      return null;
    }
    return JobCompletionStatus.fromValue(status);
  }

}
