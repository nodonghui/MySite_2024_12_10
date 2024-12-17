package com.site.sss.question;

import com.site.sss.Comment.AnswerComment.AnswerComment;
import com.site.sss.Comment.AnswerComment.AnswerCommentService;
import com.site.sss.Comment.CommentForm;
import com.site.sss.Comment.QuestionComment.QuestionComment;
import com.site.sss.Comment.QuestionComment.QuestionCommentService;
import com.site.sss.answer.Answer;
import com.site.sss.answer.AnswerForm;
import com.site.sss.answer.AnswerService;
import com.site.sss.user.SiteUser;
import com.site.sss.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.config.ConfigDataLocationNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@RequestMapping("/question")
@AllArgsConstructor
@Controller
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;
    private final AnswerService answerService;
    private final QuestionCommentService questionCommentService;
    private final AnswerCommentService answerCommentService;

    @GetMapping("/list")
    public String list(Model model,@RequestParam(value="page",defaultValue = "0") int page
                        ,@RequestParam(value="kw",defaultValue = "") String kw) {
        Page<Question> paging = this.questionService.getList(page,kw);
        model.addAttribute("paging",paging);
        model.addAttribute("kw",kw);
        return "question_list";
    }


    //기본적으로 value 초기화, 생략가능함
    //url 패턴을 설정하기 위해 사용
    @GetMapping(value="/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm
                         , CommentForm questionCommentForm, CommentForm answerCommentForm
                         , @RequestParam(value="page",defaultValue = "0") int page) {

        Question question = this.questionService.getQuestion(id);
        Page<Answer> answerPaging= answerService.getList(question,page);

        //question 내 answer의 모든 댓글을 가져온다 answer id -> answerComment 리스트 맵핑
        List<List<AnswerComment>> answerCommentList=answerCommentService.getList(question,page);

        model.addAttribute("question",question);
        model.addAttribute("answerPaging",answerPaging);
        model.addAttribute("answerCommentList",answerCommentList);

        return "question_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm
    , BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }

        SiteUser siteUser = this.userService.getUser(principal.getName());

        this.questionService.create(questionForm.getSubject(), questionForm.getContent(),siteUser);
        return "redirect:/question/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.questionService.delete(question);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.vote(question, siteUser);
        return String.format("redirect:/question/detail/%s", id);
    }
}
