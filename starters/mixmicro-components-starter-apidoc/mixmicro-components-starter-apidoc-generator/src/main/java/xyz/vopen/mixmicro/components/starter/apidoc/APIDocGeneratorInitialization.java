package xyz.vopen.mixmicro.components.starter.apidoc;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.yaml.snakeyaml.Yaml;
import xyz.vopen.mixmicro.components.enhance.apidoc.DocsConfig;
import xyz.vopen.mixmicro.components.enhance.apidoc.generator.APIDataDocGenerator;
import xyz.vopen.mixmicro.components.enhance.apidoc.generator.APIGenerator;
import xyz.vopen.mixmicro.components.enhance.apidoc.repository.APIDocGenRepository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Map;

/**
 * springboot APIDoc initialization generator
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/6/2
 */
public class APIDocGeneratorInitialization implements ApplicationRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(APIDocGeneratorInitialization.class);
  private static final String ABSOLUTE_PATH = System.getProperty("user.dir");

  private final APIDocProperties apiDocProperties;
  private final APIDocGenRepository apiDocGenRepository;

  public APIDocGeneratorInitialization(
      APIDocProperties apiDocProperties, APIDocGenRepository apiDocGenRepository) {
    this.apiDocProperties = apiDocProperties;
    this.apiDocGenRepository = apiDocGenRepository;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    DocsConfig docsConfig = new DocsConfig();
    docsConfig.setApiVersion(apiDocProperties.getCurrentVersion());
    docsConfig.setAutoGenerate(true);
    docsConfig.setIgnorePackages(apiDocProperties.getIgnorePackages());
    docsConfig.setOpenReflection(true);
    if (StringUtils.isBlank(apiDocProperties.getProjectPath())) {
      // default scan root path
      docsConfig.setProjectPath(ABSOLUTE_PATH);
    } else {
      docsConfig.setProjectPath(apiDocProperties.getProjectPath());
    }
    docsConfig.setProjectName(getProjectName());
    APIGenerator apiGenerator = new APIDataDocGenerator(apiDocGenRepository);
    apiGenerator.generate(docsConfig);
  }

  /**
   * get project name by spring.application.name
   *
   * @return app name
   */
  private String getProjectName() {
    Yaml yaml = new Yaml();
    URL url = this.getClass().getClassLoader().getResource("application.yaml");
    if (null == url) {
      url = this.getClass().getClassLoader().getResource("application.yml");
    }
    if (url != null) {
      try {
        Map<String, Object> map = yaml.load(new FileInputStream(url.getFile()));
        Map<?, ?> map2 = (Map<?, ?>) map.get("spring");
        Map<?, ?> map3 = (Map<?, ?>) map2.get("application");
        return map3.get("name").toString();
      } catch (FileNotFoundException e) {
        LOGGER.error("'application.yaml' or 'application.yml' not fount in 'resources' path");
      }
    }
    return null;
  }
}
