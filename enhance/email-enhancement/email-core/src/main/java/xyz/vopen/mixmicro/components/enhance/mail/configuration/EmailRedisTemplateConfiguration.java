package xyz.vopen.mixmicro.components.enhance.mail.configuration;

import xyz.vopen.mixmicro.components.enhance.mail.model.MixmicroEmailSchedulingPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import xyz.vopen.mixmicro.components.enhance.mail.service.impl.ConditionalExpression;

import java.io.IOException;

/**
 * {@link EmailRedisTemplateConfiguration}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Configuration
@ConditionalOnExpression(ConditionalExpression.PERSISTENCE_IS_ENABLED_WITH_REDIS)
public class EmailRedisTemplateConfiguration {

  private final RedisConnectionFactory redisConnectionFactory;

  @Autowired
  public EmailRedisTemplateConfiguration(final RedisConnectionFactory redisConnectionFactory) {
    this.redisConnectionFactory = redisConnectionFactory;
  }

  @Bean
  @Qualifier("orderingTemplate")
  public StringRedisTemplate createOrderingTemplate() throws IOException {
    StringRedisTemplate template = new StringRedisTemplate(redisConnectionFactory);
    template.setEnableTransactionSupport(true);
    return template;
  }

  @Bean
  @Qualifier("valueTemplate")
  public RedisTemplate<String, MixmicroEmailSchedulingPayload> createValueTemplate() throws IOException {
    RedisTemplate<String, MixmicroEmailSchedulingPayload> template = new RedisTemplate<>();
    RedisSerializer<String> stringSerializer = new StringRedisSerializer();
    JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
    template.setKeySerializer(stringSerializer);
    template.setValueSerializer(jdkSerializationRedisSerializer);
    template.setHashKeySerializer(stringSerializer);
    template.setHashValueSerializer(stringSerializer);

    template.setConnectionFactory(redisConnectionFactory);
    template.setEnableTransactionSupport(true);
    template.afterPropertiesSet();
    return template;
  }

}
