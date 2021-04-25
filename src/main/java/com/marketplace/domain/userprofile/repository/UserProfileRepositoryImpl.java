package com.marketplace.domain.userprofile.repository;

import com.marketplace.domain.shared.UserId;
import com.marketplace.domain.userprofile.UserProfile;
import com.marketplace.domain.userprofile.entity.UserProfileEntity;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Optional;

@Named
@Singleton
public class UserProfileRepositoryImpl implements UserProfileRepository {

    @Inject
    public UserProfileRepositoryImpl() {
    }

    @Override
    public boolean exists(UserId id) {
        return load(id).isPresent();
    }

    @Override
    public Optional<UserProfile> load(UserId id) {
//        Optional<UserProfileEntity> found = mongoTemplate.findById(id.id(), collectionName, UserProfileEntity.class);
//        return found.map(UserProfileEntity::toUserProfile);
        return Optional.empty();
    }

    @Override
    public UserProfile add(UserProfile entity) {
//        var userProfileEntity = UserProfileEntity.create(entity);
//        var save = mongoTemplate.add(userProfileEntity, entity.getId().id(), collectionName, UserProfileEntity.class);
//        if (save != null) {
//            return entity;
//        }
        return null;
    }

    @Override
    public void deleteAll() {
    }
}
