package xyz.vopen.mixmicro.components.boot.errors.adapter.attributes;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import xyz.vopen.mixmicro.components.boot.errors.WebErrorHandlers;
import xyz.vopen.mixmicro.components.boot.errors.adapter.HttpErrorAttributesAdapter;
import org.junit.Test;
import org.junit.runner.RunWith;

import static xyz.vopen.mixmicro.components.boot.errors.Params.p;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link ServletErrorAttributes}.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@RunWith(JUnitParamsRunner.class)
public class ServletErrorAttributesTest {

    @Test
    @Parameters(method = "provideInvalidParamsToConstructor")
    public void constructor_ShouldEnforceItsPreconditions(WebErrorHandlers handlers,
                                                          HttpErrorAttributesAdapter adapter,
                                                          Class<? extends Throwable> expectedException,
                                                          String expectedMessage) {
        assertThatThrownBy(() -> new ServletErrorAttributes(handlers, adapter))
            .isInstanceOf(expectedException)
            .hasMessage(expectedMessage);
    }

    private Object[] provideInvalidParamsToConstructor() {
        return p(
            p(null, null, NullPointerException.class, "Web error handlers is required"),
            p(mock(WebErrorHandlers.class), null, NullPointerException.class, "Adapter is required")
        );
    }
}
