package pro.smartum.botapiai.controllers;


import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.smartum.botapiai.annotations.swagger.SwaggerResponseStatus;
import pro.smartum.botapiai.dto.MessageDto;
import pro.smartum.botapiai.services.MessageService;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @SwaggerResponseStatus
    @ApiOperation(value = "Mark message as read", response = MessageDto.class)
    @PostMapping("/{id}/read")
    public ResponseEntity<Object> markAsRead(@PathVariable("id") Long messageId) {
        return new ResponseEntity<>(messageService.markAsRead(messageId), HttpStatus.OK);
    }
}
