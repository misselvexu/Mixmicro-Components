package xyz.vopen.mixmicro.components.enhance.cache;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public interface AutoReleaseLock extends AutoCloseable {
  /** Release the lock use Java 7 try-with-resources. */
  @Override
  void close();
}
