package xyz.vopen.mixmicro.components.boot.errors.adapter;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import xyz.vopen.mixmicro.components.boot.errors.Argument;
import xyz.vopen.mixmicro.components.boot.errors.HttpError;
import xyz.vopen.mixmicro.components.boot.errors.conf.ErrorsProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static xyz.vopen.mixmicro.components.boot.errors.Params.p;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

/**
 * Unit tests for {@link DefaultHttpErrorAttributesAdapter}.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@RunWith(JUnitParamsRunner.class)
public class DefaultHttpErrorAttributesAdapterTest {

    @Test
    @SuppressWarnings("unchecked")
    public void adapt_ShouldAdaptTheHttpErrorToAMapProperly() {
        ErrorsProperties errorsProperties = new ErrorsProperties();
        errorsProperties.setExposeArguments(ErrorsProperties.ArgumentExposure.NON_EMPTY);
        HttpErrorAttributesAdapter adapter = new DefaultHttpErrorAttributesAdapter(errorsProperties);

        HttpError.CodedMessage first = new HttpError.CodedMessage("f", null, emptyList());
        HttpError.CodedMessage sec = new HttpError.CodedMessage("s", "a message", singletonList(Argument.arg("param", 123)));

        HttpError httpError = new HttpError(asList(first, sec), HttpStatus.BAD_REQUEST);
        assertThat(httpError.toString()).isNotNull();

        Map<String, Object> adapted = adapter.adapt(httpError);

        List<Map<String, Object>> errors = (List<Map<String, Object>>) adapted.get("errors");
        assertThat(errors).isNotNull();
        assertThat(errors.get(0)).containsOnly(entry("code", "f"), entry("message", null));
        assertThat(errors.get(1)).containsOnly(entry("code", "s"), entry("message", "a message"), entry("arguments", singletonMap("param", 123)));
    }

    @Test
    public void adapt_ShouldAdaptFingerprintToAMapProperly() {
        HttpErrorAttributesAdapter adapter = new DefaultHttpErrorAttributesAdapter(new ErrorsProperties());

        HttpError httpError = new HttpError(emptyList(), HttpStatus.BAD_REQUEST);
        httpError.setFingerprint("fingerprint");

        Map<String, Object> adapted = adapter.adapt(httpError);

        assertThat(adapted).contains(entry("fingerprint", "fingerprint"));
    }

    @Test
    @Parameters(method = "provideExposureParams")
    @SuppressWarnings("unchecked")
    public void adapt_ShouldAdaptTheHttpErrorWithArgumentsToAMapProperly(
        ErrorsProperties.ArgumentExposure exposure,
        List<Argument> arguments,
        boolean parametersFieldPresent) {
        ErrorsProperties errorsProperties = new ErrorsProperties();
        errorsProperties.setExposeArguments(exposure);
        HttpErrorAttributesAdapter adapter = new DefaultHttpErrorAttributesAdapter(errorsProperties);

        HttpError.CodedMessage codedMessage = new HttpError.CodedMessage("c", "msg", arguments);

        HttpError httpError = new HttpError(singletonList(codedMessage), HttpStatus.BAD_REQUEST);

        Map<String, Object> adapted = adapter.adapt(httpError);

        List<Map<String, Object>> errors = (List<Map<String, Object>>) adapted.get("errors");

        assertThat(errors.get(0).containsKey("arguments")).isEqualTo(parametersFieldPresent);
    }

    private Object[] provideExposureParams() {
        return p(
            p(ErrorsProperties.ArgumentExposure.NEVER, emptyList(), false),
            p(ErrorsProperties.ArgumentExposure.NEVER, singletonList(Argument.arg("name", "value")), false),
            p(ErrorsProperties.ArgumentExposure.NON_EMPTY, emptyList(), false),
            p(ErrorsProperties.ArgumentExposure.NON_EMPTY, singletonList(Argument.arg("name", "value")), true),
            p(ErrorsProperties.ArgumentExposure.ALWAYS, emptyList(), true),
            p(ErrorsProperties.ArgumentExposure.ALWAYS, singletonList(Argument.arg("name", "value")), true)
        );
    }
}
