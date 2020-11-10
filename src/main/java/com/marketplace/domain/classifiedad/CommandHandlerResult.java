package com.marketplace.domain.classifiedad;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class CommandHandlerResult<T> {

    public T result;
    boolean successful;
    String message;
}
