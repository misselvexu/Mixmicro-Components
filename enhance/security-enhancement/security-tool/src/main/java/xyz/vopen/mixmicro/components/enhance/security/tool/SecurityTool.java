package xyz.vopen.mixmicro.components.enhance.security.tool;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.cli.*;
import xyz.vopen.mixmicro.components.enhance.security.encryption.pbe.PooledPBEStringEncryptor;
import xyz.vopen.mixmicro.components.enhance.security.encryption.pbe.config.SimpleStringPBEConfig;

import static xyz.vopen.mixmicro.kits.StringUtils.isBlank;

/**
 * {@link SecurityTool}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/3/9
 */
public class SecurityTool {

  static PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
  static SimpleStringPBEConfig config = new SimpleStringPBEConfig();

  public static void main(String[] args) {
    String header = "java -jar encrypt.jar --help\r\nOptions:";
    String footer = "\r\n© 2020 YunLSP+, Inc";
    final Options options = new Options();

    try {

      CommandLineParser parser = new DefaultParser();

      // token -id
      options.addOption(
          Option.builder(Function.ENCRYPT.shortCommand())
              .longOpt(Function.ENCRYPT.longCommand())
              .hasArg(false)
              .required(false)
              .desc("Encrypt input with assigned key")
              .build());

      // base option
      options.addOption(
          Option.builder("key")
              .longOpt("encryptkey")
              .hasArg()
              .argName("KEY")
              .type(String.class)
              .required(true)
              .desc("Master Password used for Encryption/Decryption.")
              .build());

      options.addOption(
          Option.builder("text")
              .longOpt("plaintext")
              .hasArg()
              .argName("TEXT")
              .type(String.class)
              .required(true)
              .desc("Content to be encrypted.")
              .build());

      // parse longCommand
      CommandLine commandLine = parser.parse(options, args);

      ExecuteParams.ExecuteParamsBuilder builder = ExecuteParams.builder();

      // 1. check token generate
      if (commandLine.hasOption(Function.ENCRYPT.longCommand())) {
        builder.function(Function.ENCRYPT);
        builder.plainText(commandLine.getOptionValue("text"));
        builder.encryptKey(commandLine.getOptionValue("key"));
      }

      ExecuteParams params = builder.build();

      // execute
      execute(params);

    } catch (Exception e) {
      new HelpFormatter().printHelp(128, "java -jar encrypt.jar", header, options, footer, true);
      System.exit(-1);
    }
  }

  private static void execute(ExecuteParams params) {
    if (params != null) {
      if (params.getFunction() != null) {

        switch (params.getFunction()) {

          case ENCRYPT:

            if (isBlank(params.getPlainText())) {
              System.out.println("-encrypt function must set parameter: [-text | --plaintext] ..");
              System.exit(-1);
            }

            if (isBlank(params.getEncryptKey())) {
              System.out.println("-encrypt function must set parameter: [-key | --encryptkey] ..");
              System.exit(-1);
            }

            config.setPassword(params.getEncryptKey());
            config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
            config.setKeyObtentionIterations("1000");
            config.setPoolSize("1");
            config.setProviderName("SunJCE");
            config.setSaltGeneratorClassName("xyz.vopen.mixmicro.components.enhance.security.salt.RandomSaltGenerator");
            config.setIvGeneratorClassName("xyz.vopen.mixmicro.components.enhance.security.iv.RandomIvGenerator");
            config.setStringOutputType("base64");
            encryptor.setConfig(config);

            String result = encryptor.encrypt(params.getPlainText());

            System.out.println("===================Encrypt Result===================");
            System.out.println(String.format("    Plain Text     : %s", params.getPlainText()));
            System.out.println(String.format("    Cipher Text    : %s", result));

            System.out.println("\r\nTips: Please do not reveal your key & Ciphertext !");

            break;

          default:
            break;
        }

      } else {
        System.out.println("java -jar encrypt.jar must assign longCommand type , Like: '-encrypt' ... eg.");
        System.exit(-1);
      }
    }
  }

  /** Function Defined */
  public enum Function {
    /** */
    ENCRYPT("e", "encrypt");
    private String shortCommand;
    private String longCommand;

    Function(String shortCommand, String longCommand) {
      this.longCommand = longCommand;
      this.shortCommand = shortCommand;
    }

    public String longCommand() {
      return longCommand;
    }

    public String shortCommand() {
      return shortCommand;
    }
  }

  @Data
  @Builder
  private static class ExecuteParams {

    private Function function;

    private String plainText;

    private String encryptKey;
  }
}
