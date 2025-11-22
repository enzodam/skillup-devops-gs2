package br.com.fiap.skillup.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "recomendacoes")
public class Recomendacao {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @NotNull
    private Usuario usuario;

    @ManyToOne @NotNull
    private Curso curso;

    @DecimalMin("0.0") @DecimalMax("1.0")
    private BigDecimal scoreCompatibilidade;

    @NotNull
    private LocalDateTime dataGeracao;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }

    public BigDecimal getScoreCompatibilidade() { return scoreCompatibilidade; }
    public void setScoreCompatibilidade(BigDecimal scoreCompatibilidade) { this.scoreCompatibilidade = scoreCompatibilidade; }

    public LocalDateTime getDataGeracao() { return dataGeracao; }
    public void setDataGeracao(LocalDateTime dataGeracao) { this.dataGeracao = dataGeracao; }
}
