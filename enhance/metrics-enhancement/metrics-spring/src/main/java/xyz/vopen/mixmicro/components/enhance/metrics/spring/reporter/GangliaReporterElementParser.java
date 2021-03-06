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

import static xyz.vopen.mixmicro.components.enhance.metrics.spring.reporter.GangliaReporterFactoryBean.*;

public class GangliaReporterElementParser extends AbstractReporterElementParser {

	private static final String UUID_STRING_REGEX = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";

	@Override
	public String getType() {
		return "ganglia";
	}

	@Override
	protected Class<?> getBeanClass() {
		return GangliaReporterFactoryBean.class;
	}

	@Override
	protected void validate(ValidationContext c) {
		c.require(GangliaReporterFactoryBean.GROUP);
		c.require(GangliaReporterFactoryBean.PORT, PORT_NUMBER_REGEX, "Port number is required and must be between 1-65536");
		c.require(GangliaReporterFactoryBean.UDP_MODE);
		c.require(GangliaReporterFactoryBean.PERIOD, DURATION_STRING_REGEX, "Period is required and must be in the form '\\d+(ns|us|ms|s|m|h|d)'");
		c.require(GangliaReporterFactoryBean.TTL, INTEGER_REGEX);

		c.optional(GangliaReporterFactoryBean.SPOOF);
		c.optional(GangliaReporterFactoryBean.PROTOCOL, "^v?3\\.[01]$", "Protocol version must be 'v3.0' or 'v3.1'");
		c.optional(GangliaReporterFactoryBean.DMAX, INTEGER_REGEX);
		c.optional(GangliaReporterFactoryBean.TMAX, INTEGER_REGEX);

		c.optional(GangliaReporterFactoryBean.RATE_UNIT, TIMEUNIT_STRING_REGEX, "Rate unit must be one of the enum constants from java.util.concurrent.TimeUnit");
		c.optional(GangliaReporterFactoryBean.DURATION_UNIT, TIMEUNIT_STRING_REGEX, "Duration unit must be one of the enum constants from java.util.concurrent.TimeUnit");

		c.optional(GangliaReporterFactoryBean.UUID, UUID_STRING_REGEX);

		c.optional(PREFIX);
		c.optional(PREFIX_SUPPLIER_REF);
		if (c.has(PREFIX) && c.has(PREFIX_SUPPLIER_REF)) {
			c.reject(PREFIX_SUPPLIER_REF, "Reporter element must not specify both the 'prefix' and 'prefix-supplier-ref' attributes");
		}

		c.optional(FILTER_PATTERN);
		c.optional(FILTER_REF);
		if (c.has(FILTER_PATTERN) && c.has(FILTER_REF)) {
			c.reject(FILTER_REF, "Reporter element must not specify both the 'filter' and 'filter-ref' attributes");
		}

		c.rejectUnmatchedProperties();
	}

}
