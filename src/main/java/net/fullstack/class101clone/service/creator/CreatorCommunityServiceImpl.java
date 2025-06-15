package net.fullstack.class101clone.service.creator;

import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.domain.CreatorCommunityEntity;
import net.fullstack.class101clone.dto.CreatorCommunityDTO;
import net.fullstack.class101clone.repository.creator.CreatorCommunityRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreatorCommunityServiceImpl implements CreatorCommunityService {

    private final CreatorCommunityRepository communityRepository;

    @Override
    public List<CreatorCommunityDTO> getCommunityPostsByCreator(Long creatorId) {
        return communityRepository.findByCreator_CreatorIdAndDeletedFalseOrderByCreatedAtDesc(creatorId)
                .stream()
                .map(CreatorCommunityDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<CreatorCommunityDTO> getRecentPostsByCreator(Long creatorId) {
        return communityRepository.findTop3ByCreator_CreatorIdAndDeletedFalseOrderByCreatedAtDesc(creatorId)
                .stream()
                .map(CreatorCommunityDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void createPost(Long creatorId, CreatorCommunityDTO dto) {

    }

    @Override
    public void updatePost(Long postId, CreatorCommunityDTO dto) {

    }

    @Override
    public void deletePost(Long postId) {

    }

    @Override
    public CreatorCommunityDTO getPostDetail(Long postId) {
        CreatorCommunityEntity entity = communityRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 존재하지 않습니다: " + postId));
        return CreatorCommunityDTO.fromEntity(entity);
    }
}
