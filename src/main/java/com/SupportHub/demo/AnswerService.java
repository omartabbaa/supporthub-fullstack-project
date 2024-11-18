package com.SupportHub.demo.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SupportHub.demo.dtos.InputDTOs.AnswerInputDTO;
import com.SupportHub.demo.dtos.OutputDTOs.AnswerOutputDTO;
import com.SupportHub.demo.mappers.AnswerMapper;
import com.SupportHub.demo.models.Answer;
import com.SupportHub.demo.models.Question;
import com.SupportHub.demo.models.User;
import com.SupportHub.demo.repositories.AnswerRepository;
import com.SupportHub.demo.repositories.QuestionRepository;
import com.SupportHub.demo.repositories.UserRepository;

@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final AnswerMapper answerMapper;

    @Autowired
    public AnswerService(AnswerRepository answerRepository, QuestionRepository questionRepository, UserRepository userRepository, AnswerMapper answerMapper) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.answerMapper = answerMapper;
    }

    public Optional<AnswerOutputDTO> findAnswerById(Long answerId) {
        return answerRepository.findById(answerId)
                .map(answerMapper::toDto);
    }

    public List<AnswerOutputDTO> findAllAnswers() {
        return answerRepository.findAll().stream()
                .map(answerMapper::toDto)
                .collect(Collectors.toList());
    }

    public AnswerOutputDTO createAnswer(AnswerInputDTO answerInputDTO) {
        Question question = questionRepository.findById(answerInputDTO.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found with ID: " + answerInputDTO.getQuestionId()));
        
        // Assuming you have a way to get the current user, replace this with your actual implementation
        User currentUser = getCurrentUser();
        
        Answer answer = answerMapper.toEntity(answerInputDTO, currentUser, question);
        Answer savedAnswer = answerRepository.save(answer);
        return answerMapper.toDto(savedAnswer);
    }

    public AnswerOutputDTO updateAnswer(Long answerId, AnswerInputDTO answerInputDTO) {
        return answerRepository.findById(answerId)
                .map(answer -> {
                    answer.setAnswerText(answerInputDTO.getAnswerText());
                    // Update other fields as necessary
                    return answerMapper.toDto(answerRepository.save(answer));
                }).orElseThrow(() -> new RuntimeException("Answer not found with ID: " + answerId));
    }

    public void deleteAnswerById(Long answerId) {
        if (answerRepository.existsById(answerId)) {
            answerRepository.deleteById(answerId);
        } else {
            throw new RuntimeException("Answer not found with ID: " + answerId);
        }
    }

    // This method should be implemented to get the current user
    private User getCurrentUser() {
        // For demonstration purposes, we'll return the first user in the database
        // In a real application, this should be replaced with proper user authentication logic
        return userRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No users found in the database"));
    }
}
