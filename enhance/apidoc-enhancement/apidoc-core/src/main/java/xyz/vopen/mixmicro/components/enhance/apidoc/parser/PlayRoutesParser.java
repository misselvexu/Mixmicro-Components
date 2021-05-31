package xyz.vopen.mixmicro.components.enhance.apidoc.parser;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.vopen.mixmicro.components.enhance.apidoc.DocContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * play framework routes parser
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class PlayRoutesParser {

  private static final Logger LOGGER = LoggerFactory.getLogger(PlayRoutesParser.class);

  private final String routeFile;
  private final String javaSrcPath;

  private final List<RouteNode> routeNodeList = new ArrayList<>();

  private PlayRoutesParser() {
    this.routeFile = DocContext.getProjectPath().concat("conf/routes");
    javaSrcPath = DocContext.getJavaSrcPaths().get(0);
    parse();
  }

  public static final PlayRoutesParser INSTANCE = new PlayRoutesParser();

  private void parse() {
    try (BufferedReader reader = new BufferedReader(new FileReader(routeFile))) {

      String lineText;
      String[] nodes;
      while ((lineText = reader.readLine()) != null) {
        lineText = lineText.trim();
        if (StringUtils.isNotEmpty(lineText) && lineText.startsWith("#")) {
          nodes = lineText.split("\\s+");
          if (nodes.length >= 3 && !nodes[2].matches("\\d+")) {
            String[] actions = nodes[2].split("\\.");
            if (actions.length != 1) {
              StringBuilder ctrlPathBuilder = new StringBuilder();
              ctrlPathBuilder.append("controllers");
              for (int i = 0; i != actions.length - 1; i++) {
                ctrlPathBuilder.append('/');
                ctrlPathBuilder.append(actions[i]);
              }
              String controllerFileName = getControllerFile(ctrlPathBuilder.toString());
              if (new File(controllerFileName).exists()) {
                RouteNode routeNode =
                    new RouteNode(
                        nodes[0].trim(),
                        nodes[1].trim(),
                        controllerFileName,
                        actions[actions.length - 1]);

                routeNodeList.add(routeNode);
              }
            }
          }
        }
      }
    } catch (IOException e) {
      LOGGER.error("parse Play Routes Error", e);
    }
  }

  public RouteNode getRouteNode(File controllerFile, String methodName) {
    Optional<RouteNode> routeNode =
        routeNodeList.stream()
            .filter(
                node ->
                    controllerFile.getAbsolutePath().equals(node.controllerFile)
                        && node.actionMethod.equals(methodName))
            .findFirst();
    return routeNode.orElse(null);
  }

  public List<RouteNode> getRouteNodeList() {
    return routeNodeList;
  }

  private String getControllerFile(String relativePath) {
    return javaSrcPath + relativePath + ".java";
  }

  public static class RouteNode {
    public final String method;
    public final String routeUrl;
    public final String controllerFile;
    public final String actionMethod;

    public RouteNode(String method, String routeUrl, String controllerFile, String actionMethod) {
      this.method = method;
      this.routeUrl = routeUrl;
      this.controllerFile = controllerFile;
      this.actionMethod = actionMethod;
    }
  }
}
