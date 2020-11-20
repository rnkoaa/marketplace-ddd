package com.marketplace.context.mongo.codec;

import org.bson.Document;

public interface DocumentDeserializer<T> {
    T deserialize(Document document);
}
