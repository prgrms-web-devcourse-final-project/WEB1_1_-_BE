package com.somemore.domains.interestcenter.event.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.somemore.domains.recruitboard.event.CreateRecruitBoardEvent;
import com.somemore.global.common.event.DomainEventSubType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CreateRecruitBoardMessageConverter {

    private final ObjectMapper objectMapper;

    public CreateRecruitBoardEvent from(String message) {
        try {
            JsonNode rootNode = objectMapper.readTree(message);
            String eventType = rootNode.get("subType").asText();

            return switch (DomainEventSubType.from(eventType)) {
                case CREATE_RECRUIT_BOARD -> parseCreateRecruitBoardEvent(message);
                case VOLUNTEER_HOURS_SETTLE -> null;
            };
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalStateException();
        }
    }

    private CreateRecruitBoardEvent parseCreateRecruitBoardEvent(String message) throws JsonProcessingException {

        return objectMapper.readValue(message, CreateRecruitBoardEvent.class);
    }

}
