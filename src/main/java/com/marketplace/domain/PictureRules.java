package com.marketplace.domain;

public class PictureRules {
    public boolean hasCorrectSize(Picture picture) {
        return picture != null
                && picture.getSize().width() >= 800
                && picture.getSize().height() >= 600;
    }
}
