package com.marketplace.domain;

import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.ClassifiedAdId;
import com.marketplace.domain.classifiedad.Picture;
import com.marketplace.domain.shared.UserId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class PictureTest {

    @Test
    void testPictureValidationWithIncorrectSize() {
        var classifiedAd = new ClassifiedAd(new ClassifiedAdId(UUID.randomUUID()), new UserId(UUID.randomUUID()));
        Assertions.assertThrows(InvalidStateException.class, () -> {
            classifiedAd.createPicture(new PictureSize(200, 300), "", 0);
        });
    }

    @Test
    void testPictureValidation() {
        var classifiedAd = new ClassifiedAd(new ClassifiedAdId(UUID.randomUUID()), new UserId(UUID.randomUUID()));
        Picture picture = classifiedAd.createPicture(new PictureSize(800, 600), "", 0);
        assertThat(picture.hasCorrectSize()).isTrue();
    }
}
