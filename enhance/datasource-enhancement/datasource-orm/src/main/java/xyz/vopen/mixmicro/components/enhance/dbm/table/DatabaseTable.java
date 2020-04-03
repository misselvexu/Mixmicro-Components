package xyz.vopen.mixmicro.components.enhance.dbm.table;

import xyz.vopen.mixmicro.components.enhance.dbm.repository.RepositoryManager;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation that marks a class to be stored in the database. It is only required if you want to
 * mark the class or change its default tableName. You specify this annotation above the classes
 * that you want to persist to the database. For example:
 *
 * <pre>
 * &#64;DatabaseTable(tableName = "accounts")
 * public class Account {
 *   ...
 * </pre>
 *
 * <p><b>NOTE:</b> Classes that are persisted using this package <i>must</i> have a no-argument
 * constructor with at least package visibility so objects can be created when you do a query, etc..
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface DatabaseTable {

  /**
   * The name of the column in the database. If not set then the name is taken from the class name
   * lowercased.
   */
  String tableName() default "";

  /**
   * The Repository class that corresponds to this class. This is used by the {@link RepositoryManager} when it
   * constructs a Repository internally.
   */
  Class<?> repositoryClass() default Void.class;
}
