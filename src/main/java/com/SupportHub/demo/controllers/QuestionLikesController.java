package com.SupportHub.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SupportHub.demo.dtos.InputDTOs.QuestionLikesInputDTO;
import com.SupportHub.demo.dtos.OutputDTOs.QuestionLikesOutputDTO;
import com.SupportHub.demo.services.QuestionLikesService;

@RestController
@RequestMapping("/api/question-likes")
public class QuestionLikesController {

    private final QuestionLikesService questionLikesService;

    @Autowired
    public QuestionLikesController(QuestionLikesService questionLikesService) {
        this.questionLikesService = questionLikesService;
    }

    // GET /api/question-likes/{likeId}
    @GetMapping("/{likeId}")
    public ResponseEntity<QuestionLikesOutputDTO> getQuestionLikeById(@PathVariable Long likeId) {
        Optional<QuestionLikesOutputDTO> questionLike = questionLikesService.findQuestionLikeById(likeId);
        return questionLike.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // GET /api/question-likes
    @GetMapping
    public ResponseEntity<List<QuestionLikesOutputDTO>> getAllQuestionLikes() {
        List<QuestionLikesOutputDTO> questionLikes = questionLikesService.findAllQuestionLikes();
        return ResponseEntity.ok(questionLikes);
    }

    // POST /api/question-likes
    @PostMapping
    public ResponseEntity<QuestionLikesOutputDTO> createQuestionLike(@RequestBody QuestionLikesInputDTO questionLikesInputDTO) {
        try {
            QuestionLikesOutputDTO createdQuestionLike = questionLikesService.createQuestionLike(questionLikesInputDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestionLike);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Return 404 if user or question not found
        }
    }

    // DELETE /api/question-likes/{likeId}
    @DeleteMapping("/{likeId}")
    public ResponseEntity<Void> deleteQuestionLike(@PathVariable Long likeId) {
        try {
            questionLikesService.deleteQuestionLikeById(likeId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
