package com.nichesoftware.giftlist.repository.datasource;

import com.nichesoftware.giftlist.error.UserNotConnectedError;
import com.nichesoftware.giftlist.model.User;
import com.nichesoftware.giftlist.utils.StringUtils;

import java.util.List;

import io.reactivex.Observable;

/**
 * Decorator for {@link ConnectedDataSource} in order to check
 * if the {@link User} is connected
 */
public class CloudDataSourceWrapper<T> extends ConnectedDataSource<T> {

    /**
     * Concrete {@link ConnectedDataSource}
     */
    private ConnectedDataSource<T> mConcreteCloudDataSource;

    /**
     * Default constructor
     *
     * @param concreteCloudDataSource   Implementation of a {@link ConnectedDataSource}
     */
    public CloudDataSourceWrapper(ConnectedDataSource<T> concreteCloudDataSource) {
        super(concreteCloudDataSource.getToken(), concreteCloudDataSource.getService());
        mConcreteCloudDataSource = concreteCloudDataSource;
    }

    @Override
    public Observable<T> add(T element) {
        if (!StringUtils.isEmpty(mToken)) {
            return mConcreteCloudDataSource.add(element);
        } else {
            return Observable.error(new UserNotConnectedError("The user need to be connected..."));
        }
    }

    @Override
    public Observable<List<T>> getAll() {
        if (!StringUtils.isEmpty(mToken)) {
            return mConcreteCloudDataSource.getAll();
        } else {
            return Observable.error(new UserNotConnectedError("The user need to be connected..."));
        }
    }

    @Override
    public Observable<T> get(String id) {
        if (!StringUtils.isEmpty(mToken)) {
            return mConcreteCloudDataSource.get(id);
        } else {
            return Observable.error(new UserNotConnectedError("The user need to be connected..."));
        }
    }

    @Override
    public Observable<T> update(T element) {
        if (!StringUtils.isEmpty(mToken)) {
            return mConcreteCloudDataSource.update(element);
        } else {
            return Observable.error(new UserNotConnectedError("The user need to be connected..."));
        }
    }

    @Override
    public Observable<List<T>> delete(T element) {
        if (!StringUtils.isEmpty(mToken)) {
            return mConcreteCloudDataSource.delete(element);
        } else {
            return Observable.error(new UserNotConnectedError("The user need to be connected..."));
        }
    }
}
