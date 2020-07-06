/*
 * Copyright (c) 2009-2020 VOPEN.XYZ
 *
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without
 *  restriction, including without limitation the rights to use,
 *  copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following
 *  conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 *  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *  OTHER DEALINGS IN THE SOFTWARE.
 */

import xyz.vopen.mixmicro.components.enhance.socket.websocket.server.SSLParametersWebSocketServerFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.security.KeyStore;

/**
 * Copy of SSLServerExample except we use @link SSLEngineWebSocketServerFactory to customize
 * clientMode/ClientAuth to force client to present a cert. Example of Two-way
 * ssl/MutualAuthentication/ClientAuthentication
 */
public class TwoWaySSLServerExample {

  /*
   * Keystore with certificate created like so (in JKS format):
   *
   *keytool -genkey -keyalg RSA -validity 3650 -keystore "keystore.jks" -storepass "storepassword" -keypass "keypassword" -alias "default" -dname "CN=127.0.0.1, OU=MyOrgUnit, O=MyOrg, L=MyCity, S=MyRegion, C=MyCountry"
   */
  public static void main(String[] args) throws Exception {
    ChatServer chatserver =
        new ChatServer(
            8887); // Firefox does allow multible ssl connection only via port 443 //tested on FF16

    // load up the key store
    String STORETYPE = "JKS";
    String KEYSTORE =
        Paths.get("src", "test", "java", "org", "java_websocket", "keystore.jks").toString();
    String STOREPASSWORD = "storepassword";
    String KEYPASSWORD = "keypassword";

    KeyStore ks = KeyStore.getInstance(STORETYPE);
    File kf = new File(KEYSTORE);
    ks.load(new FileInputStream(kf), STOREPASSWORD.toCharArray());

    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(ks, KEYPASSWORD.toCharArray());
    TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
    tmf.init(ks);

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

    SSLParameters sslParameters = new SSLParameters();
    // This is all we need
    sslParameters.setNeedClientAuth(true);
    chatserver.setWebSocketFactory(
        new SSLParametersWebSocketServerFactory(sslContext, sslParameters));

    chatserver.start();
  }
}
