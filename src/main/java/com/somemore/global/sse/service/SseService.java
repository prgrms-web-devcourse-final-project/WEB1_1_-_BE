package com.somemore.global.sse.service;

import com.somemore.global.sse.domain.SseEvent;
import com.somemore.global.sse.domain.SseInitMessage;
import com.somemore.global.sse.sender.SseSender;
import com.somemore.global.sse.subscriber.SseSubscriptionManager;
import com.somemore.global.sse.usecase.SseUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseService implements SseUseCase {

    private final SseSubscriptionManager subscriptionManager;
    private final SseSender sender;

    @Override
    public SseEmitter subscribe(UUID userId) {
        SseEmitter sseEmitter = subscriptionManager.subscribe(userId);
        sendInitMessage(userId);

        return sseEmitter;
    }

    @Override
    public <T> void send(SseEvent<T> event) {
        sender.send(event);
    }

    private void sendInitMessage(UUID userId) {
        sender.send(SseInitMessage.createInitEvent(userId));
    }
}
