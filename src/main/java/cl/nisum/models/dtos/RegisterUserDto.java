package cl.nisum.models.dtos;

import java.util.List;

import cl.nisum.models.validator.PasswordRegex;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RegisterUserDto {

    @NotBlank
    private String name;
    
    @NotBlank
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
    private String email;
    
    @NotBlank
    @PasswordRegex
    private String password;
    
    @Valid
    private List<RegisterPhoneDto> phones;
}
