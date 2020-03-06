package xyz.vopen.mixmicro.components.enhance.cache.autoconfigure;

import xyz.vopen.mixmicro.components.enhance.cache.CacheBuilder;
import xyz.vopen.mixmicro.components.enhance.cache.CacheConfigException;
import xyz.vopen.mixmicro.components.enhance.cache.external.ExternalCacheBuilder;
import xyz.vopen.mixmicro.components.enhance.cache.redis.springdata.RedisSpringDataCacheBuilder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.util.Map;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
@Configuration
@Conditional(RedisSpringDataAutoConfiguration.SpringDataRedisCondition.class)
public class RedisSpringDataAutoConfiguration {

    public static class SpringDataRedisCondition extends MixCacheCondition {
        public SpringDataRedisCondition() {
            super("redis.springdata");
        }
    }

    @Bean
    public SpringDataRedisAutoInit springDataRedisAutoInit() {
        return new SpringDataRedisAutoInit();
    }

    public static class SpringDataRedisAutoInit extends ExternalCacheAutoInit implements ApplicationContextAware {

        private ApplicationContext applicationContext;

        public SpringDataRedisAutoInit() {
            super("redis.springdata");
        }

        @Override
        protected CacheBuilder initCache(ConfigTree ct, String cacheAreaWithPrefix) {
            Map<String, RedisConnectionFactory> beans = applicationContext.getBeansOfType(RedisConnectionFactory.class);
            if (beans == null || beans.isEmpty()) {
                throw new CacheConfigException("no RedisConnectionFactory in spring context");
            }
            RedisConnectionFactory factory = beans.values().iterator().next();
            if (beans.size() > 1) {
                String connectionFactoryName = ct.getProperty("connectionFactory");
                if (connectionFactoryName == null) {
                    throw new CacheConfigException(
                            "connectionFactory is required, because there is multiple RedisConnectionFactory in Spring context");
                }
                if (!beans.containsKey(connectionFactoryName)) {
                    throw new CacheConfigException("there is no RedisConnectionFactory named "
                            + connectionFactoryName + " in Spring context");
                }
                factory = beans.get(connectionFactoryName);
            }
            ExternalCacheBuilder builder = RedisSpringDataCacheBuilder.createBuilder().connectionFactory(factory);
            parseGeneralConfig(builder, ct);
            return builder;
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
        }
    }
}
