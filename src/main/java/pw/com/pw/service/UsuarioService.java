package pw.com.pw.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import pw.com.pw.domain.Usuario;
import pw.com.pw.repository.UsuarioRepository;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;
    public UsuarioService(UsuarioRepository repository){
        this.repository = repository;
    }

    public Optional<Usuario> findById(String id){
        return repository.findById(id);
    }

    public Optional<Usuario> findByUsernameAndPassword(String username, String password) {
        // Implemente a lógica para encontrar o usuário pelo username e password
        // Por exemplo, usando um método customizado no repositório
        return repository.findByUsernameAndPassword(username, password);
    }

    public void delete(String id) {
        Usuario usuario = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario not found"));
        // Aqui você pode definir um campo `isDeleted` ou algo semelhante no `Usuario` se necessário
        // usuario.setIsDeleted(new Date()); // Exemplo de uso, remova se não tiver esse campo
        repository.delete(usuario);
    }

    public Usuario update(Usuario usuario){
       return repository.save(usuario);
    }

    public Usuario create(Usuario usuario) {
        return repository.save(usuario);
    }

    public List<Usuario> findAll() {
        return repository.findAll();
    }
}
