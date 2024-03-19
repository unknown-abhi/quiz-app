package com.abhi.quizapp.service;

import com.abhi.quizapp.dao.QuestionDao;
import com.abhi.quizapp.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private final QuestionDao questionDao;

    public QuestionService(QuestionDao questionDao) { // Constructor Injection
        this.questionDao = questionDao;
    }

    public List<Question> getAllQuestions() {
        return questionDao.findAll();
    }

    public List<Question> getQuestionsByCategory(String category) {
        return questionDao.findByCategory(category);
    }

    public Question addQuestion(Question question) {
        return questionDao.save(question);
    }

    public ResponseEntity<String> deleteQuestion(int id) {
        Optional<Question> questionOptional = questionDao.findById(id);
        if (questionOptional.isPresent()) {
            Question question = questionOptional.get();
            questionDao.delete(question); // Or use questionDao.deleteById(id)
            return ResponseEntity.ok("Data with ID :: " + id + " deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id :: " + id + " not found");
        }
    }
}
