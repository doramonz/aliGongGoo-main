package com.doramonz.aligonggoo.service;

import com.doramonz.aligonggoo.dto.AliProductInfo;
import com.doramonz.aligonggoo.dto.CrawlerErrorDto;
import com.doramonz.aligonggoo.dto.URLParsingDto;
import com.doramonz.aligonggoo.mq.OutputQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MQService {

    private final OutputQueue outputQueue;

    public void processURL(URLParsingDto urlParsingDto) {
        outputQueue.sendURLParsingRequest(urlParsingDto);
    }

}
