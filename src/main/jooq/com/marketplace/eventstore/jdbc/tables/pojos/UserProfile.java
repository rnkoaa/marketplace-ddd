/*
 * This file is generated by jOOQ.
 */
package com.marketplace.eventstore.jdbc.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Integer id;
    private final String  userProfileId;
    private final String  firstname;
    private final String  lastname;
    private final String  middlename;
    private final String  displayname;
    private final String  photos;
    private final String  created;
    private final String  updated;

    public UserProfile(UserProfile value) {
        this.id = value.id;
        this.userProfileId = value.userProfileId;
        this.firstname = value.firstname;
        this.lastname = value.lastname;
        this.middlename = value.middlename;
        this.displayname = value.displayname;
        this.photos = value.photos;
        this.created = value.created;
        this.updated = value.updated;
    }

    public UserProfile(
        Integer id,
        String  userProfileId,
        String  firstname,
        String  lastname,
        String  middlename,
        String  displayname,
        String  photos,
        String  created,
        String  updated
    ) {
        this.id = id;
        this.userProfileId = userProfileId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.middlename = middlename;
        this.displayname = displayname;
        this.photos = photos;
        this.created = created;
        this.updated = updated;
    }

    /**
     * Getter for <code>USER_PROFILE.ID</code>.
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Getter for <code>USER_PROFILE.USER_PROFILE_ID</code>.
     */
    public String getUserProfileId() {
        return this.userProfileId;
    }

    /**
     * Getter for <code>USER_PROFILE.FIRSTNAME</code>.
     */
    public String getFirstname() {
        return this.firstname;
    }

    /**
     * Getter for <code>USER_PROFILE.LASTNAME</code>.
     */
    public String getLastname() {
        return this.lastname;
    }

    /**
     * Getter for <code>USER_PROFILE.MIDDLENAME</code>.
     */
    public String getMiddlename() {
        return this.middlename;
    }

    /**
     * Getter for <code>USER_PROFILE.DISPLAYNAME</code>.
     */
    public String getDisplayname() {
        return this.displayname;
    }

    /**
     * Getter for <code>USER_PROFILE.PHOTOS</code>.
     */
    public String getPhotos() {
        return this.photos;
    }

    /**
     * Getter for <code>USER_PROFILE.CREATED</code>.
     */
    public String getCreated() {
        return this.created;
    }

    /**
     * Getter for <code>USER_PROFILE.UPDATED</code>.
     */
    public String getUpdated() {
        return this.updated;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("UserProfile (");

        sb.append(id);
        sb.append(", ").append(userProfileId);
        sb.append(", ").append(firstname);
        sb.append(", ").append(lastname);
        sb.append(", ").append(middlename);
        sb.append(", ").append(displayname);
        sb.append(", ").append(photos);
        sb.append(", ").append(created);
        sb.append(", ").append(updated);

        sb.append(")");
        return sb.toString();
    }
}
