package xyz.vopen.mixmicro.components.boot.errors.adapter.attributes;

import xyz.vopen.mixmicro.components.boot.errors.annotation.ExceptionMapping;
import xyz.vopen.mixmicro.components.boot.errors.annotation.ExposeAsArg;

import java.util.Map;

import static xyz.vopen.mixmicro.components.boot.errors.handlers.ServletWebErrorHandler.NO_HANDLER;
import static xyz.vopen.mixmicro.components.boot.errors.handlers.SpringSecurityWebErrorHandler.ACCESS_DENIED;
import static xyz.vopen.mixmicro.components.boot.errors.handlers.SpringSecurityWebErrorHandler.AUTH_REQUIRED;
import static org.springframework.http.HttpStatus.*;

/**
 * A simple container of a few peculiar exceptions.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
class Exceptions {

    /**
     * Given a classic set of error attributes, it will determines the to-be-handled
     * exception from the status code.
     *
     * @param attributes Key-value pairs representing the error attributes.
     * @return The mapped exception.
     */
    static Exception refineUnknownException(Map<String, Object> attributes) {
        switch (getStatusCode(attributes)) {
            case 401:
                return new UnauthorizedException();
            case 403:
                return new ForbiddenException();
            case 404:
                return new HandlerNotFoundException(getPath(attributes));
            default:
                return new IllegalStateException("The exception is null: " + attributes);
        }
    }

    private static String getPath(Map<String, Object> attributes) {
        Object path = attributes.get("path");
        return path == null ? "unknown" : path.toString();
    }

    /**
     * Extracts the status code from error attributes.
     *
     * @param attributes The error attributes.
     * @return Extracted status code.
     */
    private static int getStatusCode(Map<String, Object> attributes) {
        try {
            return (Integer) attributes.getOrDefault("status", 0);
        } catch (Exception e) {
            return 0;
        }
    }

    @ExceptionMapping(statusCode = UNAUTHORIZED, errorCode = AUTH_REQUIRED)
    private static final class UnauthorizedException extends RuntimeException {
    }

    @ExceptionMapping(statusCode = FORBIDDEN, errorCode = ACCESS_DENIED)
    private static final class ForbiddenException extends RuntimeException {
    }

    @ExceptionMapping(statusCode = NOT_FOUND, errorCode = NO_HANDLER)
    private static final class HandlerNotFoundException extends RuntimeException {

        /**
         * The to-be-exposed path.
         */
        @ExposeAsArg(0)
        @SuppressWarnings("FieldCanBeLocal")
        private final String path;

        private HandlerNotFoundException(String path) {
            this.path = path;
        }
    }
}
