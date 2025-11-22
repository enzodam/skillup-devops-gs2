package br.com.fiap.skillup.security;

import br.com.fiap.skillup.model.Usuario;
import br.com.fiap.skillup.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UsuarioUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario nao encontrado"));

        return new User(
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getRoles()
                        .stream()
                        .map(r -> new SimpleGrantedAuthority(r.getNome()))
                        .collect(Collectors.toList())
        );
    }
}
