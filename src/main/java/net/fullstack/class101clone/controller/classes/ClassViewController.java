package net.fullstack.class101clone.controller.classes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/class")
@RequiredArgsConstructor
public class ClassViewController {

    @GetMapping("/{id}")
    public String getClassDetail(@PathVariable Integer id) {
        return "class/main";
    }

    @GetMapping("/category/{categoryIdx}")
    public String showClassListByCategoryPath(@PathVariable Integer categoryIdx, Model model) {
        Map<Integer, String> categoryMap = Map.ofEntries(
                Map.entry(1, "디지털 드로잉"),
                Map.entry(2, "드로잉"),
                Map.entry(3, "공예"),
                Map.entry(4, "요리 · 음료"),
                Map.entry(5, "베이킹 · 디저트"),
                Map.entry(6, "음악"),
                Map.entry(7, "운동"),
                Map.entry(8, "라이프스타일"),
                Map.entry(9, "사진 · 영상"),
                Map.entry(10, "금융 · 재테크"),
                Map.entry(11, "창업 · 부업"),
                Map.entry(12, "성공 마인드"),
                Map.entry(13, "프로그래밍"),
                Map.entry(14, "데이터사이언스"),
                Map.entry(15, "제품 기획"),
                Map.entry(16, "비즈니스"),
                Map.entry(17, "생산성"),
                Map.entry(18, "마케팅"),
                Map.entry(19, "디자인"),
                Map.entry(20, "영상/3D"),
                Map.entry(21, "영어"),
                Map.entry(22, "외국어 시험"),
                Map.entry(23, "제2 외국어"),
                Map.entry(24, "아이 교육"),
                Map.entry(25, "부모 교육")
        );

        String categoryName = categoryMap.get(categoryIdx);
        model.addAttribute("categoryIdx", categoryIdx);
        model.addAttribute("categoryName", categoryName);

        return "class/list";
    }

    @GetMapping("/search")
    public String showSearchPage() {
        return "class/search";
    }

    @GetMapping("/lecture")
    public String showLectureByClassIdx() {
        return "class/lecture";
    }
}
