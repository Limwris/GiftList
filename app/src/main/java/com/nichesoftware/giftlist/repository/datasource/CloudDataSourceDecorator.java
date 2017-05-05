package com.nichesoftware.giftlist.repository.datasource;

import com.nichesoftware.giftlist.BaseApplication;
import com.nichesoftware.giftlist.utils.NetworkUtils;

import java.net.ConnectException;
import java.util.List;

import io.reactivex.Observable;

/**
 * Decorator for {@link CloudDataSource} used to handle connectivity check
 */
public class CloudDataSourceDecorator<T> extends CloudDataSource<T> {

    /**
     * Concrete {@link CloudDataSource}
     */
    private CloudDataSource<T> mConcreteCloudDataSource;

    /**
     * Default constructor
     *
     * @param concreteCloudDataSource   Implementation of a {@link CloudDataSource}
     */
    public CloudDataSourceDecorator(CloudDataSource<T> concreteCloudDataSource) {
        super(concreteCloudDataSource.getService());
        mConcreteCloudDataSource = concreteCloudDataSource;
    }

    @Override
    public Observable<T> add(T element) {
        if (NetworkUtils.isConnectionAvailable(BaseApplication.getAppContext())) {
            return mConcreteCloudDataSource.add(element);
        } else {
            return Observable.error(new ConnectException("No network connection"));
        }
    }

    @Override
    public Observable<List<T>> getAll() {
        if (NetworkUtils.isConnectionAvailable(BaseApplication.getAppContext())) {
            return mConcreteCloudDataSource.getAll();
        } else {
            return Observable.error(new ConnectException("No network connection"));
        }
    }

    @Override
    public Observable<T> get(String id) {
        if (NetworkUtils.isConnectionAvailable(BaseApplication.getAppContext())) {
            return mConcreteCloudDataSource.get(id);
        } else {
            return Observable.error(new ConnectException("No network connection"));
        }
    }

    @Override
    public Observable<T> update(T element) {
        if (NetworkUtils.isConnectionAvailable(BaseApplication.getAppContext())) {
            return mConcreteCloudDataSource.update(element);
        } else {
            return Observable.error(new ConnectException("No network connection"));
        }
    }

    @Override
    public Observable<List<T>> delete(T element) {
        if (NetworkUtils.isConnectionAvailable(BaseApplication.getAppContext())) {
            return mConcreteCloudDataSource.delete(element);
        } else {
            return Observable.error(new ConnectException("No network connection"));
        }
    }
}
