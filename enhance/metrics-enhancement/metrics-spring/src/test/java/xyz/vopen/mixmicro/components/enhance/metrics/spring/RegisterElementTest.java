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
package xyz.vopen.mixmicro.components.enhance.metrics.spring;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.codahale.metrics.MetricRegistry;

public class RegisterElementTest {

	@Test
	public void testRegisterMetrics() {
		ClassPathXmlApplicationContext ctx = null;
		try {
			ctx = new ClassPathXmlApplicationContext("classpath:register-element-test.xml");
			MetricRegistry registry = ctx.getBean(MetricRegistry.class);
			Assert.assertTrue(registry.getMetrics().size() > 0);
			for (String metricName : registry.getMetrics().keySet()) {
				Assert.assertTrue(metricName.startsWith("jvm."));
			}
		}
		finally {
			if (ctx != null) {
				ctx.close();
			}
		}
	}

}
