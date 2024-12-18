package com.site.sss.Category;

import com.site.sss.Comment.AnswerComment.AnswerComment;
import com.site.sss.question.Question;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Entity
@Setter
@Getter
public class Category {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;




}
