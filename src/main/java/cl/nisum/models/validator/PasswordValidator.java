package cl.nisum.models.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class PasswordValidator implements ConstraintValidator<PasswordRegex, String> {

    @Value("${password.regex:}")
    private String passwordRegex;

    @Override
    public void initialize(PasswordRegex constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (passwordRegex.isBlank()) {
            return true;
        }

        // Validar la contraseña contra la regex si está definida.
        return password != null && password.matches(passwordRegex);
    }
}
