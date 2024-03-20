package com.meohin.todo_pe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    @RequestMapping("/")
    public String root() {
        // viewResolver 에 의해서 요청한 클라이언트에게 회신.
        // - (1) 보통은 template 하고 조립해서 http protocol에 의해서 html body를 담아서 회신.
        // - (2) redirect가 붙으면 http protocol 구격에 해당하는 문자열을 회신.
        // (1)는 body에 thymeleaf와 협력해서 만들어진 html 문자열을 body에 담아서 보냄. 
        // (2)는 body 없음. http protocol 규격에 맞춤 문자열을 생성하는데, 헤더 정보를 담음.
        //      상태코드느 304 이고, 이 때 담긴 추가 정보는 /task/list이고 브라우저가 이 데이터를 받아서 처리. 304니까 /task/list 로 자동으로 요청하게 됨.
        // https://velog.io/@teddybearjung/HTTP-%EA%B5%AC%EC%A1%B0-%EB%B0%8F-%ED%95%B5%EC%8B%AC-%EC%9A%94%EC%86%8C
        // 진짜 오피셜 정보는 RFC 문서다.
        return "redirect:/task/list";
    }
}
