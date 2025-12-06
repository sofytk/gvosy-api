package com.sonchasapps.controller;

import com.sonchasapps.dto.MessageDTO;
import com.sonchasapps.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/{assistantId}")
    public List<MessageDTO> getMessages(@PathVariable UUID assistantId) {
        return messageService.getMessages(assistantId);
    }
}
