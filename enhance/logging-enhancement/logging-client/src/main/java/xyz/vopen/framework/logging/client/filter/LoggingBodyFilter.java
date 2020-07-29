/*
 * Copyright (c) 2018 VOPEN.XYZ
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 *
 */

package xyz.vopen.framework.logging.client.filter;

import xyz.vopen.framework.web.request.RequestWrapper;
import xyz.vopen.framework.web.response.ResponseWrapper;
import xyz.vopen.framework.web.util.HttpRequestUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Mixmicro Logging Body（Request / Response） Filter Encapsulation principal obtains the corresponding
 * replicated content
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@WebFilter(urlPatterns = "/*", filterName = "loggingBodyFilter")
public class LoggingBodyFilter implements Filter {
  /** the bean name of {@link LoggingBodyFilter} */
  public static final String BEAN_NAME = "loggingBodyFilter";

  /**
   * Wrapper Body replace http request body instance replace http response body instance
   *
   * @param request http request
   * @param response http response
   * @param filterChain filter chain
   * @throws IOException ioException
   * @throws ServletException servlet Exception
   */
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
    if (!HttpRequestUtil.isMultipart(request)) {
      RequestWrapper requestWrapper = new RequestWrapper((HttpServletRequest) request);
      ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) response);
      filterChain.doFilter(requestWrapper, responseWrapper);
      responseWrapper.flushBuffer();
    } else {
      filterChain.doFilter(request, response);
    }
  }
}
