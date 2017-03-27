package com.nichesoftware.giftlist.repository.cache;

import java.util.List;

import io.reactivex.Observable;

/**
 * An interface representing a element in Cache.
 */
public interface Cache<T> {
    /**
     * Gets an {@link Observable} which will emit a List of all {@link T}.
     *
     * @return          List of {@link T}
     */
    Observable<List<T>> getAll();

    /**
     * Gets an {@link Observable} which will emit a {@link T}.
     *
     * @param id The id to retrieve data.
     */
    Observable<T> get(final String id);

    /**
     * Puts a {@link T} into the cache.
     *
     * @param element Element to insert in the cache.
     */
    void put(T element);

    /**
     * Puts a List of {@link T} into the cache.
     *
     * @param elements List of {@link T} to insert in the cache.
     */
    void putAll(List<T> elements);

    /**
     * Evict all {@link T} of the cache.
     */
    void evictAll();

    /**
     * Checks if a {@link T} exists in the cache.
     *
     * @param id The id used to look for inside the cache.
     * @return true if the element is cached, otherwise false.
     */
    boolean isCached(final String id);

    /**
     * Checks if the cache is expired.
     *
     * @return true, the cache is expired, otherwise false.
     */
    boolean isExpired();
}
