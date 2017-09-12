package pro.smartum.botapiai.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.smartum.botapiai.dto.rq.IncomingMessageRq;
import pro.smartum.botapiai.dto.rq.ReplyRq;
import pro.smartum.botapiai.services.ConversationService;
import pro.smartum.botapiai.services.MessageService;

@RestController
@RequestMapping("/anon")
public class AnonController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private ConversationService conversationService;

    @PostMapping("/message")
    public ResponseEntity<Object> message(@RequestBody IncomingMessageRq request) {
        messageService.handleMessage(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/conversations")
    public ResponseEntity<Object> getConversations() {
        return new ResponseEntity<>(conversationService.getConversations(), HttpStatus.OK);
    }

    @GetMapping("/conversations/{id}/history")
    public ResponseEntity<Object> getConversation(@PathVariable("id") Long conversationId) {
        return new ResponseEntity<>(conversationService.getConversationHistory(conversationId), HttpStatus.OK);
    }

    @PostMapping("/conversations/{id}/reply")
    public ResponseEntity<Object> replyToConversation(@PathVariable("id") Long conversationId, @RequestBody ReplyRq rq) {
        return new ResponseEntity<>(conversationService.replyToConversation(conversationId, rq), HttpStatus.OK);
    }
}
