package com.marketplace.domain.classifiedad.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.marketplace.domain.classifiedad.Money;
import java.io.IOException;

public class MoneySerializer extends JsonSerializer<Money> {

  @Override
  public void serialize(Money value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    gen.writeStartObject();
    gen.writeNumberField("amount", value.amount());
    gen.writeStringField("currency_code", value.currencyCode());
    gen.writeEndObject();
  }
}
