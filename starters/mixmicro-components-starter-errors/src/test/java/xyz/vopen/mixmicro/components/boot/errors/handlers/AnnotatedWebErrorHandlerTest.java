package xyz.vopen.mixmicro.components.boot.errors.handlers;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import xyz.vopen.mixmicro.components.boot.errors.Argument;
import xyz.vopen.mixmicro.components.boot.errors.HandledException;
import xyz.vopen.mixmicro.components.boot.errors.annotation.ExceptionMapping;
import xyz.vopen.mixmicro.components.boot.errors.annotation.ExposeAsArg;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static xyz.vopen.mixmicro.components.boot.errors.Params.p;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Unit tests for {@link AnnotatedWebErrorHandler} error handler.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@RunWith(JUnitParamsRunner.class)
public class AnnotatedWebErrorHandlerTest {

    /**
     * Subject under test.
     */
    private final AnnotatedWebErrorHandler handler = new AnnotatedWebErrorHandler();

    @Test
    @Parameters(method = "provideParamsForCanHandle")
    public void canHandle_ShouldOnlyReturnTrueForExceptionsAnnotatedWithExceptionMapping(Exception exception,
                                                                                         boolean expected) {
        assertThat(handler.canHandle(exception))
            .isEqualTo(expected);
    }

    @Test
    @Parameters(method = "provideParamsForHandle")
    public void handle_ShouldProperlyHandleTheGivenException(Exception exception,
                                                             String code,
                                                             HttpStatus status,
                                                             List<Argument> args) {
        HandledException handled = handler.handle(exception);

        assertThat(handled).isNotNull();
        assertThat(handled.getErrorCodes()).hasSize(1);
        assertThat(handled.getErrorCodes()).containsExactly(code);
        assertThat(handled.getStatusCode()).isEqualTo(status);
        assertThat((handled.getArguments().get(code))).isEqualTo(args);
    }

    private Object[] provideParamsForCanHandle() {
        return p(
            p(null, false),
            p(new NotAnnotated(), false),
            p(new Annotated("", ""), true),
            p(new Inherited(), true)
        );
    }

    private Object[] provideParamsForHandle() {
        return p(
            p(new Annotated("f", "s"), "annotated", BAD_REQUEST, asList(
                Argument.arg("staticExposure", "42"),
                Argument.arg("some_value", "f"),
                Argument.arg("other", "s"))),
            p(new Inherited(), "annotated", BAD_REQUEST, asList(
                Argument.arg("staticExposure", "42"),
                Argument.arg("random", "random"),
                Argument.arg("other", "s"))),
            p(new NoExposedArgs(), "no_exposed", BAD_REQUEST, Collections.emptyList())
        );
    }

    // Auxiliary exception definitions!

    private class NotAnnotated extends RuntimeException {
    }

    @ExceptionMapping(statusCode = BAD_REQUEST, errorCode = "no_exposed")
    private class NoExposedArgs extends RuntimeException {
    }

    @ExceptionMapping(statusCode = BAD_REQUEST, errorCode = "annotated")
    private class Annotated extends RuntimeException {

        @ExposeAsArg(value = -1, name = "some_value")
        private final String someValue;
        private final String other;

        private Annotated(String someValue, String other) {
            this.someValue = someValue;
            this.other = other;
        }

        @ExposeAsArg(0)
        public void shouldBeDiscarded() {
        }

        @ExposeAsArg(-100)
        public String shouldBeIgnoredToo(String howToPassThis) {
            return "";
        }

        @ExposeAsArg(-11)
        public String staticExposure() {
            return "42";
        }

        @ExposeAsArg(value = 100, name = "other")
        public String getOther() {
            return other;
        }
    }

    private class Inherited extends Annotated {
        private Inherited() {
            super("f", "s");
        }

        @ExposeAsArg(-1)
        public String random() {
            return "random";
        }

        @ExposeAsArg(-2)
        public int thrower() {
            throw new IllegalStateException(":(");
        }
    }
}
