package br.com.fiap.skillup.web;

import br.com.fiap.skillup.model.Usuario;
import br.com.fiap.skillup.repository.UsuarioRepository;
import br.com.fiap.skillup.repository.CursoRepository;
import br.com.fiap.skillup.repository.HabilidadeRepository;
import br.com.fiap.skillup.repository.RecomendacaoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminUsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;
    private final HabilidadeRepository habilidadeRepository;
    private final RecomendacaoRepository recomendacaoRepository;

    public AdminUsuarioController(UsuarioRepository usuarioRepository,
                                  CursoRepository cursoRepository,
                                  HabilidadeRepository habilidadeRepository,
                                  RecomendacaoRepository recomendacaoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
        this.habilidadeRepository = habilidadeRepository;
        this.recomendacaoRepository = recomendacaoRepository;
    }

    /* ========= helper pra montar o painel ========= */
    private void preencherDashboard(Model model, boolean isAdmin) {
        model.addAttribute("isAdmin", isAdmin);

        model.addAttribute("usuariosCount", usuarioRepository.count());
        model.addAttribute("cursosCount", cursoRepository.count());
        model.addAttribute("habilidadesCount", habilidadeRepository.count());
        model.addAttribute("recomendacoesCount", recomendacaoRepository.count());

        model.addAttribute("usuariosList", usuarioRepository.findAll());
        model.addAttribute("cursosList", cursoRepository.findAll());
        model.addAttribute("habilidadesList", habilidadeRepository.findAll());
    }

    /* ========= tela principal do admin ========= */
    @GetMapping("/admin")
    public String homeAdmin(Model model) {
        preencherDashboard(model, true);
        return "home-admin";
    }

    /* ========= CRIAR / ATUALIZAR USUÁRIO ========= */
    @PostMapping("/admin/usuarios")
    public String salvarUsuario(Usuario usuario,
                                RedirectAttributes redirectAttributes,
                                Model model) {

        usuarioRepository.save(usuario);
        redirectAttributes.addFlashAttribute("mensagemUsuario",
                "Usuário salvo com sucesso!");

        return "redirect:/admin";
    }

    /* ========= BUSCAR POR ID (form + botão Editar) ========= */
    @GetMapping("/admin/usuarios/buscar")
    public String buscarUsuarioPorId(@RequestParam("id") Long id,
                                     Model model) {

        boolean isAdmin = true; // aqui é o painel de admin

        var usuarioBusca = usuarioRepository.findById(id).orElse(null);

        preencherDashboard(model, isAdmin);
        model.addAttribute("usuarioBusca", usuarioBusca);
        model.addAttribute("usuario", usuarioBusca); // preenche o form de cima

        return "home-admin";
    }

    /* ========= EXCLUIR USUÁRIO ========= */
    @PostMapping("/admin/usuarios/{id}/excluir")
    public String excluirUsuario(@PathVariable("id") Long id,
                                 RedirectAttributes redirectAttributes) {

        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("mensagemUsuario",
                    "Usuário excluído com sucesso!");
        } else {
            redirectAttributes.addFlashAttribute("erroUsuario",
                    "Usuário não encontrado para exclusão.");
        }

        return "redirect:/admin";
    }
}
