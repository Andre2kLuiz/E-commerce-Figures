package pw.com.pw.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "usuario_tbl")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Size(min = 2, max = 30, message = "Houve um erro no cadastro do campo título.")
    @NotBlank (message = "O Nome não pode conter caracteres em branco.")
    String username;

    @Size(min = 8, message = "Houve um erro no cadastro do campo título.")
    @NotBlank (message = "O Nome não pode conter caracteres em branco.")
    String password;

    Boolean isAdmin;

    // Getter para isAdmin
    public boolean isAdmin() {
        return isAdmin;
    }

    // Setter para isAdmin
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }


}
