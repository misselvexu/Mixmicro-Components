package xyz.vopen.mixmicro.components.enhance.apidoc.utils;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import xyz.vopen.mixmicro.components.enhance.apidoc.DocContext;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.ClassNode;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.ControllerNode;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.ResponseNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * cache all controller nodes
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class CacheUtils {

  private CacheUtils() {}

  private static final String CACHE_FILE = ".cache.json";
  private static final Logger LOGGER = LoggerFactory.getLogger(CacheUtils.class);

  /**
   * save controller nodes of this version
   *
   * @param controllerNodes controller nodes
   */
  public static void saveControllerNodes(List<ControllerNode> controllerNodes) {
    try {
      controllerNodes.forEach(
          controllerNode ->
              controllerNode
                  .getRequestNodes()
                  .forEach(
                      requestNode -> {
                        requestNode.setControllerNode(null);
                        requestNode.setLastRequestNode(null);
                        ResponseNode responseNode = requestNode.getResponseNode();
                        removeLoopNode(responseNode);
                      }));
      CommonUtils.writeToDisk(
          new File(DocContext.getDocPath(), CACHE_FILE), JSON.toJSONString(controllerNodes));
    } catch (Exception ex) {
      LOGGER.error("saveControllerNodes error!!!", ex);
    }
  }

  private static void removeLoopNode(ClassNode classNode) {
    classNode.setParentNode(null);
    classNode.setGenericNodes(null);
    classNode
        .getChildNodes()
        .forEach(
            fieldNode -> {
              fieldNode.setClassNode(null);
              if (fieldNode.getChildNode() != null) {
                ClassNode fieldClassNode = new ClassNode();
                BeanUtils.copyProperties(fieldNode.getChildNode(), fieldClassNode);
                removeLoopNode(fieldClassNode);
              }
            });
  }

  /**
   * get controller nodes of api version
   *
   * @param apiVersion api version
   * @return return null if cache not exists
   */
  public static List<ControllerNode> getControllerNodes(String apiVersion) {
    File apiRootPath = new File(new File(DocContext.getDocPath()).getParentFile(), apiVersion);
    if (!apiRootPath.exists()) {
      return new ArrayList<>();
    }
    File cacheFile = new File(apiRootPath, CACHE_FILE);
    if (!cacheFile.exists()) {
      return new ArrayList<>();
    }
    try {
      ControllerNode[] controllerNodes =
          JSON.parseObject(
              CommonUtils.streamToString(new FileInputStream(cacheFile)), ControllerNode[].class);
      return Arrays.asList(controllerNodes);
    } catch (IOException ex) {
      LOGGER.error("get ControllerNodes error!!!", ex);
    }
    return new ArrayList<>();
  }
}
