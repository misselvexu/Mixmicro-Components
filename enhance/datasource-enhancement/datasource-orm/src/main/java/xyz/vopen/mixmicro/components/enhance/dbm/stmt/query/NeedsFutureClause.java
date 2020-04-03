package xyz.vopen.mixmicro.components.enhance.dbm.stmt.query;

/**
 * Internal interface which defines a clause that consumes a future clause. This allows us to do:
 *
 * <pre>
 * where.not();
 * where.eq(&quot;id&quot;, 1234);
 * </pre>
 *
 * <p>and
 *
 * <pre>
 * where.eq(&quot;id&quot;, 1234);
 * where.and();
 * where.gt(&quot;age&quot;, 44);
 * </pre>
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface NeedsFutureClause extends Clause {

  /** Set the right side of the binary operation. */
  public void setMissingClause(Clause right);
}
