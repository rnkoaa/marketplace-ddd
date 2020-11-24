package com.marketplace.domain.classifiedad.service;

import com.marketplace.domain.classifiedad.*;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd;
import com.marketplace.domain.classifiedad.repository.ClassifiedAdCommandRepository;
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
    ClassifiedAdCommandRepository classifiedAdCommandRepository;

    ClassifiedAdService classifiedAdService;


    @BeforeEach
    void setup() {
        classifiedAdService = new ClassifiedAdService(classifiedAdCommandRepository);
    }

    @Test
    void canBeSaved() {
        var classifiedAd = new ClassifiedAd(new ClassifiedAdId(UUID.randomUUID()), new UserId(UUID.randomUUID()));

        Mockito.when(classifiedAdCommandRepository.add(classifiedAd)).thenReturn(classifiedAd);

        var saved = classifiedAdCommandRepository.add(classifiedAd);
        assertThat(saved).isNotNull().isEqualTo(classifiedAd);
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

        Mockito.when(classifiedAdCommandRepository.load(new ClassifiedAdId(UUID.fromString(uuidString)))).thenReturn(Optional.of(classifiedAd));
        Mockito.when(classifiedAdCommandRepository.add(classifiedAd)).thenReturn(expectedClassifiedAd);

        var updateClassifiedAd = new UpdateClassifiedAd();
        updateClassifiedAd.setId(UUID.fromString(uuidString));
        updateClassifiedAd.setText("test text");
        updateClassifiedAd.setTitle("test title");

        var handleResponse = classifiedAdService.handle(updateClassifiedAd);
        assertThat(handleResponse).isNotNull();
        assertThat(handleResponse.result).isNotNull();
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

        Mockito.when(classifiedAdCommandRepository.load(new ClassifiedAdId(UUID.fromString(uuidString)))).thenReturn(Optional.of(classifiedAd));
        Mockito.when(classifiedAdCommandRepository.add(classifiedAd)).thenReturn(expectedClassifiedAd);

        var updateClassifiedAd = new UpdateClassifiedAd();
        updateClassifiedAd.setId(UUID.fromString(uuidString));
        updateClassifiedAd.setText("test text");
        updateClassifiedAd.setTitle("test title");

        var handleResponse = classifiedAdService.handle(updateClassifiedAd);
        assertThat(handleResponse).isNotNull();
        assertThat(handleResponse.result).isNotNull();
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

        Mockito.when(classifiedAdCommandRepository.load(new ClassifiedAdId(UUID.fromString(uuidString)))).thenReturn(Optional.of(classifiedAd));
        Mockito.when(classifiedAdCommandRepository.add(classifiedAd)).thenReturn(expectedClassifiedAd);

        var updateClassifiedAd = new UpdateClassifiedAd();
        updateClassifiedAd.setId(UUID.fromString(uuidString));
        updateClassifiedAd.setText("test text");

        var handleResponse = classifiedAdService.handle(updateClassifiedAd);
        assertThat(handleResponse).isNotNull();
        assertThat(handleResponse.result).isNotNull();
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


        Mockito.when(classifiedAdCommandRepository.load(new ClassifiedAdId(UUID.fromString(uuidString)))).thenReturn(Optional.of(classifiedAd));

        var found = classifiedAdCommandRepository.load(new ClassifiedAdId(UUID.fromString(uuidString)));

        assertThat(found).isPresent();

        ClassifiedAd foundClassifiedAd = found.get();
        AssertionsForInterfaceTypes.assertThat(foundClassifiedAd.getChanges()).hasSize(1);
        assertThat(foundClassifiedAd.getId()).isEqualTo(classifiedAdId);
        assertThat(foundClassifiedAd.getOwnerId()).isEqualTo(ownerId);
    }
}