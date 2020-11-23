package com.marketplace;

import com.marketplace.domain.PictureSize;
import com.marketplace.domain.classifiedad.*;
import com.marketplace.domain.classifiedad.command.CreateClassifiedAd;
import com.marketplace.domain.shared.UserId;
import com.marketplace.fixtures.LoadCreateAdEvent;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ClassifiedAdRepositoryTest extends BaseMongoRepositoryTest {
    String insertId = "0b8a557d-32f6-4268-80d5-6a38df8a9520";

    @Test
    void insertedItemCanBeShownToExist() throws IOException {
        CreateClassifiedAd createAdDto = LoadCreateAdEvent.loadCreateAdDto();

        assertThat(createAdDto).isNotNull();
        assertThat(createAdDto.getOwnerId()).isNotNull();
        var classifiedAd = new ClassifiedAd(ClassifiedAdId.fromString(insertId),
                new UserId(createAdDto.getOwnerId()));
        classifiedAd.updateTitle(new ClassifiedAdTitle("Snow Blower for sale"));
        classifiedAd.updateText(new ClassifiedAdText("Snow Blower for sale for Cheap"));
        classifiedAd.updatePrice(new Price(Money.fromDecimal(4.59, "USD")));
        classifiedAd.addPicture("uri", new PictureSize(800, 600), 0);
        classifiedAd.setState(ClassifiedAdState.ACTIVE);

        ClassifiedAd savedClassifiedAd = classifiedAdRepository.add(classifiedAd);

        assertThat(savedClassifiedAd.getId()).isNotNull();
        assertThat(savedClassifiedAd.getId().id()).isNotNull()
                .isEqualByComparingTo(UUID.fromString(insertId));

        Optional<ClassifiedAd> load = classifiedAdRepository.load(ClassifiedAdId.fromString(insertId));

        assertThat(load).isPresent();
        ClassifiedAd found = load.get();
        assertThat(found.getId()).isEqualTo(ClassifiedAdId.fromString(insertId));
        assertThat(found.getOwnerId()).isEqualTo(new UserId(createAdDto.getOwnerId()));
        assertThat(found.getText()).isEqualTo(new ClassifiedAdText("Snow Blower for sale for Cheap"));
        assertThat(found.getTitle()).isEqualTo(new ClassifiedAdTitle("Snow Blower for sale"));
        assertThat(found.getPrice()).isEqualTo(new Price(Money.fromDecimal(4.59, "USD")));
        assertThat(found.getPictures()).hasSize(1);
        assertThat(found.getChanges()).hasSameSizeAs(classifiedAd.getChanges());
    }
}
