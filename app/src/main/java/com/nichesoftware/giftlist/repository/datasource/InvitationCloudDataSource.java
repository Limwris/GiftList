package com.nichesoftware.giftlist.repository.datasource;

import com.nichesoftware.giftlist.model.Invitation;
import com.nichesoftware.giftlist.model.Room;
import com.nichesoftware.giftlist.repository.cache.Cache;
import com.nichesoftware.giftlist.service.Service;
import com.nichesoftware.giftlist.utils.StringUtils;

import java.util.List;

import io.reactivex.Observable;

/**
 * {@link CloudDataSource} implementation for {@link Invitation}
 */
public class InvitationCloudDataSource extends CloudDataSource<Invitation> {

    /**
     * Public constructor
     *
     * @param service   The bound {@link Service}
     */
    public InvitationCloudDataSource(Service service) {
        super(service);
    }

    @Override
    public Observable<Invitation> add(Invitation element) {
        return mService.inviteUserToRoom(element.getRoom().getId(), element);
    }

    @Override
    public Observable<List<Invitation>> getAll() {
        return mService.getPendingInvitation();
    }

    @Override
    public Observable<Invitation> get(String id) {
        throw new Error("Not implemented yet");
    }

    @Override
    public Observable<Invitation> update(Invitation element) {
        return mService.acceptInvitationToRoom(element.getRoom().getId(), element);
    }

    @Override
    public Observable<List<Invitation>> delete(Invitation element) {
        if (element.getRoom() == null || StringUtils.isEmpty(element.getRoom().getId())) {
            throw new IllegalStateException("Room should not be empty...");
        }
        return mService.declineInvitation(element.getRoom().getId(), element);
    }
}
