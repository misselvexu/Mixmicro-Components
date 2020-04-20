package xyz.vopen.mixmicro.components.boot.web.aspect;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;
import xyz.vopen.mixmicro.components.common.SerializableBean;

import static com.alibaba.fastjson.serializer.SerializerFeature.*;

/**
 * {@link WebApiBean}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020-04-17.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebApiBean extends SerializableBean {

  private String url;

  private String method;

  private String className;

  private String methodName;

  private String methodDescription;

  /**
   * Remote Address
   *
   * <p>
   */
  private String remoteAddress;

  /**
   * Request Params Detail
   *
   * <p>
   */
  private String params;

  @JSONField(serialzeFeatures = {WriteNullListAsEmpty, WriteNullStringAsEmpty})
  private Object response;

  /**
   * Exception Cause
   *
   * <p>
   */
  private String exception;

  /**
   * Starting time
   *
   * <p>
   */
  private long startTime;

  /**
   * Endpoint time
   *
   * <p>
   */
  private long endTime;
}
