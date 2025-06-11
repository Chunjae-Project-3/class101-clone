package net.fullstack.class101clone.controller;

import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.dto.ClassDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/class")
@RequiredArgsConstructor
public class ClassViewController {

    @GetMapping("/{id}")
    public String getClassDetail(@PathVariable Integer id, Model model) {
        model.addAttribute("class", new ClassDTO(
                id,
                "무명 이모티콘 작가가 인기 작가가 된 비결!",
                "경쟁에서 살아남는 인기 이모티콘 만들기",
                "/images/default-image.png",
                "일러스트"
        ));

        model.addAttribute("classImageList", List.of(
                "/images/default-image.png",
                "/images/default-image.png"
        ));

        model.addAttribute("creatorName", "유랑");

        model.addAttribute("classReviews", List.of(
                Map.of("content", "정말 유익한 강의였어요!"),
                Map.of("content", "초보자에게도 이해하기 쉬웠습니다.")
        ));

        return "class/main";
    }
}
