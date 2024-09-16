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
    
    @NotBlank
    private String number;

    @NotBlank
    private String citycode;

    @NotBlank
    private String contrycode;
}
