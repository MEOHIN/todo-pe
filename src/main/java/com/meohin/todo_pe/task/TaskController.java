package com.meohin.todo_pe.task;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TaskController {

    @RequestMapping("/task/list")
    @ResponseBody
    public String list() {
        return "task list";
    }
}
