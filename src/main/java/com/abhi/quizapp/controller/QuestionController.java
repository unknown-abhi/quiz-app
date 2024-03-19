package com.abhi.quizapp.controller;

import com.abhi.quizapp.model.Question;
import com.abhi.quizapp.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("question")
public class QuestionController {

    @Autowired
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) { // Constructor Injection
        this.questionService = questionService;
    }

    @GetMapping("allQuestions")
    public ResponseEntity<List<?>> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("questionsByCategory/{category}")
    public ResponseEntity<List<?>> getQuestionsByCategory(@PathVariable String category) {
        return questionService.getQuestionsByCategory(category);
    }

    @PostMapping("addQuestion")
    public ResponseEntity<?> addQuestion(@RequestBody Question question) {
        return questionService.addQuestion(question);
    }

    @DeleteMapping("deleteQuestion/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable int id) {
        return questionService.deleteQuestion(id);
    }
}
