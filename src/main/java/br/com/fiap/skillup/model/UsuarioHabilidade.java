package br.com.fiap.skillup.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "usuarios_habilidades")
public class UsuarioHabilidade {

    @EmbeddedId
    private UsuarioHabilidadeId id;

    @ManyToOne
    @MapsId("usuarioId")
    private Usuario usuario;

    @ManyToOne
    @MapsId("habilidadeId")
    private Habilidade habilidade;

    @Min(1) @Max(5)
    private Integer nivelDominio;

    public UsuarioHabilidadeId getId() { return id; }
    public void setId(UsuarioHabilidadeId id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Habilidade getHabilidade() { return habilidade; }
    public void setHabilidade(Habilidade habilidade) { this.habilidade = habilidade; }

    public Integer getNivelDominio() { return nivelDominio; }
    public void setNivelDominio(Integer nivelDominio) { this.nivelDominio = nivelDominio; }
}
