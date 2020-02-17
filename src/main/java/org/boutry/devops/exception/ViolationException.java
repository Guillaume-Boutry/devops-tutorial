package org.boutry.devops.exception;


import javax.validation.ConstraintViolation;

public class ViolationException extends Exception {

    private ConstraintViolation<?>[] violations;

    public ViolationException(ConstraintViolation<?>[] violations) {
        this.violations = violations;
    }

    public ConstraintViolation<?>[] getViolations() {
        return violations;
    }
}
