package com.marketplace.domain.classifiedad.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketplace.cqrs.command.Command;
import com.marketplace.domain.classifiedad.ClassifiedAdState;
import java.util.List;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableUpdateClassifiedAd.class)
@JsonDeserialize(as = ImmutableUpdateClassifiedAd.class)
public interface UpdateClassifiedAd extends Command {

    @JsonProperty("classified_ad_id")
    Optional<UUID> getClassifiedAdId();

    Optional<String> getText();

    Optional<String> getTitle();

    Optional<PriceDto> getPrice();

    @JsonProperty("owner")
    Optional<UUID> getOwnerId();

    @JsonProperty("approver")
    Optional<UUID> getApprovedBy();

    List<PictureDto> getPictures();

    Optional<ClassifiedAdState> getState();

    @Value.Immutable
    @JsonSerialize(as = ImmutablePictureDto.class)
    @JsonDeserialize(as = ImmutablePictureDto.class)
    interface PictureDto {

        @Value.Default
        default UUID getId() {
            return UUID.randomUUID();
        }

        @Value.Default
        default int getOrder() {
            return 0;
        }

        String getUri();

        int getWidth();

        int getHeight();
    }

    @Value.Immutable
    @JsonSerialize(as = ImmutablePriceDto.class)
    @JsonDeserialize(as = ImmutablePriceDto.class)
    interface PriceDto {

        @JsonProperty("currency_code")
        String getCurrencyCode();

        BigDecimal getAmount();
    }
}
