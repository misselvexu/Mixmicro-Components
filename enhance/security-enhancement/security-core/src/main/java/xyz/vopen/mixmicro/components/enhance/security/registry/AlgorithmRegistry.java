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
package xyz.vopen.mixmicro.components.enhance.security.registry;

import java.security.Security;
import java.util.*;

/**
 * Utility class for retrieving the names of all the digest or encryption algorithms available to
 * Mixsecurity.
 *
 * <p>This class is <i>thread-safe</i>.
 *
 * @since 1.7
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public final class AlgorithmRegistry {

  /**
   * Returns a set with the names of all the registered digest algorithms. This set will also
   * include algorithms from any third-party (non-JVM) registered providers.
   *
   * @since 1.7
   * @return a Set of Strings with the names of all the registered digest algorithms.
   */
  public static Set getAllDigestAlgorithms() {
    final List algos = new ArrayList(Security.getAlgorithms("MessageDigest"));
    Collections.sort(algos);
    return Collections.unmodifiableSet(new LinkedHashSet(algos));
  }

  /**
   * Returns a set with the names of all the registered PBE (Password-Based Encryption) algorithms.
   * This set will also include algorithms from any third-party (non-JVM) registered providers.
   *
   * @since 1.7
   * @return a Set of Strings with the names of all the registered PBE algorithms.
   */
  public static Set getAllPBEAlgorithms() {
    final List algos = new ArrayList(Security.getAlgorithms("Cipher"));
    Collections.sort(algos);
    final LinkedHashSet pbeAlgos = new LinkedHashSet();
    final Iterator algosIter = algos.iterator();
    while (algosIter.hasNext()) {
      final String algo = (String) algosIter.next();
      if (algo != null && algo.startsWith("PBE")) {
        pbeAlgos.add(algo);
      }
    }
    return Collections.unmodifiableSet(pbeAlgos);
  }

  private AlgorithmRegistry() {
    super();
  }
}
