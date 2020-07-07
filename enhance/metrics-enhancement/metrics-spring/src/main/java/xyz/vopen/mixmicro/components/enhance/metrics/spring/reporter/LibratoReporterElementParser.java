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

public class LibratoReporterElementParser extends AbstractReporterElementParser {

	@Override
	public String getType() {
		return "librato";
	}

	@Override
	protected Class<?> getBeanClass() {
		return LibratoReporterFactoryBean.class;
	}

	@Override
	protected void validate(ValidationContext c) {
		c.require(LibratoReporterFactoryBean.USERNAME);
		c.require(LibratoReporterFactoryBean.TOKEN);
		c.require(LibratoReporterFactoryBean.PERIOD, DURATION_STRING_REGEX, "Period is required and must be in the form '\\d+(ns|us|ms|s|m|h|d)'");

		c.optional(LibratoReporterFactoryBean.SOURCE);
		c.optional(LibratoReporterFactoryBean.SOURCE_SUPPLIER_REF);
		if (!c.has(LibratoReporterFactoryBean.SOURCE) && !c.has(LibratoReporterFactoryBean.SOURCE_SUPPLIER_REF)) {
			c.require(LibratoReporterFactoryBean.SOURCE);
		}
		else if (c.has(LibratoReporterFactoryBean.SOURCE) && c.has(LibratoReporterFactoryBean.SOURCE_SUPPLIER_REF)) {
			c.reject(LibratoReporterFactoryBean.SOURCE_SUPPLIER_REF, "Reporter element must not specify both the 'source' and 'source-supplier-ref' attributes");
		}

		c.optional(LibratoReporterFactoryBean.TIMEOUT, DURATION_STRING_REGEX, "Timeout must be in the form '\\d+(ns|us|ms|s|m|h|d)'");
		c.optional(LibratoReporterFactoryBean.NAME);
		c.optional(LibratoReporterFactoryBean.SANITIZER_REF);
		c.optional(LibratoReporterFactoryBean.PREFIX_DELIMITER);
		c.optional(LibratoReporterFactoryBean.CLOCK_REF);
		c.optional(LibratoReporterFactoryBean.SOURCE_REGEX);

		c.optional(LibratoReporterFactoryBean.HTTP_POSTER_REF);
		c.optional(LibratoReporterFactoryBean.HTTP_CLIENT_CONFIG_REF);

		c.optional(LibratoReporterFactoryBean.DELETE_IDLE_STATS);
		c.optional(LibratoReporterFactoryBean.OMIT_COMPLEX_GAUGES);

		c.optional(LibratoReporterFactoryBean.EXPANSION_CONFIG);
		c.optional(LibratoReporterFactoryBean.EXPANSION_CONFIG_REF);
		if (c.has(LibratoReporterFactoryBean.EXPANSION_CONFIG) && c.has(LibratoReporterFactoryBean.EXPANSION_CONFIG_REF)) {
			c.reject(LibratoReporterFactoryBean.EXPANSION_CONFIG, "Librato Reporter element must not specify both the 'expansion-config' and 'expansion-config-ref' attributes");
		}

		c.optional(LibratoReporterFactoryBean.RATE_UNIT, TIMEUNIT_STRING_REGEX, "Rate unit must be one of the enum constants from java.util.concurrent.TimeUnit");
		c.optional(LibratoReporterFactoryBean.DURATION_UNIT, TIMEUNIT_STRING_REGEX, "Duration unit must be one of the enum constants from java.util.concurrent.TimeUnit");

		c.optional(AbstractReporterFactoryBean.PREFIX);
		c.optional(AbstractReporterFactoryBean.PREFIX_SUPPLIER_REF);
		if (c.has(AbstractReporterFactoryBean.PREFIX) && c.has(AbstractReporterFactoryBean.PREFIX_SUPPLIER_REF)) {
			c.reject(AbstractReporterFactoryBean.PREFIX_SUPPLIER_REF, "Reporter element must not specify both the 'prefix' and 'prefix-supplier-ref' attributes");
		}

		c.optional(AbstractReporterFactoryBean.FILTER_PATTERN);
		c.optional(AbstractReporterFactoryBean.FILTER_REF);
		if (c.has(AbstractReporterFactoryBean.FILTER_PATTERN) && c.has(AbstractReporterFactoryBean.FILTER_REF)) {
			c.reject(AbstractReporterFactoryBean.FILTER_REF, "Reporter element must not specify both the 'filter' and 'filter-ref' attributes");
		}

		c.rejectUnmatchedProperties();
	}

}
