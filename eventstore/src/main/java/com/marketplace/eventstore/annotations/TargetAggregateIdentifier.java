package com.marketplace.eventstore.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
public @interface TargetAggregateIdentifier {}
