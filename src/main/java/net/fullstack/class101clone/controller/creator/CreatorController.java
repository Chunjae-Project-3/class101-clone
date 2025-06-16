package net.fullstack.class101clone.controller.creator;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack.class101clone.domain.CreatorEntity;
import net.fullstack.class101clone.dto.ClassDTO;
import net.fullstack.class101clone.service.creator.CreatorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Log4j2
@Controller
@RequiredArgsConstructor
public class CreatorController {
    private final CreatorService creatorService;

    @GetMapping("/creators/{creatorId}")
    public String showCreator(@PathVariable Integer creatorId, Model model) {
        CreatorEntity creator = creatorService.getCreator(creatorId);
        List<ClassDTO> recentClasses = creatorService.showRecentClassesOfCreator(creatorId);

        log.info("recentClasses size: {}", recentClasses.size());

        model.addAttribute("creator", creator);
        model.addAttribute("recentClasses", recentClasses);

        return "creator/creator";
    }


}
