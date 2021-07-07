package xyz.vopen.mixmicro.components.enhance.apidoc.parser;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import xyz.vopen.mixmicro.components.enhance.apidoc.annotations.ApiDoc;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.RequestNode;
import xyz.vopen.mixmicro.components.enhance.apidoc.utils.CommonUtils;

/**
 * can apply to any java project, but you have to set the request url and method in annotation
 * ${@link ApiDoc} by yourself.
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class GenericControllerParser extends AbsControllerParser {

  @Override
  protected void afterHandleMethod(RequestNode requestNode, MethodDeclaration md) {
    md.getAnnotationByName("ApiDoc")
        .ifPresent(
            an -> {
              if (an instanceof NormalAnnotationExpr) {
                ((NormalAnnotationExpr) an)
                    .getPairs()
                    .forEach(
                        p -> {
                          String n = p.getNameAsString();
                          if (n.equals("url")) {
                            requestNode.setUrl(
                                CommonUtils.removeQuotations(p.getValue().toString()));
                          } else if (n.equals("method")) {
                            requestNode.addMethod(
                                CommonUtils.removeQuotations(p.getValue().toString()));
                          }
                        });
              }
            });
  }
}
