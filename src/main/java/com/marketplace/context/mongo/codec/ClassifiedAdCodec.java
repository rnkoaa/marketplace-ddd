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
    private final CodecRegistry codecRegistry;
    private PictureConverter pictureConverter;

    public ClassifiedAdCodec(CodecRegistry registry) {
        this.codecRegistry = registry;
    }

    @Override
    public ClassifiedAd decode(BsonReader reader, DecoderContext decoderContext) {
        Codec<Document> documentCodec = codecRegistry.get(Document.class);
        Document document = documentCodec.decode(reader, decoderContext);
        String id = document.getString("_id");
        ClassifiedAdId classifiedAdId = ClassifiedAdId.fromString(id);

        String ownerIdStr = document.getString("ownerId");
        UserId ownerId = UserId.fromString(ownerIdStr);
        ClassifiedAd classifiedAd = new ClassifiedAd(classifiedAdId, ownerId);

        this.pictureConverter = new PictureConverter(classifiedAd);

        String title = document.getString("title");
        if (!Strings.isNullOrEmpty(title)) {
            ClassifiedAdTitle classifiedAdTitle = ClassifiedAdTitle.fromString(title);
            classifiedAd.setTitle(classifiedAdTitle);
        }
        String text = document.getString("text");
        if (!Strings.isNullOrEmpty(text)) {
            ClassifiedAdText classifiedAdText = ClassifiedAdText.fromString(text);
            classifiedAd.setText(classifiedAdText);
        }

        List<Document> pictureDocuments = document.getList("pictures", Document.class);
        if (pictureDocuments != null && pictureDocuments.size() > 0) {

            long count = pictureDocuments.stream()
                    .map(doc -> pictureConverter.deserialize(doc))
                    .count();

            System.out.println("processed ");
        }


        return null;
    }

    @Override
    public void encode(BsonWriter writer, ClassifiedAd value, EncoderContext encoderContext) {
        Codec<Document> documentCodec = codecRegistry.get(Document.class);
        Document document = new Document();
        document.put("_id", value.getId().toString());
        document.put("ownerId", value.getId().toString());
        if (value.getTitle() != null) {
            document.put("title", value.getTitle().toString());
        }
        if (value.getText() != null) {
            document.put("text", value.getText().toString());
        }

        if (value.getState() != null) {
            document.put("state", value.getState().name());
        }

        if (value.getApprovedBy() != null) {
            document.put("approvedBy", value.getApprovedBy().toString());
        }

        List<Picture> pictures = value.getPictures();
        List<Document> pictureDocuments = pictures.stream().map(picture -> pictureConverter.serialize(picture))
                .collect(Collectors.toList());
        document.put("pictures", pictureDocuments);

        documentCodec.encode(writer, document, encoderContext);
    }

    @Override
    public Class<ClassifiedAd> getEncoderClass() {
        return ClassifiedAd.class;
    }
}
