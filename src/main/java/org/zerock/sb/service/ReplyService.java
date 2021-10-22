package org.zerock.sb.service;

import org.springframework.transaction.annotation.Transactional;
import org.zerock.sb.dto.PageRequestDTO;
import org.zerock.sb.dto.PageResponseDTO;
import org.zerock.sb.dto.ReplyDTO;

@Transactional
public interface ReplyService {

    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);

    Long register(ReplyDTO replyDTO);

    PageResponseDTO<ReplyDTO> removeReply(Long bno, Long rno, PageRequestDTO pageRequestDTO);

    PageResponseDTO<ReplyDTO> modifyReply(ReplyDTO replyDTO, PageRequestDTO pageRequestDTO);

}
