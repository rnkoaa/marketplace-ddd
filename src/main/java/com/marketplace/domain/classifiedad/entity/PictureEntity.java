package com.marketplace.domain.classifiedad.entity;

import com.marketplace.domain.classifiedad.Picture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PictureEntity {
    private UUID id;
    private UUID parentId;
    private int height;
    private int width;
    private String uri;
    private int order;

    public static PictureEntity from(Picture picture) {
        return PictureEntity.builder()
                .height(picture.getSize().height())
                .width(picture.getSize().width())
                .id(picture.getId().id())
                .parentId(picture.getParentId().id())
                .uri(picture.getUri())
                .order(picture.getOrder())
                .build();
    }
}
