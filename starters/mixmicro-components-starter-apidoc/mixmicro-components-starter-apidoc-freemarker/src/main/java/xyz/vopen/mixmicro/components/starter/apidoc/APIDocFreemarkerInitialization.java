package xyz.vopen.mixmicro.components.starter.apidoc;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import xyz.vopen.mixmicro.components.enhance.apidoc.APIHtmlDocGenerator;
import xyz.vopen.mixmicro.components.enhance.apidoc.DocsConfig;
import xyz.vopen.mixmicro.components.enhance.apidoc.IResponseWrapper;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * springboot APIDoc initialization generator
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/6/2
 */
public class APIDocFreemarkerInitialization implements ApplicationRunner {

  private final APIDocProperties apiDocProperties;

  private static final String ABSOLUTE_PATH = System.getProperty("user.dir");

  public APIDocFreemarkerInitialization(APIDocProperties apiDocProperties) {
    this.apiDocProperties = apiDocProperties;
  }

  private static final IResponseWrapper iResponseWrapper =
      responseNode -> {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 200);
        resultMap.put("data", responseNode);
        resultMap.put("message", "success");
        return resultMap;
      };

  @Override
  public void run(ApplicationArguments args) throws Exception {
    DocsConfig docsConfig = new DocsConfig();
    docsConfig.setApiVersion(apiDocProperties.getCurrentVersion());
    docsConfig.setAutoGenerate(true);
    docsConfig.setIgnorePackages(apiDocProperties.getIgnorePackages());
    docsConfig.setLocale(Locale.CHINA);
    docsConfig.setOpenReflection(true);
    if (StringUtils.isBlank(apiDocProperties.getProjectPath())) {
      // default scan root path
      docsConfig.setProjectPath(ABSOLUTE_PATH);
    } else {
      docsConfig.setProjectPath(apiDocProperties.getProjectPath());
    }
    // docs generate destination path
    docsConfig.setDocsPath(apiDocProperties.getDistPath());
    // auto generate
    docsConfig.setAutoGenerate(Boolean.TRUE);
    APIHtmlDocGenerator apiHtmlDocGenerator = new APIHtmlDocGenerator(iResponseWrapper);
    apiHtmlDocGenerator.generate(docsConfig);
  }
}
