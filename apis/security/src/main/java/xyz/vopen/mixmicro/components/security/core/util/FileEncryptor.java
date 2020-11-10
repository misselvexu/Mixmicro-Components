package xyz.vopen.mixmicro.components.security.core.util;

import org.apache.commons.cli.*;
import xyz.vopen.mixmicro.components.security.core.Encryptor;
import xyz.vopen.mixmicro.components.security.core.factory.EncryptorFactory;
import xyz.vopen.mixmicro.components.security.core.factory.KeyFactory;

import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Base64;

/**
 * Class for encrypting and decrypting files.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class FileEncryptor {

  /*
   * Attributes
   */

  private Encryptor encryptor;
  private int bufferSize;

  /*
   * Constructor(s)
   */

  /**
   * Constructs a default AES <code>FileEncryptor</code> instance using a randomly generated key.
   * Obtain the key by calling {@link #getEncryptor()} and {@link Encryptor#getKey()}.
   */
  public FileEncryptor() {
    this(KeyFactory.AES.randomKey());
  }

  /**
   * Constructs a default AES <code>FileEncryptor</code> instance using the given password.
   *
   * @param password the password used for encryption/decryption
   */
  public FileEncryptor(String password) {
    this(KeyFactory.AES.keyFromPassword(password.toCharArray()));
  }

  /**
   * Constructs a default AES <code>FileEncryptor</code> instance using the given AES key.
   *
   * @param key the key used for encryption/decryption
   */
  public FileEncryptor(Key key) {
    this(EncryptorFactory.AES.streamEncryptor(key));
  }

  /**
   * Constructs a default <code>FileEncryptor</code> instance using the given <code>Encryptor</code>
   * .
   *
   * @param encryptor the encryptor used for encryption/decryption
   */
  public FileEncryptor(Encryptor encryptor) {
    super();
    this.encryptor = encryptor;
    this.bufferSize = 65536;
  }

  /*
   * Class methods
   */

  /**
   * Reads and encrypts file <code>src</code> and writes the encrypted result to file <code>dest
   * </code>.
   *
   * @param src the source file
   * @param dest the destination file
   * @param src
   * @param dest
   * @throws FileNotFoundException
   * @throws GeneralSecurityException
   * @throws IOException
   */
  public void encrypt(File src, File dest) throws GeneralSecurityException, IOException {
    InputStream is = null;
    OutputStream os = null;
    try {
      is = new FileInputStream(src);
      os = encryptor.wrapOutputStream(new FileOutputStream(dest));
      copy(is, os);
    } finally {
      if (is != null) {
        is.close();
      }
      if (os != null) {
        os.close();
      }
    }
  }

  /**
   * Reads and decrypts file <code>src</code> and writes the decrypted result to file <code>dest
   * </code>.
   *
   * @param src the source file
   * @param dest the destination file
   * @throws FileNotFoundException
   * @throws GeneralSecurityException
   * @throws IOException
   */
  public void decrypt(File src, File dest) throws GeneralSecurityException, IOException {
    InputStream is = null;
    OutputStream os = null;
    try {
      is = encryptor.wrapInputStream(new FileInputStream(src));
      os = new FileOutputStream(dest);
      copy(is, os);
    } finally {
      if (is != null) {
        is.close();
      }
      if (os != null) {
        os.close();
      }
    }
  }

  /**
   * Reads data from the <code>InputStream</code> and writes it to the <code>OutputStream</code>.
   *
   * @param is the input stream
   * @param os the output stream
   * @throws IOException
   */
  private void copy(InputStream is, OutputStream os) throws IOException {
    byte[] buffer = new byte[bufferSize];
    int nRead;
    while ((nRead = is.read(buffer)) != -1) {
      os.write(buffer, 0, nRead);
    }
    os.flush();
  }

  /**
   * Returns the encryptor.
   *
   * @return
   */
  public Encryptor getEncryptor() {
    return encryptor;
  }

  /**
   * Returns the buffer size.
   *
   * @return
   */
  public int getBufferSize() {
    return bufferSize;
  }

  /**
   * Sets the buffer size.
   *
   * @param bufferSize the buffer size
   */
  public void setBufferSize(int bufferSize) {
    this.bufferSize = bufferSize;
  }

  /*
   * Static methods
   */

  /** @param args */
  public static void main(String[] args) {
    Options options = new Options();
    options.addOption(
        Option.builder("i")
            .longOpt("in-file")
            .hasArg()
            .argName("file")
            .desc("input file")
            .required()
            .build());

    options.addOption(
        Option.builder("o")
            .longOpt("out-file")
            .hasArg()
            .argName("file")
            .desc("output file")
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

      File inFile = new File(cmd.getOptionValue("i"));
      File outFile = new File(cmd.getOptionValue("o"));

      FileEncryptor fe;
      if (cmd.hasOption("p")) {
        fe = new FileEncryptor(cmd.getOptionValue("p"));
      } else if (cmd.hasOption("k")) {
        String encodedKey = cmd.getOptionValue("k");
        byte[] keyBytes = Base64.getDecoder().decode(encodedKey);
        Key key = new SecretKeySpec(keyBytes, "AES");
        fe = new FileEncryptor(key);
      } else {
        fe = new FileEncryptor();
        byte[] keyBytes = fe.getEncryptor().getKey().getEncoded();
        String encodedKey = Base64.getEncoder().encodeToString(keyBytes);
        System.out.println("Base64 encoded key: " + encodedKey);
      }
      try {
        if (cmd.hasOption("d")) {
          fe.decrypt(inFile, outFile);
        } else {
          fe.encrypt(inFile, outFile);
        }
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (GeneralSecurityException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp(FileEncryptor.class.getSimpleName(), options);
    }
    System.exit(0);
  }
}
