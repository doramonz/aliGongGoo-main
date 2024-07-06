package com.doramonz.aligonggoo.config.batch;

import com.doramonz.aligonggoo.dto.URLParsingDto;
import com.doramonz.aligonggoo.service.MQService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class ProductGongGooUpdateStateProcessor implements ItemProcessor<ProductGongGooBatchDao, Integer> {

    private final MQService mqService;

    @Override
    public Integer process(ProductGongGooBatchDao item) throws Exception {
        if (item.getCreated().plusDays(1).isBefore(LocalDateTime.now())) {
            log.debug("ProductGongGooBatchDao is expired. id: {}", item.getId());
            return item.getId();
        }

        mqService.processURL(new URLParsingDto(item.getUrl()));
        return null;
    }
}
