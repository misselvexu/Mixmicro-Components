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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.Counted;
import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;

/**
 * Purpose of test:
 * Verify that calling a method that is annotated at the interface
 * level but not the implementation level doesn't throw an NPE.
 * Also verifies that it doesn't register any metrics.
 */
public class MeteredInterfaceTest {

	ClassPathXmlApplicationContext ctx;
	MetricRegistry metricRegistry;

	@Before
	public void init() {
		this.ctx = new ClassPathXmlApplicationContext("classpath:metered-interface.xml");
		this.metricRegistry = this.ctx.getBean(MetricRegistry.class);
	}

	@After
	public void destroy() throws Throwable {
		this.ctx.stop();
	}

	@Test
	public void noMetricsRegistered() {
		Assert.assertTrue("No metrics registered", this.metricRegistry.getNames().isEmpty());
	}

	@Test
	public void testMeteredInterface() {
		MeteredInterface mi = ctx.getBean(MeteredInterface.class);
		Assert.assertNotNull("Expected to be able to get MeteredInterface by interface and not by class.", mi);
	}

	@Test(expected = NoSuchBeanDefinitionException.class)
	public void testMeteredInterfaceImpl() {
		MeteredInterfaceImpl mc = ctx.getBean(MeteredInterfaceImpl.class);
		Assert.assertNull("Expected to be unable to get MeteredInterfaceImpl by class.", mc);
	}

	@Test
	public void testTimedMethod() {
		ctx.getBean(MeteredInterface.class).timedMethod();
		Assert.assertTrue("No metrics should be registered", this.metricRegistry.getNames().isEmpty());
	}

	@Test
	public void testMeteredMethod() {
		ctx.getBean(MeteredInterface.class).meteredMethod();
		Assert.assertTrue("No metrics should be registered", this.metricRegistry.getNames().isEmpty());
	}

	@Test(expected = BogusException.class)
	public void testExceptionMeteredMethod() throws Throwable {
		try {
			ctx.getBean(MeteredInterface.class).exceptionMeteredMethod();
		}
		catch (Throwable t) {
			Assert.assertTrue("No metrics should be registered", this.metricRegistry.getNames().isEmpty());
			throw t;
		}
	}

	@Test
	public void testCountedMethod() {
		ctx.getBean(MeteredInterface.class).countedMethod();
		Assert.assertTrue("No metrics should be registered", this.metricRegistry.getNames().isEmpty());
	}

	public interface MeteredInterface {

		@Timed
		public void timedMethod();

		@Metered
		public void meteredMethod();

		@ExceptionMetered
		public void exceptionMeteredMethod() throws Throwable;

		@Counted
		public void countedMethod();

	}

	public static class MeteredInterfaceImpl implements MeteredInterface {

		@Override
		public void timedMethod() {}

		@Override
		public void meteredMethod() {}

		@Override
		public void exceptionMeteredMethod() throws Throwable {
			throw new BogusException();
		}

		@Override
		public void countedMethod() {}

	}

	@SuppressWarnings("serial")
	public static class BogusException extends Throwable {}

}
