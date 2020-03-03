/*
 * Copyright 2006-2017 vopen.xyz
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.vopen.mixmicro.kits.http.client;

import org.apache.http.HttpResponse;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.*;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultHttpResponseParser;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.impl.io.DefaultHttpResponseParserFactory;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.LineParser;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

/** http连接池客户端抽象类 */
@SuppressWarnings({"Duplicates"})
public abstract class AbstractHttpClient {

  private static final Logger logger = LoggerFactory.getLogger(AbstractHttpClient.class);

  private static final RequestConfig DEFAULT_REQUEST_CONFIG;

  private static final CloseableHttpClient HTTPCLIENT;

  private static final String HTTP_PROPERTIES = "http.properties";

  private static final String MAX_TOTAL = "100";

  private static final String PROXY_ADDRESS = "http.proxy";

  private static final String REQUEST_INTERCEPTORS = "http.request.interceptor";

  private static final String RESPONSE_INTERCEPTORS = "http.response.interceptor";

  private static final String DEFAULT_MAX_PER_ROUTE = "20";

  private static final String MAX_PER_ROUTE = "10";

  private static final Properties PROPERTIES;

  private static List<HttpRequestInterceptor> requestInterceptors = new LinkedList<>();

  private static List<HttpResponseInterceptor> responseInterceptors = new LinkedList<>();

