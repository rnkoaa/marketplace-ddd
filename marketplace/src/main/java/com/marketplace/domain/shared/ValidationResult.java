package com.marketplace.domain.shared;

public record ValidationResult(boolean valid, String message) {

    public static ValidationResult of(boolean valid, String message) {
        return new ValidationResult(valid, message);
    }

    public boolean isNotValid() {
        return !this.valid;
    }

    public ValidationResult and(ValidationResult validationResult) {
        var allValid = validationResult.valid && this.valid;
        if (allValid) {
            return this;
        }
        return ValidationResult.of(false, this.message + "\n" + validationResult.message());
    }
}