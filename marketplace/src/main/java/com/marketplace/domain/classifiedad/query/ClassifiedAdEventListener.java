package com.marketplace.domain.classifiedad.query;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ClassifiedAdEventListener {

//    private static final Map<UUID, ClassifiedAdEntity> classifiedAds = new HashMap<>();
//
//    @Subscribe
//    public void on(ClassifiedAdCreatedEvent event) {
//        Builder builder = ImmutableClassifiedAdEntity.builder()
//            .id(event.aggregateId().value())
//            .owner(event.owner().value())
//            .state(ClassifiedAdState.INACTIVE);
//
//        builder.build();
//        classifiedAds.put(event.aggregateId().value(), builder.build());
//    }
//
//    @Subscribe
//    public void on(TitleUpdatedEvent event) {
//        findById(event.aggregateId().value())
//            .map(c -> {
//                var classifiedAdEntity = ImmutableClassifiedAdEntity.copyOf(c)
//                    .withTitle(event.title().value());
//                classifiedAds.put(event.aggregateId().value(), classifiedAdEntity);
//                return classifiedAdEntity;
//            })
//            .orElseThrow(() -> new NotFoundException(" classified ad not found"));
//    }
//
//    @Subscribe
//    public void on(PriceUpdatedEvent event) {
//        findById(event.aggregateId().value())
//            .map(c -> {
//                var classifiedAdEntity = ImmutableClassifiedAdEntity.copyOf(c)
//                    .withPrice(event.price());
//                classifiedAds.put(event.aggregateId().value(), classifiedAdEntity);
//                return classifiedAdEntity;
//            })
//            .orElseThrow(() -> new NotFoundException(" classified ad not found"));
//    }
//
//    @Subscribe
//    public void on(ApprovedEvent event) {
//        findById(event.aggregateId().value())
//            .map(c -> {
//                var classifiedAdEntity = ImmutableClassifiedAdEntity.copyOf(c)
//                    .withApprover(event.approver().value())
//                    .withState(ClassifiedAdState.APPROVED);
//                classifiedAds.put(event.aggregateId().value(), classifiedAdEntity);
//                return classifiedAdEntity;
//            })
//            .orElseThrow(() -> new NotFoundException(" classified ad not found"));
//    }
//
//    @Subscribe
//    public void on(TextUpdatedEvent event) {
//        findById(event.aggregateId().value())
//            .map(c -> {
//                var classifiedAdEntity = ImmutableClassifiedAdEntity.copyOf(c)
//                    .withText(event.text().value());
//                classifiedAds.put(event.aggregateId().value(), classifiedAdEntity);
//                return classifiedAdEntity;
//            })
//            .orElseThrow(() -> new NotFoundException(" classified ad not found"));
//    }
//
//    @Subscribe
//    public void on(ClassifiedAdPublishedEvent event) {
//        findById(event.aggregateId().value())
//            .map(c -> {
//                var classifiedAdEntity = ImmutableClassifiedAdEntity.copyOf(c)
//                    .withState(ClassifiedAdState.PENDING_REVIEW);
//                classifiedAds.put(event.aggregateId().value(), classifiedAdEntity);
//                return classifiedAdEntity;
//            })
//            .orElseThrow(() -> new NotFoundException(" classified ad not found"));
//    }
//
//    @Subscribe
//    public void on(ClassifiedAdSoldEvent event) {
//        findById(event.aggregateId().value())
//            .map(c -> {
//                var classifiedAdEntity = ImmutableClassifiedAdEntity.copyOf(c)
//                    .withState(ClassifiedAdState.MARKED_AS_SOLD);
//                classifiedAds.put(event.aggregateId().value(), classifiedAdEntity);
//                return classifiedAdEntity;
//            })
//            .orElseThrow(() -> new NotFoundException(" classified ad not found"));
//    }
//
//    @Subscribe
//    public void on(RejectedEvent event) {
//        findById(event.aggregateId().value())
//            .map(c -> {
//                var classifiedAdEntity = ImmutableClassifiedAdEntity.copyOf(c)
//                    .withState(ClassifiedAdState.REJECTED)
//                    .withApprover(event.approver().value())
//                    .withRejectionMessage(event.rejectedMessage());
//                classifiedAds.put(event.aggregateId().value(), classifiedAdEntity);
//                return classifiedAdEntity;
//            })
//            .orElseThrow(() -> new NotFoundException(" classified ad not found"));
//    }
//
//    public Optional<ClassifiedAdEntity> findById(UUID id) {
//        return Optional.ofNullable(classifiedAds.get(id));
//    }

}
