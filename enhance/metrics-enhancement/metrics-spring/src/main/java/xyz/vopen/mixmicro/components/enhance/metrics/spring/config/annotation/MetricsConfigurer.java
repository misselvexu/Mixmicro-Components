/*
 * Copyright (C) 2018 VOPEN.XYZ (iskp.me@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.vopen.mixmicro.components.enhance.metrics.spring.config.annotation;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;

/**
 * Defines callback methods to customize the Java-based configuration
 * for Spring Metrics enabled via {@link EnableMixmicroMetrics @EnableMetrics}.
 *
 * @see EnableMixmicroMetrics
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @since 3.0
 */
public interface MetricsConfigurer {

	/**
	 * Configure reporters.
	 * @param metricRegistry
	 */
	void configureReporters(MetricRegistry metricRegistry);

	/**
	 * Override this method to provide a custom {@code MetricRegistry}.
	 * @return
	 */
	MetricRegistry getMetricRegistry();

	/**
	 * Override this method to provide a custom {@code HealthCheckRegistry}.
	 * @return
	 */
	HealthCheckRegistry getHealthCheckRegistry();

}
