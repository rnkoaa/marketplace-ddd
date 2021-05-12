/*
 * This file is generated by jOOQ.
 */
package com.marketplace.eventstore.jdbc.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ClassifiedAd implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;
    private final String classifiedAdId;
    private final String approver;
    private final String owner;
    private final String title;
    private final String text;
    private final String price;
    private final String pictures;
    private final String created;
    private final String updated;

    public ClassifiedAd(ClassifiedAd value) {
        this.id = value.id;
        this.classifiedAdId = value.classifiedAdId;
        this.approver = value.approver;
        this.owner = value.owner;
        this.title = value.title;
        this.text = value.text;
        this.price = value.price;
        this.pictures = value.pictures;
        this.created = value.created;
        this.updated = value.updated;
    }

    public ClassifiedAd(
        String id,
        String classifiedAdId,
        String approver,
        String owner,
        String title,
        String text,
        String price,
        String pictures,
        String created,
        String updated
    ) {
        this.id = id;
        this.classifiedAdId = classifiedAdId;
        this.approver = approver;
        this.owner = owner;
        this.title = title;
        this.text = text;
        this.price = price;
        this.pictures = pictures;
        this.created = created;
        this.updated = updated;
    }

    /**
     * Getter for <code>classified_ad.id</code>.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Getter for <code>classified_ad.classified_ad_id</code>.
     */
    public String getClassifiedAdId() {
        return this.classifiedAdId;
    }

    /**
     * Getter for <code>classified_ad.approver</code>.
     */
    public String getApprover() {
        return this.approver;
    }

    /**
     * Getter for <code>classified_ad.owner</code>.
     */
    public String getOwner() {
        return this.owner;
    }

    /**
     * Getter for <code>classified_ad.title</code>.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Getter for <code>classified_ad.text</code>.
     */
    public String getText() {
        return this.text;
    }

    /**
     * Getter for <code>classified_ad.price</code>.
     */
    public String getPrice() {
        return this.price;
    }

    /**
     * Getter for <code>classified_ad.pictures</code>.
     */
    public String getPictures() {
        return this.pictures;
    }

    /**
     * Getter for <code>classified_ad.created</code>.
     */
    public String getCreated() {
        return this.created;
    }

    /**
     * Getter for <code>classified_ad.updated</code>.
     */
    public String getUpdated() {
        return this.updated;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ClassifiedAd (");

        sb.append(id);
        sb.append(", ").append(classifiedAdId);
        sb.append(", ").append(approver);
        sb.append(", ").append(owner);
        sb.append(", ").append(title);
        sb.append(", ").append(text);
        sb.append(", ").append(price);
        sb.append(", ").append(pictures);
        sb.append(", ").append(created);
        sb.append(", ").append(updated);

        sb.append(")");
        return sb.toString();
    }
}
