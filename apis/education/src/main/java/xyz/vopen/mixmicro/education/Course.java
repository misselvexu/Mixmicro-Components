package xyz.vopen.mixmicro.education;

import lombok.Builder;
import lombok.Data;

/**
 * {@link Course}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020-04-18.
 */
@Data
@Builder
public class Course {

  private String name;

  private long time;

  private String timeString;

  private String itemId;
}
