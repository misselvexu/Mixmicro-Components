package xyz.vopen.mixmicro.components.boot.errors.fingerprint;

import xyz.vopen.mixmicro.components.boot.errors.FingerprintProvider;
import xyz.vopen.mixmicro.components.boot.errors.HttpError;
import org.springframework.lang.NonNull;

import java.util.UUID;

/**
 * Generates a random and unique UUID for HTTP errors.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class UuidFingerprintProvider implements FingerprintProvider {

    /**
     * Generates a random UUID regardless of the given input.
     *
     * @param httpError Error event for which fingerprint is generated.
     * @return The generated UUID based fingerprint.
     */
    @Override
    public String generate(@NonNull HttpError httpError) {
        return UUID.randomUUID().toString();
    }
}
