package br.com.fiap.skillup.controller;

import br.com.fiap.skillup.dto.CursoRequestDTO;
import br.com.fiap.skillup.dto.CursoResponseDTO;
import br.com.fiap.skillup.model.Curso;
import br.com.fiap.skillup.service.CursoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/cursos")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCursoWebController {

    private final CursoService cursoService;

    public AdminCursoWebController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    // SALVAR / ATUALIZAR (form "Adicionar / Atualizar")
    @PostMapping
    public String salvarOuAtualizar(
            @RequestParam(required = false) Long id,
            @RequestParam String nome,
            @RequestParam String area,
            @RequestParam String nivel,
            @RequestParam(required = false) String cargaHoraria,
            RedirectAttributes redirectAttributes
    ) {

        try {
            Integer carga = null;
            if (cargaHoraria != null && !cargaHoraria.isBlank()) {
                long value = Long.parseLong(cargaHoraria.trim());
                if (value > Integer.MAX_VALUE) {
                    redirectAttributes.addFlashAttribute("erroCurso",
                            "Carga horária muito alta. Informe um valor menor.");
                    return "redirect:/app";
                }
                carga = (int) value;
            }

            CursoRequestDTO dto = new CursoRequestDTO();
            dto.setNome(nome);
            dto.setArea(area);
            dto.setNivel(nivel);
            dto.setCargaHoraria(carga);

            CursoResponseDTO salvo;

            if (id == null) {
                salvo = cursoService.criar(dto);
                redirectAttributes.addFlashAttribute("mensagemCurso",
                        "Curso cadastrado com sucesso!");
            } else {
                salvo = cursoService.atualizar(id, dto);
                redirectAttributes.addFlashAttribute("mensagemCurso",
                        "Curso atualizado com sucesso!");
            }

            // mantém formulário preenchido
            redirectAttributes.addFlashAttribute("curso", salvo);

        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("erroCurso",
                    "Carga horária inválida. Informe apenas números.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erroCurso",
                    "Erro ao salvar curso: " + e.getMessage());
        }

        return "redirect:/app";
    }

    // BUSCAR POR ID (botão Buscar e botões Editar)
    @GetMapping("/buscar")
    public String buscar(@RequestParam Long id,
                         RedirectAttributes redirectAttributes) {

        try {
            Curso curso = cursoService.buscarEntidade(id);

            CursoResponseDTO dto = new CursoResponseDTO();
            dto.setId(curso.getId());
            dto.setNome(curso.getNome());
            dto.setArea(curso.getArea());
            dto.setNivel(curso.getNivel());
            dto.setCargaHoraria(curso.getCargaHoraria());

            redirectAttributes.addFlashAttribute("cursoBusca", dto);
            redirectAttributes.addFlashAttribute("curso", dto);

        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("erroCurso",
                    "Curso não encontrado para o ID informado.");
        }

        return "redirect:/app";
    }

    // EXCLUIR CURSO
    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id,
                          RedirectAttributes redirectAttributes) {

        try {
            cursoService.deletar(id);
            redirectAttributes.addFlashAttribute("mensagemCurso",
                    "Curso excluído com sucesso!");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("erroCurso",
                    "Curso não encontrado para o ID informado.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erroCurso",
                    "Erro ao excluir curso: " + e.getMessage());
        }

        return "redirect:/app";
    }
}
