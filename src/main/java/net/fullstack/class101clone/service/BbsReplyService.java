//package net.fullstack.class101clone.service;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import net.fullstack.api.domain.BbsEntity;
//import net.fullstack.api.domain.BbsReplyEntity;
//import net.fullstack.api.dto.BbsReplyDTO;
//import net.fullstack.api.repository.bbs.BbsReplyRepository;
//import org.modelmapper.ModelMapper;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@Log4j2
//@Transactional
//public class BbsReplyService {
//    private final BbsReplyRepository bbsReplyRepository;
//    private final ModelMapper modelMapper;
//
//    public long countReplies(Long board_idx) {
//        return bbsReplyRepository.countByBoardIdx(board_idx);
//    }
//
//    public long replyRegister(BbsReplyDTO bbsReplyDTO) {
//        BbsReplyEntity entity = modelMapper.map(bbsReplyDTO, BbsReplyEntity.class);
//        entity.setBoard(BbsEntity.builder().idx(bbsReplyDTO.getBoard_idx()).build());
//        BbsReplyEntity result = bbsReplyRepository.save(entity);
//        return result.getIdx();
//    }
//
//    //    public List<BbsReplyDTO> bbsReplyList(long board_idx, Pageable pageable) {
////        Page<BbsReplyEntity> resultEntity = bbsReplyRepository.list(board_idx, pageable);
////        return resultEntity.getContent().stream()
////                .map(reply -> modelMapper.map(reply, BbsReplyDTO.class)).collect(Collectors.toList());
////    }
//    public List<BbsReplyDTO> bbsReplyList(long board_idx, Pageable pageable) {
//        Page<BbsReplyEntity> resultEntity = bbsReplyRepository.list(board_idx, pageable);
//        return resultEntity.getContent().stream()
//                .map(reply -> BbsReplyDTO.builder()
//                        .idx(reply.getIdx())
//                        .board_idx(reply.getBoard() != null ? reply.getBoard().getIdx() : 0L)
//                        .reply_user_id(reply.getReply_user_id())
//                        .reply_content(reply.getReply_content())
//                        .reply_date(reply.getReply_date())
//                        .build())
//                .collect(Collectors.toList());
//    }
//
//    public BbsReplyDTO getReply(long idx) {
//        Optional<BbsReplyEntity> replyEntity = bbsReplyRepository.findById(idx);
//        BbsReplyEntity reply = replyEntity.orElseThrow();
//
//        BbsReplyDTO replyDTO = modelMapper.map(reply, BbsReplyDTO.class);
//        replyDTO.setBoard_idx(reply.getBoard().getIdx() != null ? reply.getBoard().getIdx() : 0L);
//
//        return replyDTO;
//    }
//
//    public void putReply(BbsReplyDTO bbsReplyDTO) {
//        Optional<BbsReplyEntity> replyOptional = bbsReplyRepository.findById(bbsReplyDTO.getIdx());
//        if (replyOptional.isPresent()) {
//            BbsReplyEntity reply = replyOptional.get();
//            reply.setReply_content(bbsReplyDTO.getReply_content());
//            reply.setReply_modify_date(LocalDateTime.now());
//            bbsReplyRepository.save(reply); // 얘를 하면 디비를 한 번 다녀와야 해서... 근데 없애면 운영보수 면에서 불편??
//        }
//    }
//
//    public void deleteReply(long idx) {
//        bbsReplyRepository.deleteById(idx);
//    }
//
//}
