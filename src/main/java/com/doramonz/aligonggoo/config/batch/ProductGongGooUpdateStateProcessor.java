package com.doramonz.aligonggoo.config.batch;

import com.doramonz.aligonggoo.util.AliProductUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class ProductGongGooUpdateStateProcessor implements ItemProcessor<ProductGongGooBatchDao, Integer> {

    private final AliProductUtil aliProductUtil;

    @Override
    public Integer process(ProductGongGooBatchDao item) throws Exception {
        if (item.getCreated().plusDays(1).isBefore(LocalDateTime.now())) {
            log.debug("ProductGongGooBatchDao is expired. id: {}", item.getId());
            return item.getId();
        }

        try {
            aliProductUtil.getProductInfo(item.getUrl());
        } catch (Exception e) {
            log.debug("Failed to get product info. id: {}", item.getId(), e);
            return item.getId();
        }
        return null;
    }
}
