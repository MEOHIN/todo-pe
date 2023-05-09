package com.meohin.todo_pe.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @RequestMapping("/timetodo")
    @ResponseBody
    public String index() {
        return "안녀하세요 TimeTODO에 오신것을 환영합니다.";
    }
}
