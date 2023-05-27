package com.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entity.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Long> 
{
	List<Quiz> findByStatus(String status);
}
