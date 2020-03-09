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
package xyz.vopen.mixmicro.components.enhance.security.properties;

import xyz.vopen.mixmicro.components.enhance.security.encryption.StringEncryptor;
import xyz.vopen.mixmicro.components.enhance.security.util.text.TextEncryptor;

/**
 * Utility class to encrypt/decrypt values in properties files which could be encrypted.
 *
 * <p>A value is considered "encrypted" when it appears surrounded by <tt>ENC(...)</tt>, like:
 *
 * <p><center> <tt>my.value=ENC(!"DGAS24FaIO$)</tt> </center>
 *
 * <p><b>This class is meant for internal Mixsecurity use only.</b>
 *
 * @since 1.4
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public final class PropertyValueEncryptionUtils {

  private static final String ENCRYPTED_VALUE_PREFIX = "ENC(";
  private static final String ENCRYPTED_VALUE_SUFFIX = ")";

  public static boolean isEncryptedValue(final String value) {
    if (value == null) {
      return false;
    }
    final String trimmedValue = value.trim();
    return (trimmedValue.startsWith(ENCRYPTED_VALUE_PREFIX)
        && trimmedValue.endsWith(ENCRYPTED_VALUE_SUFFIX));
  }

  private static String getInnerEncryptedValue(final String value) {
    return value.substring(
        ENCRYPTED_VALUE_PREFIX.length(), (value.length() - ENCRYPTED_VALUE_SUFFIX.length()));
  }

  public static String decrypt(final String encodedValue, final StringEncryptor encryptor) {
    return encryptor.decrypt(getInnerEncryptedValue(encodedValue.trim()));
  }

  public static String decrypt(final String encodedValue, final TextEncryptor encryptor) {
    return encryptor.decrypt(getInnerEncryptedValue(encodedValue.trim()));
  }

  public static String encrypt(final String decodedValue, final StringEncryptor encryptor) {
    return ENCRYPTED_VALUE_PREFIX + encryptor.encrypt(decodedValue) + ENCRYPTED_VALUE_SUFFIX;
  }

  public static String encrypt(final String decodedValue, final TextEncryptor encryptor) {
    return ENCRYPTED_VALUE_PREFIX + encryptor.encrypt(decodedValue) + ENCRYPTED_VALUE_SUFFIX;
  }

  private PropertyValueEncryptionUtils() {
    super();
  }
}
