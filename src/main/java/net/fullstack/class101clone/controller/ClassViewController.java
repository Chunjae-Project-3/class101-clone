package net.fullstack.class101clone.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/class")
@RequiredArgsConstructor
public class ClassViewController {

    @GetMapping("/{id}")
    public String getClassDetail(@PathVariable Integer id) {
        return "class/main";
    }
}
