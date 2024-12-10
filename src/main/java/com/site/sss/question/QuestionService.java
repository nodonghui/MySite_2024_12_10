package com.site.sss.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

//롬북 코드 추가는 컴파일 바이트코드 레벨에서 진행됨 ast생성시 같이 생성되어 삽입
//빈 같은 스프링은 실행 시점에 객체 주입. 주입받는 객체를 매개변수로 가지는 생성자를 통해 주입
//롬북은 필요시 사용, 코드 줄일수 있 하지만 명시적으로 코드 보고 싶음 사용 x


@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository=questionRepository;
    }

    public List<Question> getList() {

        return this.questionRepository.findAll();
    }
}
