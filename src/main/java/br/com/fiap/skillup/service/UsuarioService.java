package br.com.fiap.skillup.service;

import br.com.fiap.skillup.dto.UsuarioRequestDTO;
import br.com.fiap.skillup.dto.UsuarioResponseDTO;
import br.com.fiap.skillup.model.Role;
import br.com.fiap.skillup.model.Usuario;
import br.com.fiap.skillup.repository.RoleRepository;
import br.com.fiap.skillup.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /* =========================================================
       MÉTODOS DE APOIO
       ========================================================= */

    // >>> AGORA É PUBLIC, pra poder ser usado no RecomendacaoService
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + id));
    }

    private UsuarioResponseDTO toResponseDTO(Usuario u) {
        if (u == null) return null;

        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(u.getId());
        dto.setNome(u.getNome());
        dto.setEmail(u.getEmail());
        dto.setProfissaoAtual(u.getProfissaoAtual());
        dto.setMetaProfissional(u.getMetaProfissional());

        String tipo = u.getRoles().stream()
                .anyMatch(r -> "ROLE_ADMIN".equals(r.getNome())) ? "ADMIN" : "USER";
        dto.setTipo(tipo);

        return dto;
    }

    /**
     * Seta as roles de acordo com o tipo informado.
     * Sempre garante ROLE_USER; se tipo = ADMIN, adiciona também ROLE_ADMIN.
     */
    private void aplicarTipoRole(Usuario usuario, String tipo) {
        usuario.getRoles().clear();

        // sempre garante ROLE_USER
        Role roleUser = roleRepository.findByNome("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("ROLE_USER não encontrada no banco"));
        usuario.getRoles().add(roleUser);

        if ("ADMIN".equalsIgnoreCase(tipo)) {
            Role roleAdmin = roleRepository.findByNome("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("ROLE_ADMIN não encontrada no banco"));
            usuario.getRoles().add(roleAdmin);
        }
    }

    /* =========================================================
       CRUD COM DTO (usado pelo UsuarioController /api/usuarios)
       ========================================================= */

    // LISTAR
    public Page<UsuarioResponseDTO> listar(Pageable pageable) {
        return usuarioRepository.findAll(pageable)
                .map(this::toResponseDTO);
    }

    // BUSCAR POR ID (DTO)
    public UsuarioResponseDTO buscarPorIdDTO(Long id) {
        Usuario u = buscarPorId(id);
        return toResponseDTO(u);
    }

    // CRIAR (ADMIN criando usuário pela API)
    public UsuarioResponseDTO criar(UsuarioRequestDTO dto) {
        Usuario u = new Usuario();
        u.setNome(dto.getNome());
        u.setEmail(dto.getEmail());
        u.setProfissaoAtual(dto.getProfissaoAtual());
        u.setMetaProfissional(dto.getMetaProfissional());

        // senha obrigatória na criação
        u.setSenha(passwordEncoder.encode(dto.getSenha()));

        aplicarTipoRole(u, dto.getTipo()); // USER ou ADMIN

        Usuario salvo = usuarioRepository.save(u);
        return toResponseDTO(salvo);
    }

    // ATUALIZAR
    public UsuarioResponseDTO atualizar(Long id, UsuarioRequestDTO dto) {
        Usuario u = buscarPorId(id);

        u.setNome(dto.getNome());
        u.setEmail(dto.getEmail());
        u.setProfissaoAtual(dto.getProfissaoAtual());
        u.setMetaProfissional(dto.getMetaProfissional());

        // só troca senha se vier algo preenchido
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            u.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        aplicarTipoRole(u, dto.getTipo());

        Usuario salvo = usuarioRepository.save(u);
        return toResponseDTO(salvo);
    }

    // DELETAR
    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    /* =========================================================
       REGISTRO DA TELA /register (USADO PELO AuthController)
       ========================================================= */

    public Usuario registrarNovoUsuario(String nome,
                                        String email,
                                        String senha,
                                        String profissaoAtual,
                                        String metaProfissional,
                                        String tipo) {

        // valida se já existe email
        usuarioRepository.findByEmail(email).ifPresent(u -> {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com esse e-mail.");
        });

        Usuario u = new Usuario();
        u.setNome(nome);
        u.setEmail(email);
        u.setProfissaoAtual(profissaoAtual);
        u.setMetaProfissional(metaProfissional);

        // senha criptografada
        u.setSenha(passwordEncoder.encode(senha));

        // tipo vindo do formulário (USER ou ADMIN)
        aplicarTipoRole(u, tipo);

        return usuarioRepository.save(u);
    }

    // opção via DTO (se quiser usar em outro lugar)
    public Usuario registrarUsuarioPublico(UsuarioRequestDTO dto) {
        Usuario u = new Usuario();
        u.setNome(dto.getNome());
        u.setEmail(dto.getEmail());
        u.setProfissaoAtual(dto.getProfissaoAtual());
        u.setMetaProfissional(dto.getMetaProfissional());
        u.setSenha(passwordEncoder.encode(dto.getSenha()));

        aplicarTipoRole(u, "USER");
        return usuarioRepository.save(u);
    }
}
