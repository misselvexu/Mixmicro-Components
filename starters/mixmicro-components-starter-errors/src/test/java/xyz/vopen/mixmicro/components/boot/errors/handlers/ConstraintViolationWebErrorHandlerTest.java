package xyz.vopen.mixmicro.components.boot.errors.handlers;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import xyz.vopen.mixmicro.components.boot.errors.Argument;
import xyz.vopen.mixmicro.components.boot.errors.HandledException;
import xyz.vopen.mixmicro.components.boot.errors.WebErrorHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonMap;
import static xyz.vopen.mixmicro.components.boot.errors.Params.p;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link ConstraintViolationWebErrorHandler} handler.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@RunWith(JUnitParamsRunner.class)
public class ConstraintViolationWebErrorHandlerTest {

    /**
     * Subject under test.
     */
    private final WebErrorHandler handler = new ConstraintViolationWebErrorHandler();

    /**
     * Will be used to generate valid constraint violation exceptions.
     */
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @Parameters(method = "provideParamsForCanHandle")
    public void canHandle_ShouldOnlyReturnTrueForViolationExceptionsWithAtLeastOneViolation(Exception exception,
                                                                                            boolean expected) {
        assertThat(handler.canHandle(exception)).isEqualTo(expected);
    }

    @Test
    @Parameters(method = "provideParamsForHandle")
    public void handle_ShouldHandleViolationExceptionsProperly(ConstraintViolationException exception,
                                                               Set<String> errorCodes,
                                                               Map<String, List<Argument>> arguments) {
        HandledException handledException = handler.handle(exception);

        assertThat(handledException.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(handledException.getErrorCodes()).containsAll(errorCodes);
        assertThat(handledException.getArguments()).containsAllEntriesOf(arguments);
    }

    @SuppressWarnings("unchecked")
    private Object[] provideParamsForCanHandle() {
        Set<ConstraintViolation<?>> violations = singleton(mock(ConstraintViolation.class));

        return p(
            p(null, false),
            p(new RuntimeException(), false),
            p(new ConstraintViolationException(null), false),
            p(new ConstraintViolationException(Collections.emptySet()), false),
            p(new ConstraintViolationException(violations), true)
        );
    }

    private Object[] provideParamsForHandle() {
        return p(
            p(
                v(new Person("alidg", 19)),
                setOf("username.size"),
                singletonMap("username.size", asList(
                    Argument.arg("max", 10),
                    Argument.arg("min", 6),
                    Argument.arg("invalid", "alidg"),
                    Argument.arg("property", "username")))
            ),
            p(
                v(new Person("", 19)),
                setOf("username.blank", "username.size"),
                singletonMap("username.size", asList(
                    Argument.arg("max", 10),
                    Argument.arg("min", 6),
                    Argument.arg("invalid", ""),
                    Argument.arg("property", "username")))
            ),
            p(
                v(new Person("ali", 12)),
                setOf("username.size", "age.min"),
                new HashMap<String, List<?>>() {{
                    put("username.size", asList(
                        Argument.arg("max", 10),
                        Argument.arg("min", 6),
                        Argument.arg("invalid", "ali"),
                        Argument.arg("property", "username")));
                    put("age.min", asList(
                        Argument.arg("value", 18L),
                        Argument.arg("invalid", 12),
                        Argument.arg("property", "age")));
                }}
            ),
            p(
                v(new Person("ali", 35)),
                setOf("username.size", "age.max"),
                new HashMap<String, List<?>>() {{
                    put("username.size", asList(
                        Argument.arg("max", 10),
                        Argument.arg("min", 6),
                        Argument.arg("invalid", "ali"),
                        Argument.arg("property", "username")));
                    put("age.max", asList(
                        Argument.arg("value", 30L),
                        Argument.arg("invalid", 35),
                        Argument.arg("property", "age")));
                }}
            )
        );
    }

    private Set<String> setOf(String... elements) {
        return Arrays.stream(elements).collect(Collectors.toSet());
    }

    private ConstraintViolationException v(Object pojo) {
        return new ConstraintViolationException(validator.validate(pojo));
    }

    private static class Person {

        @NotBlank(message = "{username.blank}")
        @Size(min = 6, max = 10, message = "username.size")
        private String username;

        @Min(message = "age.min", value = 18)
        @Max(message = "{age.max}", value = 30)
        private int age;

        public Person() {
        }

        public Person(String username, int age) {
            this.username = username;
            this.age = age;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
