package com.marketplace.context.mongo.codec;

import com.marketplace.domain.classifiedad.Money;
import com.marketplace.domain.classifiedad.Price;
import org.bson.Document;

public class PriceConverter implements DocumentConverter<Price> {

    private static final String CURRENCY_CODE = "currencyCode";
    private static final String AMOUNT = "amount";

    @Override
    public Price deserialize(Document document) {
        String currencyCode = document.getString(CURRENCY_CODE);
        double amount = document.getDouble(AMOUNT);
        Money money = Money.fromDecimal(amount, currencyCode);
        return new Price(money);
    }

    @Override
    public Document serialize(Price value) {
        Document document = new Document();
        document.put(CURRENCY_CODE, value.money().currencyCode());
        document.put(AMOUNT, value.money().amount().doubleValue());
        return document;
    }
}
