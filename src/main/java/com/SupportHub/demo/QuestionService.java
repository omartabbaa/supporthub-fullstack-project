package com.SupportHub.demo.services;

import com.SupportHub.demo.dtos.InputDTOs.QuestionInputDTO;
import com.SupportHub.demo.dtos.OutputDTOs.QuestionOutputDTO;
import com.SupportHub.demo.exceptions.ResourceNotFoundException;
import com.SupportHub.demo.mappers.QuestionMapper;
import com.SupportHub.demo.models.Question;
import com.SupportHub.demo.models.Project;
import com.SupportHub.demo.repositories.QuestionRepository;
import com.SupportHub.demo.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final ProjectRepository projectRepository;
    private final QuestionMapper questionMapper;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, ProjectRepository projectRepository, QuestionMapper questionMapper) {
        this.questionRepository = questionRepository;
        this.projectRepository = projectRepository;
        this.questionMapper = questionMapper;
    }

    public Optional<QuestionOutputDTO> findQuestionById(Long questionId) {
        return questionRepository.findById(questionId).map(questionMapper::toDto);
    }

    public List<QuestionOutputDTO> findAllQuestions() {
        return questionRepository.findAll().stream()
                .map(questionMapper::toDto)
                .collect(Collectors.toList());
    }

    public QuestionOutputDTO createQuestion(QuestionInputDTO questionInputDTO) {
        Project project = projectRepository.findById(questionInputDTO.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + questionInputDTO.getProjectId()));

        Question question = questionMapper.toEntity(questionInputDTO, project);
        Question savedQuestion = questionRepository.save(question);
        return questionMapper.toDto(savedQuestion);
    }

    public QuestionOutputDTO updateQuestion(Long questionId, QuestionInputDTO questionInputDTO) {
        return questionRepository.findById(questionId)
                .map(question -> {
                    question.setQuestionTitle(questionInputDTO.getQuestionTitle());
                    question.setQuestionText(questionInputDTO.getQuestionText());
                    return questionMapper.toDto(questionRepository.save(question));
                }).orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + questionId));
    }

    public void deleteQuestionById(Long questionId) {
        if (questionRepository.existsById(questionId)) {
            questionRepository.deleteById(questionId);
        } else {
            throw new ResourceNotFoundException("Question not found with ID: " + questionId);
        }
    }
}
