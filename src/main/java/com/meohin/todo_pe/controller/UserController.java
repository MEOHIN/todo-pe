package com.meohin.todo_pe.controller;

import com.meohin.todo_pe.validationObject.UserVO;
import com.meohin.todo_pe.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String login(Principal principal) {
        if (principal != null) {
            // 서버가 클라이언트한테 응답을 보낸다. HTML로 보내는게 아니라. redirect 하라는 http status를 보내는 것임.
            // 브라우저는 HTML이 아니니까 렌더링안하고, http status 가 304 redirect 이기 떄문에 , 바로 해당 URL로 이동시킴.
            // 브라우저는 /task/list URL로 이동하게됨. 이 의미는 /task/list 라는 리소스를 서버에 요청하는 것임.
            // 즉, /task/list 컨트롤러에 가게됨. (실제로는 Spring의 모든 단계를 거치고, Spring Security  등을 거쳐 Controller 까지 닿는 것임)
            // 자세한 내용은 Spring MVC architecture diagram 참고.
            // 위 아키텍쳐에서 Spring Security가 어디에 끼어있고, 관여하는지 알면 좋음. Spring security architecture diagram
            return "redirect:/task/list";
        }

        // Template File을 렌더링 해서 리턴하라.
        // Template Engine 이 동작한다.
        //  엔진의 종류가 여러개인데, 이중에 타임리프인걸 얘가 어떻게 알아차리지?
        // 타임 리프인지 어떻게 알아볼까?
        //      spring-boot-starter-thymeleaf 의존성을 추가하면 자동으로 설정이 구성된다.
        //          viewResolver를 thymeleaf 로 대체하는 환경설정이 들어 있다.
        //      만약 starter 가 아닌 그냥 thymeleaf 의존성을 추가하는 경우라면 (spring boot 가 아닌 spring 인경우)
        //          개발자가 직접 viewResolver를 thymeleaf로 구성해야 한다.
        //      만약 두개 이상의 템플릿 엔진이 의존성에 추가된 경우라면, 둘다 사용할 수 있지만,
        //          어떤 상황에 어떤 엔진을 사용할지 명시적으로 개발자가 설정해야 한다.
        //          application.properties 에 템플릿 엔진별로 해석할 템플릿 파일의 경로를 분리해서 설정하는 식이다.
        // 아래 경로의 template 파일을 읽고, 변수 정보 등을 참조해서 html 문서를 서버에서 생성하게 되고, 이 결과를
        // 클라이언트에 회신해준다.
        // 브라우저는 회신받은 데이터가 HTML 이므로, 렌더링해서 보여주게 된다. http 프로토콜 (통신규약) = http header + http body(http body에는 html 문서가 담겨있음.)
        // https://datatracker.ietf.org/doc/html/rfc2616 HTTP/1.1 프로토콜 구조에 맞춰서 헤더 + body 가 브라우저에 전송
        // 브라우저가 헤더를 읽고, 해석하는 방식을 결정하고, 처리. html 문서는 text/html 형태로 렌더링하라는 정보가 헤더에 있음.
        // 헤더는 브라우저가 서버로부터 받은 리소스를 어떻게 해석해야하는지 정보가 담겨있고, 실제 리소스 정보는 body에 담겨옴.
        return "login/login";

        /* 터미널 프로그램인 MobaXterm을 사용하여 서버에 접속햇을 때 '템플릿 이름을 반환하는 방법의 문제'로 발생한 오류는 다음 링크의 해결법처럼
        슬래시가 없어야 한다. "/login/login" 가 아닌 "login/login"가 옳다
        * https://stackoverflow.com/questions/48963242/cannot-access-templates-running-spring-boot-with-jar
        * */
    }

    @GetMapping("/signup")
    public String signup(UserVO userVO, Principal principal) {
        if (principal != null) {
            return "redirect:/task/list";
        }
        return "login/signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserVO userVO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "login/signup_form";
        }

        if (!userVO.getInputPW().equals(userVO.getConfirmPW())) {
            bindingResult.rejectValue("pw2", "passwordInCorrect", "입력한 2개의 패스워드가 일치하지 않습니다.");
            return "/login/signup_form";
        }

        try {
            this.userService.saveUserInfo(userVO.getInputUserId(), userVO.getInputPW(), userVO.getInputEmail());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 가입된 회원입니다.");
            return "/login/signup_form";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "/login/signup_form";
        }
        return "redirect:/task/list";
    }

    // Without Spring security, 사용자 세션에 인증 여부를 저장하고, 검증할 때, 세션에 담긴 인증 정보를 통화 확인
    // 저장시 logged=true 또는 회원 아이디 정보를 저장, 인증할 때는 이 정보가 세션에 있는지 여부를 검사해서 확인. 있으면 인증된 사용자, 없으면 인증되지 않은 사용자.
    // 인증 넘어서면 -> 인가 회원 레벨에 따라 접근가능한 URL이 달라질 수 있음.
    // 인가는 규칙, 레벨이 있고, 허용할 기능 ADMIN, MANAGER, USER, GUEST
    // Security 대표적으로 DB에 이룰을 저장하고, SErucitry 연동.
    // 회원 가입당시 , 인가 (접근 제어 , access-control) 된 권한레벨을 동시 지정. default GUEST 하고,
    // 관리자가 직접 권한을 부여하는 것.
    @PostMapping("/loginCheck")
    public String loginCheck(@RequestBody String userId, @RequestBody String pw) {
        //TODO: process POST request

        // 직접 DB에 접속해서 쿼리
        // 정보가 있으면 승인 없으면 미승인

        // 승인이면 = 세션에 로그인 정보를 담아.
        // 세션 접속할 때마다 메모리상에 접속자의 세션 ID로 기록되어 있어.
        // 해당 세션에 정보를 담아.
        // Key - value
        // logged = true

        // 판단
        // 필요한 곳에서 세션이 정상인 확인하는 작업
        // 현재 접속자의 세션 ID를 가져오고, 이 변수에 logged가 true 승인
        // 매번 코드를 넣어야 한다.
        // AOP 를 쓴다.
        // pointcut 이게 적용할 곳. before request
        // 요청 전단계에 함수나 메서드의 규칙 정할 수 있다. **Controller 클래스의 메서드에 아래 코드를 적용한다.
        // 검증하는 코드를 집어 넣으면 된다.
        return "";
    }
}
