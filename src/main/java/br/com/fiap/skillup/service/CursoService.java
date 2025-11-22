package br.com.fiap.skillup.service;

import br.com.fiap.skillup.dto.CursoRequestDTO;
import br.com.fiap.skillup.dto.CursoResponseDTO;
import br.com.fiap.skillup.model.Curso;
import br.com.fiap.skillup.repository.CursoRepository;
import br.com.fiap.skillup.repository.RecomendacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;
    private final RecomendacaoRepository recomendacaoRepository;

    public CursoService(CursoRepository cursoRepository,
                        RecomendacaoRepository recomendacaoRepository) {
        this.cursoRepository = cursoRepository;
        this.recomendacaoRepository = recomendacaoRepository;
    }

    // LISTAR (API) – com filtro opcional por área (pra manter compatível com o controller)
    @Cacheable("cursos")
    public Page<CursoResponseDTO> listar(String area, Pageable pageable) {
        Page<Curso> page;
        if (area != null && !area.isBlank()) {
            page = cursoRepository.findByAreaContainingIgnoreCase(area, pageable);
        } else {
            page = cursoRepository.findAll(pageable);
        }
        return page.map(this::toDTO);
    }

    public CursoResponseDTO buscarPorId(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso nao encontrado"));
        return toDTO(curso);
    }

    @CacheEvict(value = "cursos", allEntries = true)
    public CursoResponseDTO criar(CursoRequestDTO dto) {
        Curso curso = new Curso();
        curso.setNome(dto.getNome());
        curso.setArea(dto.getArea());
        curso.setNivel(dto.getNivel());
        curso.setCargaHoraria(dto.getCargaHoraria());
        curso = cursoRepository.save(curso);
        return toDTO(curso);
    }

    @CacheEvict(value = "cursos", allEntries = true)
    public CursoResponseDTO atualizar(Long id, CursoRequestDTO dto) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso nao encontrado"));

        curso.setNome(dto.getNome());
        curso.setArea(dto.getArea());
        curso.setNivel(dto.getNivel());
        curso.setCargaHoraria(dto.getCargaHoraria());

        curso = cursoRepository.save(curso);
        return toDTO(curso);
    }

    // AQUI É O PULO DO GATO: apaga recomendações antes de apagar o curso
    @Transactional
    @CacheEvict(value = "cursos", allEntries = true)
    public void deletar(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new EntityNotFoundException("Curso nao encontrado");
        }

        // 1) Deleta todas as recomendações que apontam pra esse curso
        recomendacaoRepository.deleteByCursoId(id);

        // 2) Agora o banco deixa apagar o curso sem estourar FK
        cursoRepository.deleteById(id);
    }

    // Usado na tela admin pra preencher o formulário
    public Curso buscarEntidade(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso nao encontrado"));
    }

    private CursoResponseDTO toDTO(Curso c) {
        CursoResponseDTO dto = new CursoResponseDTO();
        dto.setId(c.getId());
        dto.setNome(c.getNome());
        dto.setArea(c.getArea());
        dto.setNivel(c.getNivel());
        dto.setCargaHoraria(c.getCargaHoraria());
        return dto;
    }
}
