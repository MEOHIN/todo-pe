package com.meohin.todo_pe.task;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TaskController {

    @RequestMapping("/task/list")
    public String list() {
        return "task_list";
    }
}
