package pw.com.pw.repository;

import pw.com.pw.domain.Usuario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    Optional<Usuario> findByUsernameAndPassword(String username, String password);
    
}
