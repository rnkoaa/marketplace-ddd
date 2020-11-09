package com.marketplace.domain.command;

import lombok.Value;

import java.util.UUID;

@Value
public class ApproveClassifiedAd {
    UUID approverId;
}
