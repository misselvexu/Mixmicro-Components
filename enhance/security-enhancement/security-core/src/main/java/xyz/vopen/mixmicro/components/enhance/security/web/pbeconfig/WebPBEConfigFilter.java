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

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This filter is intended to avoid access to the web application until an admin has set the
 * encryption passwords. It will query the web PBE config system to know whether passwords have been
 * set and, if not, it will show the user a plain <i>Access Forbidden</i> page.
 *
 * <p>An example <tt>web.xml</tt> fragment (being applied on a Struts servlet):
 *
 * <pre>
 *    &lt;filter>
 *        &lt;filter-name>webPBEConfigFilter&lt;/filter-name>
 *        &lt;filter-class>WebPBEConfigFilter&lt;/filter-class>
 *    &lt;/filter>
 *
 *    &lt;filter-mapping>
 *        &lt;filter-name>webPBEConfigFilter&lt;/filter-name>
 *        &lt;servlet-name>strutsActionServlet&lt;/servlet-name>
 *    &lt;/filter-mapping>
 * </pre>
 *
 * @since 1.3
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public final class WebPBEConfigFilter implements Filter {

  public void doFilter(
      final ServletRequest request, final ServletResponse response, final FilterChain chain)
      throws IOException, ServletException {

    final WebPBEConfigRegistry registry = WebPBEConfigRegistry.getInstance();
    if (registry.isWebConfigurationDone()) {
      // If it is initialized, quickly continue filter chain
      chain.doFilter(request, response);
    } else {
      // Not initialized, a Forbidden page must be shown, chain broken
      PrintWriter printWriter = response.getWriter();
      printWriter.write(WebPBEConfigHtmlUtils.createNotInitializedHtml());
      printWriter.flush();
    }
  }

  public void init(final FilterConfig filterConfig) throws ServletException {
    // Nothing to be done here.
  }

  public void destroy() {
    // Nothing to be done here.
  }
}
