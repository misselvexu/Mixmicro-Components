package xyz.vopen.mixmicro.components.enhance.metrics.test;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import org.springframework.context.annotation.Configuration;
import xyz.vopen.mixmicro.components.enhance.metrics.spring.config.annotation.EnableMixmicroMetrics;
import xyz.vopen.mixmicro.components.enhance.metrics.spring.config.annotation.MetricsConfigurerAdapter;

import java.util.concurrent.TimeUnit;


@Configuration
@EnableMixmicroMetrics
public class SpringConfiguringClass extends MetricsConfigurerAdapter {

  @Override
  public void configureReporters(MetricRegistry metricRegistry) {
    // registerReporter allows the MetricsConfigurerAdapter to shut down the reporter when the Spring context is closed
    registerReporter(ConsoleReporter.forRegistry(metricRegistry).build())
        .start(1, TimeUnit.MINUTES);
  }
}
