package com.abhi.quizapp.service;

import com.abhi.quizapp.dao.QuestionDao;
import com.abhi.quizapp.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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

    public ResponseEntity<List<?>> getAllQuestions() {
        try {
            List<Question> questions = questionDao.findAll();
            return new ResponseEntity<>(questions, HttpStatus.OK);
        } catch (DataAccessException e) {
            // Log the exception or handle it appropriately
            return new ResponseEntity<>(List.of("Error occurred while fetching all questions"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<?>> getQuestionsByCategory(String category) {
        try {
            List<Question> questions = questionDao.findByCategory(category);
            return new ResponseEntity<>(questions, HttpStatus.OK);
        } catch (DataAccessException e) {
            // Log the exception or handle it appropriately
            return new ResponseEntity<>(List.of("Error occurred while fetching questions by category"), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> addQuestion(Question question) {
        try {
            Question savedQuestion = questionDao.save(question);
            return new ResponseEntity<>(savedQuestion, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            // Log the exception or handle it appropriately
            return new ResponseEntity<>("Error occurred while adding a new question", HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
