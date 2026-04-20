package weather_app.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EqualsPasswordsValidator.class)
@Target( ElementType.TYPE )
@Retention(RetentionPolicy.RUNTIME)
public @interface EqualsPasswordFields {
    String message() default "password should be equals";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}