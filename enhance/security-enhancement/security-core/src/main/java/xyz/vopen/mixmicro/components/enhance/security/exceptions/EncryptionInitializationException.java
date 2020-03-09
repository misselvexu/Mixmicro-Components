/*
 * =============================================================================
 *
 *   Copyright (c) 2017-2019, VOPEN.XYZ (http://vopen.xyz)
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 * =============================================================================
 */
package xyz.vopen.mixmicro.components.enhance.security.exceptions;

/**
 * Exception thrown when an error is raised during initialization of an entity.
 *
 * @since 1.0
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public final class EncryptionInitializationException extends RuntimeException {

  private static final long serialVersionUID = 8929638240023639778L;

  public EncryptionInitializationException() {
    super();
  }

  public EncryptionInitializationException(final Throwable t) {
    super(t);
  }

  public EncryptionInitializationException(final String msg, final Throwable t) {
    super(msg, t);
  }

  public EncryptionInitializationException(final String msg) {
    super(msg);
  }
}
