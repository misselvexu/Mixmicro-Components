package xyz.vopen.mixmicro.components.authorization;

import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.io.Serializable;

import static xyz.vopen.mixmicro.components.authorization.Constants.DEFAULT_PERIOD_OF_REFRESH_TOKEN_VALIDITY_MS;

/**
 * {@link RefreshToken}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020-04-12.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken implements Serializable {

  @SerializedName("atid")
  private String accessTokenId;

  /**
   * Access Token Generated Timestamp (ms)
   *
   * <p>
   */
  @SerializedName("rtgt")
  @Builder.Default
  private long generateTimestamp = System.currentTimeMillis();

  /**
   * Access Token Expired Timestamp (ms)
   *
   * <p>default :{@link #generateTimestamp} + {@link
   * Constants#DEFAULT_PERIOD_OF_REFRESH_TOKEN_VALIDITY_MS}
   */
  @SerializedName("rtet")
  private long expiredTimestamp =
      this.generateTimestamp + DEFAULT_PERIOD_OF_REFRESH_TOKEN_VALIDITY_MS;
}
