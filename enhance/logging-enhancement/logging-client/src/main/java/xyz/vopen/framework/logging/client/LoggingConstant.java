/*
 * Copyright (c) 2018 VOPEN.XYZ
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 *
 */

package xyz.vopen.framework.logging.client;

/**
 * Log Constant
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface LoggingConstant {
  /** TraceId Header Name */
  String HEADER_NAME_TRACE_ID = "MIXMICRO-LOGGING-X-TRACE-ID";
  /** Parent SpanId Header Name */
  String HEADER_NAME_PARENT_SPAN_ID = "MIXMICRO-LOGGING-X-PARENT-SPAN-ID";
}
