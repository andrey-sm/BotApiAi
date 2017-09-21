package pro.smartum.botapiai.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.smartum.botapiai.dto.rq.ReplyRq;
import pro.smartum.botapiai.services.ConversationService;

@RestController
@RequestMapping("/conversations")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @GetMapping
    public ResponseEntity<Object> getConversations() {
        return new ResponseEntity<>(conversationService.getConversations(), HttpStatus.OK);
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<Object> getConversation(@PathVariable("id") Long conversationId,
                                                  @RequestParam(value = "number", defaultValue = "0") Integer number,
                                                  @RequestParam(value = "count", defaultValue = "10") Integer count) {
        return new ResponseEntity<>(conversationService.getConversationHistory(number, count, conversationId), HttpStatus.OK);
    }

    @PostMapping("/{id}/reply")
    public ResponseEntity<Object> replyToConversation(@PathVariable("id") Long conversationId, @RequestBody ReplyRq rq) {
        return new ResponseEntity<>(conversationService.replyToConversation(conversationId, rq), HttpStatus.OK);
    }
}
