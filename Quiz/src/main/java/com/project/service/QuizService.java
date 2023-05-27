package com.project.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.project.entity.Quiz;
import com.project.repository.QuizRepository;

@Service
public class QuizService {
    private final QuizRepository quizRepository;

    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public Quiz createQuiz(Quiz quiz) {
        // Set the initial status as "inactive"
        quiz.setStatus("inactive");
        return quizRepository.save(quiz);
    }

    public Quiz getActiveQuiz() {
        LocalDateTime now = LocalDateTime.now();
        List<Quiz> activeQuizzes = quizRepository.findByStatus("active");
        for (Quiz quiz : activeQuizzes) {
            if (now.isAfter(quiz.getStartDate()) && now.isBefore(quiz.getEndDate())) {
                return quiz;
            }
        }
        return null; // No active quiz found
    }

    public String getQuizResult(Long quizId) throws NotFoundException {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new NotFoundException());
        if (quiz.getStatus().equals("finished")) {
            return quiz.getOptions().get(quiz.getRightAnswer());
        } else {
            throw new IllegalStateException("Quiz result is not available yet");
        }
    }

    @Scheduled(fixedRate = 60000) // Update quiz status every minute
    public void updateQuizStatus() {
        LocalDateTime now = LocalDateTime.now();
        List<Quiz> quizzes = quizRepository.findAll();
        for (Quiz quiz : quizzes) {
            if (now.isBefore(quiz.getStartDate())) {
                quiz.setStatus("inactive");
            } else if (now.isAfter(quiz.getEndDate())) {
                quiz.setStatus("finished");
            } else {
                quiz.setStatus("active");
            }
            quizRepository.save(quiz);
        }
    }

	public List<Quiz> getAllQuizzes() {
		
		return null;
	}
}
