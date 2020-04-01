package xyz.vopen.mixmicro.components.enhance.dbm.stmt;

import xyz.vopen.mixmicro.components.enhance.dbm.repository.Repository;

/**
 * Interface returned by the {@link QueryBuilder#prepare()} which supports custom SELECT queries.
 * This should be in turn passed to the {@link Repository#query(PreparedQuery)} or {@link
 * Repository#iterator(PreparedQuery)} methods.
 *
 * @param <T> The class that the code will be operating on.
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface PreparedQuery<T> extends PreparedStmt<T> {}
