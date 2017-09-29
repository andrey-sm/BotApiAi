package pro.smartum.botapiai.controllers;


import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.smartum.botapiai.annotations.swagger.SwaggerResponseStatus;
import pro.smartum.botapiai.dto.ConversationDto;
import pro.smartum.botapiai.dto.MessageDto;
import pro.smartum.botapiai.dto.rq.ReplyRq;
import pro.smartum.botapiai.dto.rs.ConversationsRs;
import pro.smartum.botapiai.dto.rs.MessagesRs;
import pro.smartum.botapiai.services.ConversationService;

@RestController
@RequestMapping("/conversations")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @SwaggerResponseStatus
    @ApiOperation(value = "Get list of conversations", response = ConversationsRs.class)
    @GetMapping
    public ResponseEntity<Object> getConversations() {
        return new ResponseEntity<>(conversationService.getConversations(), HttpStatus.OK);
    }

    @SwaggerResponseStatus
    @ApiOperation(value = "Get conversation", response = ConversationDto.class)
    @GetMapping("/{id}")
    public ResponseEntity<Object> getConversation(@PathVariable("id") Long conversationId) {
        return new ResponseEntity<>(conversationService.getConversation(conversationId), HttpStatus.OK);
    }

    @SwaggerResponseStatus
    @ApiOperation(value = "Get conversation history", response = MessagesRs.class)
    @GetMapping("/{id}/history")
    public ResponseEntity<Object> getConversationHistory(@PathVariable("id") Long conversationId,
                                                         @RequestParam(value = "number", defaultValue = "0") Integer number,
                                                         @RequestParam(value = "count", defaultValue = "10") Integer count) {
        return new ResponseEntity<>(conversationService.getConversationHistory(number, count, conversationId), HttpStatus.OK);
    }

    @SwaggerResponseStatus
    @ApiOperation(value = "Reply to conversation", response = MessageDto.class)
    @PostMapping("/{id}/reply")
    public ResponseEntity<Object> replyToConversation(@PathVariable("id") Long conversationId, @RequestBody ReplyRq rq) {
        return new ResponseEntity<>(conversationService.replyToConversation(conversationId, rq), HttpStatus.OK);
    }
}
