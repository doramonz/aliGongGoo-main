package com.doramonz.aligonggoo.mq;

import com.doramonz.aligonggoo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InputRabbitMQ implements InputQueue {

    private final ProductService productService;

    @RabbitListener(queues = "${rabbitmq.outputQueue.name}")
    public void receiveMessage(String message) {
        System.out.println("Received <" + message + ">");
    }

    @RabbitListener(queues = "${rabbitmq.errorQueue.name}")
    public void receiveError(String message) {
        System.out.println("Received <" + message + ">");
    }
}
