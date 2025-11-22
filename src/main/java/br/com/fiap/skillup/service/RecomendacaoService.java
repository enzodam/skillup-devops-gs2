package br.com.fiap.skillup.service;

import br.com.fiap.skillup.dto.RecomendacaoResponseDTO;
import br.com.fiap.skillup.model.Curso;
import br.com.fiap.skillup.model.Recomendacao;
import br.com.fiap.skillup.model.Usuario;
import br.com.fiap.skillup.messaging.RecomendacaoProducer;
import br.com.fiap.skillup.repository.CursoRepository;
import br.com.fiap.skillup.repository.RecomendacaoRepository;
import br.com.fiap.skillup.service.ai.RecomendacaoAIService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecomendacaoService {

    private final RecomendacaoRepository recomendacaoRepository;
    private final CursoRepository cursoRepository;
    private final UsuarioService usuarioService;
    private final RecomendacaoAIService aiService;
    private final RecomendacaoProducer producer;

    public RecomendacaoService(RecomendacaoRepository recomendacaoRepository,
                               CursoRepository cursoRepository,
                               UsuarioService usuarioService,
                               RecomendacaoAIService aiService,
                               RecomendacaoProducer producer) {
        this.recomendacaoRepository = recomendacaoRepository;
        this.cursoRepository = cursoRepository;
        this.usuarioService = usuarioService;
        this.aiService = aiService;
        this.producer = producer;
    }

    public Page<RecomendacaoResponseDTO> listarPorUsuario(Long usuarioId, Pageable pageable) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        return recomendacaoRepository.findByUsuario(usuario, pageable).map(this::toDTO);
    }

    public List<RecomendacaoResponseDTO> gerarParaUsuario(Long usuarioId) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        List<Curso> cursos = cursoRepository.findAll();
        if (cursos.isEmpty()) {
            throw new EntityNotFoundException("Nao ha cursos cadastrados para recomendar");
        }
        List<Recomendacao> recomendacoes = aiService.gerarRecomendacoes(usuario, cursos);
        recomendacoes = recomendacaoRepository.saveAll(recomendacoes);
        // Envia cada uma para fila assíncrona
        recomendacoes.forEach(r -> producer.enviarMensagem(r.getId()));
        return recomendacoes.stream().map(this::toDTO).toList();
    }

    private RecomendacaoResponseDTO toDTO(Recomendacao r) {
        RecomendacaoResponseDTO dto = new RecomendacaoResponseDTO();
        dto.setId(r.getId());
        dto.setUsuarioId(r.getUsuario().getId());
        dto.setCursoId(r.getCurso().getId());
        dto.setNomeCurso(r.getCurso().getNome());
        dto.setScoreCompatibilidade(r.getScoreCompatibilidade());
        dto.setDataGeracao(r.getDataGeracao());
        return dto;
    }
}
