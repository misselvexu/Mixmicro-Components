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
package xyz.vopen.mixmicro.components.enhance.security.intf.cli;

import xyz.vopen.mixmicro.components.enhance.security.registry.AlgorithmRegistry;

import java.util.Set;

/**
 * This class supports the CLI "listAlgorithms" operation.
 *
 * <p><b>It should NEVER be used inside your code, only from the supplied command-line tools</b>.
 *
 * @since 1.7
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public final class AlgorithmRegistryCLI {

  /**
   * CLI execution method.
   *
   * @param args the command execution arguments
   */
  public static void main(final String[] args) {

    try {

      final Set digestAlgos = AlgorithmRegistry.getAllDigestAlgorithms();
      final Set pbeAlgos = AlgorithmRegistry.getAllPBEAlgorithms();

      System.out.println();
      System.out.println("DIGEST ALGORITHMS:   " + digestAlgos);
      System.out.println();
      System.out.println("PBE ALGORITHMS:      " + pbeAlgos);
      System.out.println();

    } catch (Throwable t) {
      t.printStackTrace(System.err);
    }
  }

  /*
   * Instantiation is forbidden.
   */
  private AlgorithmRegistryCLI() {
    super();
  }
}
