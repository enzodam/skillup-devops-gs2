package br.com.fiap.skillup.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UsuarioHabilidadeId implements Serializable {

    private Long usuarioId;
    private Long habilidadeId;

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public Long getHabilidadeId() { return habilidadeId; }
    public void setHabilidadeId(Long habilidadeId) { this.habilidadeId = habilidadeId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioHabilidadeId that)) return false;
        return Objects.equals(usuarioId, that.usuarioId) && Objects.equals(habilidadeId, that.habilidadeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuarioId, habilidadeId);
    }
}
