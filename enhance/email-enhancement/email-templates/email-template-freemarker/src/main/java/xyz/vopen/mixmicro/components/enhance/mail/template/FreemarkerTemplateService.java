package xyz.vopen.mixmicro.components.enhance.mail.template;

import freemarker.template.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import xyz.vopen.mixmicro.components.enhance.mail.service.TemplateService;
import xyz.vopen.mixmicro.components.enhance.mail.service.exception.TemplateException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.io.Files.getFileExtension;

/**
 * {@link FreemarkerTemplateService}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Service
public class FreemarkerTemplateService implements TemplateService {

  private final Configuration freemarkerConfiguration;

  public FreemarkerTemplateService(Configuration freemarkerConfiguration) {
    this.freemarkerConfiguration = freemarkerConfiguration;
  }

  @Override
  @NonNull
  public String mergeTemplateIntoString(final @NonNull String templateReference,
                                        final @NonNull Map<String, Object> model)
      throws IOException, TemplateException {
    final String trimmedTemplateReference = templateReference.trim();
    checkArgument(!isNullOrEmpty(trimmedTemplateReference), "The given template is null, empty or blank");
    if (trimmedTemplateReference.contains(".")) {
      checkArgument(Objects.equals(getFileExtension(trimmedTemplateReference), expectedTemplateExtension()),
          "Expected a Freemarker template file with extension 'ftl', while '%s' was given",
          getFileExtension(trimmedTemplateReference));
    }

    try {

      final String normalizedTemplateReference = trimmedTemplateReference.endsWith(expectedTemplateExtension()) ? trimmedTemplateReference : trimmedTemplateReference + '.' + expectedTemplateExtension();
      return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate(normalizedTemplateReference, StandardCharsets.UTF_8.name()), model);
    } catch (Exception e) {
      throw new TemplateException(e);
    }
  }

  @Override
  public String expectedTemplateExtension() {
    return "ftl";
  }

}