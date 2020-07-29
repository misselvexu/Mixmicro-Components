package xyz.vopen.mixmicro.components.boot.logging.tracing.admin;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import xyz.vopen.framework.logging.admin.LoggingAdminFactoryBean;

/**
 * Mixmicro Boot Logging Admin SpringSecurity Config {@link WebSecurityConfigurerAdapter}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Configuration
@ConditionalOnClass({WebSecurityConfiguration.class, LoggingAdminFactoryBean.class})
@AutoConfigureAfter(MixmicroLoggingTracingAdminAutoConfiguration.class)
public class MixmicroLoggingTracingAdminSecurityAutoConfiguration extends WebSecurityConfigurerAdapter {
  /**
   * Logging Admin Login Page
   */
  private static final String LOGIN_PAGE = "/login";
  /**
   * Logging Admin UI Resource Prefix
   */
  private static final String ASSETS_RESOURCE_PREFIX = "/assets/**";

  /**
   * Configure logging admin security authentication related information Open Resource Path Access
   * {@link MixmicroLoggingTracingAdminSecurityAutoConfiguration#ASSETS_RESOURCE_PREFIX} Open Login Page
   * Path Access {@link MixmicroLoggingTracingAdminSecurityAutoConfiguration#LOGIN_PAGE} Enable Http Basic
   * Auth {@link HttpSecurity#httpBasic()} Disable Csrf {@link HttpSecurity#csrf()}
   *
   * @param http HttpSecurity Instance
   * @throws Exception Config Exception
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    SavedRequestAwareAuthenticationSuccessHandler successHandler =
        new SavedRequestAwareAuthenticationSuccessHandler();
    successHandler.setTargetUrlParameter("redirectTo");
    http.authorizeRequests()
        .antMatchers(ASSETS_RESOURCE_PREFIX)
        .permitAll()
        .antMatchers(LOGIN_PAGE)
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .formLogin()
        .loginPage(LOGIN_PAGE)
        .successHandler(successHandler)
        .and()
        .logout()
        .and()
        .httpBasic()
        .and()
        .csrf()
        .disable();
  }
}
