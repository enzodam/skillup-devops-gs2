package br.com.fiap.skillup.controller;

import br.com.fiap.skillup.model.Usuario;
import br.com.fiap.skillup.repository.CursoRepository;
import br.com.fiap.skillup.repository.HabilidadeRepository;
import br.com.fiap.skillup.repository.RecomendacaoRepository;
import br.com.fiap.skillup.repository.UsuarioRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app")
public class AppController {

    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;
    private final HabilidadeRepository habilidadeRepository;
    private final RecomendacaoRepository recomendacaoRepository;

    public AppController(
            UsuarioRepository usuarioRepository,
            CursoRepository cursoRepository,
            HabilidadeRepository habilidadeRepository,
            RecomendacaoRepository recomendacaoRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
        this.habilidadeRepository = habilidadeRepository;
        this.recomendacaoRepository = recomendacaoRepository;
    }

    @GetMapping
    public String home(Authentication authentication, Model model) {

        if (authentication == null) return "redirect:/login";

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("isAdmin", isAdmin);

        // Usuário logado
        Usuario usuarioLogado = usuarioRepository.findByEmail(authentication.getName())
                .orElse(null);

        if (usuarioLogado != null)
            model.addAttribute("username", usuarioLogado.getNome());

        // Admin → vê tudo
        if (isAdmin) preencherDadosAdmin(model);

            // User → vê apenas sua área
        else preencherDadosUser(model, usuarioLogado);

        return "home-admin";
    }

    /* ===========================================================
       =======================   ADMIN   ===========================
       =========================================================== */
    private void preencherDadosAdmin(Model model) {

        // KPIs do admin
        model.addAttribute("usuariosCount", usuarioRepository.count());
        model.addAttribute("cursosCount", cursoRepository.count());
        model.addAttribute("habilidadesCount", habilidadeRepository.count());
        model.addAttribute("recomendacoesCount", recomendacaoRepository.count());

        // Listas completas (admin pode ver tudo)
        model.addAttribute("usuariosList", usuarioRepository.findAll());
        model.addAttribute("cursosList", cursoRepository.findAll());
        model.addAttribute("habilidadesList", habilidadeRepository.findAll());

        // Tabela de recomendações no admin
        model.addAttribute(
                "recomendacoesUsuario",
                recomendacaoRepository.findAll(Pageable.unpaged()).getContent()
        );
    }

    /* ===========================================================
       ========================   USER   ===========================
       =========================================================== */
    private void preencherDadosUser(Model model, Usuario usuarioLogado) {

        model.addAttribute("cursosCount", cursoRepository.count());
        model.addAttribute("habilidadesCount", habilidadeRepository.count());

        if (usuarioLogado != null) {
            var page = recomendacaoRepository
                    .findByUsuario(usuarioLogado, Pageable.unpaged());

            model.addAttribute("recomendacoesCount", page.getTotalElements());
            model.addAttribute("recomendacoesUsuario", page.getContent());
        }

        // Usuário também pode cadastrar habilidades e cursos
        model.addAttribute("cursosList", cursoRepository.findAll());
        model.addAttribute("habilidadesList", habilidadeRepository.findAll());
    }
}
