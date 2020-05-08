package com.wsep202.TradingSystem.web.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wsep202.TradingSystem.dto.NotificationDto;
import com.wsep202.TradingSystem.service.user_service.NotificationService;
import com.wsep202.TradingSystem.web.controllers.api.PublicApiPaths;
import com.wsep202.TradingSystem.web.controllers.shakeHandler.ConnectionStart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    private final ObjectMapper objectMapper;

    @MessageMapping("/connect-notification-system")
    @SendToUser(PublicApiPaths.CLIENT_DESTINATIONS_PREFIXED + PublicApiPaths.NOTIFICATION_PATH)
    public NotificationDto connectNotificationSystem(String connectionStartJsonNode, Principal principal) throws JsonProcessingException {
        ConnectionStart connectionStart = objectMapper.readValue(connectionStartJsonNode, ConnectionStart.class);
        log.info(String.format("The username : %s with principal name: %s,connect to notification system", connectionStart.getUsername(), principal.getName()));
        notificationService.addUser(connectionStart.getUsername(), connectionStart.getUuid(), principal.getName());
        return NotificationDto.builder()
        .content("You are connecting to the notification system")
       .principal(principal.getName())
        .build();
    }
}
