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

import xyz.vopen.mixmicro.components.enhance.security.commons.CommonUtils;
import xyz.vopen.mixmicro.components.enhance.security.exceptions.EncryptionInitializationException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * ContextListener which takes a {@link WebPBEInitializer} implementation class name as a parameter
 * (&lt;context-param>) and calls its <tt>initializeWebPBEConfigs()</tt> method to allow the webapp
 * to create its PBE encryptors and declare their associated {@link WebPBEConfig} objects.
 *
 * <p>An example <tt>web.xml</tt> fragment:
 *
 * <pre>
 *    &lt;context-param>
 *      &lt;param-name>webPBEInitializerClassName&lt;/param-name>
 *      &lt;param-value>myapp.MyWebPBEInitializer&lt;/param-value>
 *    &lt;/context-param>
 *
 *    &lt;listener>
 *      &lt;listener-class>
 *        WebPBEInitializationContextListener
 *      &lt;/listener-class>
 *    &lt;/listener>
 * </pre>
 *
 * <p><b>Important</b>: If the web application uses Spring Framework, <tt>WebPBEConfig</tt> objects
 * are declared as beans in the Spring context and this Spring context is initialized at application
 * deploy time (with Spring's <tt>ContextLoaderListener</tt>), the use of this context listener will
 * become unnecessary.
 *
 * @since 1.3
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public final class WebPBEInitializationContextListener implements ServletContextListener {

  public static final String INIT_PARAM_INITIALIZER_CLASS_NAME = "webPBEInitializerClassName";

  public void contextDestroyed(final ServletContextEvent sce) {
    // nothing to be done here
  }

  public void contextInitialized(final ServletContextEvent sce) {

    final String className =
        sce.getServletContext().getInitParameter(INIT_PARAM_INITIALIZER_CLASS_NAME);

    if (CommonUtils.isEmpty(className)) {
      throw new EncryptionInitializationException(
          INIT_PARAM_INITIALIZER_CLASS_NAME
              + " context "
              + "initialization parameter not set in web.xml");
    }

    Class initializerClass = null;
    try {
      initializerClass = Thread.currentThread().getContextClassLoader().loadClass(className);
    } catch (ClassNotFoundException e) {
      throw new EncryptionInitializationException(e);
    }

    if (!WebPBEInitializer.class.isAssignableFrom(initializerClass)) {
      throw new EncryptionInitializationException(
          "Class "
              + className
              + " does not implement interface "
              + WebPBEInitializer.class.getName());
    }

    WebPBEInitializer initializer = null;
    try {
      initializer = (WebPBEInitializer) initializerClass.newInstance();
    } catch (InstantiationException e) {
      throw new EncryptionInitializationException(e);
    } catch (IllegalAccessException e) {
      throw new EncryptionInitializationException(e);
    }

    // Let the user initialize his/her encryptors and WebPBEConfig objects.
    initializer.initializeWebPBEConfigs();
  }
}
