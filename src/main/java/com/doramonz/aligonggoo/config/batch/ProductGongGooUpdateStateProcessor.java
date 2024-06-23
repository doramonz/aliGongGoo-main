package com.doramonz.aligonggoo.config.batch;

import com.doramonz.aligonggoo.util.AliProductUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;

@RequiredArgsConstructor
public class ProductGongGooUpdateStateProcessor implements ItemProcessor<ProductGongGooBatchDao, Integer> {

    private final AliProductUtil aliProductUtil;

    @Override
    public Integer process(ProductGongGooBatchDao item) throws Exception {
        try {
            aliProductUtil.getProductInfo(item.getUrl());
        } catch (Exception e) {
            return item.getId();
        }
        return null;
    }
}
