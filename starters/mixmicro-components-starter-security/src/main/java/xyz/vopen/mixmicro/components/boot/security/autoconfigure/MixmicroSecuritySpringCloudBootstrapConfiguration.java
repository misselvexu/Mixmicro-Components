package xyz.vopen.mixmicro.components.boot.security.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import xyz.vopen.mixmicro.components.boot.security.core.EnableEncryptablePropertiesConfiguration;

/**
 * Bootstrap core applicable only in spring-cloud environments. Can be explicitly
 * turned-off by <code>mixmicro.security.encryptor.bootstrap=false</code> core (in
 * bootstrap.properties or as a command line argument) in that case MixSecurity will be
 * auto-configured as usual.
 *
 * @author Fahim Farook
 */
@Configuration
@ConditionalOnProperty(
    name = "mixmicro.security.encryptor.bootstrap",
    havingValue = "true",
    matchIfMissing = true)
@Import(EnableEncryptablePropertiesConfiguration.class)
public class MixmicroSecuritySpringCloudBootstrapConfiguration {}
