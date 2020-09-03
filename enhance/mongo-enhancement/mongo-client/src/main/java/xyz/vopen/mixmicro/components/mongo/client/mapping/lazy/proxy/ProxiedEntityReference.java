package xyz.vopen.mixmicro.components.mongo.client.mapping.lazy.proxy;

import xyz.vopen.mixmicro.components.mongo.client.Key;

/** @author Uwe Schaefer, (schaefer@thomas-daily.de) */
// CHECKSTYLE:OFF
public interface ProxiedEntityReference extends ProxiedReference {
  Key<?> __getKey();
}
