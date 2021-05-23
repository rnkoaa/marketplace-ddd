package com.marketplace;

import com.marketplace.domain.PictureSize;
import com.marketplace.domain.classifiedad.*;
import com.marketplace.domain.classifiedad.command.CreateClassifiedAd;
import com.marketplace.domain.shared.UserId;
import com.marketplace.fixtures.LoadCreateAdEvent;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ClassifiedAdCommandRepositoryTest extends BaseRepositoryTest {

    String insertId = "87a7fef0-1527-4a47-b196-504d9f9ce0fe";

    @Test
    void insertedItemCanBeShownToExist() throws IOException {
        CreateClassifiedAd createAdDto = LoadCreateAdEvent.loadCreateAdDto();

        assertThat(createAdDto).isNotNull();
        assertThat(createAdDto.getOwnerId()).isNotNull();
        var classifiedAd = new ClassifiedAd(createAdDto);
        classifiedAd.updateTitle(new ClassifiedAdTitle("Snow Blower for sale"));
        classifiedAd.updateText(new ClassifiedAdText("Snow Blower for sale for Cheap"));
        classifiedAd.updatePrice(new Price(Money.fromDecimal(4.59, "USD")));
        classifiedAd.addPicture("uri", new PictureSize(800, 600), 0);
        classifiedAd.setState(ClassifiedAdState.ACTIVE);

        Optional<ClassifiedAd> savedClassifiedAd = aggregateStoreRepository.add(classifiedAd)
            .map(it -> (ClassifiedAd) it);

        assertThat(savedClassifiedAd).isPresent();

        var actual = savedClassifiedAd.get();

        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getId().id()).isNotNull()
            .isEqualByComparingTo(UUID.fromString(insertId));

        Optional<ClassifiedAd> load = aggregateStoreRepository.load(ClassifiedAdId.fromString(insertId))
            .map(it -> (ClassifiedAd) it);

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
