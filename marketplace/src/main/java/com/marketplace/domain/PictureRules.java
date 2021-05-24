package com.marketplace.domain;

import com.marketplace.domain.classifiedad.Picture;

public class PictureRules {
    public static boolean hasCorrectSize(Picture picture) {
        return picture != null
                && picture.getSize().width() >= 800
                && picture.getSize().height() >= 600;
    }
}
