package br.com.fiap.skillup.controller;

import br.com.fiap.skillup.dto.RecomendacaoResponseDTO;
import br.com.fiap.skillup.model.Usuario;
import br.com.fiap.skillup.repository.UsuarioRepository;
import br.com.fiap.skillup.service.RecomendacaoService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@PreAuthorize("isAuthenticated()") // todo mundo aqui precisa estar logado
public class RecomendacaoWebController {

    private final RecomendacaoService recomendacaoService;
    private final UsuarioRepository usuarioRepository;

    public RecomendacaoWebController(RecomendacaoService recomendacaoService,
                                     UsuarioRepository usuarioRepository) {
        this.recomendacaoService = recomendacaoService;
        this.usuarioRepository = usuarioRepository;
    }

    /* ========================= ADMIN ========================= */

    @PostMapping("/admin/recomendacoes/gerar")
    @PreAuthorize("hasRole('ADMIN')")
    public String gerarAdmin(@RequestParam Long usuarioId,
                             RedirectAttributes ra) {
        try {
            List<RecomendacaoResponseDTO> recs =
                    recomendacaoService.gerarParaUsuario(usuarioId);

            ra.addFlashAttribute("recomendacoesUsuario", recs);
            ra.addFlashAttribute("usuarioRecomendacaoId", usuarioId);
            ra.addFlashAttribute(
                    "mensagemRecomendacao",
                    "Recomendações geradas com sucesso e enviadas para a fila de mensageria."
            );
        } catch (Exception e) {
            ra.addFlashAttribute(
                    "erroRecomendacao",
                    "Não foi possível conectar ao provedor de IA. Detalhes: " + e.getMessage()
            );
        }

        return "redirect:/app";
    }

    @GetMapping("/admin/recomendacoes/buscar")
    @PreAuthorize("hasRole('ADMIN')")
    public String buscarAdmin(@RequestParam Long usuarioId,
                              RedirectAttributes ra) {

        var page = recomendacaoService.listarPorUsuario(usuarioId, Pageable.unpaged());

        ra.addFlashAttribute("recomendacoesUsuario", page.getContent());
        ra.addFlashAttribute("usuarioRecomendacaoId", usuarioId);

        return "redirect:/app";
    }

    /* ========================= USER ========================= */

    @PostMapping("/recomendacoes/gerar")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public String gerarUsuarioLogado(Authentication auth,
                                     RedirectAttributes ra) {

        if (auth == null) {
            ra.addFlashAttribute("erroRecomendacao", "Você precisa estar logado para gerar recomendações.");
            return "redirect:/login";
        }

        Usuario usuario = usuarioRepository.findByEmail(auth.getName())
                .orElse(null);

        if (usuario == null) {
            ra.addFlashAttribute("erroRecomendacao", "Usuário não encontrado.");
            return "redirect:/login";
        }

        try {
            recomendacaoService.gerarParaUsuario(usuario.getId());
            ra.addFlashAttribute("mensagemRecomendacao", "Suas recomendações foram geradas com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute(
                    "erroRecomendacao",
                    "Erro ao gerar recomendações: " + e.getMessage()
            );
        }

        return "redirect:/app";
    }
}
