/*
 * =============================================================================
 *
 *   Copyright (c) 2017-2019, VOPEN.XYZ (http://vopen.xyz)
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 * =============================================================================
 */
package xyz.vopen.mixmicro.components.enhance.security.web.pbeconfig;

/**
 * Interface which must be implemented by the user-defined classes which create and initialize
 * webapp PBE encryptors for being configured through the web pbe configuration servlet.
 *
 * <p>This interface only has one method, {@link #initializeWebPBEConfigs()}, which the
 * implementation class should use for creating encryptors and setting {@link WebPBEConfig} objects
 * to them, like:
 *
 * <pre>
 *  package myapp;
 *  ...
 *  public class MyWebPBEInitializer implements WebPBEInitializer {
 *
 *      public void initializeWebPBEConfigs() {
 *
 *          StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
 *          encryptor.setAlgorithm("PBEWithMD5AndDES");
 *
 *          WebPBEConfig webConfig = new WebPBEConfig();
 *          webConfig.setValidationWord("mixSecurity");
 *          webConfig.setName("Main Password");
 *
 *          encryptor.setConfig(webConfig);
 *
 *          // Get some user-defined singleton or similar, and register
 *          // the encryptor with it so that it can be accessed from the
 *          // rest of the application.
 *
 *      }
 *
 *  }
 * </pre>
 *
 * <p>All <tt>WebPBEConfig</tt> objects get internally registered at instantiation time, and once
 * assigned to an encryptor they will appear in the web form for password setting.
 *
 * <p><b>Important</b>: If the web application uses Spring Framework, <tt>WebPBEConfig</tt> objects
 * are declared as beans in the Spring context and this Spring context is initialized at application
 * deploy time (with Spring's <tt>ContextLoaderListener</tt>), it will NOT be necessary to create
 * any classes implementing this <tt>WebPBEInitializer</tt> interface. The use {@link
 * WebPBEInitializationContextListener} will also become unnecessary.
 *
 * @since 1.3
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface WebPBEInitializer {

  /**
   * Method called by {@link WebPBEInitializationContextListener} at application deploy time for
   * initialization of security encryptors.
   */
  public void initializeWebPBEConfigs();
}
