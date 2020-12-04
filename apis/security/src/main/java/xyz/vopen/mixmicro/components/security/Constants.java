package xyz.vopen.mixmicro.components.security;

/**
 * {@link Constants}
 *
 * <p>Class Constants Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/11/10
 */
public final class Constants {

  //

  public enum SecurityHeader {

    /**
     * Public Key
     *
     * <p>
     */
    PUBKEY("mixmicro-security-pubkey", "security public key"),

    /**
     * Signature Value
     *
     * <p>
     */
    SIGNATURE("mixmicro-security-signature", "security signature value");

    // ~~ properties

    private final String headerName;
    private final String description;

    SecurityHeader(String headerName, String description) {
      this.headerName = headerName;
      this.description = description;
    }

    public String headerName() {
      return this.headerName;
    }

    public String description() {
      return description;
    }
  }
}
