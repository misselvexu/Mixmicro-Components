package xyz.vopen.framework.logging.admin.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * logging report client service details mongodb mapping model
 *
 * @author tino
 * @version ${project.version}
 * @date 2020/9/8
 */
@Data
@Document(collection = "logging_service_details")
public class LogServiceDetailModel implements Serializable {
    /**
     * logging service details id
     */
    @Id
    private String id;
    /**
     * logging service details service id relation to ${spring.application.name} config
     */
    private String serviceId;
    /**
     * logging service details service ip
     */
    private String serviceIp;
    /**
     * logging service details service port
     */
    private Integer servicePort;
    /**
     * the last report time
     */
    private Long lastReportTime;
    /**
     * the first report time
     */
    private Long createTime;
}
