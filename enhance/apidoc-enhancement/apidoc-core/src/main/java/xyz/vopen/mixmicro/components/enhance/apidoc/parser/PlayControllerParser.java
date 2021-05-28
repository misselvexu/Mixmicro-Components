package xyz.vopen.mixmicro.components.enhance.apidoc.parser;

import com.github.javaparser.ast.body.MethodDeclaration;
import xyz.vopen.mixmicro.components.enhance.apidoc.consts.RequestMethod;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.ParamNode;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.RequestNode;

import java.util.Arrays;

/**
 * user for play framework
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class PlayControllerParser extends AbsControllerParser {

  @Override
  protected void afterHandleMethod(RequestNode requestNode, MethodDeclaration md) {
    PlayRoutesParser.RouteNode routeNode =
        PlayRoutesParser.INSTANCE.getRouteNode(getControllerFile(), md.getNameAsString());
    if (routeNode == null) {
      return;
    }

    String method = routeNode.method.toUpperCase();
    if ("*".equals(method)) {
      requestNode.setMethod(Arrays.asList(RequestMethod.GET.name(), RequestMethod.POST.name()));
    } else {
      requestNode.addMethod(RequestMethod.valueOf(method).name());
    }

    requestNode.setUrl(routeNode.routeUrl);

    md.getParameters()
        .forEach(
            p -> {
              String paraName = p.getName().asString();
              ParamNode paramNode = requestNode.getParamNodeByName(paraName);
              if (paramNode != null) {
                p.getAnnotationByName("Required")
                    .ifPresent(
                        r -> {
                          paramNode.setRequired(true);
                        });
              }
            });
  }
}
