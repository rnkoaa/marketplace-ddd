package com.marketplace.domain.userprofile.query;

import com.marketplace.domain.classifiedad.query.QueryRepository;
import com.marketplace.domain.userprofile.entity.UserProfileEntity;
import java.util.UUID;

public interface UserProfileQueryRepository extends QueryRepository<UserProfileEntity, UUID> {

}
