package com.doramonz.aligonggoo.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class AWSInstanceService {

    private final Map<String, LocalDateTime> instanceMap = new HashMap<>();
    private final int waitTime = 1;
    private final List<String> restartedInstanceList = new CopyOnWriteArrayList<>();

    private final Thread instanceRefreshThread = new Thread(() -> {
        while (true) {
            try {
                Thread.sleep(10 * 1000);
                instanceMap.forEach((id, time) -> {
                    if (time.isBefore(LocalDateTime.now().minusMinutes(waitTime))) {
                        startInstance(id);
                        restartedInstanceList.add(id);
                    }
                });
                restartedInstanceList.forEach(instanceMap.keySet()::remove);
                restartedInstanceList.clear();
            } catch (InterruptedException e) {
                log.error("Instance Refresh Thread interrupted", e);
            }

        }
    });

    @PostConstruct
    public void init() {
        instanceRefreshThread.start();
    }

    private void startInstance(String id) {
        Runtime rt = Runtime.getRuntime();
        Process pc = null;
        try {
            log.info("Starting instance: " + id);
            pc = rt.exec("aws ec2 start-instances --instance-ids " + id);
            pc.waitFor();
        } catch (Exception e) {
            log.error("Error starting instance: " + id, e);
        } finally {
            if (pc != null) {
                log.info("Instance started: " + id);
                pc.destroy();
            }
        }
    }

    private void stopInstance(String id) {
        Runtime rt = Runtime.getRuntime();
        Process pc = null;
        try {
            log.info("Stopping instance: " + id);
            pc = rt.exec("aws ec2 stop-instances --instance-ids " + id);
            pc.waitFor();
        } catch (Exception e) {
            log.error("Error stopping instance: " + id, e);
        } finally {
            if (pc != null) {
                log.info("Instance stopped: " + id);
                pc.destroy();
            }
        }
    }

    public void restartInstance(String id) {
        stopInstance(id);
        instanceMap.put(id, LocalDateTime.now());
    }
}
