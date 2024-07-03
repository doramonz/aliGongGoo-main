package com.doramonz.aligonggoo.mq;

import com.doramonz.aligonggoo.dto.URLParsingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutputRabbitMQ implements OutputQueue {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.inputQueue.name}")
    private String outputQueueName;

    @Override
    public void sendURLParsingRequest(URLParsingDto urlParsingDto) {
        rabbitTemplate.convertAndSend(outputQueueName, urlParsingDto);
    }
}
