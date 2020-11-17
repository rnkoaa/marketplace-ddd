package com.marketplace.annotations;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface MongoRecordValue {
    boolean wrapped() default false;
}
