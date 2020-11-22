package com.marketplace.mongo.entity;

import org.bson.codecs.pojo.annotations.BsonIgnore;

public interface MongoEntity {
    @BsonIgnore
    default String getCollection() {
        return getClass().getSimpleName().toLowerCase();
    }

}
