package weather_app.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import weather_app.dto.RegisterDTO;

public class EqualsPasswordsValidator implements
        ConstraintValidator<EqualsPasswordFields, RegisterDTO> {

    @Override
    public void initialize(EqualsPasswordFields contactNumber) {
    }

    @Override
    public boolean isValid(RegisterDTO registerDTO, ConstraintValidatorContext context) {
        String password = registerDTO.getPassword();
        String repeatedPassword = registerDTO.getRepeatedPassword();
        boolean equals = password.equals(repeatedPassword);
        System.out.println(equals);

        if (!equals) {
            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate("passwords should be equal")
                    .addPropertyNode("repeatedPassword")
                    .addConstraintViolation();
        }

        return equals;
    }

}