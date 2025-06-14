package net.fullstack.class101clone.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.service.ChatService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ChatService chatService;

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        List<String> sections = List.of(
                "주간101", "실시간 인기 클래스", "새로 오픈한 클래스", "오늘의 BEST 클래스",
                "금융 · 재테크 인기 클래스", "음악 인기 클래스", "창업 · 부업 인기 클래스",
                "생산성 인기 클래스", "외국어 시험 인기 클래스", "브랜딩 인기 클래스",
                "트렌드가 뭔데, 지금 BUY 인기 클래스", "스마트한 일상을 위한 클래스", "클로저 HOT 클래스"
        );
        model.addAttribute("sections", sections);

        String loginId = (String) session.getAttribute("loginId");

        boolean hasUnread = false;
        if (loginId != null && !"master".equals(loginId)) {
            hasUnread = chatService.hasUnreadMessages(loginId);
        }
        model.addAttribute("hasUnread", hasUnread);

        return "main/index";
    }


    @GetMapping("/video")
    public String video() {
        return "video/videoPlayer";
    }
}
