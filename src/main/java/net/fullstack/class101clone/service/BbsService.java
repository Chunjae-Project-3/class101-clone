package net.fullstack.class101clone.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack.api.domain.BbsEntity;
import net.fullstack.api.dto.*;
import net.fullstack.api.repository.BbsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class BbsService implements BbsServiceIf {
    private final ModelMapper modelMapper;
    private final BbsRepository bbsRepository;

    @Override
    public int getTotalCount() {
        return bbsRepository.getTotalCount();
    }

    @Override
    public PageResponseDTO<BbsDTO> bbsList(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("idx");
        String[] categories = pageRequestDTO.getSearch_categories();
        String keyword = pageRequestDTO.getSearch_word();

        Page<BbsEntity> entityPage = bbsRepository.search(pageable, categories, keyword);

        List<BbsDTO> dtoList = entityPage.getContent().stream()
                .map(entity -> modelMapper.map(entity, BbsDTO.class))
                .collect(Collectors.toList());

        return PageResponseDTO.<BbsDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .totalCount((int) entityPage.getTotalElements())
                .dtoList(dtoList)
                .build();
    }

    public PageResponseDTO<BbsListDTO> bbsListWithReplyCnt(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("idx");
        String[] categories = pageRequestDTO.getSearch_categories();
        String keyword = pageRequestDTO.getSearch_word();

        Page<BbsListDTO> result = bbsRepository.searchBbsWithReplyCnt(categories, keyword, pageable);

        List<BbsListDTO> dtoList = result.getContent();
        long totalCount = result.getTotalElements();

        PageResponseDTO<BbsListDTO> responseDTO = PageResponseDTO.<BbsListDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .totalCount((int) totalCount)
                .build();

        return responseDTO;
    }

    public PageResponseDTO<BbsListWithFileDTO> bbsListWithFile(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("idx");
        String[] categories = pageRequestDTO.getSearch_categories();
        String keyword = pageRequestDTO.getSearch_word();

        Page<BbsListWithFileDTO> result = bbsRepository.searchBbsWithFiles(categories, keyword, pageable);

        List<BbsListWithFileDTO> dtoList = result.getContent();
        long totalCount = result.getTotalElements();

        return PageResponseDTO.<BbsListWithFileDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .totalCount((int) totalCount)
                .build();
    }

    @Override
    public BbsDTO bbsView(Long idx) {
        Optional<BbsEntity> optionalEntity = bbsRepository.findById(idx);
        if (optionalEntity.isPresent()) {
            return modelMapper.map(optionalEntity.get(), BbsDTO.class);
        }
        return null;
    }

    @Override
    public long bbsRegister(BbsDTO bbsDTO) {
        BbsEntity entity = modelMapper.map(bbsDTO, BbsEntity.class);
        BbsEntity savedEntity = bbsRepository.save(entity);
        long result = savedEntity.getIdx();
        return result;
    }

    @Override
    public long bbsUpdate(BbsDTO bbsDTO) {
        BbsEntity entity = modelMapper.map(bbsDTO, BbsEntity.class);
        entity.setModify_date(LocalDateTime.now());
        BbsEntity savedEntity = bbsRepository.save(entity);
        long result = savedEntity.getIdx();
        return result;
    }

    @Override
    public long bbsDelete(Long idx) {
        Optional<BbsEntity> optionalEntity = bbsRepository.findById(idx);
        if (optionalEntity.isPresent()) {
            BbsEntity bbsEntity = optionalEntity.get();
            bbsRepository.delete(bbsEntity);
            return bbsEntity.getIdx();
        }
        return 0;
    }
}
