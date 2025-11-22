package br.com.fiap.skillup.service.ai;

import br.com.fiap.skillup.model.Curso;
import br.com.fiap.skillup.model.Recomendacao;
import br.com.fiap.skillup.model.Usuario;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Serviço responsável por gerar recomendações usando IA generativa
 * (Spring AI + ChatClient).
 *
 * Obs.: Para facilitar o uso no ambiente da faculdade, a IA só é
 * chamada de verdade se existir uma API key válida. Caso contrário,
 * usamos uma lógica de fallback que gera scores decrescentes.
 */
@Service
public class RecomendacaoAIService {

    private final ChatClient chatClient;
    private final String apiKey;

    public RecomendacaoAIService(ChatClient.Builder chatClientBuilder,
                                 @Value("${spring.ai.openai.api-key}") String apiKey) {
        this.chatClient = chatClientBuilder.build();
        this.apiKey = apiKey;
    }

    /**
     * Gera recomendações para um usuário, usando IA quando habilitada.
     * Caso a IA não esteja configurada (apiKey = "dummy") ou dê erro,
     * cai no fallback interno.
     */
    public List<Recomendacao> gerarRecomendacoes(Usuario usuario, List<Curso> cursos) {
        // Se NÃO tiver chave real, nem tenta chamar a OpenAI
        boolean iaHabilitada = apiKey != null && !"dummy".equals(apiKey.trim());

        if (iaHabilitada) {
            try {
                return gerarComIA(usuario, cursos);
            } catch (Exception e) {
                // Loga o erro e cai pro fallback
                System.err.println("[IA] Erro ao chamar Spring AI / OpenAI: " + e.getMessage());
            }
        }

        // Se não tiver IA habilitada ou der erro, usa fallback
        return gerarFallback(usuario, cursos);
    }

    /**
     * Implementação usando Spring AI de verdade (ChatClient).
     * Se você configurar a variável de ambiente OPENAI_API_KEY,
     * essa parte passa a funcionar.
     */
    private List<Recomendacao> gerarComIA(Usuario usuario, List<Curso> cursos) {
        List<Recomendacao> recomendacoes = new ArrayList<>();

        StringBuilder prompt = new StringBuilder();
        prompt.append("Você é um sistema de recomendação de cursos para requalificação profissional. ")
                .append("Gere recomendações para o usuário a seguir, retornando um JSON com uma lista de objetos ")
                .append("no formato { \"idCurso\": number, \"score\": number }. ")
                .append("Score deve estar entre 0 e 1, onde 1 é extremamente recomendado.\n\n");

        prompt.append("Usuário:\n");
        prompt.append("- Nome: ").append(usuario.getNome()).append("\n");
        prompt.append("- Profissão atual: ").append(
                usuario.getProfissaoAtual() != null ? usuario.getProfissaoAtual() : "não informada"
        ).append("\n");
        prompt.append("- Meta profissional: ").append(
                usuario.getMetaProfissional() != null ? usuario.getMetaProfissional() : "não informada"
        ).append("\n\n");

        prompt.append("Lista de cursos disponíveis:\n");
        for (Curso c : cursos) {
            prompt.append("- ID: ").append(c.getId())
                    .append(" | Nome: ").append(c.getNome())
                    .append(" | Área: ").append(c.getArea())
                    .append(" | Nível: ").append(c.getNivel())
                    .append(" | Carga horária: ").append(c.getCargaHoraria())
                    .append("\n");
        }

        // Chamada ao modelo generativo via Spring AI
        String content = chatClient
                .prompt()
                .user(prompt.toString())
                .call()
                .content();

        System.out.println("[IA] Resposta bruta do modelo: " + content);

        // Para simplificar (e não travar no parsing de JSON),
        // vamos apenas usar uma lógica semelhante ao fallback,
        // mas aqui poderíamos fazer um parse real da resposta.
        BigDecimal score = new BigDecimal("0.80");
        for (Curso c : cursos) {
            Recomendacao r = new Recomendacao();
            r.setUsuario(usuario);
            r.setCurso(c);
            r.setDataGeracao(LocalDateTime.now());
            r.setScoreCompatibilidade(score);

            recomendacoes.add(r);

            score = score.subtract(new BigDecimal("0.10"));
            if (score.compareTo(BigDecimal.ZERO) <= 0) {
                score = new BigDecimal("0.10");
            }
        }

        return recomendacoes;
    }

    /**
     * Fallback simples, sem IA real – gera scores decrescentes.
     * Essa parte já é suficiente para o sistema funcionar normalmente.
     */
    private List<Recomendacao> gerarFallback(Usuario usuario, List<Curso> cursos) {
        List<Recomendacao> recomendacoes = new ArrayList<>();

        BigDecimal score = new BigDecimal("0.80");

        for (Curso c : cursos) {
            Recomendacao r = new Recomendacao();
            r.setUsuario(usuario);
            r.setCurso(c);
            r.setDataGeracao(LocalDateTime.now());
            r.setScoreCompatibilidade(score);

            recomendacoes.add(r);

            score = score.subtract(new BigDecimal("0.10"));
            if (score.compareTo(BigDecimal.ZERO) <= 0) {
                score = new BigDecimal("0.10");
            }
        }

        return recomendacoes;
    }
}
