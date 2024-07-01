package com.doramonz.aligonggoo.mq;

import com.doramonz.aligonggoo.dto.URLParsingDto;

public interface OutputQueue {
    void sendURLParsingRequest(URLParsingDto urlParsingDto);
}
