package xyz.vopen.mixmicro.components.starter.apidoc;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.CollectionUtils;
import org.yaml.snakeyaml.Yaml;
import xyz.vopen.mixmicro.components.enhance.apidoc.DocsConfig;
import xyz.vopen.mixmicro.components.enhance.apidoc.generator.APIDataDocGenerator;
import xyz.vopen.mixmicro.components.enhance.apidoc.generator.APIGenerator;
import xyz.vopen.mixmicro.components.enhance.apidoc.repository.APIDocGenRepository;
import xyz.vopen.mixmicro.components.enhance.apidoc.repository.impl.APIDocGenRepositoryImpl;

import javax.validation.constraints.NotNull;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/6/15
 */
public class APIGeneratorRunner {
  private static final Logger LOGGER = LoggerFactory.getLogger(APIGeneratorRunner.class);
  private static final String ABSOLUTE_PATH = System.getProperty("user.dir");
  private static APIDocProperties apiDocProperties;
  private static APIDocGenRepository apiDocGenRepository;

  public static void main(String[] args) {
    apiDocProperties = getApiDocProperties();
    MongoClient mongoClient = MongoClients.create(apiDocProperties.getMongoUri());
    apiDocGenRepository =
        new APIDocGenRepositoryImpl(
            new MongoTemplate(mongoClient, apiDocProperties.getApiDatabase()));
    generator();
  }

  public static void generator() {
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
   * init api docs required properties
   *
   * @return properties
   */
  private static @NotNull APIDocProperties getApiDocProperties() {
    APIDocProperties apiDocProperties = new APIDocProperties();
    Map<String, Object> map = getApplicationYaml();
    if (CollectionUtils.isEmpty(map)) {
      LOGGER.error("no any properties in application.yaml or application.yml");
      return apiDocProperties;
    }
    Map<?, ?> mixmicroMap = (Map<?, ?>) map.get("mixmicro");
    Map<?, ?> apidocMap = (Map<?, ?>) mixmicroMap.get("apidoc");
    Map<?, ?> clientMap = (Map<?, ?>) apidocMap.get("client");
    apiDocProperties.setCurrentVersion(clientMap.get("current-version").toString());
    String ignorePackages = clientMap.get("ignore-packages").toString();
    if (StringUtils.isNotBlank(ignorePackages)) {
      String[] packageArray = ignorePackages.split(",");
      apiDocProperties.setIgnorePackages(new HashSet<>(Arrays.asList(packageArray)));
    }
    apiDocProperties.setProjectPath(clientMap.get("project-path").toString());
    return apiDocProperties;
  }

  /**
   * get project name by spring.application.name
   *
   * @return app name
   */
  private static String getProjectName() {
    Map<String, Object> map = getApplicationYaml();
    if (CollectionUtils.isEmpty(map)) {
      LOGGER.error("no any properties in application.yaml or application.yml");
      return null;
    }
    Map<?, ?> springMap = (Map<?, ?>) map.get("spring");
    Map<?, ?> applicationMap = (Map<?, ?>) springMap.get("application");
    return applicationMap.get("name").toString();
  }

  private static Map<String, Object> getApplicationYaml() {
    URL url = APIGeneratorRunner.class.getClassLoader().getResource("application.yaml");
    if (null == url) {
      url = APIGeneratorRunner.class.getClassLoader().getResource("application.yml");
    }
    if (url != null) {
      try {
        Yaml yaml = new Yaml();
        return yaml.load(new FileInputStream(url.getFile()));
      } catch (FileNotFoundException e) {
        LOGGER.error("'application.yaml' or 'application.yml' not fount in 'resources' path");
      }
    }
    return new HashMap<>();
  }
}
