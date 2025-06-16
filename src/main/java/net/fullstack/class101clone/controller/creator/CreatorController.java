package net.fullstack.class101clone.controller.creator;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack.class101clone.domain.CreatorEntity;
import net.fullstack.class101clone.dto.ClassDTO;
import net.fullstack.class101clone.service.creator.CreatorCommunityService;
import net.fullstack.class101clone.service.creator.CreatorServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Log4j2
@Controller
@RequiredArgsConstructor
public class CreatorController {
    private final CreatorServiceImpl creatorService;
    private final CreatorCommunityService communityService;

    @GetMapping("/creators/{creatorId}")
    public String showCreator(@PathVariable Integer creatorId, Model model) {
        CreatorEntity creator = creatorService.getCreator(creatorId);
        List<ClassDTO> recentClasses = creatorService.showRecentClassesOfCreator(creatorId);

        model.addAttribute("creator", creator);
        model.addAttribute("recentClasses", recentClasses);
        model.addAttribute("allClasses", creatorService.showAllClassesOfCreator(creatorId));

        // 💡 추가할 부분
        model.addAttribute("recentPosts", communityService.getRecentPostsByCreator(creatorId.longValue()));
        model.addAttribute("allPosts", communityService.getCommunityPostsByCreator(creatorId.longValue()));

        return "creator/creator";
    }


}
