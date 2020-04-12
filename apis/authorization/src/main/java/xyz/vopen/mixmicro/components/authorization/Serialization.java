package xyz.vopen.mixmicro.components.authorization.kit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static xyz.vopen.mixmicro.components.authorization.Constants.AUTHORIZATION_LOG_NAME;

/**
 * {@link Serialization}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020-04-10.
 */
public final class Serialization {

  private static final Logger log = LoggerFactory.getLogger(AUTHORIZATION_LOG_NAME);

  // DEFAULT CONSTRUCTOR

  private Serialization() {}

  private static Gson gson;

  static {
    GsonBuilder builder = new GsonBuilder();



    builder.create();
  }
}
