package com.marketplace.domain.classifiedad.query;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.common.ObjectMapperBuilder;
import com.marketplace.domain.classifiedad.ClassifiedAdState;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd.PictureDto;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd.PriceDto;
import com.marketplace.domain.classifiedad.query.ImmutableClassifiedAdQueryEntity.Builder;
import com.marketplace.eventstore.jdbc.tables.records.ClassifiedAdRecord;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ClassifiedAdQueryEntityMapper {

    private static final ObjectMapper objectMapper = new ObjectMapperBuilder().build();

    public static ClassifiedAdRecord convert(ClassifiedAdQueryEntity entity) {
        ClassifiedAdRecord classifiedAdRecord = new ClassifiedAdRecord()
            .setOwner(entity.getOwnerId().toString())
            .setId(entity.getId().toString())
            .setStatus(entity.getState().name());

        entity.getApprover().ifPresent(approver -> classifiedAdRecord.setApprover(approver.toString()));
        entity.getText().ifPresent(classifiedAdRecord::setText);
        entity.getTitle().ifPresent(classifiedAdRecord::setTitle);
        if (entity.getPictures() != null) {
            serialize(entity.getPictures())
                .ifPresent(classifiedAdRecord::setPictures);
        }
        entity.getPrice()
            .flatMap(priceDto -> serialize(entity.getPrice()))
            .ifPresent(classifiedAdRecord::setPictures);
        return classifiedAdRecord;
    }

    public static ClassifiedAdQueryEntity convert(ClassifiedAdRecord classifiedAdRecord) {
        Builder builder = ImmutableClassifiedAdQueryEntity.builder()
            .id(UUID.fromString(classifiedAdRecord.getId()))
            .ownerId(UUID.fromString(classifiedAdRecord.getOwner()))
            .state(ClassifiedAdState.fromString(classifiedAdRecord.getStatus()));

        if (!isNullOrEmpty(classifiedAdRecord.getApprover())) {
            builder.approver(UUID.fromString(classifiedAdRecord.getApprover()));
        }
        if (!isNullOrEmpty(classifiedAdRecord.getTitle())) {
            builder.title(classifiedAdRecord.getTitle());
        }
        if (!isNullOrEmpty(classifiedAdRecord.getText())) {
            builder.text(classifiedAdRecord.getText());
        }
        if (!isNullOrEmpty(classifiedAdRecord.getPictures())) {
            builder.pictures(deserializePictures(classifiedAdRecord.getPictures()));
        }
        if (!isNullOrEmpty(classifiedAdRecord.getPrice())) {
            builder.price(deserializePrice(classifiedAdRecord.getPrice()));
        }

        return builder.build();
    }

    private static Optional<PriceDto> deserializePrice(String value) {
        try {
            return Optional.ofNullable(objectMapper.readValue(value, PriceDto.class));
        } catch (JsonProcessingException ignored) {
            //TODO handle
            return Optional.empty();
        }
    }

    private static List<PictureDto> deserializePictures(String value) {
        List<PictureDto> res = List.of();
        try {
            res = Arrays.asList(objectMapper.readValue(value, PictureDto[].class));
        } catch (JsonProcessingException ignored) {
            //TODO handle
        }
        return res;
    }

    private static Optional<String> serialize(Object pictures) {
        try {
            return Optional.ofNullable(objectMapper.writeValueAsString(pictures));
        } catch (JsonProcessingException ignored) {
            return Optional.empty();
        }
    }

    static boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }


}
