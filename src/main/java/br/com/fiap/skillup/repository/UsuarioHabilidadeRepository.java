package br.com.fiap.skillup.repository;

import br.com.fiap.skillup.model.UsuarioHabilidade;
import br.com.fiap.skillup.model.UsuarioHabilidadeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioHabilidadeRepository extends JpaRepository<UsuarioHabilidade, UsuarioHabilidadeId> {}
