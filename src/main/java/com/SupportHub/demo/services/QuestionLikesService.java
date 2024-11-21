package com.SupportHub.demo.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SupportHub.demo.dtos.InputDTOs.QuestionLikesInputDTO;
import com.SupportHub.demo.dtos.OutputDTOs.QuestionLikesOutputDTO;
import com.SupportHub.demo.dtos.OutputDTOs.QuestionOutputDTO;
import com.SupportHub.demo.dtos.OutputDTOs.UserOutputDTO;
import com.SupportHub.demo.mappers.QuestionLikesMapper;
import com.SupportHub.demo.models.Question;
import com.SupportHub.demo.models.QuestionLikes;
import com.SupportHub.demo.models.User;
import com.SupportHub.demo.repositories.QuestionLikesRepository;

@Service
public class QuestionLikesService {

    private final QuestionLikesRepository questionLikesRepository;
    private final QuestionLikesMapper questionLikesMapper;
    private final UserService userService;
    private final QuestionService questionService;

    @Autowired
    public QuestionLikesService(QuestionLikesRepository questionLikesRepository, 
                                QuestionLikesMapper questionLikesMapper,
                                UserService userService,
                                QuestionService questionService) {
        this.questionLikesRepository = questionLikesRepository;
        this.questionLikesMapper = questionLikesMapper;
        this.userService = userService;
        this.questionService = questionService;
    }

    public Optional<QuestionLikesOutputDTO> findQuestionLikeById(Long likeId) {
        return questionLikesRepository.findById(likeId)
                .map(questionLikesMapper::toDto);
    }

    public List<QuestionLikesOutputDTO> findAllQuestionLikes() {
        return questionLikesRepository.findAll().stream()
                .map(questionLikesMapper::toDto)
                .collect(Collectors.toList());
    }

    public QuestionLikesOutputDTO createQuestionLike(QuestionLikesInputDTO questionLikesInputDTO) {
        // Fetch user and question using their services
        UserOutputDTO userOutputDTO = userService.findUserById(questionLikesInputDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        QuestionOutputDTO questionOutputDTO = questionService.findQuestionById(questionLikesInputDTO.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found"));

        // Map DTOs to entities
        User user = new User(); 
        user.setUserId(userOutputDTO.getUserId());
        user.setName(userOutputDTO.getName());
        user.setEmail(userOutputDTO.getEmail());

        Question question = new Question();
        question.setQuestionId(questionOutputDTO.getQuestionId()); // Fixed the ID setting
        question.setQuestionTitle(questionOutputDTO.getQuestionTitle());
        question.setQuestionText(questionOutputDTO.getQuestionText());

        // Map the input DTO and entities to the QuestionLikes entity
        QuestionLikes questionLikes = questionLikesMapper.toEntity(questionLikesInputDTO, user, question);

        // Save and return the created like as a DTO
        QuestionLikes savedQuestionLikes = questionLikesRepository.save(questionLikes);
        return questionLikesMapper.toDto(savedQuestionLikes);
    }

    public void deleteQuestionLikeById(Long likeId) {
        if (questionLikesRepository.existsById(likeId)) {
            questionLikesRepository.deleteById(likeId);
        } else {
            throw new RuntimeException("Like not found with ID: " + likeId);
        }
    }
}
