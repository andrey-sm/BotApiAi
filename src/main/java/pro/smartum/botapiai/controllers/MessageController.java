package pro.smartum.botapiai.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.smartum.botapiai.dto.MessageDto;
import pro.smartum.botapiai.services.MessageService;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;


    @PostMapping("/{id}/read")
    public ResponseEntity<Object> markAsRead(@PathVariable("id") Long messageId) {
        return new ResponseEntity<>(messageService.markAsRead(messageId), HttpStatus.OK);
    }
}
