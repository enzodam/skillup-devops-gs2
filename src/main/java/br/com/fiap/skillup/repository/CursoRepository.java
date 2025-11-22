package br.com.fiap.skillup.repository;

import br.com.fiap.skillup.model.Curso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    Page<Curso> findByAreaContainingIgnoreCase(String area, Pageable pageable);
}
