package com.marketplace.domain.classifiedad;

import java.util.Objects;

public record ClassifiedAdTitle(String value) {

    static ClassifiedAdTitle DEFAULT = new ClassifiedAdTitle("");

    public ClassifiedAdTitle {
        Objects.requireNonNull(value, "classifiedAd title cannot be null");
    }

    public static ClassifiedAdTitle fromString(String title) {
        return new ClassifiedAdTitle(title);
    }

    @Override
    public String toString() {
        return value;
    }

    public ClassifiedAdTitle fromHtmlString(String htmlString) {
//        var supportedTagsReplaced = htmlTitle
//                .Replace("<i>", "*")
//                .Replace("</i>", "*")
//                .Replace("<b>", "**")
//                .Replace("</b>", "**");
//
//        var value = Regex.Replace(supportedTagsReplaced, "<.*?>", string.Empty);
//        CheckValidity(value);
//
//        if (value == null || value.isEmpty() || value.length() > 100) {
        return new ClassifiedAdTitle("");
    }

    boolean isValid() {
        return !value.isEmpty();
    }

    public boolean isValid(String value) {
        return value != null && !value.isEmpty() && !(value.length() > 100);
    }

}
