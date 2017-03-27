package com.nichesoftware.giftlist.repository.datasource;

import java.util.List;

import io.reactivex.Observable;

/**
 * Interface that represents a data source from where data is retrieved.
 */
public interface DataSource<T> {
    /**
     * Get an {@link Observable} which will emit the {@link T} after it was added.
     *
     * @param element   Element {@link T} to add
     * @return          Element {@link T} that was added
     */
    Observable<T> add(T element);

    /**
     * Get an {@link Observable} which will emit a List of all {@link T}
     *
     * @return          List of {@link T}
     */
    Observable<List<T>> getAll();

    /**
     * Get an {@link Observable} which will emit the {@link T} after it was added.
     *
     * @param id        Identifier of the element {@link T} to retrieve
     * @return          Element {@link T} corresponding to the given id
     */
    Observable<T> get(final String id);
    /**
     * Get an {@link Observable} which will emit the {@link T} after it was updated.
     *
     * @param element   Element {@link T} to update
     * @return          Element {@link T} that was updated
     */
    Observable<T> update(T element);

    /**
     * Get an {@link Observable} which will emit the List of remaining {@link T} after
     * the given one was deleted.
     *
     * @param element   Element {@link T} to delete
     * @return          Element {@link T} that was deleted
     */
    Observable<List<T>> delete(T element);
}
