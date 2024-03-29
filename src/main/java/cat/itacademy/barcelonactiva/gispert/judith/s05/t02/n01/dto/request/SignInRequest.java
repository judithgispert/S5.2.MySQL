package cat.itacademy.barcelonactiva.gispert.judith.s05.t02.n01.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {
    private String email;
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;
}
