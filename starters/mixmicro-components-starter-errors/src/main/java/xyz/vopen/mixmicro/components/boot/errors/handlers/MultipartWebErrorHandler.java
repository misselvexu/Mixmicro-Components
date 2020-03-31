package xyz.vopen.mixmicro.components.boot.errors.handlers;

import xyz.vopen.mixmicro.components.boot.errors.Argument;
import xyz.vopen.mixmicro.components.boot.errors.HandledException;
import xyz.vopen.mixmicro.components.boot.errors.WebErrorHandler;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import java.util.List;
import java.util.Map;

import static java.util.Collections.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Simple {@link WebErrorHandler} implementation to handle all upload related
 * exceptions.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class MultipartWebErrorHandler implements WebErrorHandler {

    /**
     * When exceeding the maximum possible file size, this error code would be used.
     */
    public static final String MAX_SIZE = "web.max_request_size";

    /**
     * When a multipart controller has been called with a non-multipart request.
     */
    public static final String MULTIPART_EXPECTED = "web.multipart_expected";

    /**
     * Only can handle instances of {@link MultipartException}s.
     *
     * @param exception The exception to examine.
     * @return {@code true} for {@link MultipartException} subclasses, {@code false} otherwise.
     */
    @Override
    public boolean canHandle(Throwable exception) {
        return exception instanceof MultipartException;
    }

    @NonNull
    @Override
    public HandledException handle(Throwable exception) {
        String errorCode = MULTIPART_EXPECTED;
        Map<String, List<Argument>> arguments = emptyMap();

        if (exception instanceof MaxUploadSizeExceededException) {
            long maxSize = ((MaxUploadSizeExceededException) exception).getMaxUploadSize();
            errorCode = MAX_SIZE;
            arguments = singletonMap(MAX_SIZE, singletonList(Argument.arg("max_size", maxSize)));
        }

        return new HandledException(errorCode, BAD_REQUEST, arguments);
    }
}
