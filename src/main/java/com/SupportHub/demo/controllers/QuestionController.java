package com.SupportHub.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.SupportHub.demo.dtos.InputDTOs.QuestionInputDTO;
import com.SupportHub.demo.dtos.OutputDTOs.QuestionOutputDTO;
import com.SupportHub.demo.services.question.QuestionService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    // GET /api/questions/{questionId}
    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionOutputDTO> getQuestionById(@PathVariable Long questionId) {
        Optional<QuestionOutputDTO> question = questionService.findQuestionById(questionId);
        return question.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // GET /api/questions
    @GetMapping
    public ResponseEntity<List<QuestionOutputDTO>> getAllQuestions() {
        List<QuestionOutputDTO> questions = questionService.findAllQuestions();
        return ResponseEntity.ok(questions);
    }

    // POST /api/questions
    @PostMapping
    public ResponseEntity<QuestionOutputDTO> createQuestion(@RequestBody QuestionInputDTO questionInputDTO) {
        QuestionOutputDTO createdQuestion = questionService.createQuestion(questionInputDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion);
    }

    // PUT /api/questions/{questionId}
    @PutMapping("/{questionId}")
    public ResponseEntity<QuestionOutputDTO> updateQuestion(@PathVariable Long questionId,
                                                            @RequestBody QuestionInputDTO questionInputDTO) {
        try {
            QuestionOutputDTO updatedQuestion = questionService.updateQuestion(questionId, questionInputDTO);
            return ResponseEntity.ok(updatedQuestion);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // DELETE /api/questions/{questionId}
    @DeleteMapping("/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long questionId) {
        try {
            questionService.deleteQuestionById(questionId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
