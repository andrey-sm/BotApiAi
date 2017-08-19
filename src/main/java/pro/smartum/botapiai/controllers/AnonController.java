package pro.smartum.botapiai.controllers;


import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.smartum.botapiai.dto.rq.MessageRq;
import pro.smartum.botapiai.services.MessageService;
import pro.smartum.botapiai.util.Logger;

@RestController
@RequestMapping("/anon")
public class AnonController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/message")
    public ResponseEntity<Object> login(@RequestBody MessageRq request) {
        return new ResponseEntity<>(messageService.replyToMessage(request), HttpStatus.OK);
    }
}
