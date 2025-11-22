package br.com.fiap.skillup.controller;

import br.com.fiap.skillup.model.Habilidade;
import br.com.fiap.skillup.repository.HabilidadeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/habilidades")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
public class AdminHabilidadeWebController {

    private final HabilidadeRepository habilidadeRepository;

    public AdminHabilidadeWebController(HabilidadeRepository habilidadeRepository) {
        this.habilidadeRepository = habilidadeRepository;
    }

    // CREATE / UPDATE
    @PostMapping
    public String salvar(@RequestParam(required = false) Long id,
                         @RequestParam String nome,
                         @RequestParam(required = false) String descricao,
                         RedirectAttributes ra) {

        Habilidade habilidade = (id != null)
                ? habilidadeRepository.findById(id).orElse(new Habilidade())
                : new Habilidade();

        habilidade.setNome(nome);
        habilidade.setDescricao(descricao);

        habilidadeRepository.save(habilidade);

        ra.addFlashAttribute(
                "mensagemHabilidade",
                id == null ? "Habilidade criada com sucesso!"
                        : "Habilidade atualizada com sucesso!"
        );
        // mantém form preenchido
        ra.addFlashAttribute("habilidade", habilidade);

        return "redirect:/app";
    }

    // BUSCAR POR ID
    @GetMapping("/buscar")
    public String buscar(@RequestParam Long id,
                         RedirectAttributes ra) {

        try {
            Habilidade habilidade = habilidadeRepository.findById(id)
                    .orElseThrow(EntityNotFoundException::new);

            // tabela "Buscar por ID"
            ra.addFlashAttribute("habilidadeBusca", habilidade);
            // form "Adicionar / Atualizar"
            ra.addFlashAttribute("habilidade", habilidade);

        } catch (EntityNotFoundException e) {
            ra.addFlashAttribute("erroHabilidadeBusca", "Habilidade não encontrada.");
        }

        return "redirect:/app";
    }

    // DELETE
    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id,
                          RedirectAttributes ra) {

        try {
            habilidadeRepository.deleteById(id);
            ra.addFlashAttribute("mensagemHabilidade", "Habilidade excluída com sucesso!");
        } catch (DataIntegrityViolationException e) {
            ra.addFlashAttribute("erroHabilidadeBusca",
                    "Não é possível excluir a habilidade, pois ela está vinculada a usuários.");
        } catch (Exception e) {
            ra.addFlashAttribute("erroHabilidadeBusca",
                    "Erro ao excluir habilidade: " + e.getMessage());
        }

        return "redirect:/app";
    }
}
