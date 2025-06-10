//package net.fullstack.class101clone.controller;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import net.fullstack.api.dto.*;
//import net.fullstack.api.service.BbsService;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//@Controller
//@RequestMapping("/bbs")
//@RequiredArgsConstructor
//@Log4j2
//public class BbsController {
//    private final BbsService bbsService;
//
//    @GetMapping("/list")
//    public String list(@ModelAttribute PageRequestDTO pageRequestDTO,
//                       Model model) {
//        PageResponseDTO<BbsDTO> responseDTO = bbsService.bbsList(pageRequestDTO);
//        PageResponseDTO<BbsListDTO> reponseDTOwithReplies = bbsService.bbsListWithReplyCnt(pageRequestDTO);
//        PageResponseDTO<BbsListWithFileDTO> reponseDTOwithFiles = bbsService.bbsListWithFile(pageRequestDTO);
//
//        model.addAttribute("responseDTO", reponseDTOwithFiles);
//        return "bbs/list";
//    }
//
//    @GetMapping("/view")
//    public String bbsView(Model model, Long idx,
//                          @ModelAttribute PageRequestDTO pageRequestDTO) {
//        BbsDTO bbsDTO = bbsService.bbsView(idx);
//        model.addAttribute("bbsDTO", bbsDTO);
//        model.addAttribute("idx", idx);
//        model.addAttribute("pageRequestDTO", pageRequestDTO);
//        return "bbs/view";
//    }
//
//    @GetMapping("/register")
//    public String bbsRegister() {
//        return "bbs/register";
//    }
//
//    @PostMapping("/register")
//    public String bbsRegister(@ModelAttribute BbsDTO bbsDTO,
//                              RedirectAttributes redirectAttributes) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        bbsDTO.setDisplay_date(sdf.format(new Date()));
//        bbsDTO.setUser_id("user1");
//        bbsDTO.setRead_cnt(0);
//        bbsService.bbsRegister(bbsDTO);
//        redirectAttributes.addFlashAttribute("success", "게시글이 등록되었습니다.");
//        return "redirect:/bbs/list";
//    }
//
//    @GetMapping("/modify")
//    public String bbsModify(Model model, @RequestParam("idx") Long idx) {
//        BbsDTO bbsDTO = bbsService.bbsView(idx);
//        model.addAttribute("bbsDTO", bbsDTO);
//        return "bbs/modify";
//    }
//
//    @PostMapping("/modify")
//    public String bbsModify(@ModelAttribute BbsDTO bbsDTO,
//                            RedirectAttributes redirectAttributes) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        bbsDTO.setDisplay_date(sdf.format(new Date()));
//        bbsDTO.setUser_id("user1");
//        bbsDTO.setRead_cnt(0);
//        bbsService.bbsUpdate(bbsDTO);
//        redirectAttributes.addFlashAttribute("success", "게시글이 수정되었습니다.");
//        return "redirect:/bbs/view?idx=" + bbsDTO.getIdx();
//    }
//
//    @GetMapping("/delete")
//    public String bbsDelete(@RequestParam("idx") Long idx, RedirectAttributes redirectAttributes) {
//        bbsService.bbsDelete(idx);
//        redirectAttributes.addFlashAttribute("success", "게시글이 삭제되었습니다.");
//        return "redirect:/bbs/list";
//    }
//}
