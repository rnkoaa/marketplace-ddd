/*
 * This file is generated by jOOQ.
 */
package com.marketplace.evenstore.jooq.tables.records;


import com.marketplace.evenstore.jooq.tables.UserProfile;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record8;
import org.jooq.Row8;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UserProfileRecord extends UpdatableRecordImpl<UserProfileRecord> implements Record8<String, String, String, String, String, String, String, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>user_profile.id</code>.
     */
    public UserProfileRecord setId(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>user_profile.id</code>.
     */
    public String getId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>user_profile.first_name</code>.
     */
    public UserProfileRecord setFirstName(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>user_profile.first_name</code>.
     */
    public String getFirstName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>user_profile.last_name</code>.
     */
    public UserProfileRecord setLastName(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>user_profile.last_name</code>.
     */
    public String getLastName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>user_profile.middle_name</code>.
     */
    public UserProfileRecord setMiddleName(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>user_profile.middle_name</code>.
     */
    public String getMiddleName() {
        return (String) get(3);
    }

    /**
     * Setter for <code>user_profile.display_name</code>.
     */
    public UserProfileRecord setDisplayName(String value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>user_profile.display_name</code>.
     */
    public String getDisplayName() {
        return (String) get(4);
    }

    /**
     * Setter for <code>user_profile.photos</code>.
     */
    public UserProfileRecord setPhotos(String value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>user_profile.photos</code>.
     */
    public String getPhotos() {
        return (String) get(5);
    }

    /**
     * Setter for <code>user_profile.created</code>.
     */
    public UserProfileRecord setCreated(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>user_profile.created</code>.
     */
    public String getCreated() {
        return (String) get(6);
    }

    /**
     * Setter for <code>user_profile.updated</code>.
     */
    public UserProfileRecord setUpdated(String value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>user_profile.updated</code>.
     */
    public String getUpdated() {
        return (String) get(7);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record8 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row8<String, String, String, String, String, String, String, String> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    @Override
    public Row8<String, String, String, String, String, String, String, String> valuesRow() {
        return (Row8) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return UserProfile.USER_PROFILE.ID;
    }

    @Override
    public Field<String> field2() {
        return UserProfile.USER_PROFILE.FIRST_NAME;
    }

    @Override
    public Field<String> field3() {
        return UserProfile.USER_PROFILE.LAST_NAME;
    }

    @Override
    public Field<String> field4() {
        return UserProfile.USER_PROFILE.MIDDLE_NAME;
    }

    @Override
    public Field<String> field5() {
        return UserProfile.USER_PROFILE.DISPLAY_NAME;
    }

    @Override
    public Field<String> field6() {
        return UserProfile.USER_PROFILE.PHOTOS;
    }

    @Override
    public Field<String> field7() {
        return UserProfile.USER_PROFILE.CREATED;
    }

    @Override
    public Field<String> field8() {
        return UserProfile.USER_PROFILE.UPDATED;
    }

    @Override
    public String component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getFirstName();
    }

    @Override
    public String component3() {
        return getLastName();
    }

    @Override
    public String component4() {
        return getMiddleName();
    }

    @Override
    public String component5() {
        return getDisplayName();
    }

    @Override
    public String component6() {
        return getPhotos();
    }

    @Override
    public String component7() {
        return getCreated();
    }

    @Override
    public String component8() {
        return getUpdated();
    }

    @Override
    public String value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getFirstName();
    }

    @Override
    public String value3() {
        return getLastName();
    }

    @Override
    public String value4() {
        return getMiddleName();
    }

    @Override
    public String value5() {
        return getDisplayName();
    }

    @Override
    public String value6() {
        return getPhotos();
    }

    @Override
    public String value7() {
        return getCreated();
    }

    @Override
    public String value8() {
        return getUpdated();
    }

    @Override
    public UserProfileRecord value1(String value) {
        setId(value);
        return this;
    }

    @Override
    public UserProfileRecord value2(String value) {
        setFirstName(value);
        return this;
    }

    @Override
    public UserProfileRecord value3(String value) {
        setLastName(value);
        return this;
    }

    @Override
    public UserProfileRecord value4(String value) {
        setMiddleName(value);
        return this;
    }

    @Override
    public UserProfileRecord value5(String value) {
        setDisplayName(value);
        return this;
    }

    @Override
    public UserProfileRecord value6(String value) {
        setPhotos(value);
        return this;
    }

    @Override
    public UserProfileRecord value7(String value) {
        setCreated(value);
        return this;
    }

    @Override
    public UserProfileRecord value8(String value) {
        setUpdated(value);
        return this;
    }

    @Override
    public UserProfileRecord values(String value1, String value2, String value3, String value4, String value5, String value6, String value7, String value8) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached UserProfileRecord
     */
    public UserProfileRecord() {
        super(UserProfile.USER_PROFILE);
    }

    /**
     * Create a detached, initialised UserProfileRecord
     */
    public UserProfileRecord(String id, String firstName, String lastName, String middleName, String displayName, String photos, String created, String updated) {
        super(UserProfile.USER_PROFILE);

        setId(id);
        setFirstName(firstName);
        setLastName(lastName);
        setMiddleName(middleName);
        setDisplayName(displayName);
        setPhotos(photos);
        setCreated(created);
        setUpdated(updated);
    }
}