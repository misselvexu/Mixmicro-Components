package xyz.vopen.mixmicro.components.boot.security.autoconfigure;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import xyz.vopen.mixmicro.components.boot.security.core.EnableEncryptablePropertiesConfiguration;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
@Configuration
@Import(EnableEncryptablePropertiesConfiguration.class)
public class MixmicroSecuritySpringBootAutoConfiguration {}
