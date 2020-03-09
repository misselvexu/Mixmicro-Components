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

import xyz.vopen.mixmicro.components.enhance.security.intf.service.MixmicroSecurityStatelessService;

import java.util.Properties;

/**
 * This class supports the CLI "encrypt" operation.
 *
 * <p><b>It should NEVER be used inside your code, only from the supplied command-line tools</b>.
 *
 * @since 1.4
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public final class MixmicroSecurityPBEStringEncryptionCLI {

  /*
   * The required arguments for this CLI operation.
   */
  private static final String[][] VALID_REQUIRED_ARGUMENTS =
      new String[][] {
        new String[] {ArgumentNaming.ARG_INPUT}, new String[] {ArgumentNaming.ARG_PASSWORD}
      };

  /*
   * The optional arguments for this CLI operation.
   */
  private static final String[][] VALID_OPTIONAL_ARGUMENTS =
      new String[][] {
        new String[] {ArgumentNaming.ARG_VERBOSE},
        new String[] {ArgumentNaming.ARG_ALGORITHM},
        new String[] {ArgumentNaming.ARG_KEY_OBTENTION_ITERATIONS},
        new String[] {ArgumentNaming.ARG_SALT_GENERATOR_CLASS_NAME},
        new String[] {ArgumentNaming.ARG_PROVIDER_NAME},
        new String[] {ArgumentNaming.ARG_PROVIDER_CLASS_NAME},
        new String[] {ArgumentNaming.ARG_STRING_OUTPUT_TYPE},
        new String[] {ArgumentNaming.ARG_IV_GENERATOR_CLASS_NAME}
      };

  /**
   * CLI execution method.
   *
   * @param args the command execution arguments
   */
  public static void main(final String[] args) {

    final boolean verbose = CLIUtils.getVerbosity(args);

    try {

      String applicationName = null;
      String[] arguments = null;
      if (args[0] == null || args[0].indexOf("=") != -1) {
        applicationName = MixmicroSecurityPBEStringEncryptionCLI.class.getName();
        arguments = args;
      } else {
        applicationName = args[0];
        arguments = new String[args.length - 1];
        System.arraycopy(args, 1, arguments, 0, args.length - 1);
      }

      final Properties argumentValues =
          CLIUtils.getArgumentValues(
              applicationName, arguments,
              VALID_REQUIRED_ARGUMENTS, VALID_OPTIONAL_ARGUMENTS);

      CLIUtils.showEnvironment(verbose);

      final MixmicroSecurityStatelessService service = new MixmicroSecurityStatelessService();

      final String input = argumentValues.getProperty(ArgumentNaming.ARG_INPUT);

      CLIUtils.showArgumentDescription(argumentValues, verbose);

      final String result =
          service.encrypt(
              input,
              argumentValues.getProperty(ArgumentNaming.ARG_PASSWORD),
              null,
              null,
              argumentValues.getProperty(ArgumentNaming.ARG_ALGORITHM),
              null,
              null,
              argumentValues.getProperty(ArgumentNaming.ARG_KEY_OBTENTION_ITERATIONS),
              null,
              null,
              argumentValues.getProperty(ArgumentNaming.ARG_SALT_GENERATOR_CLASS_NAME),
              null,
              null,
              argumentValues.getProperty(ArgumentNaming.ARG_PROVIDER_NAME),
              null,
              null,
              argumentValues.getProperty(ArgumentNaming.ARG_PROVIDER_CLASS_NAME),
              null,
              null,
              argumentValues.getProperty(ArgumentNaming.ARG_STRING_OUTPUT_TYPE),
              null,
              null,
              argumentValues.getProperty(ArgumentNaming.ARG_IV_GENERATOR_CLASS_NAME),
              null,
              null);

      CLIUtils.showOutput(result, verbose);

    } catch (Throwable t) {
      CLIUtils.showError(t, verbose);
    }
  }

  /*
   * Instantiation is forbidden.
   */
  private MixmicroSecurityPBEStringEncryptionCLI() {
    super();
  }
}
