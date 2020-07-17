package xyz.vopen.mixmicro.components.enhance.mail.template;

import com.google.common.base.Preconditions;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import xyz.vopen.mixmicro.components.enhance.mail.service.TemplateService;
import xyz.vopen.mixmicro.components.enhance.mail.service.exception.TemplateException;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.io.Files.getFileExtension;

/**
 * {@link ThymeleafTemplateService}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Service
public class ThymeleafTemplateService implements TemplateService {

  final private SpringTemplateEngine thymeleafEngine;

  @Value("${spring.thymeleaf.suffix:.html}")
  private String thymeleafSuffix;

  public ThymeleafTemplateService(SpringTemplateEngine thymeleafEngine) {
    this.thymeleafEngine = thymeleafEngine;
  }

  @Override
  public
  @NonNull
  String mergeTemplateIntoString(final @NonNull String templateReference,
                                 final @NonNull Map<String, Object> model)
      throws IOException, TemplateException {
    final String trimmedTemplateReference = templateReference.trim();
    Preconditions.checkArgument(!isNullOrEmpty(trimmedTemplateReference), "The given template is null, empty or blank");
    if (trimmedTemplateReference.contains(".")) {
      Preconditions.checkArgument(Objects.equals(getNormalizedFileExtension(trimmedTemplateReference), expectedTemplateExtension()),
          "Expected a Thymeleaf template file with extension '%s', while '%s' was given. To check " +
              "the default extension look at 'spring.thymeleaf.suffix' in your application.properties file",
          expectedTemplateExtension(), getNormalizedFileExtension(trimmedTemplateReference));
    }

    final Context context = new Context();
    context.setVariables(model);

    return thymeleafEngine.process(FilenameUtils.removeExtension(trimmedTemplateReference), context);
  }

  @Override
  public String expectedTemplateExtension() {
    return thymeleafSuffix;
  }

  private String getNormalizedFileExtension(final String templateReference) {
    return "." + getFileExtension(templateReference);
  }

}