package com.nichesoftware.giftlist.repository.provider;

import com.nichesoftware.giftlist.repository.cache.Cache;
import com.nichesoftware.giftlist.repository.datasource.CloudDataSourceWrapper;
import com.nichesoftware.giftlist.repository.datasource.ConnectedDataSource;
import com.nichesoftware.giftlist.repository.datasource.DataSource;

/**
 * Factory that creates different implementations of {@link DataSource} for {@link ConnectedDataSource}.
 */
public class ConnectedDataSourceProvider<T> extends DataSourceProvider<T> {
    /**
     * Default constructor
     *
     * @param cache           {@link Cache}
     * @param cloudDataSource {@link ConnectedDataSource}
     */
    public ConnectedDataSourceProvider(Cache<T> cache, ConnectedDataSource<T> cloudDataSource) {
        super(cache, new CloudDataSourceWrapper<>(cloudDataSource));
    }
}
