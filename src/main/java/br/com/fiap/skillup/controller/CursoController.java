package br.com.fiap.skillup.controller;

import br.com.fiap.skillup.dto.CursoRequestDTO;
import br.com.fiap.skillup.dto.CursoResponseDTO;
import br.com.fiap.skillup.service.CursoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping
    public Page<CursoResponseDTO> listar(@RequestParam(required = false) String area, Pageable pageable) {
        return cursoService.listar(area, pageable);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CursoResponseDTO> criar(@RequestBody @Valid CursoRequestDTO dto) {
        CursoResponseDTO created = cursoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CursoResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid CursoRequestDTO dto) {
        return ResponseEntity.ok(cursoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        cursoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