  static {
    // 读取配置文件
    InputStream in = AbstractHttpClient.class.getClassLoader().getResourceAsStream(HTTP_PROPERTIES);
    PROPERTIES = new Properties();

    try {
      if (in != null) {
        PROPERTIES.load(in);
      }
    } catch (Exception e) {
      // ignore...
    } finally {
      try {
        if (in != null) {
          in.close();
        }
      } catch (IOException e) {
        // do nothing...
      }
    }

    // 使用自定义消息解析器 / 写入者制定http消息的方式解析和写入数据流
    HttpMessageParserFactory<HttpResponse> responseParserFactory =
        new DefaultHttpResponseParserFactory() {
          @Override
          public HttpMessageParser<HttpResponse> create(
              SessionInputBuffer buffer, MessageConstraints constraints) {
            LineParser lineParser =
                new BasicLineParser() {
                  @Override
                  public Header parseHeader(final CharArrayBuffer buffer) {
                    try {
                      return super.parseHeader(buffer);
                    } catch (ParseException ex) {
                      return new BasicHeader(buffer.toString(), null);
                    }
                  }
                };
            return new DefaultHttpResponseParser(
                buffer, lineParser, DefaultHttpResponseFactory.INSTANCE, constraints) {
              @Override
              protected boolean reject(CharArrayBuffer line, int count) {
                return false;
              }
            };
          }
        };
    HttpMessageWriterFactory<HttpRequest> requestWriterFactory =
        new DefaultHttpRequestWriterFactory();

    // 使用自定义HTTP连接工厂.
    HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory =
        new ManagedHttpClientConnectionFactory(requestWriterFactory, responseParserFactory);

    SSLContext sslcontext = null;

    try {
      sslcontext =
          SSLContexts.custom()
              .loadTrustMaterial(
                  new TrustStrategy() {
                    @Override
                    public boolean isTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                      // 信任所有
                      return true;
                    }
                  })
              .build();
    } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
      logger.error(e.getMessage(), e);
    }

    Args.notNull(sslcontext, "SSL Context .");

    SSLConnectionSocketFactory sslConnectionSocketFactory =
        new SSLConnectionSocketFactory(
            sslcontext,
            new String[] {"TLSv1.2"},
            null,
            SSLConnectionSocketFactory.getDefaultHostnameVerifier());

    Registry<ConnectionSocketFactory> socketFactoryRegistry =
        RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", PlainConnectionSocketFactory.INSTANCE)
            // .register("https",
            // new SSLConnectionSocketFactory(sslcontext,
            // SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER))
            .register("https", sslConnectionSocketFactory)
            .build();

    // 使用自定义DNS解析器覆盖系统的DNS解析
    DnsResolver dnsResolver =
        new SystemDefaultDnsResolver() {
          @Override
          public InetAddress[] resolve(final String host) throws UnknownHostException {
            if (host.equalsIgnoreCase("localhost")) {
              return new InetAddress[] {InetAddress.getByAddress(new byte[] {127, 0, 0, 1})};
            } else {
              return super.resolve(host);
            }
          }
        };

    // 创建一个自定义参数的连接管理类
    PoolingHttpClientConnectionManager connManager =
        new PoolingHttpClientConnectionManager(socketFactoryRegistry, connFactory, dnsResolver);

    // 创建socket配置
    SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();

    connManager.setDefaultSocketConfig(socketConfig);
    connManager.setSocketConfig(new HttpHost("somehost", 80), socketConfig);
    //		connManager.setValidateAfterInactivity(1000);

    MessageConstraints messageConstraints =
        MessageConstraints.custom().setMaxHeaderCount(200).setMaxLineLength(2000).build();

    // 连接配置
    ConnectionConfig connectionConfig =
        ConnectionConfig.custom()
            .setMalformedInputAction(CodingErrorAction.IGNORE)
            // .setBufferSize(4128) 为预备解决斗鱼response无响应
            .setUnmappableInputAction(CodingErrorAction.IGNORE)
            .setCharset(Consts.UTF_8)
            .setMessageConstraints(messageConstraints)
            .build();

    connManager.setDefaultConnectionConfig(connectionConfig);
    connManager.setConnectionConfig(new HttpHost("somehost", 80), ConnectionConfig.DEFAULT);

    // 设置连接参数, total route

    // 连接池最大连接数
    int maxTotal = Integer.parseInt(PROPERTIES.getProperty("http.max_total", MAX_TOTAL));
    connManager.setMaxTotal(maxTotal);

    int defaultMaxPerRoute =
        Integer.parseInt(
            PROPERTIES.getProperty("http.default_max_per_route", DEFAULT_MAX_PER_ROUTE));
    // 每个路由最大连接数
    connManager.setDefaultMaxPerRoute(defaultMaxPerRoute);

    int maxPerRoute = Integer.parseInt(PROPERTIES.getProperty("http.max_per_route", MAX_PER_ROUTE));
    connManager.setMaxPerRoute(new HttpRoute(new HttpHost("somehost", 80)), maxPerRoute);

    // 使用自定义cookie
    CookieStore cookieStore = new ThreadLocalCookieStore();
    // 使用自定义证书
    CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

    String proxyAddress = PROPERTIES.getProperty(PROXY_ADDRESS, "");
    if (proxyAddress != null && proxyAddress.trim().length() > 0) {

      logger.info("HttpClient Request Proxy Address is : {}", proxyAddress);
      // request config
      DEFAULT_REQUEST_CONFIG =
          RequestConfig.custom()
              .setProxy(HttpHost.create(proxyAddress))
              .setCookieSpec(CookieSpecs.DEFAULT)
              .setRedirectsEnabled(true)
              .setRelativeRedirectsAllowed(false)
              .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
              .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
              .build();
    } else {

      // request config
      DEFAULT_REQUEST_CONFIG =
          RequestConfig.custom()
              .setCookieSpec(CookieSpecs.DEFAULT)
              .setRedirectsEnabled(true)
              .setRelativeRedirectsAllowed(false)
              .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
              .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
              .build();
    }

    HttpClientBuilder httpClientBuilder =
        HttpClients.custom()
            .setConnectionManager(connManager)
            .setDefaultCookieStore(cookieStore)
            .setDefaultCredentialsProvider(credentialsProvider)
            .setDefaultRequestConfig(DEFAULT_REQUEST_CONFIG);

    // processor interceptors
    String configedRequestInterceptors = PROPERTIES.getProperty(REQUEST_INTERCEPTORS, "");
    if (configedRequestInterceptors != null && configedRequestInterceptors.trim().length() > 0) {
      for (String interceptorClassName : configedRequestInterceptors.split(",")) {
        try {
          Class<?> clazz = Class.forName(interceptorClassName);
          Constructor constructor = clazz.getConstructor();
          requestInterceptors.add((HttpRequestInterceptor) constructor.newInstance());
        } catch (Exception ignore) {
        }
      }
    }

    String configedResponseInterceptors = PROPERTIES.getProperty(RESPONSE_INTERCEPTORS, "");
    if (configedResponseInterceptors != null && configedResponseInterceptors.trim().length() > 0) {
      for (String interceptorClassName : configedResponseInterceptors.split(",")) {
        try {
          Class<?> clazz = Class.forName(interceptorClassName);
          Constructor constructor = clazz.getConstructor();
          responseInterceptors.add((HttpResponseInterceptor) constructor.newInstance());
        } catch (Exception ignore) {
        }
      }
    }

    for (HttpRequestInterceptor requestInterceptor : requestInterceptors) {
      httpClientBuilder.addInterceptorLast(requestInterceptor);
    }

    for (HttpResponseInterceptor responseInterceptor : responseInterceptors) {
      httpClientBuilder.addInterceptorLast(responseInterceptor);
    }

    // 创建一个http连接
    HTTPCLIENT = httpClientBuilder.build();
  }

  /**
   * get请求方法
   *
   * @param url 请求地址
   * @param params 请求参数
   * @param options 访问参数
   * @param response 返回结果
   */
  protected void get(
      String url,
      HttpParams params,
      xyz.vopen.mixmicro.kits.http.client.HttpOptions options,
      xyz.vopen.mixmicro.kits.http.client.HttpResponse response) {
    StringBuilder buffer = new StringBuilder(url);
    try {
      if (params != null) {
        Map<String, String> pms = params.toMap();
        if (url.indexOf('?') != -1) { // 连接参数
          buffer.append("&");
        } else {
          buffer.append("?");
        }
        for (Map.Entry<String, String> entry : pms.entrySet()) {
          if (!buffer.toString().endsWith("?") && !buffer.toString().endsWith("&")) { // 已经添加过参数
            buffer.append("&");
          }
          buffer.append(URLEncoder.encode(entry.getKey(), "utf-8"));
          buffer.append("=");
          buffer.append(URLEncoder.encode(entry.getValue(), "utf-8"));
        }
      }
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("Encoding not supported", e);
    }

    HttpClientContext context = HttpClientContext.create();

    HttpGet httpget = new HttpGet(buffer.toString());
    RequestConfig requestConfig = null;
    if (options != null) {
      requestConfig =
          RequestConfig.copy(DEFAULT_REQUEST_CONFIG)
              .setSocketTimeout(options.getSocketTimeout())
              .setConnectTimeout(options.getConnectTimeout())
              .setConnectionRequestTimeout(options.getConnectionRequestTimeout())
              .setExpectContinueEnabled(options.getExpectContinueEnabled())
              .build();

      // 设置头信息
      Header[] headers = options.getHeaders();
      if (headers != null && headers.length != 0) {
        httpget.setHeaders(headers);
      }

      if (options.getCookieStore() != null) {
        context.setCookieStore(options.getCookieStore());
      }

      if (options.getCredentialsProvider() != null) {
        context.setCredentialsProvider(options.getCredentialsProvider());
      }
    } else {
      requestConfig = RequestConfig.copy(DEFAULT_REQUEST_CONFIG).build();
    }
    httpget.setConfig(requestConfig);

    try {
      CloseableHttpResponse httpresponse = HTTPCLIENT.execute(httpget, context);
      try {
        if (response != null) { // 回调
          HttpEntity entity = httpresponse.getEntity();

          if (response.isParseCookie()) {
            setCookie(httpresponse, response);
          }

          if (response.isParseHeader()) {
            setHeader(httpresponse, response);
          }

          // 得到http状态码
          int statusCode = httpresponse.getStatusLine().getStatusCode();
          response.setStatusCode(statusCode);

          // 实体不为空时, 返回结果
          if (entity != null) {
            String result = EntityUtils.toString(entity, response.getEncoding());
            response.setResult(result);
          }
        }
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        throw new RuntimeException(e);
      } finally {
        if (httpresponse != null) {
          httpresponse.close();
        }
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    } finally {
      httpget.releaseConnection();
    }
  }

  protected void file(
      String url,
      HttpParams params,
      xyz.vopen.mixmicro.kits.http.client.HttpOptions options,
      xyz.vopen.mixmicro.kits.http.client.HttpResponse response) {

    StringBuilder buffer = new StringBuilder(url);
    try {
      if (params != null) {
        Map<String, String> pms = params.toMap();
        if (url.indexOf('?') != -1) { // 连接参数
          buffer.append("&");
        } else {
          buffer.append("?");
        }
        for (Map.Entry<String, String> entry : pms.entrySet()) {
          if (!buffer.toString().endsWith("?") && !buffer.toString().endsWith("&")) { // 已经添加过参数
            buffer.append("&");
          }
          buffer.append(URLEncoder.encode(entry.getKey(), "utf-8"));
          buffer.append("=");
          buffer.append(URLEncoder.encode(entry.getValue(), "utf-8"));
        }
      }
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("Encoding not supported", e);
    }

    HttpClientContext context = HttpClientContext.create();

    HttpGet httpget = new HttpGet(buffer.toString());
    RequestConfig requestConfig = null;
    if (options != null) {
      requestConfig =
          RequestConfig.copy(DEFAULT_REQUEST_CONFIG)
              .setSocketTimeout(options.getSocketTimeout())
              .setConnectTimeout(options.getConnectTimeout())
              .setConnectionRequestTimeout(options.getConnectionRequestTimeout())
              .setExpectContinueEnabled(options.getExpectContinueEnabled())
              .build();

      // 设置头信息
      Header[] headers = options.getHeaders();
      if (headers != null && headers.length != 0) {
        httpget.setHeaders(headers);
      }

      if (options.getCookieStore() != null) {
        context.setCookieStore(options.getCookieStore());
      }

      if (options.getCredentialsProvider() != null) {
        context.setCredentialsProvider(options.getCredentialsProvider());
      }
    } else {
      requestConfig = RequestConfig.copy(DEFAULT_REQUEST_CONFIG).build();
    }
    httpget.setConfig(requestConfig);

    try {
      CloseableHttpResponse httpresponse = HTTPCLIENT.execute(httpget, context);

      try {
        if (response != null) { // 回调
          HttpEntity entity = httpresponse.getEntity();

          if (response.isParseCookie()) {
            setCookie(httpresponse, response);
          }

          if (response.isParseHeader()) {
            setHeader(httpresponse, response);
          }

          // 得到http状态码
          int statusCode = httpresponse.getStatusLine().getStatusCode();
          response.setStatusCode(statusCode);

          // 实体不为空时, 返回结果
          if (entity != null) {

            InputStream inputStream = entity.getContent();
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[1024 * 2];
            int rc = 0;
            while ((rc = inputStream.read(buff, 0, 100)) > 0) {
              swapStream.write(buff, 0, rc);
            }
            byte[] in2b = swapStream.toByteArray();
            response.setResultByte(in2b);

            inputStream.close();
          }
        }
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        throw new RuntimeException(e);
      } finally {
        if (httpresponse != null) {
          httpresponse.close();
        }
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    } finally {
      httpget.releaseConnection();
    }
  }

  /**
   * post请求
   */
  protected void post(
      String url,
      HttpParams params,
      xyz.vopen.mixmicro.kits.http.client.HttpOptions options,
      xyz.vopen.mixmicro.kits.http.client.HttpResponse response) {
    execute(METHOD.POST, url, params, options, response);
  }

  protected void execute(
      METHOD method,
      String url,
      HttpParams params,
      HttpOptions options,
      xyz.vopen.mixmicro.kits.http.client.HttpResponse response) {

    HttpEntityEnclosingRequestBase requestBase = null;

    switch (method) {
      case POST:
        requestBase = new HttpPost(url);
        break;
      case PUT:
        requestBase = new HttpPut(url);
        break;
      default:
        throw new RuntimeException("un-supported request method: " + method.name());
    }

    RequestConfig requestConfig = null;

    HttpClientContext context = HttpClientContext.create();
    if (options != null) {
      requestConfig =
          RequestConfig.copy(DEFAULT_REQUEST_CONFIG)
              .setSocketTimeout(options.getSocketTimeout())
              .setConnectTimeout(options.getConnectTimeout())
              .setConnectionRequestTimeout(options.getConnectionRequestTimeout())
              .setExpectContinueEnabled(options.getExpectContinueEnabled())
              .build();

      // 设置头信息
      Header[] headers = options.getHeaders();
      if (headers != null && headers.length != 0) {
        requestBase.setHeaders(headers);
      }

      if (options.getCookieStore() != null) {
        context.setCookieStore(options.getCookieStore());
      }
      if (options.getCredentialsProvider() != null) {
        context.setCredentialsProvider(options.getCredentialsProvider());
      }

    } else {
      requestConfig = RequestConfig.copy(DEFAULT_REQUEST_CONFIG).build();
    }
    requestBase.setConfig(requestConfig);
    // 构建实体
    HttpEntity httpEntity = getEntity(params);
    if (httpEntity != null) {
      requestBase.setEntity(httpEntity);
    }

    try {
      CloseableHttpResponse httpresponse = HTTPCLIENT.execute(requestBase, context);
      try {

        if (response != null) { // 回调
          HttpEntity entity = httpresponse.getEntity();
          if (response.isParseCookie()) {
            setCookie(httpresponse, response);
          }

          if (response.isParseHeader()) {
            setHeader(httpresponse, response);
          }

          // 得到http状态码
          int statusCode = httpresponse.getStatusLine().getStatusCode();
          response.setStatusCode(statusCode);

          // 实体不为空时, 返回结果
          if (entity != null) {
            String result = EntityUtils.toString(entity, response.getEncoding());
            response.setResult(result);
          }
        }

      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        throw new RuntimeException(e);
      } finally {
        httpresponse.close();
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    } finally {
      requestBase.releaseConnection();
    }
  }

  private HttpEntity getEntity(HttpParams params) {
    HttpEntity httpEntity = null;
    if (params == null) {
      return null;
    }

    Map<String, String> pms = params.toMap();
    HttpParams.ENTITY entity = params.getEntity();

    if (entity == HttpParams.ENTITY.MULTIPART) {

      MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();

      multipartEntityBuilder
          .addBinaryBody(
              "file",
              params.getFile(),
              ContentType.create("application/dicom"),
              params.getFile().getName())
          .setMode(HttpMultipartMode.RFC6532);

      return multipartEntityBuilder.build();
    }

    if (entity == HttpParams.ENTITY.FORM) {
      List<NameValuePair> list = new ArrayList<>();
      for (Map.Entry<String, String> entry : pms.entrySet()) {
        list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
      }
      try {
        httpEntity = new UrlEncodedFormEntity(list);
      } catch (UnsupportedEncodingException e) {
        throw new RuntimeException("Encoding not supported", e);
      }
    }

    if (entity == HttpParams.ENTITY.BYTE) {
      // byte流类型的实体, params需要传key=...
      String value = params.getValue();
      if (value == null) {
        throw new RuntimeException("params must value");
      }
      try {
        httpEntity = new ByteArrayEntity(value.getBytes("UTF-8"));
      } catch (UnsupportedEncodingException e) {
        throw new RuntimeException("Encoding not supported", e);
      }
    }

    if (entity == HttpParams.ENTITY.STRING) {
      // String类型的实体, params需要传key=...
      String value = params.getValue();
      ContentType type = params.getContentType();
      if (value == null || type == null) {
        throw new RuntimeException("params must key and type");
      }
      httpEntity = new StringEntity(value, type);
    }

    return httpEntity;
  }

  public void setHeader(
      HttpResponse httpResponse, xyz.vopen.mixmicro.kits.http.client.HttpResponse response) {
    if (httpResponse != null) {
      Header[] headers = httpResponse.getAllHeaders();
      if (headers != null && headers.length > 0) {
        for (Header header : headers) {
          response.getHeaderMap().put(header.getName(), header.getValue());
        }
      }
    }
  }

  @SuppressWarnings("UnusedReturnValue")
  private String setCookie(
      HttpResponse httpResponse, xyz.vopen.mixmicro.kits.http.client.HttpResponse response) {
    Header[] headers = httpResponse.getHeaders("Set-Cookie");
    if (headers == null || headers.length == 0) {
      System.out.println("----there are no cookies");
      return null;
    }
    StringBuilder cookie = new StringBuilder();
    for (int i = 0; i < headers.length; i++) {
      cookie.append(headers[i].getValue());
      if (i != headers.length - 1) {
        cookie.append(";");
      }
    }

    String[] cookies = cookie.toString().split(";");
    for (String c : cookies) {
      c = c.trim();
      response.getCookieMap().remove(c.split("=")[0]);
      response
          .getCookieMap()
          .put(
              c.split("=")[0],
              c.split("=").length == 1
                  ? ""
                  : (c.split("=").length == 2 ? c.split("=")[1] : c.split("=", 2)[1]));
    }

    StringBuilder cookiesTmp = new StringBuilder();
    for (String key : response.getCookieMap().keySet()) {
      cookiesTmp.append(key).append("=").append(response.getCookieMap().get(key)).append(";");
    }

    return cookiesTmp.substring(0, cookiesTmp.length() - 2);
  }

  public enum METHOD { // 请求方法
    GET,
    POST,
    PUT,
    DELETE
  }
}
