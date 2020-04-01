package xyz.vopen.mixmicro.components.enhance.dbm.stmt;

import xyz.vopen.mixmicro.components.enhance.dbm.repository.Repository;

/**
 * Interface returned by the {@link UpdateBuilder#prepare()} which supports custom UPDATE
 * statements. This should be in turn passed to the {@link Repository#update(PreparedUpdate)} method.
 *
 * @param <T> The class that the code will be operating on.
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface PreparedUpdate<T> extends PreparedStmt<T> {}
