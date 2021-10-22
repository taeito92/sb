package org.zerock.sb.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.zerock.sb.dto.PageRequestDTO;
import org.zerock.sb.dto.PageResponseDTO;
import org.zerock.sb.dto.ReplyDTO;
import org.zerock.sb.service.ReplyService;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/replies")
public class ReplyController {

    private final ReplyService replyService;

    @GetMapping("/list/{bno}")
    public PageResponseDTO<ReplyDTO> getListOfBoard(@PathVariable("bno") Long bno, PageRequestDTO pageRequestDTO) {

        return replyService.getListOfBoard(bno, pageRequestDTO);

    }

    @PostMapping("")
    public PageResponseDTO<ReplyDTO> register(@RequestBody ReplyDTO replyDTO) {

        replyService.register(replyDTO);
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(-1).build(); //page가 -1이 되면 마지막 페이지로 이동함

        return  replyService.getListOfBoard(replyDTO.getBno(), pageRequestDTO);
    }

    @DeleteMapping("/{bno}/{rno}")
    public PageResponseDTO<ReplyDTO> remove(
            @PathVariable("bno") Long bno,
            @PathVariable("rno") Long rno,
            PageRequestDTO pageRequestDTO) {

        return replyService.removeReply(bno, rno, pageRequestDTO);
    }

    @PutMapping("/{bno}/{rno}")
    public PageResponseDTO<ReplyDTO> modify(
            @PathVariable("bno") Long bno,
            @PathVariable("rno") Long rno,
            @RequestBody ReplyDTO replyDTO, //obj를 json으로 자동변환
            PageRequestDTO pageRequestDTO) {

        return replyService.modifyReply(replyDTO, pageRequestDTO);
    }
}
