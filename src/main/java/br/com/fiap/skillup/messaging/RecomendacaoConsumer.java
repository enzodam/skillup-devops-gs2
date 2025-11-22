package br.com.fiap.skillup.messaging;

import br.com.fiap.skillup.model.Recomendacao;
import br.com.fiap.skillup.repository.RecomendacaoRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RecomendacaoConsumer {

    private final RecomendacaoRepository recomendacaoRepository;

    public RecomendacaoConsumer(RecomendacaoRepository recomendacaoRepository) {
        this.recomendacaoRepository = recomendacaoRepository;
    }

    @RabbitListener(queues = RecomendacaoProducer.QUEUE_RECOMENDACOES)
    public void processarRecomendacao(Long idRecomendacao) {
        // Exemplo simples: apenas carrega do banco e faz um log (poderia enviar e-mail, etc.)
        Recomendacao rec = recomendacaoRepository.findById(idRecomendacao).orElse(null);
        if (rec != null) {
            System.out.println("Recomendação processada de forma assíncrona: " + rec.getId());
        }
    }
}
