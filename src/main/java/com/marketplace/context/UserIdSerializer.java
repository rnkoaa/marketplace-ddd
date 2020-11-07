package com.marketplace.context;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.marketplace.domain.UserId;

import java.io.IOException;

public class UserIdSerializer extends JsonSerializer<UserId> {
    @Override
    public void serialize(UserId userId, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (userId.id() == null) {
            gen.writeString("");
            return;
        }
        gen.writeString(userId.id().toString());
    }
}
