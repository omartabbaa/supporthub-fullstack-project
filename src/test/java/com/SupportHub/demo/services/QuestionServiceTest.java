package com.SupportHub.demo.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.SupportHub.demo.dtos.InputDTOs.QuestionInputDTO;
import com.SupportHub.demo.dtos.OutputDTOs.QuestionOutputDTO;
import com.SupportHub.demo.exceptions.ResourceNotFoundException;
import com.SupportHub.demo.mappers.QuestionMapper;
import com.SupportHub.demo.models.Project;
import com.SupportHub.demo.models.Question;
import com.SupportHub.demo.repositories.QuestionRepository;
import com.SupportHub.demo.repositories.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private QuestionMapper questionMapper;

    @InjectMocks
    private QuestionService questionService;

    @Test
    void testFindQuestionByIdSuccess() {
        // Arrange
        Long questionId = 1L;
        Question question = new Question();
        QuestionOutputDTO questionDTO = new QuestionOutputDTO();
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
        when(questionMapper.toDto(question)).thenReturn(questionDTO);

        // Act
        Optional<QuestionOutputDTO> result = questionService.findQuestionById(questionId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(questionDTO, result.get());
    }

    @Test
    void testFindQuestionByIdNotFound() {
        // Arrange
        Long questionId = 1L;
        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        // Act
        Optional<QuestionOutputDTO> result = questionService.findQuestionById(questionId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateQuestion() {
        // Arrange
        QuestionInputDTO inputDTO = new QuestionInputDTO();
        Project project = new Project();
        Question question = new Question();
        Question savedQuestion = new Question();
        QuestionOutputDTO outputDTO = new QuestionOutputDTO();
        
        when(projectRepository.findById(inputDTO.getProjectId())).thenReturn(Optional.of(project));
        when(questionMapper.toEntity(inputDTO, project)).thenReturn(question);
        when(questionRepository.save(question)).thenReturn(savedQuestion);
        when(questionMapper.toDto(savedQuestion)).thenReturn(outputDTO);

        // Act
        QuestionOutputDTO result = questionService.createQuestion(inputDTO);

        // Assert
        assertEquals(outputDTO, result);
    }

    @Test
    void testDeleteQuestionById() {
        // Arrange
        Long questionId = 1L;
        when(questionRepository.existsById(questionId)).thenReturn(true);

        // Act
        questionService.deleteQuestionById(questionId);

        // Assert
        verify(questionRepository, times(1)).deleteById(questionId);
    }

    @Test
    void testDeleteQuestionByIdNotFound() {
        // Arrange
        Long questionId = 1L;
        when(questionRepository.existsById(questionId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> questionService.deleteQuestionById(questionId));
    }
}