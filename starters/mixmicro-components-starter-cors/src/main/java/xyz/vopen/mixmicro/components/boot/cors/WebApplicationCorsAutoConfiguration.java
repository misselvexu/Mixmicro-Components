package xyz.vopen.mixmicro.components.boot.cors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Web Application CORS Configuration
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version v1.0 - 12/10/2018.
 */
@Configuration
@EnableConfigurationProperties(WebApplicationCorsAutoConfiguration.CorsProperties.class)
@ConditionalOnClass(
    name = "org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent")
public class WebApplicationCorsAutoConfiguration {

  private static final Logger log = LoggerFactory.getLogger(WebApplicationCorsAutoConfiguration.class);
  private static final String CORS_PREFIX = "server.cors";
  private static final String CORS_ENABLED = "server.cors.enabled";

  @Bean
  @ConditionalOnProperty(value = CORS_ENABLED, havingValue = "true", matchIfMissing = true)
  public CorsFilter corsFilter() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    final CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin("*");
    config.addAllowedHeader("*");
    config.setMaxAge(18000L);
    config.addAllowedMethod("OPTIONS");
    config.addAllowedMethod("HEAD");
    config.addAllowedMethod("GET");
    config.addAllowedMethod("PUT");
    config.addAllowedMethod("POST");
    config.addAllowedMethod("DELETE");
    config.addAllowedMethod("PATCH");
    source.registerCorsConfiguration("/**", config);

    log.info("== [CORS] == already enabled cors on [OPTIONS/HEAD/GET/PUT/POST/DELETE/PATCH] mapping path:/**");

    return new CorsFilter(source);
  }


  @ConfigurationProperties(prefix = CORS_PREFIX)
  public static class CorsProperties {

    /**
     * Enabled flag: default: true
     */
    private boolean enabled = true;

    public boolean isEnabled() {
      return enabled;
    }

    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }
  }
}
