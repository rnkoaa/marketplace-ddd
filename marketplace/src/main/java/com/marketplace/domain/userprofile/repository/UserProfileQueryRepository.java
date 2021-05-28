package com.marketplace.domain.userprofile.repository;

import com.marketplace.domain.classifiedad.query.QueryRepository;
import com.marketplace.domain.userprofile.UserProfile;
import com.marketplace.domain.userprofile.entity.UserProfileEntity;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileQueryRepository extends QueryRepository<UserProfileEntity, UUID> {

    Optional<UserProfileEntity> findByDisplayName(String displayName);
}
