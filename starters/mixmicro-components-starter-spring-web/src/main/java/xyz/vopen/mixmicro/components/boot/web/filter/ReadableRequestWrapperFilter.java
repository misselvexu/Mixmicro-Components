package xyz.vopen.mixmicro.components.boot.web.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import xyz.vopen.mixmicro.kits.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

/**
 * {@link ReadableRequestWrapperFilter}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020-04-16.
 */
@Slf4j
@WebFilter(urlPatterns = "/*")
public class ReadableRequestWrapperFilter implements Filter {
  @Override
  public void init(FilterConfig filterConfig) {
    // Do nothing
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    ReadableRequestWrapper wrapper = new ReadableRequestWrapper((HttpServletRequest) request);
    chain.doFilter(wrapper, response);
  }

  @Override
  public void destroy() {
    // Do nothing
  }

  public static class ReadableRequestWrapper extends HttpServletRequestWrapper {
    private final Charset encoding;
    private byte[] rawData;
    private Map<String, String[]> params = new HashMap<>();

    public ReadableRequestWrapper(HttpServletRequest request) {
      super(request);
      this.params.putAll(request.getParameterMap());

      String charEncoding = request.getCharacterEncoding();
      this.encoding = StringUtils.isBlank(charEncoding) ? StandardCharsets.UTF_8 : Charset.forName(charEncoding);

      try {
        InputStream is = request.getInputStream();
        this.rawData = IOUtils.toByteArray(is);

        // body 파싱
        String collect =
            this.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        if (StringUtils.isEmpty(collect)) {
          return;
        }
        if (request.getContentType() != null && request.getContentType().contains(MULTIPART_FORM_DATA_VALUE)) {
          return;
        }
        JSONParser jsonParser = new JSONParser();
        Object parse = jsonParser.parse(collect);
        if (parse instanceof JSONArray) {
          JSONArray jsonArray = (JSONArray) jsonParser.parse(collect);
          setParameter("requestBody", jsonArray.toJSONString());
        } else {
          JSONObject jsonObject = (JSONObject) jsonParser.parse(collect);
          Iterator<?> iterator = jsonObject.keySet().iterator();
          while (iterator.hasNext()) {
            String key = (String) iterator.next();
            setParameter(key, jsonObject.get(key).toString().replace("\"", "\\\""));
          }
        }
      } catch (Exception e) {
        log.error("ReadableRequestWrapper init error", e);
      }
    }

    @Override
    public String getParameter(String name) {
      String[] paramArray = getParameterValues(name);
      if (paramArray != null && paramArray.length > 0) {
        return paramArray[0];
      } else {
        return null;
      }
    }

    @Override
    public Map<String, String[]> getParameterMap() {
      return Collections.unmodifiableMap(params);
    }

    @Override
    public Enumeration<String> getParameterNames() {
      return Collections.enumeration(params.keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
      String[] result = null;
      String[] dummyParamValue = params.get(name);

      if (dummyParamValue != null) {
        result = new String[dummyParamValue.length];
        System.arraycopy(dummyParamValue, 0, result, 0, dummyParamValue.length);
      }
      return result;
    }

    public void setParameter(String name, String value) {
      String[] param = {value};
      setParameter(name, param);
    }

    public void setParameter(String name, String[] values) {
      params.put(name, values);
    }

    @Override
    public ServletInputStream getInputStream() {
      final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.rawData);

      return new ServletInputStream() {
        @Override
        public boolean isFinished() {
          return false;
        }

        @Override
        public boolean isReady() {
          return false;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
          // Do nothing
        }

        @Override
        public int read() {
          return byteArrayInputStream.read();
        }
      };
    }

    @Override
    public BufferedReader getReader() {
      return new BufferedReader(new InputStreamReader(this.getInputStream(), this.encoding));
    }
  }
}
