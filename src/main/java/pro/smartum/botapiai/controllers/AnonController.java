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

@RestController
@RequestMapping("/anon")
public class AnonController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/message")
    public ResponseEntity<Object> message(@RequestBody IncomingMessageRq request) {
        messageService.handleMessage(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
