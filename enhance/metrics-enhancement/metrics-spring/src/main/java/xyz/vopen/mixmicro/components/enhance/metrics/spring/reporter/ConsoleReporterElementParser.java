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

public class ConsoleReporterElementParser extends AbstractReporterElementParser {

	private static final String LOCALE_STRING_REGEX = "^[a-z]{2}(_[A-Z]{2})?$";

	@Override
	public String getType() {
		return "console";
	}

	@Override
	protected Class<?> getBeanClass() {
		return ConsoleReporterFactoryBean.class;
	}

	@Override
	protected void validate(ValidationContext c) {
		c.require(ConsoleReporterFactoryBean.PERIOD, DURATION_STRING_REGEX, "Period is required and must be in the form '\\d+(ns|us|ms|s|m|h|d)'");

		c.optional(ConsoleReporterFactoryBean.CLOCK_REF);
		c.optional(ConsoleReporterFactoryBean.OUTPUT_REF);

		c.optional(ConsoleReporterFactoryBean.LOCALE, LOCALE_STRING_REGEX, "Locale must be in the proper format");
		c.optional(ConsoleReporterFactoryBean.TIMEZONE); // Difficult to validate, if invalid will fall back to GMT

		c.optional(ConsoleReporterFactoryBean.RATE_UNIT, TIMEUNIT_STRING_REGEX, "Rate unit must be one of the enum constants from java.util.concurrent.TimeUnit");
		c.optional(ConsoleReporterFactoryBean.DURATION_UNIT, TIMEUNIT_STRING_REGEX, "Duration unit must be one of the enum constants from java.util.concurrent.TimeUnit");

		c.optional(AbstractReporterFactoryBean.FILTER_PATTERN);
		c.optional(AbstractReporterFactoryBean.FILTER_REF);
		if (c.has(AbstractReporterFactoryBean.FILTER_PATTERN) && c.has(AbstractReporterFactoryBean.FILTER_REF)) {
			c.reject(AbstractReporterFactoryBean.FILTER_REF, "Reporter element must not specify both the 'filter' and 'filter-ref' attributes");
		}

		c.rejectUnmatchedProperties();
	}

}
