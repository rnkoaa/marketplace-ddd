package com.marketplace.annotations;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface MongoSingleRecordValue {
    boolean wrapped() default false;
}
