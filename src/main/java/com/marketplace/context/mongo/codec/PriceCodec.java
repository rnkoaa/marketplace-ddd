package com.marketplace.context.mongo.codec;

import com.marketplace.domain.classifiedad.Price;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

public class PriceCodec implements Codec<Price> {
    private final PriceConverter priceConverter;
    private final CodecRegistry codecRegistry;

    public PriceCodec(CodecRegistry codecRegistry, PriceConverter priceConverter) {
        this.priceConverter = priceConverter;
        this.codecRegistry = codecRegistry;
    }

    @Override
    public Price decode(BsonReader reader, DecoderContext decoderContext) {
        Codec<Document> documentCodec = codecRegistry.get(Document.class);
        Document document = documentCodec.decode(reader, decoderContext);
        return priceConverter.deserialize(document);
    }

    @Override
    public void encode(BsonWriter writer, Price value, EncoderContext encoderContext) {
        Codec<Document> documentCodec = codecRegistry.get(Document.class);
        Document document = priceConverter.serialize(value);
        documentCodec.encode(writer, document, encoderContext);
    }

    @Override
    public Class<Price> getEncoderClass() {
        return Price.class;
    }
}
