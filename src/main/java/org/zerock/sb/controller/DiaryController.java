package org.zerock.sb.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.sb.dto.DiaryDTO;
import org.zerock.sb.dto.DiaryListDTO;
import org.zerock.sb.dto.PageRequestDTO;
import org.zerock.sb.dto.PageResponseDTO;
import org.zerock.sb.service.DiaryService;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;

    @GetMapping("/register")
    public void getRegister() {
    }

    @PostMapping("/register")
    public String postRegister(DiaryDTO diaryDTO, RedirectAttributes redirectAttributes) {

        Long dno = diaryService.register(diaryDTO);
        log.info(dno);

        redirectAttributes.addFlashAttribute("result", dno);

        return "redirect:/diary/list";
    }

    @GetMapping("/list")
    public void getList(PageRequestDTO pageRequestDTO, Model model) {

        PageResponseDTO<DiaryListDTO> pageResponseDTO = diaryService.getListWithFavorite(pageRequestDTO);

        model.addAttribute("responseDTO", pageResponseDTO);

    }

    @GetMapping("/read")
    public void getRead(Long dno, PageRequestDTO pageRequestDTO, Model model) {

        model.addAttribute("dto", diaryService.read(dno));
    }



}
