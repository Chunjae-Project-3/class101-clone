//package net.fullstack.class101clone.controller.api.v1;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import net.fullstack.api.dto.BbsReplyDTO;
//import net.fullstack.api.service.BbsReplyService;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//
//import java.net.BindException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@Log4j2
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/bbs/replies")
//@Tag(name = "bbs reply", description = "게시글 댓글")
//public class BbsReplyController {
//    private final BbsReplyService bbsReplyService;
//
//    @Operation(summary = "댓글 작성", description = "댓글 작성 api")
//    @PostMapping(value = "/register")
//    public ResponseEntity<Map<String, Long>> replyRegister(
//            @RequestBody @Valid BbsReplyDTO bbsReplyDTO,
//            BindingResult bindingResult) throws BindException {
//
//        if (bindingResult.hasErrors()) {
//            throw new BindException();
//        }
//
//        long idx = bbsReplyService.replyRegister(bbsReplyDTO);
//        Map<String, Long> map = Map.of("idx", idx);
//
//        return ResponseEntity.ok(map);
//    }
//
//    @GetMapping("/list/{boardidx}")
//    public Map<String, Object> getReplyList(
//            @PathVariable("boardidx") Long board_idx,
//            @RequestParam(value = "reply_page_no", defaultValue = "1") int page,
//            @RequestParam(value = "reply_page_size", defaultValue = "10") int size) {
//
//        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("idx").descending());
//        List<BbsReplyDTO> replyDTOList = bbsReplyService.bbsReplyList(board_idx, pageable);
//        long total = bbsReplyService.countReplies(board_idx);
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("dtoList", replyDTOList);
//        result.put("total", total);
//        result.put("page", page);
//        result.put("size", size);
//
//        int end = (int) (Math.ceil(page / 10.0)) * 10;
//        int start = end - 9;
//        int last = (int) (Math.ceil((double) total / size));
//        end = Math.min(last, end);
//
//        result.put("start", start);
//        result.put("end", end);
//        result.put("prev", start > 1);
//        result.put("next", last > end);
//
//        return result;
//    }
//
//    @GetMapping("/{idx}")
//    public BbsReplyDTO getReply(@PathVariable("idx") Long idx) {
//        BbsReplyDTO bbsReplyDTO = bbsReplyService.getReply(idx);
//        return bbsReplyDTO;
//    }
//
//    @PutMapping("/{idx}")
//    public Map<String, Long> putReply(@Valid @RequestBody BbsReplyDTO bbsReplyDTO, @PathVariable("idx") Long idx) {
//        Map<String, Long> map = new HashMap<>();
//        bbsReplyService.putReply(bbsReplyDTO);
//        map.put("idx", idx);
//        return map;
//    }
//
//    @DeleteMapping("/{idx}")
//    public Map<String, Long> deleteReply(@PathVariable("idx") Long idx) {
//        Map<String, Long> map = new HashMap<>();
//        bbsReplyService.deleteReply(idx);
//        map.put("idx", idx);
//        return map;
//    }
//}
