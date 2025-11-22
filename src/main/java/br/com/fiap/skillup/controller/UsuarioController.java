package br.com.fiap.skillup.controller;

import br.com.fiap.skillup.dto.UsuarioRequestDTO;
import br.com.fiap.skillup.dto.UsuarioResponseDTO;
import br.com.fiap.skillup.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // ====== READ (listar) – já tinha ======
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UsuarioResponseDTO> listar(Pageable pageable) {
        return usuarioService.listar(pageable);
    }

    // ====== READ (buscar por id) – já tinha ======
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorIdDTO(id));
    }

    // ====== CREATE ======
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criar(@RequestBody @Valid UsuarioRequestDTO dto) {
        UsuarioResponseDTO created = usuarioService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ====== UPDATE ======
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid UsuarioRequestDTO dto
    ) {
        UsuarioResponseDTO updated = usuarioService.atualizar(id, dto);
        return ResponseEntity.ok(updated);
    }

    // ====== DELETE ======
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
