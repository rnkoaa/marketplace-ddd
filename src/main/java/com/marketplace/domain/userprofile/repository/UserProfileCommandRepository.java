package com.marketplace.domain.userprofile.repository;

import com.marketplace.domain.repository.Repository;
import com.marketplace.domain.shared.UserId;
import com.marketplace.domain.userprofile.UserProfile;

public interface UserProfileCommandRepository extends Repository<UserProfile, UserId> {

}
