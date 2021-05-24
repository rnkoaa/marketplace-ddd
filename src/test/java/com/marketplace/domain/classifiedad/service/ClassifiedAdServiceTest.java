package com.marketplace.domain.classifiedad.service;

import com.marketplace.domain.AggregateStoreRepository;
import com.marketplace.domain.classifiedad.*;
import com.marketplace.domain.classifiedad.command.ImmutableUpdateClassifiedAd;
import com.marketplace.domain.shared.UserId;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class ClassifiedAdServiceTest {

    @Mock
    AggregateStoreRepository aggregateStoreRepository;

    ClassifiedAdService classifiedAdService;

    @BeforeEach
    void setup() {
        classifiedAdService = new ClassifiedAdService(aggregateStoreRepository);
    }

    @Test
    void canBeSaved() {
        var classifiedAd = new ClassifiedAd(new ClassifiedAdId(UUID.randomUUID()), new UserId(UUID.randomUUID()));

        Mockito.when(aggregateStoreRepository.add(classifiedAd)).thenReturn(Optional.of(classifiedAd));

        var saved = aggregateStoreRepository.add(classifiedAd);
        assertThat(saved).isNotNull().isEqualTo(Optional.of(classifiedAd));
    }

    @Test
    void existingClassifiedAdTitleTextCanBeUpdated() {
        var uuidString = "55c66481-da62-4d3c-b3df-8c6f2695e9c2";
        var userIdString = "e6ef417b-cedb-488d-8af9-6986a5fa7d51";
        var ownerId = new UserId(UUID.fromString(userIdString));
        var classifiedAdId = new ClassifiedAdId(UUID.fromString(uuidString));
        var classifiedAd = new ClassifiedAd(classifiedAdId, ownerId);

        var expectedClassifiedAd = new ClassifiedAd(classifiedAdId, ownerId);
        expectedClassifiedAd.updateText(new ClassifiedAdText("test text"));
        expectedClassifiedAd.updateTitle(new ClassifiedAdTitle("test title"));

        Mockito.when(aggregateStoreRepository.load(new ClassifiedAdId(UUID.fromString(uuidString))))
            .thenReturn(Optional.of(classifiedAd));
        Mockito.when(aggregateStoreRepository.add(classifiedAd)).thenReturn(Optional.of(expectedClassifiedAd));

        var updateClassifiedAd = ImmutableUpdateClassifiedAd.builder()
            .classifiedAdId(UUID.fromString(uuidString))
            .text("test text")
            .title("test title")
            .build();

        var handleResponse = classifiedAdService.handle(updateClassifiedAd);
        assertThat(handleResponse).isNotNull();
        assertThat(handleResponse.getResult()).isPresent();
        assertThat(handleResponse.isSuccessful()).isTrue();
        AssertionsForInterfaceTypes.assertThat(expectedClassifiedAd.getChanges()).hasSize(3);
    }

    @Test
    void existingClassifiedAdTitleTextAndPriceCanBeUpdated() {
        var uuidString = "55c66481-da62-4d3c-b3df-8c6f2695e9c2";
        var userIdString = "e6ef417b-cedb-488d-8af9-6986a5fa7d51";
        var ownerId = new UserId(UUID.fromString(userIdString));
        var classifiedAdId = new ClassifiedAdId(UUID.fromString(uuidString));
        var classifiedAd = new ClassifiedAd(classifiedAdId, ownerId);

        var expectedClassifiedAd = new ClassifiedAd(classifiedAdId, ownerId);
        expectedClassifiedAd.updateText(new ClassifiedAdText("test text"));
        expectedClassifiedAd.updateTitle(new ClassifiedAdTitle("test title"));
        expectedClassifiedAd.updatePrice(new Price(Money.fromDecimal(10.0, "USD")));

        Mockito.when(aggregateStoreRepository.load(new ClassifiedAdId(UUID.fromString(uuidString))))
            .thenReturn(Optional.of(classifiedAd));
        Mockito.when(aggregateStoreRepository.add(classifiedAd)).thenReturn(Optional.of(expectedClassifiedAd));

        var updateClassifiedAd = ImmutableUpdateClassifiedAd.builder()
            .classifiedAdId(UUID.fromString(uuidString))
            .text("test text")
            .title("test title")
            .build();

        var handleResponse = classifiedAdService.handle(updateClassifiedAd);
        assertThat(handleResponse).isNotNull();
        assertThat(handleResponse.getResult()).isPresent();
        assertThat(handleResponse.isSuccessful()).isTrue();
        AssertionsForInterfaceTypes.assertThat(expectedClassifiedAd.getChanges()).hasSize(4);
    }

    @Test
    void existingClassifiedAdTextCanBeUpdated() {
        var uuidString = "55c66481-da62-4d3c-b3df-8c6f2695e9c2";
        var userIdString = "e6ef417b-cedb-488d-8af9-6986a5fa7d51";
        var ownerId = new UserId(UUID.fromString(userIdString));
        var classifiedAdId = new ClassifiedAdId(UUID.fromString(uuidString));
        var classifiedAd = new ClassifiedAd(classifiedAdId, ownerId);

        var expectedClassifiedAd = new ClassifiedAd(classifiedAdId, ownerId);
        expectedClassifiedAd.updateText(new ClassifiedAdText("test text"));

        Mockito.when(aggregateStoreRepository.load(new ClassifiedAdId(UUID.fromString(uuidString))))
            .thenReturn(Optional.of(classifiedAd));
        Mockito.when(aggregateStoreRepository.add(classifiedAd)).thenReturn(Optional.of(expectedClassifiedAd));

        var updateClassifiedAd = ImmutableUpdateClassifiedAd.builder()
            .classifiedAdId(UUID.fromString(uuidString))
            .text("test text")
            .title("test title")
            .build();

        var handleResponse = classifiedAdService.handle(updateClassifiedAd);
        assertThat(handleResponse).isNotNull();
        assertThat(handleResponse.getResult()).isPresent();
        assertThat(handleResponse.isSuccessful()).isTrue();
        AssertionsForInterfaceTypes.assertThat(expectedClassifiedAd.getChanges()).hasSize(2);
    }

    @Test
    void canBeLoadedGivenId() {
        var uuidString = "55c66481-da62-4d3c-b3df-8c6f2695e9c2";
        var userIdString = "e6ef417b-cedb-488d-8af9-6986a5fa7d51";
        var ownerId = new UserId(UUID.fromString(userIdString));
        var classifiedAdId = new ClassifiedAdId(UUID.fromString(uuidString));
        var classifiedAd = new ClassifiedAd(classifiedAdId, ownerId);

        Mockito.when(aggregateStoreRepository.load(new ClassifiedAdId(UUID.fromString(uuidString))))
            .thenReturn(Optional.of(classifiedAd));

        var found = aggregateStoreRepository.load(new ClassifiedAdId(UUID.fromString(uuidString)))
            .map(it -> (ClassifiedAd) it);

        assertThat(found).isPresent();

        ClassifiedAd foundClassifiedAd = found.get();
        AssertionsForInterfaceTypes.assertThat(foundClassifiedAd.getChanges()).hasSize(1);
        assertThat(foundClassifiedAd.getId()).isEqualTo(classifiedAdId);
        assertThat(foundClassifiedAd.getOwnerId()).isEqualTo(ownerId);
    }
}