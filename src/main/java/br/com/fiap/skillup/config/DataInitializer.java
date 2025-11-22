package br.com.fiap.skillup.config;

import br.com.fiap.skillup.model.Role;
import br.com.fiap.skillup.model.Usuario;
import br.com.fiap.skillup.repository.RoleRepository;
import br.com.fiap.skillup.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                           UsuarioRepository usuarioRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // 1) Garante ROLE_USER e ROLE_ADMIN
        Role roleUser = createRoleIfNotExists("ROLE_USER");
        Role roleAdmin = createRoleIfNotExists("ROLE_ADMIN");

        // 2) Garante um usuário ADMIN padrão
        String adminEmail = "admin@skillup.com";

        if (usuarioRepository.findByEmail(adminEmail).isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNome("Admin SkillUp");
            admin.setEmail(adminEmail);
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setProfissaoAtual("Admin");
            admin.setMetaProfissional("Administrar plataforma SkillUp");

            Set<Role> roles = new HashSet<>();
            roles.add(roleUser);
            roles.add(roleAdmin);
            admin.setRoles(roles);

            usuarioRepository.save(admin);
            System.out.println(">>> Usuário ADMIN criado: " + adminEmail + " / senha: admin123");
        } else {
            System.out.println(">>> Usuário ADMIN já existe, não será recriado.");
        }
    }

    private Role createRoleIfNotExists(String nome) {
        return roleRepository.findByNome(nome)
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setNome(nome);
                    return roleRepository.save(r);
                });
    }
}
