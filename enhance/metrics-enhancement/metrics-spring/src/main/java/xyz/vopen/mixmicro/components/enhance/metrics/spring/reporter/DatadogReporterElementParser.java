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
package xyz.vopen.mixmicro.components.enhance.metrics.spring.reporter;

public class DatadogReporterElementParser extends AbstractReporterElementParser {

	@Override
	public String getType() {
		return "datadog";
	}

	@Override
	protected Class<?> getBeanClass() {
		return DatadogReporterFactoryBean.class;
	}

	@Override
	protected void validate(ValidationContext c) {
		// Required
		c.require(DatadogReporterFactoryBean.TRANSPORT, "^http|udp|statsd$", "Transport must be one of: http, udp, statsd");
		c.require(DatadogReporterFactoryBean.PERIOD, DURATION_STRING_REGEX, "Period is required and must be in the form '\\d+(ns|us|ms|s|m|h|d)'");

		if ("http".equals(c.get(DatadogReporterFactoryBean.TRANSPORT))) {
			c.require(DatadogReporterFactoryBean.API_KEY);
		}

		// HTTP only properties
		c.optional(DatadogReporterFactoryBean.CONNECT_TIMEOUT, DURATION_STRING_REGEX, "Connect timeout must be in the form '\\d+(ns|us|ms|s|m|h|d)'");
		c.optional(DatadogReporterFactoryBean.SOCKET_TIMEOUT, DURATION_STRING_REGEX, "Socket timeout must be in the form '\\d+(ns|us|ms|s|m|h|d)'");

		// UDP only properties
		c.optional(DatadogReporterFactoryBean.STATSD_HOST);
		c.optional(DatadogReporterFactoryBean.STATSD_PORT, PORT_NUMBER_REGEX, "Port number must be an integer between 1-65536");
		c.optional(DatadogReporterFactoryBean.STATSD_PREFIX);

		// All
		c.optional(DatadogReporterFactoryBean.HOST);
		c.optional(DatadogReporterFactoryBean.EC2_HOST);
		c.optional(DatadogReporterFactoryBean.EXPANSION);
		c.optional(DatadogReporterFactoryBean.TAGS);
		c.optional(DatadogReporterFactoryBean.DYNAMIC_TAG_CALLBACK_REF);
		c.optional(DatadogReporterFactoryBean.METRIC_NAME_FORMATTER_REF);

		c.optional(DatadogReporterFactoryBean.PREFIX);
		c.optional(DatadogReporterFactoryBean.CLOCK_REF);

		c.optional(DatadogReporterFactoryBean.RATE_UNIT, TIMEUNIT_STRING_REGEX, "Rate unit must be one of the enum constants from java.util.concurrent.TimeUnit");
		c.optional(DatadogReporterFactoryBean.DURATION_UNIT, TIMEUNIT_STRING_REGEX, "Duration unit must be one of the enum constants from java.util.concurrent.TimeUnit");

		c.optional(AbstractReporterFactoryBean.FILTER_PATTERN);
		c.optional(AbstractReporterFactoryBean.FILTER_REF);
		if (c.has(AbstractReporterFactoryBean.FILTER_PATTERN) && c.has(AbstractReporterFactoryBean.FILTER_REF)) {
			c.reject(AbstractReporterFactoryBean.FILTER_REF, "Reporter element must not specify both the 'filter' and 'filter-ref' attributes");
		}

		c.rejectUnmatchedProperties();
	}

}
