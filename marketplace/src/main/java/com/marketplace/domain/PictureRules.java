package com.marketplace.domain;

import com.marketplace.domain.classifiedad.Picture;

public class PictureRules {

    public static boolean hasCorrectSize(Picture picture) {
        return picture != null
            && picture.getSize().width() >= 400
            && picture.getSize().height() >= 300;
    }

    public static boolean hasCorrectSize(PictureSize size) {
        return size != null
            && size.width() >= 400
            && size.height() >= 300;
    }
}
