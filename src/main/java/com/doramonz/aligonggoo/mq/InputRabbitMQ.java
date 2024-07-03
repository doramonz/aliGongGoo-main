package com.doramonz.aligonggoo.mq;

import com.doramonz.aligonggoo.dto.AliProductInfo;
import com.doramonz.aligonggoo.dto.CrawlerErrorDto;
import com.doramonz.aligonggoo.service.AWSInstanceService;
import com.doramonz.aligonggoo.service.MQService;
import com.doramonz.aligonggoo.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class InputRabbitMQ implements InputQueue {

    private final ProductService productService;
    private final AWSInstanceService awsInstanceController;

    @Transactional
    @RabbitListener(queues = "${rabbitmq.outputQueue.name}")
    public void receiveMessage(AliProductInfo aliProductInfo) {
        productService.uploadProduct(aliProductInfo);
    }

    @Transactional
    @RabbitListener(queues = "${rabbitmq.errorQueue.name}")
    public void receiveError(CrawlerErrorDto errorDto) {
        if (errorDto.getErrorType().equals(CrawlerErrorDto.ErrorType.LOG)) {
            log.error("Error occurred while processing URL: {}", errorDto.getUrl());
            log.error("Error message: {}", errorDto.getMessage());
            log.error("Error stack trace: {}", errorDto.getStackTrace());
            log.error("Error time: {}", errorDto.getTime());
        } else if (errorDto.getErrorType().equals(CrawlerErrorDto.ErrorType.RESTART)) {
            log.info("Restarted instance: {}", errorDto.getInstanceId());
            awsInstanceController.restartInstance(errorDto.getInstanceId());
        } else if (errorDto.getErrorType().equals(CrawlerErrorDto.ErrorType.NOT_AVAILABLE)) {
            log.info("Removed URL from available list: {}", errorDto.getUrl());
            productService.closeGongGoo(errorDto.getUrl());
        }
    }
}
