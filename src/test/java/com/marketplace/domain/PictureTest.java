package com.marketplace.domain;

import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.ClassifiedAdId;
import com.marketplace.domain.classifiedad.Picture;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class PictureTest {

    @Test
    void testPictureValidationWithIncorrectSize() {
        var classifiedAd = new ClassifiedAd(new ClassifiedAdId(UUID.randomUUID()), new UserId(UUID.randomUUID()));
        assertThatThrownBy(() -> {
            classifiedAd.createPicture(new PictureSize(400, 300));
        }).isInstanceOf(InvalidStateException.class);

    }

    @Test
    void testPictureValidation() {
        var classifiedAd = new ClassifiedAd(new ClassifiedAdId(UUID.randomUUID()), new UserId(UUID.randomUUID()));
        Picture picture = classifiedAd.createPicture(new PictureSize(800, 600));
        assertThat(picture.hasCorrectSize()).isTrue();

    }
}
