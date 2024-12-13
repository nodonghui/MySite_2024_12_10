package com.site.sss.answer;

import com.site.sss.question.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface AnswerRepository extends JpaRepository<Answer,Integer> {
    @Query("SELECT a FROM Answer a WHERE a.question = :question ORDER BY SIZE(a.voter) DESC, a.createDate DESC")
    Page<Answer> findByQuestionOrderByVoterSizeDesc(Pageable pageable, Question question);

}
