package com.marketplace.domain.userprofile.controller;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableUpdateUserProfileResult.class)
@JsonSerialize(as = ImmutableUpdateUserProfileResult.class)
public interface UpdateUserProfileResult {

  UUID getId();
}
