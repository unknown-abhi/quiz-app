package com.abhi.quizapp.service;

import com.abhi.quizapp.dao.QuestionDao;
import com.abhi.quizapp.dao.QuizDao;
import com.abhi.quizapp.model.Question;
import com.abhi.quizapp.model.QuestionResponse;
import com.abhi.quizapp.model.QuestionWrapper;
import com.abhi.quizapp.model.Quiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuizService {

    @Autowired
    private final QuizDao quizDao;

    @Autowired
    private QuestionDao questionDao;

    public QuizService(QuizDao quizDao) {
        this.quizDao = quizDao;
    }


    public ResponseEntity<String> createQuiz(String category, int num, String title) {
        try {
            List<Question> questions = questionDao.findRandomQuestionsByCategory(category, num);

            if (questions.isEmpty()) {
                throw new Exception("No questions found for the given category.");
            }

            Quiz quiz = new Quiz();
            quiz.setTitle(title);
            quiz.setQuestions(questions);

            quizDao.save(quiz);
            return new ResponseEntity<>("Success", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        try {
            Optional<Quiz> quizOptional = quizDao.findById(id);

            if (quizOptional.isEmpty()) {
                throw new Exception("Quiz not found.");
            }

            Quiz quiz = quizOptional.get();
            List<Question> questionsFromDB = quiz.getQuestions();
            if (questionsFromDB.isEmpty()) {
                throw new Exception("No questions found in the quiz.");
            }

            List<QuestionWrapper> questionForUser = questionsFromDB.stream()
                    .map(question -> new QuestionWrapper(question.getId(), question.getQuestionTitle(),
                            question.getOption1(), question.getOption2(),
                            question.getOption3(), question.getOption4()))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(questionForUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> calculateResult(Integer id, List<QuestionResponse> questionResponses) {
        try {
            Optional<Quiz> quizOptional = quizDao.findById(id);

            if (!quizOptional.isPresent()) {
                // Handle the case where the quiz is not found
                return new ResponseEntity<>("Quiz not found", HttpStatus.NOT_FOUND);
            }

            List<Question> questions = quizOptional.get().getQuestions();
            int correctAnswers = 0;

            for (int i = 0; i < questions.size(); i++) {
                // Ensure that we do not get an IndexOutOfBoundsException
                if (questionResponses.size() > i && questionResponses.get(i).getQuestionResponses().equals(questions.get(i).getRightAnswer())) {
                    correctAnswers++;
                }
            }
            return new ResponseEntity<>(correctAnswers, HttpStatus.OK);
        } catch (Exception e) {
            // Log the exception (optional, depending on the logging framework)
            // e.printStackTrace();

            // Return a generic error response
            return new ResponseEntity<>("An error occurred while calculating the result", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
