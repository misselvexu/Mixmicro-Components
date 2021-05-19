package xyz.vopen.mixmicro.components.common;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import xyz.vopen.mixmicro.kits.jackson.JacksonDateFormat;

import java.util.Date;

import static com.alibaba.fastjson.serializer.SerializerFeature.*;
import static xyz.vopen.mixmicro.components.common.MixmicroConstants.MIXMICRO_FULL_DATE_FORMATTER;

/**
 * {@link ExceptionResponse}
 *
 * <p>Class ExceptionResponse Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2021/5/19
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionResponse<T> extends SerializableBean {

  /**
   * Biz request process status code
   *
   * <p>default: -1
   */
  @Builder.Default private int code = -1;

  /**
   * Response message detail.
   *
   * <p>
   */
  @JSONField(serialzeFeatures = {WriteNullStringAsEmpty})
  @Builder.Default
  private String message = "";

  /**
   * Response body serialized content.
   *
   * <p>
   */
  @JSONField(
      serialzeFeatures = {
        WriteMapNullValue,
        WriteNullListAsEmpty,
        WriteNullNumberAsZero,
        WriteNullStringAsEmpty
      })
  private T data;

  /**
   * Response Entity return timestamp.
   *
   * <p>
   */
  @Builder.Default
  @JacksonDateFormat
  @JSONField(format = MIXMICRO_FULL_DATE_FORMATTER)
  private Date timestamp = new Date();
}
