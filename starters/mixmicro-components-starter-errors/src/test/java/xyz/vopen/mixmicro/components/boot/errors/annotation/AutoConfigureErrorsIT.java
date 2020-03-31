package xyz.vopen.mixmicro.components.boot.errors.annotation;

import xyz.vopen.mixmicro.components.boot.errors.WebErrorHandlers;
import org.junit.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebFlux;
import org.springframework.boot.test.context.runner.ReactiveWebApplicationContextRunner;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import xyz.vopen.mixmicro.components.boot.errors.conf.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link AutoConfigureErrors} annotation.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class AutoConfigureErrorsIT {

    private final WebApplicationContextRunner servletRunner = new WebApplicationContextRunner()
        .withBean(ServerProperties.class)
        .withUserConfiguration(TestConfig.class);

    private final ReactiveWebApplicationContextRunner reactiveRunner = new ReactiveWebApplicationContextRunner()
        .withBean(ServerProperties.class)
        .withUserConfiguration(ReactiveTestConfig.class);

    @Test
    public void annotation_ShouldEnableTheWebErrorsSupport() {
        servletRunner.run(ctx -> {
            WebErrorHandlers handlers = ctx.getBean(WebErrorHandlers.class);

            assertThat(handlers).isNotNull();
        });

        reactiveRunner.run(ctx -> {
            WebErrorHandlers handlers = ctx.getBean(WebErrorHandlers.class);

            assertThat(handlers).isNotNull();
        });
    }

    @Test
    public void annotation_ShouldImportServletSpecificAutoConfigurationsForServletStack() {
        servletRunner.run(ctx -> {
            assertThat(ctx).hasSingleBean(ErrorsAutoConfiguration.class);
            assertThat(ctx).hasSingleBean(ServletErrorsAutoConfiguration.class);
            assertThat(ctx).hasSingleBean(ServletSecurityErrorsAutoConfiguration.class);
            assertThat(ctx).doesNotHaveBean(ReactiveErrorsAutoConfiguration.class);
            assertThat(ctx).doesNotHaveBean(ReactiveSecurityErrorsAutoConfiguration.class);
        });
    }

    @Test
    public void annotation_ShouldImportWebFluxSpecificAutoConfigurationsForReactiveStack() {
        reactiveRunner.run(ctx -> {
            assertThat(ctx).hasSingleBean(ErrorsAutoConfiguration.class);
            assertThat(ctx).hasSingleBean(ReactiveErrorsAutoConfiguration.class);
            assertThat(ctx).hasSingleBean(ReactiveSecurityErrorsAutoConfiguration.class);
            assertThat(ctx).doesNotHaveBean(ServletErrorsAutoConfiguration.class);
            assertThat(ctx).doesNotHaveBean(ServletSecurityErrorsAutoConfiguration.class);
        });
    }

    @AutoConfigureErrors
    static class TestConfig {
    }

    @AutoConfigureErrors
    @AutoConfigureWebFlux
    @EnableWebFluxSecurity
    @ImportAutoConfiguration(ErrorWebFluxAutoConfiguration.class)
    static class ReactiveTestConfig {

        @Bean
        public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
            return http.build();
        }
    }
}
