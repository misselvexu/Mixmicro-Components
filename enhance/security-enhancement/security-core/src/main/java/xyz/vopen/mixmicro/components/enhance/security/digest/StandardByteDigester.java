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
package xyz.vopen.mixmicro.components.enhance.security.digest;

import xyz.vopen.mixmicro.components.enhance.security.commons.CommonUtils;
import xyz.vopen.mixmicro.components.enhance.security.digest.config.DigesterConfig;
import xyz.vopen.mixmicro.components.enhance.security.exceptions.AlreadyInitializedException;
import xyz.vopen.mixmicro.components.enhance.security.exceptions.EncryptionInitializationException;
import xyz.vopen.mixmicro.components.enhance.security.exceptions.EncryptionOperationNotPossibleException;
import xyz.vopen.mixmicro.components.enhance.security.salt.RandomSaltGenerator;
import xyz.vopen.mixmicro.components.enhance.security.salt.SaltGenerator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;

/**
 * Standard implementation of the {@link ByteDigester} interface. This class lets the user specify
 * the algorithm (and provider) to be used for creating digests, the size of the salt to be applied,
 * the number of times the hash function will be applied (iterations) and the salt generator to be
 * used.
 *
 * <p>This class is <i>thread-safe</i>.
 *
 * <p><br>
 * <b><u>Configuration</u></b>
 *
 * <p>The algorithm, provider, salt size iterations and salt generator can take values in any of
 * these ways:
 *
 * <ul>
 *   <li>Using its default values.
 *   <li>Setting a <tt>{@link DigesterConfig}</tt> object which provides
 *       new configuration values.
 *   <li>Calling the corresponding <tt>setAlgorithm(...)</tt>, <tt>setProvider(...)</tt>,
 *       <tt>setProviderName(...)</tt>, <tt>setSaltSizeBytes(...)</tt>, <tt>setIterations(...)</tt>
 *       or <tt>setSaltGenerator(...)</tt> methods.
 * </ul>
 *
 * And the actual values to be used for initialization will be established by applying the following
 * priorities:
 *
 * <ol>
 *   <li>First, the default values are considered.
 *   <li>Then, if a <tt>{@link DigesterConfig}</tt> object has been set
 *       with <tt>setConfig</tt>, the non-null values returned by its <tt>getX</tt> methods override
 *       the default values.
 *   <li>Finally, if the corresponding <tt>setX</tt> method has been called on the digester itself
 *       for any of the configuration parameters, the values set by these calls override all of the
 *       above.
 * </ol>
 *
 * <p><br>
 * <b><u>Initialization</u></b>
 *
 * <p>Before it is ready to create digests, an object of this class has to be <i>initialized</i>.
 * Initialization happens:
 *
 * <ul>
 *   <li>When <tt>initialize</tt> is called.
 *   <li>When <tt>digest</tt> or <tt>matches</tt> are called for the first time, if
 *       <tt>initialize</tt> has not been called before.
 * </ul>
 *
 * Once a digester has been initialized, trying to change its configuration (algorithm, provider,
 * salt size, iterations or salt generator) will result in an <tt>AlreadyInitializedException</tt>
 * being thrown.
 *
 * <p><br>
 * <b><u>Usage</u></b>
 *
 * <p>A digester may be used in two different ways:
 *
 * <ul>
 *   <li>For <i>creating digests</i>, by calling the <tt>digest</tt> method.
 *   <li>For <i>matching digests</i>, this is, checking whether a digest corresponds adequately to a
 *       digest (as in password checking) or not, by calling the <tt>matches</tt> method.
 * </ul>
 *
 * The steps taken for creating digests are:
 *
 * <ol>
 *   <li>A salt of the specified size is generated (see {@link SaltGenerator}). If salt size is
 *       zero, no salt will be used.
 *   <li>The salt bytes are added to the message.
 *   <li>The hash function is applied to the salt and message altogether, and then to the results of
 *       the function itself, as many times as specified (iterations).
 *   <li>If specified by the salt generator (see {@link
 *       SaltGenerator#includePlainSaltInEncryptionResults()}), the
 *       <i>undigested</i> salt and the final result of the hash function are concatenated and
 *       returned as a result.
 * </ol>
 *
 * Put schematically in bytes:
 *
 * <ul>
 *   <li>DIGEST = <tt>|<b>S</b>|..(ssb)..|<b>S</b>|<b>X</b>|<b>X</b>|<b>X</b>|...|<b>X</b>|</tt>
 *       <ul>
 *         <li><tt><b>S</b></tt>: salt bytes (plain, not digested). <i>(OPTIONAL)</i>.
 *         <li><tt>ssb</tt>: salt size in bytes.
 *         <li><tt><b>X</b></tt>: bytes resulting from hashing (see below).
 *       </ul>
 *   <li><tt>|<b>X</b>|<b>X</b>|<b>X</b>|...|<b>X</b>|</tt> =
 *       <tt><i>H</i>(<i>H</i>(<i>H</i>(..(it)..<i>H</i>(<b>Z</b>|<b>Z</b>|<b>Z</b>|...|<b>Z</b>|))))</tt>
 *       <ul>
 *         <li><tt><i>H</i></tt>: Hash function (algorithm).
 *         <li><tt>it</tt>: Number of iterations.
 *         <li><tt><b>Z</b></tt>: Input for hashing (see below).
 *       </ul>
 *   <li><tt>|<b>Z</b>|<b>Z</b>|<b>Z</b>|...|<b>Z</b>|</tt> =
 *       <tt>|<b>S</b>|..(ssb)..|<b>S</b>|<b>M</b>|<b>M</b>|<b>M</b>...|<b>M</b>|</tt>
 *       <ul>
 *         <li><tt><b>S</b></tt>: salt bytes (plain, not digested).
 *         <li><tt>ssb</tt>: salt size in bytes.
 *         <li><tt><b>M</b></tt>: message bytes.
 *       </ul>
 * </ul>
 *
 * <b>If a random salt generator is used, two digests created for the same message will always be
 * different (except in the case of random salt coincidence).</b> Because of this, in this case the
 * result of the <tt>digest</tt> method will contain both the <i>undigested</i> salt and the digest
 * of the (salt + message), so that another digest operation can be performed with the same salt on
 * a different message to check if both messages match (all of which will be managed automatically
 * by the <tt>matches</tt> method).
 *
 * <p>To learn more about the mechanisms involved in digest creation, read <a
 * href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127" target="_blank">PKCS &#035;5:
 * Password-Based Cryptography Standard</a>.
 *
 * @since 1.0
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public final class StandardByteDigester implements ByteDigester {

  /** Default digest algorithm will be MD5 */
  public static final String DEFAULT_ALGORITHM = "MD5";
  /** The minimum recommended size for salt is 8 bytes */
  public static final int DEFAULT_SALT_SIZE_BYTES = 8;
  /** The minimum recommended iterations for hashing are 1000 */
  public static final int DEFAULT_ITERATIONS = 1000;

  // Algorithm to be used for hashing
  private String algorithm = DEFAULT_ALGORITHM;
  // Size of salt to be applied
  private int saltSizeBytes = DEFAULT_SALT_SIZE_BYTES;
  // Number of hash iterations to be applied
  private int iterations = DEFAULT_ITERATIONS;
  // SaltGenerator to be used. Initialization of a salt generator is costly,
  // and so default value will be applied only in initialize(), if it finally
  // becomes necessary.
  private SaltGenerator saltGenerator = null;
  // Name of the java.security.Provider which will be asked for the selected
  // algorithm
  private String providerName = null;
  // java.security.Provider instance which will be asked for the selected
  // algorithm
  private Provider provider = null;
  // Whether salt bytes will be appended after message before digesting or
  // inserted before it (which is the default).
  private boolean invertPositionOfSaltInMessageBeforeDigesting = false;
  // Whether plain (unhashed) salt bytes will be appended after the digest
  // operation result or inserted before it (which is the default).
  private boolean invertPositionOfPlainSaltInEncryptionResults = false;
  // Whether digest matching operations will allow matching digests with a
  // salt size different to the one configured in the "saltSizeBytes" property.
  private boolean useLenientSaltSizeCheck = false;

  /*
   * Config: this object can set a configuration by bringing the values in
   * whichever way the developer wants (it only has to implement the
   * DigesterConfig interface).
   *
   * Calls to setX methods OVERRIDE the values brought by this config.
   */
  private DigesterConfig config = null;

  /*
   * Set of booleans which indicate whether the config or default values
   * have to be overriden because of the setX methods having been
   * called.
   */
  private boolean algorithmSet = false;
  private boolean saltSizeBytesSet = false;
  private boolean iterationsSet = false;
  private boolean saltGeneratorSet = false;
  private boolean providerNameSet = false;
  private boolean providerSet = false;
  private boolean invertPositionOfSaltInMessageBeforeDigestingSet = false;
  private boolean invertPositionOfPlainSaltInEncryptionResultsSet = false;
  private boolean useLenientSaltSizeCheckSet = false;

  /*
   * Flag which indicates whether the digester has been initialized or not.
   *
   * Once initialized, no further modifications to its configuration will
   * be allowed.
   */
  private boolean initialized = false;

  /*
   * If the salt size is set to a value higher than zero, this flag will
   * indicate that the salt mecanism has to be used.
   */
  private boolean useSalt = true;

  /*
   * MessageDigest to be used.
   *
   * IMPORTANT: MessageDigest is not a thread-safe class, and thus any
   * use of this variable will have to be adequately synchronized.
   */
  private MessageDigest md = null;

  /*
   * Length of the result digest for the specified algorithm.
   * This might be zero if this operation is not supported by the
   * algorithm provider and the implementation is not cloneable.
   */
  private int digestLengthBytes = 0;

  /** Creates a new instance of <tt>StandardByteDigester</tt>. */
  public StandardByteDigester() {
    super();
  }

  /**
   * Sets a <tt>{@link DigesterConfig}</tt> object for the digester. If
   * this config object is set, it will be asked values for:
   *
   * <ul>
   *   <li>Algorithm
   *   <li>Security Provider (or provider name)
   *   <li>Salt size
   *   <li>Hashing iterations
   *   <li>Salt generator
   *   <li>Location of the salt in relation to the encrypted message (default: before)
   * </ul>
   *
   * <p>The non-null values it returns will override the default ones, <i>and will be overriden by
   * any values specified with a <tt>setX</tt> method</i>.
   *
   * @param config the <tt>DigesterConfig</tt> object to be used as the source for configuration
   *     parameters.
   */
  public synchronized void setConfig(final DigesterConfig config) {
    CommonUtils.validateNotNull(config, "Config cannot be set null");
    if (isInitialized()) {
      throw new AlreadyInitializedException();
    }
    this.config = config;
  }

  /**
   * Sets the algorithm to be used for digesting, like <tt>MD5</tt> or <tt>SHA-1</tt>.
   *
   * <p>This algorithm has to be supported by your security infrastructure, and it should be allowed
   * as an algorithm for creating java.security.MessageDigest instances.
   *
   * <p>If you are specifying a security provider with {@link #setProvider(Provider)} or {@link
   * #setProviderName(String)}, this algorithm should be supported by your specified provider.
   *
   * <p>If you are not specifying a provider, you will be able to use those algorithms provided by
   * the default security provider of your JVM vendor. For valid names in the Sun JVM, see <a
   * target="_blank"
   * href="http://java.sun.com/j2se/1.5.0/docs/guide/security/CryptoSpec.html#AppA">Java
   * Cryptography Architecture API Specification & Reference</a>.
   *
   * @param algorithm the name of the algorithm to be used.
   */
  public synchronized void setAlgorithm(final String algorithm) {
    CommonUtils.validateNotEmpty(algorithm, "Algorithm cannot be empty");
    if (isInitialized()) {
      throw new AlreadyInitializedException();
    }
    this.algorithm = algorithm;
    this.algorithmSet = true;
  }

  /**
   * Sets the size of the salt to be used to compute the digest. This mechanism is explained in <a
   * href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127" target="_blank">PKCS &#035;5:
   * Password-Based Cryptography Standard</a>.
   *
   * <p>If salt size is set to zero, then no salt will be used.
   *
   * @param saltSizeBytes the size of the salt to be used, in bytes.
   */
  public synchronized void setSaltSizeBytes(final int saltSizeBytes) {
    CommonUtils.validateIsTrue(saltSizeBytes >= 0, "Salt size in bytes must be non-negative");
    if (isInitialized()) {
      throw new AlreadyInitializedException();
    }
    this.saltSizeBytes = saltSizeBytes;
    this.useSalt = (saltSizeBytes > 0);
    this.saltSizeBytesSet = true;
  }

  /**
   * Set the number of times the hash function will be applied recursively. <br>
   * The hash function will be applied to its own results as many times as specified:
   * <i>h(h(...h(x)...))</i>
   *
   * <p>This mechanism is explained in <a href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127"
   * target="_blank">PKCS &#035;5: Password-Based Cryptography Standard</a>.
   *
   * @param iterations the number of iterations.
   */
  public synchronized void setIterations(final int iterations) {
    CommonUtils.validateIsTrue(iterations > 0, "Number of iterations must be greater than zero");
    if (isInitialized()) {
      throw new AlreadyInitializedException();
    }
    this.iterations = iterations;
    this.iterationsSet = true;
  }

  /**
   * Sets the salt generator to be used. If no salt generator is specified, an instance of {@link
   * RandomSaltGenerator} will be used.
   *
   * @since 1.2
   * @param saltGenerator the salt generator to be used.
   */
  public synchronized void setSaltGenerator(final SaltGenerator saltGenerator) {
    CommonUtils.validateNotNull(saltGenerator, "Salt generator cannot be set null");
    if (isInitialized()) {
      throw new AlreadyInitializedException();
    }
    this.saltGenerator = saltGenerator;
    this.saltGeneratorSet = true;
  }

  /**
   * Sets the name of the security provider to be asked for the digest algorithm. This security
   * provider has to be registered beforehand at the JVM security framework.
   *
   * <p>The provider can also be set with the {@link #setProvider(Provider)} method, in which case
   * it will not be necessary neither registering the provider beforehand, nor calling this {@link
   * #setProviderName(String)} method to specify a provider name.
   *
   * <p>Note that a call to {@link #setProvider(Provider)} overrides any value set by this method.
   *
   * <p>If no provider name / provider is explicitly set, the default JVM provider will be used.
   *
   * @since 1.3
   * @param providerName the name of the security provider to be asked for the digest algorithm.
   */
  public synchronized void setProviderName(final String providerName) {
    CommonUtils.validateNotNull(providerName, "Provider name cannot be set null");
    if (isInitialized()) {
      throw new AlreadyInitializedException();
    }
    this.providerName = providerName;
    this.providerNameSet = true;
  }

  /**
   * Sets the security provider to be asked for the digest algorithm. The provider does not have to
   * be registered at the security infrastructure beforehand, and its being used here will not
   * result in its being registered.
   *
   * <p>If this method is called, calling {@link #setProviderName(String)} becomes unnecessary.
   *
   * <p>If no provider name / provider is explicitly set, the default JVM provider will be used.
   *
   * @since 1.3
   * @param provider the provider to be asked for the chosen algorithm
   */
  public synchronized void setProvider(final Provider provider) {
    CommonUtils.validateNotNull(provider, "Provider cannot be set null");
    if (isInitialized()) {
      throw new AlreadyInitializedException();
    }
    this.provider = provider;
    this.providerSet = true;
  }

  /**
   * Whether the salt bytes are to be appended after the message ones before performing the digest
   * operation on the whole. The default behaviour is to insert those bytes before the message
   * bytes, but setting this configuration item to <tt>true</tt> allows compatibility with some
   * external systems and specifications (e.g. LDAP {SSHA}).
   *
   * <p>If this parameter is not explicitly set, the default behaviour (insertion of salt before
   * message) will be applied.
   *
   * @since 1.7
   * @param invertPositionOfSaltInMessageBeforeDigesting whether salt will be appended after the
   *     message before applying the digest operation on the whole, instead of inserted before it
   *     (which is the default).
   */
  public synchronized void setInvertPositionOfSaltInMessageBeforeDigesting(
      final boolean invertPositionOfSaltInMessageBeforeDigesting) {

    if (isInitialized()) {
      throw new AlreadyInitializedException();
    }
    this.invertPositionOfSaltInMessageBeforeDigesting =
        invertPositionOfSaltInMessageBeforeDigesting;
    this.invertPositionOfSaltInMessageBeforeDigestingSet = true;
  }

  /**
   * Whether the plain (not hashed) salt bytes are to be appended after the digest operation result
   * bytes. The default behaviour is to insert them before the digest result, but setting this
   * configuration item to <tt>true</tt> allows compatibility with some external systems and
   * specifications (e.g. LDAP {SSHA}).
   *
   * <p>If this parameter is not explicitly set, the default behaviour (insertion of plain salt
   * before digest result) will be applied.
   *
   * @since 1.7
   * @param invertPositionOfPlainSaltInEncryptionResults whether plain salt will be appended after
   *     the digest operation result instead of inserted before it (which is the default).
   */
  public synchronized void setInvertPositionOfPlainSaltInEncryptionResults(
      final boolean invertPositionOfPlainSaltInEncryptionResults) {

    if (isInitialized()) {
      throw new AlreadyInitializedException();
    }
    this.invertPositionOfPlainSaltInEncryptionResults =
        invertPositionOfPlainSaltInEncryptionResults;
    this.invertPositionOfPlainSaltInEncryptionResultsSet = true;
  }

  /**
   * Whether digest matching operations will allow matching digests with a salt size different to
   * the one configured in the "saltSizeBytes" property. This is possible because digest algorithms
   * will produce a fixed-size result, so the remaining bytes from the hashed input will be
   * considered salt.
   *
   * <p>This will allow the digester to match digests produced in environments which do not
   * establish a fixed salt size as standard (for example, SSHA password encryption in LDAP
   * systems).
   *
   * <p>The value of this property will <b>not</b> affect the creation of digests, which will always
   * have a salt of the size established by the "saltSizeBytes" property. It will only affect digest
   * matching.
   *
   * <p>Setting this property to <tt>true</tt> is not compatible with {@link SaltGenerator}
   * implementations which return false for their {@link
   * SaltGenerator#includePlainSaltInEncryptionResults()} property.
   *
   * <p>Also, be aware that some algorithms or algorithm providers might not support knowing the
   * size of the digests beforehand, which is also incompatible with a lenient behaviour.
   *
   * <p>If this parameter is not explicitly set, the default behaviour (NOT lenient) will be
   * applied.
   *
   * @since 1.7
   * @param useLenientSaltSizeCheck whether the digester will allow matching of digests with
   *     different salt sizes than established or not (default is false).
   */
  public synchronized void setUseLenientSaltSizeCheck(final boolean useLenientSaltSizeCheck) {

    if (isInitialized()) {
      throw new AlreadyInitializedException();
    }
    this.useLenientSaltSizeCheck = useLenientSaltSizeCheck;
    this.useLenientSaltSizeCheckSet = true;
  }

  /*
   * Clone this digester.
   */
  StandardByteDigester cloneDigester() {

    // Check initialization
    if (!isInitialized()) {
      initialize();
    }

    final StandardByteDigester cloned = new StandardByteDigester();
    if (CommonUtils.isNotEmpty(this.algorithm)) {
      cloned.setAlgorithm(this.algorithm);
    }
    cloned.setInvertPositionOfPlainSaltInEncryptionResults(
        this.invertPositionOfPlainSaltInEncryptionResults);
    cloned.setInvertPositionOfSaltInMessageBeforeDigesting(
        this.invertPositionOfSaltInMessageBeforeDigesting);
    cloned.setIterations(this.iterations);
    if (this.provider != null) {
      cloned.setProvider(this.provider);
    }
    if (this.providerName != null) {
      cloned.setProviderName(this.providerName);
    }
    if (this.saltGenerator != null) {
      cloned.setSaltGenerator(this.saltGenerator);
    }
    cloned.setSaltSizeBytes(this.saltSizeBytes);
    cloned.setUseLenientSaltSizeCheck(this.useLenientSaltSizeCheck);

    return cloned;
  }

  /**
   * Returns true if the digester has already been initialized, false if not.<br>
   * Initialization happens:
   *
   * <ul>
   *   <li>When <tt>initialize</tt> is called.
   *   <li>When <tt>digest</tt> or <tt>matches</tt> are called for the first time, if
   *       <tt>initialize</tt> has not been called before.
   * </ul>
   *
   * <p>Once a digester has been initialized, trying to change its configuration (algorithm,
   * provider, salt size, iterations or salt generator) will result in an
   * <tt>AlreadyInitializedException</tt> being thrown.
   *
   * @return true if the digester has already been initialized, false if not.
   */
  public boolean isInitialized() {
    return this.initialized;
  }

  /**
   * Initialize the digester.
   *
   * <p>This operation will consist in determining the actual configuration values to be used, and
   * then initializing the digester with them. <br>
   * These values are decided by applying the following priorities:
   *
   * <ol>
   *   <li>First, the default values are considered.
   *   <li>Then, if a <tt>{@link DigesterConfig}</tt> object has been set
   *       with <tt>setConfig</tt>, the non-null values returned by its <tt>getX</tt> methods
   *       override the default values.
   *   <li>Finally, if the corresponding <tt>setX</tt> method has been called on the digester itself
   *       for any of the configuration parameters, the values set by these calls override all of
   *       the above.
   * </ol>
   *
   * <p>Once a digester has been initialized, trying to change its configuration will result in an
   * <tt>AlreadyInitializedException</tt> being thrown.
   *
   * @throws EncryptionInitializationException if initialization could not be correctly done (for
   *     example, if the digest algorithm chosen cannot be used).
   */
  public synchronized void initialize() {

    // Double-check to avoid synchronization issues
    if (!this.initialized) {

      /*
       * If a DigesterConfig object has been set, we need to
       * consider the values it returns (if, for each value, the
       * corresponding "setX" method has not been called).
       */
      if (this.config != null) {

        final String configAlgorithm = this.config.getAlgorithm();
        if (configAlgorithm != null) {
          CommonUtils.validateNotEmpty(configAlgorithm, "Algorithm cannot be empty");
        }

        final Integer configSaltSizeBytes = this.config.getSaltSizeBytes();
        if (configSaltSizeBytes != null) {
          CommonUtils.validateIsTrue(
              configSaltSizeBytes.intValue() >= 0, "Salt size in bytes must be non-negative");
        }

        final Integer configIterations = this.config.getIterations();
        if (configIterations != null) {
          CommonUtils.validateIsTrue(
              configIterations.intValue() > 0, "Number of iterations must be greater than zero");
        }

        final SaltGenerator configSaltGenerator = this.config.getSaltGenerator();

        final String configProviderName = this.config.getProviderName();
        if (configProviderName != null) {
          CommonUtils.validateNotEmpty(configProviderName, "Provider name cannot be empty");
        }

        final Provider configProvider = this.config.getProvider();

        final Boolean configInvertPositionOfSaltInMessageBeforeDigesting =
            this.config.getInvertPositionOfSaltInMessageBeforeDigesting();

        final Boolean configInvertPositionOfPlainSaltInEncryptionResults =
            this.config.getInvertPositionOfPlainSaltInEncryptionResults();

        final Boolean configUseLenientSaltSizeCheck = this.config.getUseLenientSaltSizeCheck();

        this.algorithm =
            ((this.algorithmSet) || (configAlgorithm == null)) ? this.algorithm : configAlgorithm;
        this.saltSizeBytes =
            ((this.saltSizeBytesSet) || (configSaltSizeBytes == null))
                ? this.saltSizeBytes
                : configSaltSizeBytes.intValue();
        this.iterations =
            ((this.iterationsSet) || (configIterations == null))
                ? this.iterations
                : configIterations.intValue();
        this.saltGenerator =
            ((this.saltGeneratorSet) || (configSaltGenerator == null))
                ? this.saltGenerator
                : configSaltGenerator;
        this.providerName =
            ((this.providerNameSet) || (configProviderName == null))
                ? this.providerName
                : configProviderName;
        this.provider =
            ((this.providerSet) || (configProvider == null)) ? this.provider : configProvider;
        this.invertPositionOfSaltInMessageBeforeDigesting =
            ((this.invertPositionOfSaltInMessageBeforeDigestingSet)
                    || (configInvertPositionOfSaltInMessageBeforeDigesting == null))
                ? this.invertPositionOfSaltInMessageBeforeDigesting
                : configInvertPositionOfSaltInMessageBeforeDigesting.booleanValue();
        this.invertPositionOfPlainSaltInEncryptionResults =
            ((this.invertPositionOfPlainSaltInEncryptionResultsSet)
                    || (configInvertPositionOfPlainSaltInEncryptionResults == null))
                ? this.invertPositionOfPlainSaltInEncryptionResults
                : configInvertPositionOfPlainSaltInEncryptionResults.booleanValue();
        this.useLenientSaltSizeCheck =
            ((this.useLenientSaltSizeCheckSet) || (configUseLenientSaltSizeCheck == null))
                ? this.useLenientSaltSizeCheck
                : configUseLenientSaltSizeCheck.booleanValue();
      }

      /*
       * If the digester was not set a salt generator in any way,
       * it is time to apply its default value.
       */
      if (this.saltGenerator == null) {
        this.saltGenerator = new RandomSaltGenerator();
      }

      /*
       * Test compatibility of salt generator with salt size checking
       * behaviour
       */
      if (this.useLenientSaltSizeCheck) {
        if (!this.saltGenerator.includePlainSaltInEncryptionResults()) {
          throw new EncryptionInitializationException(
              "The configured Salt Generator ("
                  + this.saltGenerator.getClass().getName()
                  + ") does not include plain salt "
                  + "in encryption results, which is not compatible"
                  + "with setting the salt size checking behaviour to \"lenient\".");
        }
      }

      /*
       * MessageDigest is initialized the usual way, and the digester
       * is marked as "initialized" so that configuration cannot be
       * changed in the future.
       */
      try {
        if (this.provider != null) {
          this.md = MessageDigest.getInstance(this.algorithm, this.provider);
        } else if (this.providerName != null) {
          this.md = MessageDigest.getInstance(this.algorithm, this.providerName);
        } else {
          this.md = MessageDigest.getInstance(this.algorithm);
        }
      } catch (NoSuchAlgorithmException e) {
        throw new EncryptionInitializationException(e);
      } catch (NoSuchProviderException e) {
        throw new EncryptionInitializationException(e);
      }

      /*
       * Store the digest length (algorithm-dependent) and check
       * the operation is supported by the provider.
       */
      this.digestLengthBytes = this.md.getDigestLength();
      if (this.digestLengthBytes <= 0) {
        throw new EncryptionInitializationException(
            "The configured algorithm ("
                + this.algorithm
                + ") or its provider do  "
                + "not allow knowing the digest length beforehand "
                + "(getDigestLength() operation), which is not compatible"
                + "with setting the salt size checking behaviour to \"lenient\".");
      }

      this.initialized = true;
    }
  }

  /**
   * Performs a digest operation on a byte array message.
   *
   * <p>The steps taken for creating the digest are:
   *
   * <ol>
   *   <li>A salt of the specified size is generated (see {@link SaltGenerator}).
   *   <li>The salt bytes are added to the message.
   *   <li>The hash function is applied to the salt and message altogether, and then to the results
   *       of the function itself, as many times as specified (iterations).
   *   <li>If specified by the salt generator (see {@link
   *       SaltGenerator#includePlainSaltInEncryptionResults()}), the
   *       <i>undigested</i> salt and the final result of the hash function are concatenated and
   *       returned as a result.
   * </ol>
   *
   * Put schematically in bytes:
   *
   * <ul>
   *   <li>DIGEST = <tt>|<b>S</b>|..(ssb)..|<b>S</b>|<b>X</b>|<b>X</b>|<b>X</b>|...|<b>X</b>|</tt>
   *       <ul>
   *         <li><tt><b>S</b></tt>: salt bytes (plain, not digested). <i>(OPTIONAL)</i>.
   *         <li><tt>ssb</tt>: salt size in bytes.
   *         <li><tt><b>X</b></tt>: bytes resulting from hashing (see below).
   *       </ul>
   *   <li><tt>|<b>X</b>|<b>X</b>|<b>X</b>|...|<b>X</b>|</tt> =
   *       <tt><i>H</i>(<i>H</i>(<i>H</i>(..(it)..<i>H</i>(<b>Z</b>|<b>Z</b>|<b>Z</b>|...|<b>Z</b>|))))</tt>
   *       <ul>
   *         <li><tt><i>H</i></tt>: Hash function (algorithm).
   *         <li><tt>it</tt>: Number of iterations.
   *         <li><tt><b>Z</b></tt>: Input for hashing (see below).
   *       </ul>
   *   <li><tt>|<b>Z</b>|<b>Z</b>|<b>Z</b>|...|<b>Z</b>|</tt> =
   *       <tt>|<b>S</b>|..(ssb)..|<b>S</b>|<b>M</b>|<b>M</b>|<b>M</b>...|<b>M</b>|</tt>
   *       <ul>
   *         <li><tt><b>S</b></tt>: salt bytes (plain, not digested).
   *         <li><tt>ssb</tt>: salt size in bytes.
   *         <li><tt><b>M</b></tt>: message bytes.
   *       </ul>
   * </ul>
   *
   * <p><b>If a random salt generator is used, two digests created for the same message will always
   * be different (except in the case of random salt coincidence).</b> Because of this, in this case
   * the result of the <tt>digest</tt> method will contain both the <i>undigested</i> salt and the
   * digest of the (salt + message), so that another digest operation can be performed with the same
   * salt on a different message to check if both messages match (all of which will be managed
   * automatically by the <tt>matches</tt> method).
   *
   * @param message the byte array to be digested
   * @return the digest result
   * @throws EncryptionOperationNotPossibleException if the digest operation fails, ommitting any
   *     further information about the cause for security reasons.
   * @throws EncryptionInitializationException if initialization could not be correctly done (for
   *     example, if the digest algorithm chosen cannot be used).
   */
  public byte[] digest(byte[] message) {

    if (message == null) {
      return null;
    }

    // Check initialization
    if (!isInitialized()) {
      initialize();
    }

    // Create salt
    byte[] salt = null;
    if (this.useSalt) {
      salt = this.saltGenerator.generateSalt(this.saltSizeBytes);
    }

    // Create digest
    return digest(message, salt);
  }

  /*
   * This method truly performs the digest operation, assuming that a salt
   * has already been created (if needed) and the digester has already been
   * initialized.
   */
  private byte[] digest(final byte[] message, final byte[] salt) {

    try {

      byte[] digest = null;

      synchronized (this.md) {
        this.md.reset();

        if (salt != null) {

          if (!this.invertPositionOfSaltInMessageBeforeDigesting) {

            // The salt bytes are added before the message to be digested
            this.md.update(salt);
            this.md.update(message);

          } else {

            // The salt bytes are appended after the message to be digested
            this.md.update(message);
            this.md.update(salt);
          }

        } else {

          // No salt to be added
          this.md.update(message);
        }

        digest = this.md.digest();
        for (int i = 0; i < (this.iterations - 1); i++) {
          this.md.reset();
          digest = this.md.digest(digest);
        }
      }

      // Finally we build an array containing both the unhashed (plain) salt
      // and the digest of the (salt + message). This is done only
      // if the salt generator we are using specifies to do so.
      if (this.saltGenerator.includePlainSaltInEncryptionResults() && salt != null) {

        if (!this.invertPositionOfPlainSaltInEncryptionResults) {

          // Insert unhashed salt before the hashing result (default behaviour)
          return CommonUtils.appendArrays(salt, digest);
        }

        // Append unhashed salt after the hashing result
        return CommonUtils.appendArrays(digest, salt);
      }

      return digest;

    } catch (Exception e) {
      // If digest fails, it is more secure not to return any information
      // about the cause in nested exceptions. Simply fail.
      throw new EncryptionOperationNotPossibleException();
    }
  }

  /**
   * Checks a message against a given digest.
   *
   * <p>This method tells whether a message corresponds to a specific digest or not by getting the
   * salt with which the digest was created and applying it to a digest operation performed on the
   * message. If new and existing digest match, the message is said to match the digest.
   *
   * <p>This method will be used, for instance, for password checking in authentication processes.
   *
   * <p>A null message will only match a null digest.
   *
   * @param message the message to be compared to the digest.
   * @param digest the digest.
   * @return true if the specified message matches the digest, false if not.
   * @throws EncryptionOperationNotPossibleException if the digest matching operation fails,
   *     ommitting any further information about the cause for security reasons.
   * @throws EncryptionInitializationException if initialization could not be correctly done (for
   *     example, if the digest algorithm chosen cannot be used).
   */
  public boolean matches(final byte[] message, final byte[] digest) {

    if (message == null) {
      return (digest == null);
    } else if (digest == null) {
      return false;
    }

    // Check initialization
    if (!isInitialized()) {
      initialize();
    }

    try {

      // If we are using a salt, extract it to use it.
      byte[] salt = null;
      if (this.useSalt) {
        // If we are using a salt generator which specifies the salt
        // to be included into the digest itself, get it from there.
        // If not, the salt is supposed to be fixed and thus the
        // salt generator can be safely asked for it again.
        if (this.saltGenerator.includePlainSaltInEncryptionResults()) {

          // Compute size figures and perform length checks
          int digestSaltSize = this.saltSizeBytes;
          if (this.digestLengthBytes > 0) {
            if (this.useLenientSaltSizeCheck) {
              if (digest.length < this.digestLengthBytes) {
                throw new EncryptionOperationNotPossibleException();
              }
              digestSaltSize = digest.length - this.digestLengthBytes;
            } else {
              if (digest.length != (this.digestLengthBytes + this.saltSizeBytes)) {
                throw new EncryptionOperationNotPossibleException();
              }
            }
          } else {
            // Salt size check behaviour cannot be set to lenient
            if (digest.length < this.saltSizeBytes) {
              throw new EncryptionOperationNotPossibleException();
            }
          }

          if (!this.invertPositionOfPlainSaltInEncryptionResults) {
            salt = new byte[digestSaltSize];
            System.arraycopy(digest, 0, salt, 0, digestSaltSize);
          } else {
            salt = new byte[digestSaltSize];
            System.arraycopy(digest, digest.length - digestSaltSize, salt, 0, digestSaltSize);
          }

        } else {
          salt = this.saltGenerator.generateSalt(this.saltSizeBytes);
        }
      }

      // Digest the message with the extracted digest.
      final byte[] encryptedMessage = digest(message, salt);

      // If, using the same salt, digests match, then messages too.
      return (digestsAreEqual(encryptedMessage, digest));

    } catch (Exception e) {
      // If digest fails, it is more secure not to return any information
      // about the cause in nested exceptions. Simply fail.
      throw new EncryptionOperationNotPossibleException();
    }
  }

  // Time-constant comparison of byte arrays
  private static boolean digestsAreEqual(byte[] a, byte[] b) {

    if (a == null || b == null) {
      return false;
    }

    final int aLen = a.length;
    if (b.length != aLen) {
      return false;
    }

    int match = 0;
    for (int i = 0; i < aLen; i++) {
      match |= a[i] ^ b[i];
    }

    return (match == 0);
  }
}
