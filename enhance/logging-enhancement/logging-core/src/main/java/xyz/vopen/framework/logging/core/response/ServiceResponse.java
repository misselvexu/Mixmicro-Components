package xyz.vopen.framework.logging.core.response;

import lombok.Data;

import java.sql.Timestamp;

/**
 * logging service response
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Data
public class ServiceResponse {
  /** service application name service id */
  private String id;
  /** service ip address */
  private String ip;
  /** service port */
  private Integer port;
  /** last report time */
  private Timestamp lastReportTime;
  /** first report time */
  private Timestamp createTime;
}
