package br.com.fiap.skillup.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RecomendacaoProducer {

    public static final String QUEUE_RECOMENDACOES = "recomendacoes-geradas";

    private final RabbitTemplate rabbitTemplate;

    public RecomendacaoProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarMensagem(Long idRecomendacao) {
        rabbitTemplate.convertAndSend(QUEUE_RECOMENDACOES, idRecomendacao);
    }
}
