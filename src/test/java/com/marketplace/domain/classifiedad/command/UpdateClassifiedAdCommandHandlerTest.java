package com.marketplace.domain.classifiedad.command;

import com.marketplace.domain.*;
import com.marketplace.domain.classifiedad.*;
import com.marketplace.domain.classifiedad.repository.ClassifiedAdRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class UpdateClassifiedAdCommandHandlerTest {
    @Mock
    ClassifiedAdRepository classifiedAdRepository;

    UpdateClassifiedAdCommandHandler commandHandler;

    @BeforeEach
    void setup() {
        commandHandler = new UpdateClassifiedAdCommandHandler(classifiedAdRepository);
    }

    @Test
    void canBeSaved() {
        var classifiedAd = new ClassifiedAd(new ClassifiedAdId(UUID.randomUUID()), new UserId(UUID.randomUUID()));

        Mockito.when(classifiedAdRepository.add(classifiedAd)).thenReturn(classifiedAd);

        var saved = classifiedAdRepository.add(classifiedAd);
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

        Mockito.when(classifiedAdRepository.load(new ClassifiedAdId(UUID.fromString(uuidString)))).thenReturn(Optional.of(classifiedAd));
        Mockito.when(classifiedAdRepository.add(classifiedAd)).thenReturn(expectedClassifiedAd);

        var updateClassifiedAd = new UpdateClassifiedAd();
        updateClassifiedAd.setId(UUID.fromString(uuidString));
        updateClassifiedAd.setText("test text");
        updateClassifiedAd.setTitle("test title");

        var handleResponse = commandHandler.handle(updateClassifiedAd);
        assertThat(handleResponse).isNotNull();
        assertThat(handleResponse.result).isNotNull();
        assertThat(handleResponse.isSuccessful()).isTrue();
        assertThat(expectedClassifiedAd.getChanges()).hasSize(3);
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

        Mockito.when(classifiedAdRepository.load(new ClassifiedAdId(UUID.fromString(uuidString)))).thenReturn(Optional.of(classifiedAd));
        Mockito.when(classifiedAdRepository.add(classifiedAd)).thenReturn(expectedClassifiedAd);

        var updateClassifiedAd = new UpdateClassifiedAd();
        updateClassifiedAd.setId(UUID.fromString(uuidString));
        updateClassifiedAd.setText("test text");
        updateClassifiedAd.setTitle("test title");

        var handleResponse = commandHandler.handle(updateClassifiedAd);
        assertThat(handleResponse).isNotNull();
        assertThat(handleResponse.result).isNotNull();
        assertThat(handleResponse.isSuccessful()).isTrue();
        assertThat(expectedClassifiedAd.getChanges()).hasSize(4);
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

        Mockito.when(classifiedAdRepository.load(new ClassifiedAdId(UUID.fromString(uuidString)))).thenReturn(Optional.of(classifiedAd));
        Mockito.when(classifiedAdRepository.add(classifiedAd)).thenReturn(expectedClassifiedAd);

        var updateClassifiedAd = new UpdateClassifiedAd();
        updateClassifiedAd.setId(UUID.fromString(uuidString));
        updateClassifiedAd.setText("test text");

        var handleResponse = commandHandler.handle(updateClassifiedAd);
        assertThat(handleResponse).isNotNull();
        assertThat(handleResponse.result).isNotNull();
        assertThat(handleResponse.isSuccessful()).isTrue();
        assertThat(expectedClassifiedAd.getChanges()).hasSize(2);
    }

    @Test
    void canBeLoadedGivenId() {
        var uuidString = "55c66481-da62-4d3c-b3df-8c6f2695e9c2";
        var userIdString = "e6ef417b-cedb-488d-8af9-6986a5fa7d51";
        var ownerId = new UserId(UUID.fromString(userIdString));
        var classifiedAdId = new ClassifiedAdId(UUID.fromString(uuidString));
        var classifiedAd = new ClassifiedAd(classifiedAdId, ownerId);


        Mockito.when(classifiedAdRepository.load(new ClassifiedAdId(UUID.fromString(uuidString)))).thenReturn(Optional.of(classifiedAd));

        var found = classifiedAdRepository.load(new ClassifiedAdId(UUID.fromString(uuidString)));

        assertThat(found).isPresent();

        ClassifiedAd foundClassifiedAd = found.get();
        assertThat(foundClassifiedAd.getChanges()).hasSize(1);
        assertThat(foundClassifiedAd.getId()).isEqualTo(classifiedAdId);
        assertThat(foundClassifiedAd.getOwnerId()).isEqualTo(ownerId);
    }
}