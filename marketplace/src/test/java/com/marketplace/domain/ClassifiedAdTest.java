package com.marketplace.domain;

import com.marketplace.domain.classifiedad.*;
import com.marketplace.domain.classifiedad.events.ImmutableClassifiedAdSoldEvent;
import com.marketplace.domain.shared.UserId;
import com.marketplace.fixtures.FakeCurrencyLookup;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.marketplace.domain.classifiedad.ClassifiedAdState.INACTIVE;
import static com.marketplace.domain.classifiedad.ClassifiedAdState.MARKED_AS_SOLD;
import static com.marketplace.domain.classifiedad.ClassifiedAdState.REJECTED;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClassifiedAdTest {

    final UUID id = UUID.fromString("53a94b25-8139-4f15-b533-8fcd328c0edd");
    final UUID ownerId = UUID.fromString("c7185cc3-a8db-4030-ace2-59dd773d86fc");
    final UUID approverId = UUID.fromString("3203de96-599f-421a-a9c1-8b365d21cf37");
    private ClassifiedAd classifiedAd;

    @BeforeEach
    public void setup() {
        var classifiedAdId = new ClassifiedAdId(id);
        var classifiedOwnerId = new UserId(ownerId);
        classifiedAd = new ClassifiedAd(classifiedAdId, classifiedOwnerId);
    }

    @Test
    void testCreatedClassified() {
        assertThat(classifiedAd.getId()).isEqualTo(new ClassifiedAdId(id));
        assertThat(classifiedAd.getOwnerId()).isEqualTo(new UserId(ownerId));
        assertThat(classifiedAd.getChanges().size()).isEqualTo(1);
    }

    @Test
    void can_publish_a_valid_ad() {
        classifiedAd.updateTitle(new ClassifiedAdTitle("Test ad"));
        classifiedAd.updateText(new ClassifiedAdText("Please buy my stuff"));
        classifiedAd.updatePrice(new Price(Money.fromDecimal(100, "EUR", new FakeCurrencyLookup())));

        classifiedAd.requestToPublish();
        AssertionsForClassTypes.assertThat(classifiedAd.getState()).isEqualTo(ClassifiedAdState.PENDING_REVIEW);

        assertThat(classifiedAd.getChanges().size()).isEqualTo(5);
    }

    @Test
    void cannot_publish_without_price() {
        assertThatThrownBy(() -> {
            classifiedAd.updateTitle(new ClassifiedAdTitle("Test ad"));
            classifiedAd.updateText(new ClassifiedAdText("Please buy my stuff"));

            classifiedAd.requestToPublish();
        }).isInstanceOf(InvalidStateException.class);
    }

    @Test
    void cannot_publish_without_text() {
        assertThatThrownBy(() -> {
            classifiedAd.updateTitle(new ClassifiedAdTitle("Test ad"));
            classifiedAd.updatePrice(new Price(Money.fromDecimal(100, "EUR", new FakeCurrencyLookup())));

            classifiedAd.requestToPublish();
        }).isInstanceOf(InvalidStateException.class);
    }

    @Test
    void cannot_publish_without_title() {
        assertThatThrownBy(() -> {
            classifiedAd.updateText(new ClassifiedAdText("Please buy my stuff"));
            classifiedAd.updatePrice(new Price(Money.fromDecimal(100, "EUR", new FakeCurrencyLookup())));

            classifiedAd.requestToPublish();
        }).isInstanceOf(InvalidStateException.class);
    }

    @Test
    void cannot_publish_with_zero_price() {
        assertThatThrownBy(() -> {

            classifiedAd.updateTitle(new ClassifiedAdTitle("Test ad"));
            classifiedAd.updateText(new ClassifiedAdText("Please buy my stuff"));
            classifiedAd.updatePrice(new Price(Money.fromDecimal(0, "EUR", new FakeCurrencyLookup())));

            classifiedAd.requestToPublish();
        }).isInstanceOf(InvalidStateException.class);
    }

    @Test
    void invalidClassifiedAdState() {
        var classifiedAd = new ClassifiedAd();
        assertThrows(InvalidStateException.class, () -> {
            classifiedAd.ensureValidState(ImmutableClassifiedAdSoldEvent.builder()
                .aggregateId(id)
                .aggregateName(ClassifiedAd.class.getSimpleName())
                .id(UUID.randomUUID())
                .build());
        });
    }

    @Test
    void testCannotUpdateInvalidClassifiedAd() {
        var classifiedAd = new ClassifiedAd();

//            classifiedAd.updateText(classifiedAdId, "ClassifiedAdId or OwnerId cannot be null");
        assertThrows(InvalidStateException.class, () -> {
            classifiedAd.updateText(ClassifiedAdText.from("ClassifiedAdId or OwnerId cannot be null"));
        });
    }

    @Test
    void testCanUpdateValidClassifiedAd() {
        var classifiedAd = new ClassifiedAd(ClassifiedAdId.from(id), UserId.from(ownerId));
        classifiedAd.updateText(ClassifiedAdText.from("bad text"));
        classifiedAd.updateTitle(ClassifiedAdTitle.fromString("bad title"));

        assertThat(classifiedAd.getText().value()).isNotNull().isEqualTo("bad text");
        assertThat(classifiedAd.getTitle().value()).isNotNull().isEqualTo("bad title");
    }

    @Test
    void testCannotPublishClassifiedAdWithInvalidInvariants1() {
        var classifiedAd = new ClassifiedAd();
        assertThrows(InvalidStateException.class, classifiedAd::requestToPublish);
    }

    @Test
    void testCannotPublishClassifiedAdWithInvalidInvariants() {
        var classifiedAd = new ClassifiedAd(ClassifiedAdId.from(id), UserId.from(ownerId));
        classifiedAd.updateText(ClassifiedAdText.from("bad text"));
        classifiedAd.updateTitle(ClassifiedAdTitle.fromString("bad title"));
        assertThrows(InvalidStateException.class, classifiedAd::requestToPublish);
    }

    @Test
    void testCanOnlyPublishClassifiedAdWhichIsNotInInactive() {
        var classifiedAd = new ClassifiedAd(ClassifiedAdId.from(id), UserId.from(ownerId));
        classifiedAd.updateText(ClassifiedAdText.from("bad text"));
        classifiedAd.updateTitle(ClassifiedAdTitle.fromString("bad title"));
        classifiedAd.updatePrice(Price.from(new BigDecimal("10.00", MathContext.DECIMAL64)));

        classifiedAd.requestToPublish();
        assertThat(classifiedAd.getState()).isEqualTo(ClassifiedAdState.PENDING_REVIEW);

        assertThrows(InvalidStateException.class, classifiedAd::requestToPublish);
    }

    @Test
    void testCannotRejectUnpublishedClassifiedAd() {
        var classifiedAd = new ClassifiedAd(ClassifiedAdId.from(id), UserId.from(ownerId));
        classifiedAd.updateText(ClassifiedAdText.from("bad text"));
        classifiedAd.updateTitle(ClassifiedAdTitle.fromString("bad title"));
        classifiedAd.updatePrice(Price.from(new BigDecimal("10.00", MathContext.DECIMAL64)));

        assertThrows(InvalidStateException.class, () -> {
            classifiedAd.reject(approverId, "cannot be sold");
        });
    }

    @Test
    void testCannotApproveUnpublishedClassifiedAd() {
        var classifiedAd = new ClassifiedAd(ClassifiedAdId.from(id), UserId.from(ownerId));
        classifiedAd.updateText(ClassifiedAdText.from("bad text"));
        classifiedAd.updateTitle(ClassifiedAdTitle.fromString("bad title"));
        classifiedAd.updatePrice(Price.from(new BigDecimal("10.00", MathContext.DECIMAL64)));

        assertThrows(InvalidStateException.class, () -> {
            classifiedAd.approve(UserId.from(approverId));
        });
    }

    @Test
    void testCanOnlyPublishClassifiedAdWhichIsInactive() {
        var classifiedAd = new ClassifiedAd(ClassifiedAdId.from(id), UserId.from(ownerId));
        classifiedAd.updateText(ClassifiedAdText.from("bad text"));
        classifiedAd.updateTitle(ClassifiedAdTitle.fromString("bad title"));
        classifiedAd.updatePrice(Price.from(new BigDecimal("10.00", MathContext.DECIMAL64)));

        classifiedAd.requestToPublish();

        assertThat(classifiedAd.getState()).isEqualTo(ClassifiedAdState.PENDING_REVIEW);
    }

    @Test
    void testCannotMarkItemAsSoldWhenNotActive() {
        var amount = BigDecimal.valueOf(10.00).setScale(2, RoundingMode.CEILING);
        var classifiedAd = new ClassifiedAd(ClassifiedAdId.from(id), UserId.from(ownerId));
        classifiedAd.updateText(ClassifiedAdText.from("bad text"));
        classifiedAd.updateTitle(ClassifiedAdTitle.fromString("bad title"));
        classifiedAd.updatePrice(Price.from(new BigDecimal("10.00", MathContext.DECIMAL64)));

        assertThrows(InvalidStateException.class, classifiedAd::markItemAsSold);
    }

    @Test
    void markItemAsSoldWhenActive() {
        var classifiedAd = new ClassifiedAd(ClassifiedAdId.from(id), UserId.from(ownerId));
        classifiedAd.updateText(ClassifiedAdText.from("bad text"));
        classifiedAd.updateTitle(ClassifiedAdTitle.fromString("bad title"));
        classifiedAd.updatePrice(Price.from(new BigDecimal("10.00", MathContext.DECIMAL64)));

        classifiedAd.requestToPublish();

        classifiedAd.approve(UserId.from(approverId));

        classifiedAd.markItemAsSold();

        assertThat(classifiedAd.getState()).isEqualTo(MARKED_AS_SOLD);
    }

    @Test
    void testCannotSellItemThatIsInactive() {
        var classifiedAd = new ClassifiedAd(ClassifiedAdId.from(id), UserId.from(ownerId));
        classifiedAd.updateText(ClassifiedAdText.from("bad text"));
        classifiedAd.updateTitle(ClassifiedAdTitle.fromString("bad title"));
        classifiedAd.updatePrice(Price.from(new BigDecimal("10.00", MathContext.DECIMAL64)));

        assertThrows(InvalidStateException.class, classifiedAd::markItemAsSold);

        // status does not change
        assertThat(classifiedAd.getState()).isEqualTo(INACTIVE);
    }

    @Test
    void testItemCanBeRejectedIfRequestedForPublish() {
        var classifiedAd = new ClassifiedAd(ClassifiedAdId.from(id), UserId.from(ownerId));
        classifiedAd.updateText(ClassifiedAdText.from("bad text"));
        classifiedAd.updateTitle(ClassifiedAdTitle.fromString("bad title"));
        classifiedAd.updatePrice(Price.from(new BigDecimal("10.00", MathContext.DECIMAL64)));

        classifiedAd.requestToPublish();

        classifiedAd.reject(approverId, "Item Cannot be sold");

        assertThat(classifiedAd.getState()).isEqualTo(REJECTED);
        assertThat(classifiedAd.getRejectedMessage()).isEqualTo("Item Cannot be sold");
    }
}
