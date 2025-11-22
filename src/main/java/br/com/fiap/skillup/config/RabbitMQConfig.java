package br.com.fiap.skillup.config;

import br.com.fiap.skillup.messaging.RecomendacaoProducer;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do RabbitMQ:
 * - Habilita o processamento de @RabbitListener
 * - Declara a fila "recomendacoes-geradas"
 */
@Configuration
@EnableRabbit
public class RabbitMQConfig {

    @Bean
    public Queue recomendacoesQueue() {
        // true = fila durável (sobrevive a restart do broker)
        return new Queue(RecomendacaoProducer.QUEUE_RECOMENDACOES, true);
    }
}
