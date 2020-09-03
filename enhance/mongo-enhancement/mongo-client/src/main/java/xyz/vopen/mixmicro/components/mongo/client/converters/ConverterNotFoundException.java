package xyz.vopen.mixmicro.components.mongo.client.converters;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public class ConverterNotFoundException extends RuntimeException {

  /**
   * Creates the ConverterNotFoundException.
   *
   * @param msg the exception message
   */
  public ConverterNotFoundException(final String msg) {
    super(msg);
  }
}
