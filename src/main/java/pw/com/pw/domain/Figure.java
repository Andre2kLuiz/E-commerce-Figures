package pw.com.pw.domain;

import java.util.Date;

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
@Entity(name = "figure_tbl")
public class Figure {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    
    Date isDeleted;

    String ImageUri;
    float valor;

    @Size(min = 2, max = 30, message = "Houve um erro no cadastro do campo título.")
    @NotBlank (message = "O Nome não pode conter caracteres em branco.")
    String nome;
    
    float tamanho;
    String Material;

    public void regrasDeNegocioParaCadastro(){
        nome.toUpperCase();
    }
}
