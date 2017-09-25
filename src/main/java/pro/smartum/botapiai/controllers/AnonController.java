package pro.smartum.botapiai.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.smartum.botapiai.dto.rq.IncomingMessageRq;
import pro.smartum.botapiai.services.MessageService;

import java.io.IOException;

@RestController
@RequestMapping("/anon")
public class AnonController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/message")
    public ResponseEntity<Object> message(@RequestBody IncomingMessageRq request) {
        return new ResponseEntity<>(messageService.handleMessage(request), HttpStatus.OK);
    }

    @PostMapping("/eldar")
    public ResponseEntity<Object> eldar() throws IOException {
        messageService.sendPush();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
