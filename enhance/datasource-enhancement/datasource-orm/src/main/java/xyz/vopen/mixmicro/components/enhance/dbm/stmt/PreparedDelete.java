package xyz.vopen.mixmicro.components.enhance.dbm.stmt;

import xyz.vopen.mixmicro.components.enhance.dbm.repository.Repository;

/**
 * Interface returned by the {@link DeleteBuilder#prepare()} which supports custom DELETE
 * statements. This should be in turn passed to the {@link Repository#delete(PreparedDelete)} method.
 *
 * @param <T> The class that the code will be operating on.
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface PreparedDelete<T> extends PreparedStmt<T> {}
