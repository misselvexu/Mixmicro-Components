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
package xyz.vopen.mixmicro.components.enhance.security.digest.config;

import xyz.vopen.mixmicro.components.enhance.security.commons.CommonUtils;

/**
 * Bean implementation for {@link StringDigesterConfig}. This class allows the values for the
 * configuration parameters to be set via "standard" <tt>setX</tt> methods.
 *
 * <p>For any of the configuration parameters, if its <tt>setX</tt> method is not called, a
 * <tt>null</tt> value will be returned by the corresponding <tt>getX</tt> method.
 *
 * @since 1.3
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class SimpleStringDigesterConfig extends SimpleDigesterConfig
    implements StringDigesterConfig {

  private Boolean unicodeNormalizationIgnored = null;
  private String stringOutputType = null;
  private String prefix = null;
  private String suffix = null;

  /** Creates a new <tt>SimpleStringDigesterConfig</tt> instance. */
  public SimpleStringDigesterConfig() {
    super();
  }

  /**
   * Sets whether the unicode text normalization step should be ignored.
   *
   * <p>The Java Virtual Machine internally handles all Strings as UNICODE. When digesting or
   * matching digests in security, these Strings are first <b>normalized to its NFC form</b> so that
   * digest matching is not affected by the specific form in which the messages where input.
   *
   * <p><b>It is normally safe (and recommended) to leave this parameter set to its default FALSE
   * value (and thus DO perform normalization operations)</b>. But in some specific cases in which
   * issues with legacy software could arise, it might be useful to set this to TRUE.
   *
   * <p>For more information on unicode text normalization, see this issue of <a
   * href="http://java.sun.com/mailers/techtips/corejava/2007/tt0207.html">Core Java Technologies
   * Tech Tips</a>.
   *
   * <p>If not set, null will be returned.
   *
   * <p>Determines the result of: {@link #isUnicodeNormalizationIgnored()}
   *
   * @param unicodeNormalizationIgnored whether the unicode text normalization step should be
   *     ignored or not.
   */
  public void setUnicodeNormalizationIgnored(final Boolean unicodeNormalizationIgnored) {
    this.unicodeNormalizationIgnored = unicodeNormalizationIgnored;
  }

  /**
   * Sets whether the unicode text normalization step should be ignored.
   *
   * <p>The Java Virtual Machine internally handles all Strings as UNICODE. When digesting or
   * matching digests in security, these Strings are first <b>normalized to its NFC form</b> so that
   * digest matching is not affected by the specific form in which the messages where input.
   *
   * <p><b>It is normally safe (and recommended) to leave this parameter set to its default FALSE
   * value (and thus DO perform normalization operations)</b>. But in some specific cases in which
   * issues with legacy software could arise, it might be useful to set this to TRUE.
   *
   * <p>For more information on unicode text normalization, see this issue of <a
   * href="http://java.sun.com/mailers/techtips/corejava/2007/tt0207.html">Core Java Technologies
   * Tech Tips</a>.
   *
   * <p>If not set, null will be returned.
   *
   * <p>Determines the result of: {@link #isUnicodeNormalizationIgnored()}
   *
   * @since 1.4
   * @param unicodeNormalizationIgnored whether the unicode text normalization step should be
   *     ignored or not.
   */
  public void setUnicodeNormalizationIgnored(final String unicodeNormalizationIgnored) {
    if (unicodeNormalizationIgnored != null) {
      this.unicodeNormalizationIgnored =
          CommonUtils.getStandardBooleanValue(unicodeNormalizationIgnored);
    } else {
      this.unicodeNormalizationIgnored = null;
    }
  }

  /**
   * Sets the the form in which String output will be encoded. Available encoding types are:
   *
   * <ul>
   *   <li><tt><b>base64</b></tt> (default)
   *   <li><tt><b>hexadecimal</b></tt>
   * </ul>
   *
   * <p>If not set, null will be returned.
   *
   * <p>Determines the result of: {@link #getStringOutputType()}
   *
   * @param stringOutputType the string output type.
   */
  public void setStringOutputType(final String stringOutputType) {
    this.stringOutputType = CommonUtils.getStandardStringOutputType(stringOutputType);
  }

  /**
   * Sets the prefix to be added at the beginning of encryption results, and also to be expected at
   * the beginning of plain messages provided for matching operations (raising an {@link
   * EncryptionOperationNotPossibleException} if not).
   *
   * <p>If not set, null will be returned.
   *
   * <p>Determines the result of: {@link #getPrefix()}
   *
   * @since 1.7
   * @param prefix
   */
  public void setPrefix(final String prefix) {
    this.prefix = prefix;
  }

  /**
   * Sets the suffix to be added at the end of encryption results, and also to be expected at the
   * end of plain messages provided for matching operations (raising an {@link
   * EncryptionOperationNotPossibleException} if not).
   *
   * <p>If not set, null will be returned.
   *
   * <p>Determines the result of: {@link #getSuffix()}
   *
   * @since 1.7
   * @param suffix
   */
  public void setSuffix(final String suffix) {
    this.suffix = suffix;
  }

  public Boolean isUnicodeNormalizationIgnored() {
    return this.unicodeNormalizationIgnored;
  }

  public String getStringOutputType() {
    return this.stringOutputType;
  }

  public String getPrefix() {
    return this.prefix;
  }

  public String getSuffix() {
    return this.suffix;
  }
}
