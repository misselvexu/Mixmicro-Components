package xyz.vopen.mixmicro.components.boot.dbm.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.vopen.mixmicro.components.boot.dbm.DBMRouterProperties;
import xyz.vopen.mixmicro.components.boot.dbm.aspect.RouterAspect;

import javax.sql.DataSource;

import static xyz.vopen.mixmicro.components.boot.dbm.DBMRouterProperties.MIXMICRO_DBM_CONFIG_PROPERTIES_PREFIX;

/**
 * {@link DBMRouterAutoConfiguration}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 7/17/20
 */
@Configuration
@ConditionalOnProperty(
    prefix = MIXMICRO_DBM_CONFIG_PROPERTIES_PREFIX,
    value = "enabled",
    havingValue = "true")
@EnableConfigurationProperties(DBMRouterProperties.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class DBMRouterAutoConfiguration {

  @Bean
  @ConditionalOnBean(DataSource.class)
  RouterAspect routerAspect(DataSource dataSource) {
    return new RouterAspect(dataSource);
  }


}
