package xyz.vopen.mixmicro.components.authorization;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * {@link Payload}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020-04-10.
 */
@Getter
@Setter
public class Payload implements Serializable {

  // DEFAULT CONSTRUCTOR

  public Payload() {}

  public Payload(String userId, String mobile) {
    this.userId = userId;
    this.mobile = mobile;
  }

  private String userId;

  private String mobile;



}
