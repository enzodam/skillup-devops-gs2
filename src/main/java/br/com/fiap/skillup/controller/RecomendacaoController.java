package br.com.fiap.skillup.controller;

import br.com.fiap.skillup.dto.GerarRecomendacaoRequestDTO;
import br.com.fiap.skillup.dto.RecomendacaoResponseDTO;
import br.com.fiap.skillup.service.RecomendacaoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recomendacoes")
public class RecomendacaoController {

    private final RecomendacaoService recomendacaoService;

    public RecomendacaoController(RecomendacaoService recomendacaoService) {
        this.recomendacaoService = recomendacaoService;
    }

    @GetMapping("/usuario/{usuarioId}")
    public Page<RecomendacaoResponseDTO> listarPorUsuario(@PathVariable Long usuarioId, Pageable pageable) {
        return recomendacaoService.listarPorUsuario(usuarioId, pageable);
    }

    @PostMapping("/gerar")
    public ResponseEntity<List<RecomendacaoResponseDTO>> gerar(@RequestBody @Valid GerarRecomendacaoRequestDTO dto) {
        List<RecomendacaoResponseDTO> recs = recomendacaoService.gerarParaUsuario(dto.getUsuarioId());
        return ResponseEntity.ok(recs);
    }
}
