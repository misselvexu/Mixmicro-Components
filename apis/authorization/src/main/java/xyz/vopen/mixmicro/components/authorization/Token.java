package xyz.vopen.mixmicro.components.authorization;

import com.google.gson.annotations.SerializedName;
import lombok.*;
import xyz.vopen.mixmicro.kits.annotation.JustForTest;

import java.io.Serializable;

/**
 * {@link Token}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020-04-12.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token implements Serializable {

  /**
   * Token Instance Id
   *
   * <p>
   */
  @SerializedName("token_id")
  private String tokenId;

  /**
   * Access Token Encode
   *
   * <p>
   */
  @SerializedName("access_token")
  private String accessToken;

  /**
   * Access Token Expires Period
   *
   * <p>
   */
  @SerializedName("expires_in")
  private long expiredTime;

  /**
   * Refresh Token
   *
   * <p>
   */
  @JustForTest
  @SerializedName("refresh_token")
  private String refreshToken;
}
