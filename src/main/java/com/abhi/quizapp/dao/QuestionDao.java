package com.abhi.quizapp.dao;

import com.abhi.quizapp.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionDao extends JpaRepository<Question, Integer> {
    List<Question> findByCategory(String category);

    @Query(value = "select * from Question where category=:category ORDER BY RAND() LIMIT :num", nativeQuery = true)
    List<Question> findRandomQuestionsByCategory(String category, int num);
}
