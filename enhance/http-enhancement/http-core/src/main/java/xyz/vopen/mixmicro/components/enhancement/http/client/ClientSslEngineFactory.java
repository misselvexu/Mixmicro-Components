package xyz.vopen.mixmicro.components.enhancement.http.client;

import javax.net.ssl.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;


public class ClientSslEngineFactory {

  private static final String PROTOCOL = "TLS";
  private static final SSLContext CLIENT_CONTEXT;

  static {
    SSLContext clientContext = null;

    try {
      clientContext = SSLContext.getInstance(PROTOCOL);
      clientContext.init(null, TrustManagerFactory.getTrustManagers(),
          null);
    } catch (Exception e) {
      throw new Error(
          "Failed to initialize the client-side SSLContext", e);
    }

    CLIENT_CONTEXT = clientContext;
  }

  public static SSLEngine trustAnybody() {
    SSLEngine engine = CLIENT_CONTEXT.createSSLEngine();
    engine.setUseClientMode(true);
    return engine;
  }
}


class TrustManagerFactory extends TrustManagerFactorySpi {

  private static final TrustManager DUMMY_TRUST_MANAGER = new X509TrustManager() {
    @Override
    public X509Certificate[] getAcceptedIssuers() {
      return new X509Certificate[0];
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain,
                                   String authType) throws CertificateException {
      // Always trust
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain,
                                   String authType) throws CertificateException {
      // Always trust
    }
  };

  public static TrustManager[] getTrustManagers() {
    return new TrustManager[]{DUMMY_TRUST_MANAGER};
  }

  @Override
  protected TrustManager[] engineGetTrustManagers() {
    return getTrustManagers();
  }

  @Override
  protected void engineInit(KeyStore keystore) throws KeyStoreException {
    // Unused
  }

  @Override
  protected void engineInit(
      ManagerFactoryParameters managerFactoryParameters)
      throws InvalidAlgorithmParameterException {
    // Unused
  }
}
