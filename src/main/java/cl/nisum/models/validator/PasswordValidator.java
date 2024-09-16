package cl.nisum.models.validator;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
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

        Pattern pattern = Pattern.compile(passwordRegex);
        return password != null && pattern.matcher(password).matches();
    }
}
