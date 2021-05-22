package com.marketplace.eventstore.framework;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public sealed abstract class Result<T> {

    // getError
    // orElseThrow
    // onSuccess
    // onFailure

    public abstract Result<T> filter(Predicate<T> p, String message);

    public abstract Result<T> filter(Predicate<T> p);

    public abstract <U> Result<U> map(Function<T, U> func);

    public abstract <U> Result<U> flatmap(Function<T, Result<U>> func);

    public abstract Optional<T> toOptional();

    public abstract T orElse(T defaultValue);

    public abstract T orElse(Supplier<T> defaultValue);

    public abstract void then(Consumer<T> defaultValue);

    public abstract Exception getError();

    public static <T> Result<T> tryOf(Supplier<T> supplier) {
        try {
            return Result.of(supplier.get());
        } catch (Exception e) {
            return Result.error(e);
        }
    }

    public static <T> Result<T> of(final T value) {
        return of(value, "null value");
    }

    public static <T> Result<T> of(final T value, final String message) {
        return value != null ? success(value) : error(message);
    }

    public static <T> Result<T> ofNullable(final T value) {
        return value != null ? success(value) : empty();
    }

    public static <T> Result<T> success(T value) {
        return new Success<>(value);
    }

    public static <T> Result<T> error(Exception ex) {
        return new Failure<>(ex);
    }

    public static <T> Result<T> error(String message) {
        return new Failure<>(message);
    }

    public static <T> Result<T> error(String message, Exception ex) {
        return new Failure<>(new Exception(message, ex));
    }

    public static <T> Result<T> failure(Failure<T> failure) {
        return new Failure<>(failure.exception);
    }

    public static <T> Result<T> empty() {
        return new Empty<>();
    }

    public abstract boolean isPresent();

    public abstract T get();

    public abstract boolean isError();

    private final static class Success<T> extends Result<T> {

        private final T value;

        private Success(T value) {
            this.value = value;
        }

        @Override
        public Result<T> filter(Predicate<T> p, String message) {
            try {
                return p.test(this.value) ? success(this.value) : error(message);
            } catch (Exception ex) {
                return error(ex);
            }
        }

        @Override
        public Result<T> filter(Predicate<T> p) {
            try {
                return p.test(this.value) ? success(this.value) : error("not found");
            } catch (Exception ex) {
                return error(ex);
            }
        }

        @Override
        public <U> Result<U> map(Function<T, U> func) {
            try {
                return success(func.apply(this.value));
            } catch (Exception ex) {
                return error(ex);
            }
        }

        @Override
        public <U> Result<U> flatmap(Function<T, Result<U>> func) {
            try {
                return func.apply(this.value);
            } catch (Exception ex) {
                return error(ex);
            }
        }

        @Override
        public Optional<T> toOptional() {
            return Optional.of(this.value);
        }

        @Override
        public T orElse(T defaultValue) {
            return this.value;
        }

        @Override
        public T orElse(Supplier<T> defaultValue) {
            return this.value;
        }

        @Override
        public void then(Consumer<T> consumer) {
            try {
                consumer.accept(this.value);
            } catch (Exception ex) {
                // handle error consume
            }
        }

        @Override
        public Exception getError() {
            return null;
        }

//        @Override
//        public <T> Result<T> tryOf(Supplier<T> supplier) {
//            try {
//                return Result.of(supplier.get());
//            } catch (Exception e) {
//                return Result.error(e);
//            }
//        }

        @Override
        public boolean isPresent() {
            return this.value != null;
        }

        @Override
        public T get() {
            return this.value;
        }

        @Override
        public boolean isError() {
            return false;
        }

        @Override
        public String toString() {
            return "Success{" +
                "value=" + value +
                '}';
        }
    }

    private final static class Failure<T> extends Result<T> {

        private final Exception exception;

        private Failure(Exception exception) {
            this.exception = exception;
        }

        private Failure(String message) {
            this.exception = new Exception(message);
        }

        @Override
        public Result<T> filter(Predicate<T> p, String message) {
            return error(this.exception);
        }

        @Override
        public Result<T> filter(Predicate<T> p) {
            return error(this.exception);
        }

        @Override
        public <U> Result<U> map(Function<T, U> func) {
            return error(this.exception);
        }

        @Override
        public <U> Result<U> flatmap(Function<T, Result<U>> func) {
            return error(this.exception);
        }

        @Override
        public Optional<T> toOptional() {
            return Optional.empty();
        }

        @Override
        public T orElse(T defaultValue) {
            return defaultValue;
        }

        @Override
        public T orElse(Supplier<T> defaultValue) {
            return defaultValue.get();
        }

        @Override
        public void then(Consumer<T> consumer) {
//           consumer.accept();
        }

        @Override
        public Exception getError() {
            return this.exception;
        }

//        @Override
//        public <T1> Result<T1> tryOf(Supplier<T1> supplier) {
//            return null;
//        }

        @Override
        public boolean isPresent() {
            return false;
        }

        @Override
        public T get() {
            return null;
        }

        @Override
        public boolean isError() {
            return true;
        }

        @Override
        public String toString() {
            return "Failure{" +
                "exception=" + exception +
                '}';
        }
    }

    private final static class Empty<T> extends Result<T> {

        private Empty() {

        }

        @Override
        public Result<T> filter(Predicate<T> p, String message) {
            return empty();
        }

        @Override
        public Result<T> filter(Predicate<T> p) {
            return empty();
        }

        @Override
        public <U> Result<U> map(Function<T, U> func) {
            return empty();
        }

        @Override
        public <U> Result<U> flatmap(Function<T, Result<U>> func) {
            return empty();
        }

        @Override
        public Optional<T> toOptional() {
            return Optional.empty();
        }

        @Override
        public T orElse(T defaultValue) {
            return defaultValue;
        }

        @Override
        public T orElse(Supplier<T> defaultValue) {
            return defaultValue.get();
        }

        @Override
        public void then(Consumer<T> consumer) {

        }

        @Override
        public Exception getError() {
            return null;
        }

//        @Override
//        public <T1> Result<T1> tryOf(Supplier<T1> supplier) {
//            return null;
//        }

        @Override
        public boolean isPresent() {
            return false;
        }

        @Override
        public T get() {
            return null;
        }

        @Override
        public boolean isError() {
            return false;
        }
    }
}
