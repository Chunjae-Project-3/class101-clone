package net.fullstack.class101clone.service.creator;

import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.dto.CreatorCommunityDTO;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CreatorCommunityService {
    List<CreatorCommunityDTO> getCommunityPostsByCreator(Long creatorId);
    List<CreatorCommunityDTO> getRecentPostsByCreator(Long creatorId);
    void createPost(Long creatorId, CreatorCommunityDTO dto);
    void updatePost(Long postId, CreatorCommunityDTO dto);
    void deletePost(Long postId);
    CreatorCommunityDTO getPostDetail(Long postId);

}