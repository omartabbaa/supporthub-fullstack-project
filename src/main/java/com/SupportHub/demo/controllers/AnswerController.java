package com.SupportHub.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.SupportHub.demo.dtos.InputDTOs.AnswerInputDTO;
import com.SupportHub.demo.dtos.OutputDTOs.AnswerOutputDTO;
import com.SupportHub.demo.services.AnswerService;
import com.SupportHub.demo.services.QuestionService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {

    private final AnswerService answerService;

    @Autowired
    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    // GET /api/answers/{answerId}
    @GetMapping("/{answerId}")
    public ResponseEntity<AnswerOutputDTO> getAnswerById(@PathVariable Long answerId) {
        Optional<AnswerOutputDTO> answer = answerService.findAnswerById(answerId);
        return answer.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // GET /api/answers
    @GetMapping
    public ResponseEntity<List<AnswerOutputDTO>> getAllAnswers() {
        List<AnswerOutputDTO> answers = answerService.findAllAnswers();
        return ResponseEntity.ok(answers);
    }

    // POST /api/answers
    @PostMapping
    public ResponseEntity<AnswerOutputDTO> createAnswer(@RequestBody AnswerInputDTO answerInputDTO) {
        AnswerOutputDTO createdAnswer = answerService.createAnswer(answerInputDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAnswer);
    }

    // PUT /api/answers/{answerId}
    @PutMapping("/{answerId}")
    public ResponseEntity<AnswerOutputDTO> updateAnswer(@PathVariable Long answerId, @RequestBody AnswerInputDTO answerInputDTO) {
        try {
            AnswerOutputDTO updatedAnswer = answerService.updateAnswer(answerId, answerInputDTO);
            return ResponseEntity.ok(updatedAnswer);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // DELETE /api/answers/{answerId}
    @DeleteMapping("/{answerId}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long answerId) {
        try {
            answerService.deleteAnswerById(answerId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
