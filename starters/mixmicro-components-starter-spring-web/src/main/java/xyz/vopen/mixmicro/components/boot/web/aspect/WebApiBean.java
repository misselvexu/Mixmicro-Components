package xyz.vopen.mixmicro.components.boot.web.aspect;

import lombok.*;
import xyz.vopen.mixmicro.components.common.SerializableBean;

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

  private String methodDescription;
  private String url;
  private String method;
  private String className;
  private String methodName;
  private String remoteAddress;
  private String params;
  private Object response;
  private String exception;
  private long startTime;
  private long endTime;

}
