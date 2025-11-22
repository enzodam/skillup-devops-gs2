package br.com.fiap.skillup.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RecomendacaoResponseDTO {

    private Long id;
    private Long usuarioId;
    private Long cursoId;
    private String nomeCurso;
    private BigDecimal scoreCompatibilidade;
    private LocalDateTime dataGeracao;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }

    public String getNomeCurso() { return nomeCurso; }
    public void setNomeCurso(String nomeCurso) { this.nomeCurso = nomeCurso; }

    public BigDecimal getScoreCompatibilidade() { return scoreCompatibilidade; }
    public void setScoreCompatibilidade(BigDecimal scoreCompatibilidade) { this.scoreCompatibilidade = scoreCompatibilidade; }

    public LocalDateTime getDataGeracao() { return dataGeracao; }
    public void setDataGeracao(LocalDateTime dataGeracao) { this.dataGeracao = dataGeracao; }
}
