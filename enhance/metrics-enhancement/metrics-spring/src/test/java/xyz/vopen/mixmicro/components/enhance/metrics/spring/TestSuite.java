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

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.codahale.metrics.SharedMetricRegistries;

@RunWith(Suite.class)
// @formatter:off
@SuiteClasses({
		AopFieldInjectionInteractionTest.class,
		CovariantReturnTypeTest.class,
		EnableMetricsTest.class,
		HealthCheckTest.class,
		LegacyAnnotationMeteredClassTest.class,
		LegacyMetricAnnotationTest.class,
		MeteredClassImpementsInterfaceTest.class,
		MeteredClassTest.class,
		MeteredInterfaceTest.class,
		MetricAnnotationTest.class,
		ProxyTargetClassTest.class,
		RegistryTest.class,
		ReporterTest.class,
		SharedRegistryTest.class
	})
// @formatter:on
public class TestSuite {

	@BeforeClass
	public static void beforeClass() {
		SharedMetricRegistries.clear();
	}

}
