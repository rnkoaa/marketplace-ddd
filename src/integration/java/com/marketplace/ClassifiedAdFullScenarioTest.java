package com.marketplace;

import java.io.IOException;
import org.junit.jupiter.api.Disabled;

@Disabled
public class ClassifiedAdFullScenarioTest extends BaseRepositoryTest {

    public ClassifiedAdFullScenarioTest() throws IOException {
    }
//
//  UUID classifiedAdIdUuid = UUID.fromString("d0b00201-a612-423d-969e-96f182468514");
//  UUID ownerIdUuid = UUID.fromString("c205ac9d-d480-4c67-8714-cbbaaf2ae0c4");
//  UUID approverIdUuid = UUID.fromString("74c4b183-a7bd-431e-af57-b7377f5c0414");
//
//  @Test
//  public void classifiedAdFullScenario() throws InterruptedException {
//    classifiedAdService.handle(ImmutableCreateClassifiedAd.builder().classifiedAdId(classifiedAdIdUuid)
//        .ownerId(ownerIdUuid).build());
//
//    Optional<ClassifiedAd> maybe = classifiedAdService.findById(ClassifiedAdId.from(classifiedAdIdUuid));
//    assertThat(maybe).isPresent();
//    ClassifiedAd firstClassifiedAd = maybe.get();
//    assertThat(firstClassifiedAd.getChanges()).hasSize(1);
//    assertThat(firstClassifiedAd.getText()).isNull();
//    assertThat(firstClassifiedAd.getTitle()).isNull();
//    assertThat(firstClassifiedAd.getApprovedBy()).isNull();
//    assertThat(firstClassifiedAd.getId()).isEqualTo(ClassifiedAdId.from(classifiedAdIdUuid));
//    assertThat(firstClassifiedAd.getOwnerId()).isEqualTo(UserId.from(ownerIdUuid));
//
//    // update text
//    classifiedAdService.handle(ImmutableUpdateClassifiedAdText.builder().classifiedAdId(classifiedAdIdUuid)
//        .text("""
//            2018 Mac mini for sale
//            3.0GHz 6‑core 8th‑generation Intel Core i5 (Turbo Boost up to 4.1GHz)
//            16GB 2666MHz DDR4
//            512GB SSD storage
//            Intel UHD Graphics 630
//            Thanks for looking!""")
//        .build());
//
//    Optional<ClassifiedAd> secondMaybe = classifiedAdService.findById(ClassifiedAdId.from(classifiedAdIdUuid));
//    assertThat(secondMaybe).isPresent();
//    ClassifiedAd secondClassifiedAd = secondMaybe.get();
//    assertThat(secondClassifiedAd.getChanges()).hasSize(2);
//    assertThat(secondClassifiedAd.getText()).isNotNull();
//    assertThat(secondClassifiedAd.getText().value()).isNotBlank().contains("2018 Mac mini");
//
//    // update title
//    classifiedAdService.handle(ImmutableUpdateClassifiedAdTitle.builder().classifiedAdId(classifiedAdIdUuid)
//        .title("2018 Mac Mini 3.0GHz 6‑core 16GB 512GB SSD - $1,000 (Golden Valley)")
//        .build());
//
//    Optional<ClassifiedAd> thirdMaybe = classifiedAdService.findById(ClassifiedAdId.from(classifiedAdIdUuid));
//    assertThat(thirdMaybe).isPresent();
//    ClassifiedAd thirdClassifiedAd = thirdMaybe.get();
//    assertThat(thirdClassifiedAd.getChanges()).hasSize(3);
//    assertThat(thirdClassifiedAd.getText()).isNotNull();
//    assertThat(thirdClassifiedAd.getTitle()).isNotNull();
//    assertThat(thirdClassifiedAd.getText().value()).isNotBlank().contains("2018 Mac mini");
//    assertThat(thirdClassifiedAd.getTitle().value()).isNotBlank().contains("2018 Mac Mini");
//
//    // update price
//    classifiedAdService.handle(ImmutableUpdateClassifiedAdPrice.builder()
//        .classifiedAdId(classifiedAdIdUuid)
//        .currency("USD")
//        .amount(new BigDecimal("10.50"))
//        .build());
//
//    Optional<ClassifiedAd> fourthMaybe = classifiedAdService.findById(ClassifiedAdId.from(classifiedAdIdUuid));
//    assertThat(fourthMaybe).isPresent();
//    ClassifiedAd fourthClassifiedAd = fourthMaybe.get();
//    assertThat(fourthClassifiedAd.getChanges()).hasSize(4);
//    assertThat(fourthClassifiedAd.getText()).isNotNull();
//    assertThat(fourthClassifiedAd.getTitle()).isNotNull();
//    assertThat(fourthClassifiedAd.getPrice()).isNotNull();
//    assertThat(fourthClassifiedAd.getPrice().money()).isNotNull();
//    assertThat(fourthClassifiedAd.getPrice().money().amount()).isNotNull().isEqualTo(new BigDecimal("10.50"));
//
//    // update approve
//    classifiedAdService.handle(ImmutableApproveClassifiedAd.builder()
//        .approverId(approverIdUuid)
//        .classifiedAdId(classifiedAdIdUuid)
//        .build());
//
//    Optional<ClassifiedAd> fifthMaybe = classifiedAdService.findById(ClassifiedAdId.from(classifiedAdIdUuid));
//    assertThat(fifthMaybe).isPresent();
//    ClassifiedAd fifthClassifiedAd = fifthMaybe.get();
//    assertThat(fifthClassifiedAd.getChanges()).hasSize(5);
//    assertThat(fifthClassifiedAd.getText()).isNotNull();
//    assertThat(fifthClassifiedAd.getTitle()).isNotNull();
//    assertThat(fifthClassifiedAd.getApprovedBy()).isNotNull();
//    assertThat(fifthClassifiedAd.getState()).isEqualTo(ClassifiedAdState.APPROVED);// update title
//
//    // publish
//    classifiedAdService.handle(ImmutablePublishClassifiedAd.builder().classifiedAdId(classifiedAdIdUuid).build());
//    TimeUnit.MILLISECONDS.sleep(200);
//
//    Optional<ClassifiedAd> sixthMaybe = classifiedAdService.findById(ClassifiedAdId.from(classifiedAdIdUuid));
//    assertThat(sixthMaybe).isPresent();
//    ClassifiedAd sixthClassifiedAd = sixthMaybe.get();
//    assertThat(sixthClassifiedAd.getChanges()).hasSize(6);
//    assertThat(sixthClassifiedAd.getState()).isEqualTo(ClassifiedAdState.PENDING_REVIEW);
//
//  }
}
