package com.marketplace.context.mongo.codec;

import com.marketplace.domain.classifiedad.*;
import com.marketplace.domain.shared.UserId;
import com.marketplace.framework.Strings;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.List;
import java.util.stream.Collectors;

public class ClassifiedAdCodec implements Codec<ClassifiedAd> {
    private static final String STATE = "state";
    private static final String OWNER_ID = "ownerId";
    private static final String ID = "_id";
    private static final String TITLE = "title";
    private static final String TEXT = "text";
    private static final String PICTURES = "pictures";
    private static final String APPROVED_BY = "approvedBy";
    private static final String PRICE = "price";

    private final CodecRegistry codecRegistry;
    private final PictureConverter pictureConverter;
    private final PriceConverter priceConverter;

    public ClassifiedAdCodec(CodecRegistry registry) {
        this.codecRegistry = registry;
        this.pictureConverter = new PictureConverter();
        this.priceConverter = new PriceConverter();
    }

    @Override
    public ClassifiedAd decode(BsonReader reader, DecoderContext decoderContext) {
        Codec<Document> documentCodec = codecRegistry.get(Document.class);
        Document document = documentCodec.decode(reader, decoderContext);
        String id = document.getString(ID);
        ClassifiedAdId classifiedAdId = ClassifiedAdId.fromString(id);

        String ownerIdStr = document.getString(OWNER_ID);
        String stateStr = document.getString(STATE);
        UserId ownerId = UserId.fromString(ownerIdStr);
        ClassifiedAd classifiedAd = new ClassifiedAd(classifiedAdId, ownerId);
        classifiedAd.setState(ClassifiedAdState.valueOf(stateStr));
        this.pictureConverter.setClassifiedAd(classifiedAd);

        String title = document.getString(TITLE);
        if (!Strings.isNullOrEmpty(title)) {
            ClassifiedAdTitle classifiedAdTitle = ClassifiedAdTitle.fromString(title);
            classifiedAd.setTitle(classifiedAdTitle);
        }
        String text = document.getString(TEXT);
        if (!Strings.isNullOrEmpty(text)) {
            ClassifiedAdText classifiedAdText = ClassifiedAdText.fromString(text);
            classifiedAd.setText(classifiedAdText);
        }

        List<Document> pictureDocuments = document.getList(PICTURES, Document.class);
        if (pictureDocuments != null && pictureDocuments.size() > 0) {
            pictureDocuments.forEach(pictureConverter::deserialize);
        }

        Document priceDocument = document.get(PRICE, Document.class);
        if (priceDocument != null) {
            Price price = priceConverter.deserialize(priceDocument);
            classifiedAd.setPrice(price);
        }

        return classifiedAd;
    }

    @Override
    public void encode(BsonWriter writer, ClassifiedAd value, EncoderContext encoderContext) {
        Codec<Document> documentCodec = codecRegistry.get(Document.class);
        Document document = new Document();
        document.put(ID, value.getId().toString());
        document.put(OWNER_ID, value.getId().toString());
        document.put(STATE, value.getState().name());

        if (value.getPrice() != null) {
            var priceDocument = priceConverter.serialize(value.getPrice());
            document.put(PRICE, priceDocument);
        }
        if (value.getTitle() != null) {
            document.put(TITLE, value.getTitle().toString());
        }
        if (value.getText() != null) {
            document.put(TEXT, value.getText().toString());
        }

        if (value.getState() != null) {
            document.put(STATE, value.getState().name());
        }

        if (value.getApprovedBy() != null) {
            document.put(APPROVED_BY, value.getApprovedBy().toString());
        }

        List<Document> pictureDocuments = value.getPictures()
                .stream()
                .map(pictureConverter::serialize)
                .collect(Collectors.toList());
        document.put(PICTURES, pictureDocuments);

        documentCodec.encode(writer, document, encoderContext);
    }

    @Override
    public Class<ClassifiedAd> getEncoderClass() {
        return ClassifiedAd.class;
    }
}
