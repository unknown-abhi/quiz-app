package com.abhi.quizapp.controller;

import com.abhi.quizapp.model.QuestionResponse;
import com.abhi.quizapp.model.QuestionWrapper;
import com.abhi.quizapp.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("quiz")
public class QuizController {

    @Autowired
    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("createQuiz")
    public ResponseEntity<String> createQuiz(@RequestParam String category, @RequestParam int num, @RequestParam String title) {
        return quizService.createQuiz(category, num, title);
    }

    @GetMapping("getQuiz/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(@PathVariable Integer id) {
        return quizService.getQuizQuestions(id);
    }

    @PostMapping("submit/{id}")
    public ResponseEntity<?> submitQuiz(@PathVariable Integer id, @RequestBody List<QuestionResponse> questionResponses) {
        return quizService.calculateResult(id, questionResponses);
    }
}
