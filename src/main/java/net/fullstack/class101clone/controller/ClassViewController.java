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
                "/images/default-image.png",
                "/images/default-image.png"
        ));

        model.addAttribute("creatorName", "유랑");

        model.addAttribute("classReviews", List.of(
                Map.of("content", "정말 유익한 강의였어요!"),
                Map.of("content", "초보자에게도 이해하기 쉬웠습니다.")
        ));

        model.addAttribute("lectureCurriculum", Map.of(
                "고은센세 N3 클래스에 대해서", List.of(
                        Map.of("title", "고은센세와 N3 시작해볼까요?", "duration", "00:59"),
                        Map.of("title", "JLPT N3 시험에 대해 알려드려요!", "duration", "03:31"),
                        Map.of("title", "수업 진행 방식에 대해 알려드려요!", "duration", "01:59")
                ),
                "본격적인 JLPT N3 공부 전 꼭 알아야 할 기본 문법 정리 1", List.of(
                        Map.of("title", "[기본 문법] 필수 조사1", "duration", "19:05"),
                        Map.of("title", "[기본 문법] ます형 문법", "duration", "12:15"),
                        Map.of("title", "[기본 문법] 필수 조사2", "duration", "09:41")
                )
        ));

        return "class/main";
    }

}
