package com.marketplace.domain.userprofile;

import com.marketplace.domain.shared.UserId;
import com.marketplace.domain.userprofile.event.ProfilePhotoUploaded;
import com.marketplace.domain.userprofile.event.UserDisplayNameUpdated;
import com.marketplace.domain.userprofile.event.UserFullNameUpdated;
import com.marketplace.domain.userprofile.event.UserRegistered;
import com.marketplace.event.Event;
import com.marketplace.event.EventId;
import com.marketplace.framework.AggregateRoot;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class UserProfile extends AggregateRoot<EventId, Event> {
    private UserId id;
    private FullName fullName;
    private DisplayName displayName;
    private String photoUrl;

    public UserProfile(UserId id, FullName fullName, DisplayName displayName) {
        apply(new UserRegistered(id.id(),
                fullName.firstName(),
                fullName.middleName(),
                fullName.lastName(),
                displayName.value()));
    }

    public void updateUserFullName(FullName fullName){
        apply(new UserFullNameUpdated(id.id(), fullName.firstName(), fullName.middleName(), fullName.lastName()));
    }

    public void updateDisplayName(DisplayName displayName){
        apply(new UserDisplayNameUpdated(id.id(), displayName.value()));
    }

    public void updatePhoto(String uri){
        apply(new ProfilePhotoUploaded(id.id(), uri));
    }

    @Override
    public void ensureValidState(Event event) {
//        boolean valid = id != null && displayName != null && fullName != null;
//        if (!valid) {
//            throw new IllegalArgumentException("state is not valid while processing event " + event.getClass());
//        }
    }

    @Override
    public void when(Event event) {
        if (event instanceof UserRegistered e) {
            this.id = new UserId(e.getId());
            this.displayName = new DisplayName(e.getDisplayName());
            this.fullName = new FullName(e.getFirstName(), e.getMiddleName(), e.getLastName());
        } else if (event instanceof ProfilePhotoUploaded e) {
            this.photoUrl = e.getPhotoUrl();
        } else if (event instanceof UserFullNameUpdated e) {
            this.fullName = new FullName(e.getFirstName(), e.getMiddleName(), e.getLastName());
        } else if (event instanceof UserDisplayNameUpdated e) {
            this.displayName = new DisplayName(e.getDisplayName());
        }
    }
}
