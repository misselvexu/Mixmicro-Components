package xyz.vopen.mixmicro.components.boot.errors.handlers;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import xyz.vopen.mixmicro.components.boot.errors.HandledException;
import xyz.vopen.mixmicro.components.boot.errors.WebErrorHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingMatrixVariableException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingRequestHeaderException;
import xyz.vopen.mixmicro.components.boot.errors.Argument;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static xyz.vopen.mixmicro.components.boot.errors.Params.p;
import static xyz.vopen.mixmicro.components.boot.errors.handlers.MissingRequestParametersWebErrorHandler.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Unit tests for {@link MissingRequestParametersWebErrorHandler} handler.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@RunWith(JUnitParamsRunner.class)
public class MissingRequestParametersWebErrorHandlerTest {

    /**
     * Subject under test.
     */
    private final WebErrorHandler handler = new MissingRequestParametersWebErrorHandler();

    @Test
    @Parameters(method = "provideParamsForCanHandle")
    public void canHandle_ShouldReturnTrueForMissingRequestParamsErrors(Throwable exception, boolean expected) {
        assertThat(handler.canHandle(exception))
            .isEqualTo(expected);
    }

    @Test
    @Parameters(method = "provideParamsForHandle")
    public void handle_ShouldHandleMissingRequestParamsErrorsProperly(Throwable exception,
                                                                      String expectedCode,
                                                                      HttpStatus expectedStatus,
                                                                      Map<String, List<?>> expectedArgs) {
        HandledException handledException = handler.handle(exception);

        assertThat(handledException.getErrorCodes()).containsOnly(expectedCode);
        assertThat(handledException.getStatusCode()).isEqualTo(expectedStatus);
        assertThat(handledException.getArguments()).isEqualTo(expectedArgs);
    }

    private Object[] provideParamsForCanHandle() {
        return p(
            p(null, false),
            p(new RuntimeException(), false),
            p(new MissingPathVariableException("name", getParameter()), false),
            p(new MissingRequestHeaderException("name", getParameter()), true),
            p(new MissingRequestCookieException("name", getParameter()), true),
            p(new MissingMatrixVariableException("name", getParameter()), true)
        );
    }

    private Object[] provideParamsForHandle() {
        return p(
            p(
                new MissingRequestHeaderException("Authorization", getParameter()),
                MISSING_HEADER,
                BAD_REQUEST,
                singletonMap(MISSING_HEADER, asList(Argument.arg("name", "Authorization"), Argument.arg("expected", "boolean")))
            ),
            p(
                new MissingRequestCookieException("sessionId", getParameter()),
                MISSING_COOKIE,
                BAD_REQUEST,
                singletonMap(MISSING_COOKIE, asList(Argument.arg("name", "sessionId"), Argument.arg("expected", "boolean")))
            ),
            p(
                new MissingMatrixVariableException("name", getParameter()),
                MISSING_MATRIX_VARIABLE,
                BAD_REQUEST,
                singletonMap(MISSING_MATRIX_VARIABLE, asList(Argument.arg("name", "name"), Argument.arg("expected", "boolean")))
            )
        );
    }

    private MethodParameter getParameter() {
        Method testMethod = Arrays.stream(getClass().getMethods())
            .filter(m -> m.getName().equals("canHandle_ShouldReturnTrueForMissingRequestParamsErrors"))
            .findFirst()
            .orElseThrow(IllegalStateException::new);
        return new MethodParameter(testMethod, 1);
    }
}
