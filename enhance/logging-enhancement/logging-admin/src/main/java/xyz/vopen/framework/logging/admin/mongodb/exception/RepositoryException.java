package xyz.vopen.framework.logging.admin.mongodb.exception;

/**
 * repository exception
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/5/14
 */
public class RepositoryException extends RuntimeException {

  public RepositoryException(String message) {
    super(message);
  }

  public RepositoryException(String message, Throwable cause) {
    super(message, cause);
  }
}
