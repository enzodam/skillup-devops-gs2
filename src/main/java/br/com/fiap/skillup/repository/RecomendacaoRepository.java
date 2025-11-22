package br.com.fiap.skillup.repository;

import br.com.fiap.skillup.model.Recomendacao;
import br.com.fiap.skillup.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecomendacaoRepository extends JpaRepository<Recomendacao, Long> {

    Page<Recomendacao> findByUsuario(Usuario usuario, Pageable pageable);

    void deleteByCursoId(Long cursoId);
}
