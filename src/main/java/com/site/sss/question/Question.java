package com.site.sss.question;

import java.time.LocalDateTime;
import java.util.List;

import com.site.sss.answer.Answer;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "qusetion", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;
}