package net.fullstack.class101clone.controller.creator;

import net.fullstack.class101clone.dto.CreatorCommunityDTO;
import net.fullstack.class101clone.service.creator.CreatorCommunityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/creators/{creatorId}/community")
public class CreatorCommunityController {

    private final CreatorCommunityService communityService;

    public CreatorCommunityController(CreatorCommunityService communityService) {
        this.communityService = communityService;
    }

    @GetMapping
    public String list(@PathVariable Long creatorId, Model model) {
        List<CreatorCommunityDTO> posts = communityService.getCommunityPostsByCreator(creatorId);
        model.addAttribute("posts", posts);
        return "creator/community/list";
    }

    @GetMapping("/{postId}")
    public String detail(@PathVariable Long postId, Model model) {
        CreatorCommunityDTO post = communityService.getPostDetail(postId);
        model.addAttribute("post", post);
        return "creator/community/detail";
    }

    // create, update, delete 관련 메서드들도 추가 예정
}

