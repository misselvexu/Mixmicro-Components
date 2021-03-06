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
package xyz.vopen.mixmicro.components.enhance.metrics.spring.servlets;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.servlets.MetricsServlet;
import com.codahale.metrics.servlets.HealthCheckServlet;

public class MetricsServletsContextListener implements ServletContextListener {

	@Autowired
	private MetricRegistry metricRegistry;

	@Autowired
	private HealthCheckRegistry healthCheckRegistry;

	private final MetricsServletContextListener metricsServletContextListener = new MetricsServletContextListener();
	private final HealthCheckServletContextListener healthCheckServletContextListener = new HealthCheckServletContextListener();

	@Override
	public void contextInitialized(ServletContextEvent event) {
		WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext()).getAutowireCapableBeanFactory().autowireBean(this);

		metricsServletContextListener.contextInitialized(event);
		healthCheckServletContextListener.contextInitialized(event);
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {}

	class MetricsServletContextListener extends MetricsServlet.ContextListener {

		@Override
		protected MetricRegistry getMetricRegistry() {
			return metricRegistry;
		}

	}

	class HealthCheckServletContextListener extends HealthCheckServlet.ContextListener {

		@Override
		protected HealthCheckRegistry getHealthCheckRegistry() {
			return healthCheckRegistry;
		}

	}

}
