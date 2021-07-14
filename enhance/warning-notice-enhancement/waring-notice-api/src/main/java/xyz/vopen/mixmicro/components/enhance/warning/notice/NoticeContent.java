package xyz.vopen.mixmicro.components.enhance.warning.notice;

import lombok.*;

import java.io.Serializable;

/**
 * {@link NoticeContent}
 *
 * <p>Class NoticeContent Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2021/7/14
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeContent implements Serializable {

  /**
   * Content Supported
   *
   * <p>
   */
  private String content;
}
