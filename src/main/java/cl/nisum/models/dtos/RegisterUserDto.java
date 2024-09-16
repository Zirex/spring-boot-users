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

    @NotBlank(message = "Digite un nombre")
    private String name;
    
    @NotBlank(message = "Digite un correo")
    @Email(regexp = "^[\\w\\d\\.-]+@[\\w]+\\.[a-zA-Z]{2}$", message = "El correo no es valido, ej: aaaaaaa@dominio.cl")
    private String email;
    
    @NotBlank(message = "Digite una contraseña")
    @PasswordRegex
    private String password;
    
    @Valid
    private List<RegisterPhoneDto> phones;
}
