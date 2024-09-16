package cl.nisum.models.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RegisterPhoneDto {
    
    @NotBlank(message = "Digite un número")
    private String number;

    @NotBlank(message = "Digite un código de ciudad")
    private String citycode;

    @NotBlank(message = "Digite un código de país")
    private String contrycode;
}
