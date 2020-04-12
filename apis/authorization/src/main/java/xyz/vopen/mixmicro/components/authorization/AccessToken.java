package xyz.vopen.mixmicro.components.authorization;

import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.io.Serializable;

import static xyz.vopen.mixmicro.components.authorization.Constants.DEFAULT_PERIOD_OF_VALIDITY_MS;

/**
 * {@link AccessToken}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020-04-10.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class AccessToken implements Serializable {

  public AccessToken() {}

  /**
   * Access Token Unique Id
   *
   * <p>
   */
  @SerializedName("atuid")
  private String tokenUniqueId;

  /**
   * Token {@link Payload} data
   *
   * <p>
   */
  @SerializedName("p")
  private byte[] payload;

  /**
   * Access Token Generated Timestamp (ms)
   *
   * <p>
   */
  @SerializedName("gt")
  private long generateTimestamp = System.currentTimeMillis();

  /**
   * Access Token Expired Timestamp (ms)
   *
   * <p>default :{@link #generateTimestamp} + {@link Constants#DEFAULT_PERIOD_OF_VALIDITY_MS}
   */
  @SerializedName("et")
  private long expiredTimestamp = this.generateTimestamp + DEFAULT_PERIOD_OF_VALIDITY_MS;
}
