package com.marketplace.domain.classifiedad.query;

import java.util.List;
import java.util.UUID;

public interface ClassifiedAdQueryRepository extends QueryRepository<ClassifiedAdQueryEntity, UUID> {

    /**
     * @param ownerId id of the owner of the classified ads
     * @return
     */
    List<ClassifiedAdQueryEntity> findByOwner(UUID ownerId);

    /**
     * @param approverId id of the approver of classifiedAds
     * @return
     */
    List<ClassifiedAdQueryEntity> findByApprover(UUID approverId);

    /**
     * @return
     */
    List<ClassifiedAdQueryEntity> find();
}
