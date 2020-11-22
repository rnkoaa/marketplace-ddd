package com.marketplace.context.mongo.codec;

import org.bson.Document;

public interface DocumentSerializer<T> {
    Document serialize(T value);
}
