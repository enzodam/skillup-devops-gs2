package br.com.fiap.skillup.dto;

import jakarta.validation.constraints.NotNull;

public class GerarRecomendacaoRequestDTO {

    @NotNull
    private Long usuarioId;

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
}
