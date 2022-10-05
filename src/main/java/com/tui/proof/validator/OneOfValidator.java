package com.tui.proof.validator;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OneOfValidator implements ConstraintValidator<OneOf, Integer> {
    private List<Integer> values;
    private String message;

    @Override
    public void initialize(OneOf constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        int[] oneOf = constraintAnnotation.value();
//        this.values = new ArrayList<>(oneOf.length);
//        for (int i : oneOf) {
//            values.add(i);
//        }
        this.values = Arrays.stream(oneOf).boxed().collect(Collectors.toList());
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        if (integer == null) {
            return true;
        }
        boolean isValid = values.contains(integer);
        if (!isValid) {
            HibernateConstraintValidatorContext hibernateContext = constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class);
            hibernateContext.disableDefaultConstraintViolation();
            hibernateContext.disableDefaultConstraintViolation();
            hibernateContext.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
        }
        return isValid;
    }
}