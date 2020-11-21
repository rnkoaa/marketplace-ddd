package com.marketplace.context.mongo.codec;

import com.marketplace.domain.PictureSize;
import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.Picture;
import org.bson.Document;

public class PictureConverter implements DocumentConverter<Picture> {
    private static final String ID = "id";
    private static final String URI = "uri";
    private static final String ORDER = "order";
    protected static final String HEIGHT = "height";
    protected static final String WIDTH = "width";
    private ClassifiedAd classifiedAd;

    public PictureConverter() {
    }

    public void setClassifiedAd(ClassifiedAd classifiedAd){
        this.classifiedAd = classifiedAd;
    }

    @Override
    public Picture deserialize(Document document) {
        String uri = document.getString(URI);
        int order = document.getInteger(ORDER);

        int height = document.getInteger(HEIGHT);
        int width = document.getInteger(WIDTH);
        var pictureSize = new PictureSize(width, height);
        return this.classifiedAd.createPicture(pictureSize, uri, order);
    }

    @Override
    public Document serialize(Picture value) {
        var document = new Document();
        document.put("id", value.getId().toString());
        document.put("parentId", value.getParentId().toString());
        document.put("height", value.getSize().height());
        document.put("width", value.getSize().width());
        document.put("uri", value.getUri());
        document.put("order", value.getOrder());
        return document;
    }
}
