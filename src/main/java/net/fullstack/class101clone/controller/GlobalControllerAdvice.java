package net.fullstack.class101clone.controller;

import jakarta.servlet.http.HttpSession;
import net.fullstack.class101clone.dto.UserDTO;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("userInfo")
    public UserDTO userInfo(HttpSession session) {
        return (UserDTO) session.getAttribute("userInfo");
    }
}
