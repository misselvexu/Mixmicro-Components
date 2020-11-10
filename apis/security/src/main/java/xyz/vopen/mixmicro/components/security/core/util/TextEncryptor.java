package xyz.vopen.mixmicro.components.security.core.util;

import org.apache.commons.cli.*;
import xyz.vopen.mixmicro.components.security.core.Encryptor;
import xyz.vopen.mixmicro.components.security.core.factory.EncryptorFactory;
import xyz.vopen.mixmicro.components.security.core.factory.KeyFactory;

import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Base64;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public class TextEncryptor {

  /*
   * Attributes
   */

  private Encryptor encryptor;

  /*
   * Constructor(s)
   */

  /**
   * Constructs a default AES <code>TextEncryptor</code> instance using a randomly generated key.
   * Obtain the key by calling {@link #getEncryptor()} and {@link Encryptor#getKey()}.
   */
  public TextEncryptor() {
    this(KeyFactory.AES.randomKey());
  }

  /**
   * Constructs a default AES <code>TextEncryptor</code> instance using the given password.
   *
   * @param password the password used for encryption
   */
  public TextEncryptor(String password) {
    this(KeyFactory.AES.keyFromPassword(password.toCharArray()));
  }

  /**
   * Constructs a default AES <code>TextEncryptor</code> instance using the given AES key.
   *
   * @param key the key used for encryption
   */
  public TextEncryptor(Key key) {
    this(EncryptorFactory.AES.messageEncryptor(key));
  }

  /**
   * Constructs a <code>TextEncryptor</code> instance using the given <code>Encryptor</code>.
   *
   * @param encryptor the encryptor used for encryption
   */
  public TextEncryptor(Encryptor encryptor) {
    this.encryptor = encryptor;
  }

  /*
   * Class methods
   */

  /**
   * Encrypts and Base64 encodes a message.
   *
   * @param message the message
   * @return the encrypted message
   * @throws GeneralSecurityException
   */
  public String encrypt(String message) throws GeneralSecurityException {
    byte[] bytes = encryptor.encrypt(message.getBytes());
    return Base64.getEncoder().encodeToString(bytes);
  }

  /**
   * Base64 decodes and decrypts an encrypted message.
   *
   * @param message an encrypted message
   * @return the decrypted message
   * @throws GeneralSecurityException
   */
  public String decrypt(String message) throws GeneralSecurityException {
    byte[] bytes = Base64.getDecoder().decode(message);
    return new String(encryptor.decrypt(bytes));
  }

  /**
   * Returns the encryptor.
   *
   * @return
   */
  public Encryptor getEncryptor() {
    return encryptor;
  }

  /*
   * Static methods
   */

  /** @param args */
  public static void main(String[] args) {
    Options options = new Options();
    options.addOption(
        Option.builder("t")
            .longOpt("text")
            .hasArg()
            .argName("text")
            .desc("message text")
            .required()
            .build());

    options.addOption(Option.builder("d").longOpt("decrypt").desc("decrypt").build());

    options.addOption(
        Option.builder("p")
            .longOpt("password")
            .hasArg()
            .argName("password")
            .desc("password")
            .build());

    options.addOption(
        Option.builder("k")
            .longOpt("key")
            .hasArg()
            .argName("key")
            .desc("Base64 encoded key")
            .build());

    if (args != null && args.length > 0) {
      CommandLineParser parser = new DefaultParser();
      CommandLine cmd;
      try {
        cmd = parser.parse(options, args);
      } catch (ParseException e) {
        throw new RuntimeException("Could not parse args", e);
      }

      String text = cmd.getOptionValue("t");

      TextEncryptor te;
      if (cmd.hasOption("p")) {
        te = new TextEncryptor(cmd.getOptionValue("p"));
      } else if (cmd.hasOption("k")) {
        String encodedKey = cmd.getOptionValue("k");
        byte[] keyBytes = Base64.getDecoder().decode(encodedKey);
        Key key = new SecretKeySpec(keyBytes, "AES");
        te = new TextEncryptor(key);
      } else {
        te = new TextEncryptor();
        byte[] keyBytes = te.getEncryptor().getKey().getEncoded();
        String encodedKey = Base64.getEncoder().encodeToString(keyBytes);
        System.out.println("Base64 encoded key: " + encodedKey);
      }
      try {
        if (cmd.hasOption("d")) {
          String decrypted = te.decrypt(text);
          System.out.println("Decrypted message:");
          System.out.println(decrypted);
        } else {
          String encrypted = te.encrypt(text);
          System.out.println("Encrypted message:");
          System.out.println(encrypted);
        }
      } catch (GeneralSecurityException e) {
        e.printStackTrace();
      }
    } else {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp(TextEncryptor.class.getSimpleName(), options);
    }
    System.exit(0);
  }
}
