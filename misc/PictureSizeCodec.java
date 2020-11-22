package com.marketplace.context.mongo.codec;

import com.marketplace.domain.PictureSize;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

import static com.marketplace.context.mongo.codec.PictureConverter.HEIGHT;
import static com.marketplace.context.mongo.codec.PictureConverter.WIDTH;

public class PictureSizeCodec implements Codec<PictureSize> {
    private final CodecRegistry codecRegistry;

    public PictureSizeCodec(CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }

    @Override
    public PictureSize decode(BsonReader reader, DecoderContext decoderContext) {
        Codec<Document> documentCodec = codecRegistry.get(Document.class);
        Document document = documentCodec.decode(reader, decoderContext);
        int width = document.getInteger(WIDTH);
        int height = document.getInteger(HEIGHT);
        return new PictureSize(width, height);
    }

    @Override
    public void encode(BsonWriter writer, PictureSize value, EncoderContext encoderContext) {
        Codec<Document> documentCodec = codecRegistry.get(Document.class);
        Document pictureSizeDocument = new Document();
        pictureSizeDocument.put(WIDTH, value.width());
        pictureSizeDocument.put(HEIGHT, value.height());
        documentCodec.encode(writer, pictureSizeDocument, encoderContext);
    }

    @Override
    public Class<PictureSize> getEncoderClass() {
        return null;
    }
}
