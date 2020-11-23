package com.microsoft.gbb.k8saudit.auditalerter.stream;

import com.microsoft.azure.spring.integration.core.AzureHeaders;
import com.microsoft.azure.spring.integration.core.api.reactor.Checkpointer;
import com.microsoft.gbb.k8saudit.auditalerter.service.LogFilterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.handler.annotation.Header;

@EnableBinding(Sink.class)
@Slf4j
public class KubeAuditSink {

    LogFilterService logFilterService;

    public KubeAuditSink(LogFilterService logFilterService) {
        this.logFilterService = logFilterService;
    }

    @StreamListener(Sink.INPUT)
    public void handleMessage(String message, @Header(AzureHeaders.CHECKPOINTER) Checkpointer checkpointer) {

        logFilterService.logNetworkPolicy(message);

        checkpointer.success()
                .doOnError((msg) -> {
                    log.error(String.valueOf(msg));
                })
                .subscribe();
    }
}
