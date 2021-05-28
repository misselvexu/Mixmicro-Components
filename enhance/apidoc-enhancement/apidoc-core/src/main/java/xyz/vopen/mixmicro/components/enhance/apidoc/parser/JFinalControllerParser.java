package xyz.vopen.mixmicro.components.enhance.apidoc.parser;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import xyz.vopen.mixmicro.components.enhance.apidoc.consts.RequestMethod;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.RequestNode;
import xyz.vopen.mixmicro.components.enhance.apidoc.utils.CommonUtils;

import java.util.Arrays;

/**
 * use for JFinal
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class JFinalControllerParser extends AbsControllerParser {

  @Override
  protected void afterHandleMethod(RequestNode requestNode, MethodDeclaration md) {
    String methodName = md.getNameAsString();
    requestNode.setUrl(getUrl(methodName));
    md.getAnnotationByName("ActionKey")
        .ifPresent(
            an -> {
              if (an instanceof SingleMemberAnnotationExpr) {
                String url = ((SingleMemberAnnotationExpr) an).getMemberValue().toString();
                requestNode.setMethod(
                    Arrays.asList(RequestMethod.GET.name(), RequestMethod.POST.name()));
                requestNode.setUrl(CommonUtils.removeQuotations(url));
              }
            });
  }

  private String getUrl(String methodName) {
    JFinalRoutesParser.RouteNode routeNode =
        JFinalRoutesParser.INSTANCE.getRouteNode(getControllerFile().getAbsolutePath());
    return routeNode == null ? "" : routeNode.basicUrl + "/" + methodName;
  }
}
