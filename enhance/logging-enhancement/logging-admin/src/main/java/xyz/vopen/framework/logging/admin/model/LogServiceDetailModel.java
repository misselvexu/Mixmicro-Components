package xyz.vopen.framework.logging.admin.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * {@link LogServiceDetailModel} log service detail model
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2020/9/8
 */
@Document(collection = "logging_service_details")
public class LogServiceDetailModel implements Serializable {
  /** logging service details id */
  @Id private String id;
  /** logging service details service id relation to ${spring.application.name} config */
  private String serviceId;
  /** logging service details service ip */
  private String serviceIp;
  /** logging service details service port */
  private Integer servicePort;
  /** the last report time */
  private Long lastReportTime;
  /** the first report time */
  private Long createTime;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getServiceId() {
    return serviceId;
  }

  public void setServiceId(String serviceId) {
    this.serviceId = serviceId;
  }

  public String getServiceIp() {
    return serviceIp;
  }

  public void setServiceIp(String serviceIp) {
    this.serviceIp = serviceIp;
  }

  public Integer getServicePort() {
    return servicePort;
  }

  public void setServicePort(Integer servicePort) {
    this.servicePort = servicePort;
  }

  public Long getLastReportTime() {
    return lastReportTime;
  }

  public void setLastReportTime(Long lastReportTime) {
    this.lastReportTime = lastReportTime;
  }

  public Long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Long createTime) {
    this.createTime = createTime;
  }
}
