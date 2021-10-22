package org.zerock.sb.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.sb.dto.PageRequestDTO;
import org.zerock.sb.dto.PageResponseDTO;
import org.zerock.sb.dto.ReplyDTO;
import org.zerock.sb.entity.Reply;
import org.zerock.sb.repository.ReplyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{

    private final ReplyRepository repository;
    private final ModelMapper modelMapper;

    private int calcLastPage(Long bno, double size) {
        int count = repository.getReplyCountOfBoard(bno);
        int lastPage = (int)(Math.ceil(count / size));

        return lastPage;
    }

    @Override
    public PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO) {

        Pageable pageable = null;

        if(pageRequestDTO.getPage() == -1) {
            int lastPage = calcLastPage(bno, pageRequestDTO.getSize()); // -1 댓글 없는 경우, 마지막 댓글 페이지 리턴
            if(lastPage <= 0) {
                lastPage = 1;
            }
            pageRequestDTO.setPage(lastPage);
        }

        pageable = PageRequest.of(pageRequestDTO.getPage() -1, pageRequestDTO.getSize());


        Page<Reply> result = repository.getListByBno(bno, pageable);

        List<ReplyDTO> replyDTOList = result.get()
                .map(reply -> modelMapper.map(reply, ReplyDTO.class))
                .collect(Collectors.toList());

        //replyDTOList.forEach(replyDTO -> log.info(replyDTO));

        return new PageResponseDTO<>(pageRequestDTO, (int)result.getTotalElements(), replyDTOList);
    }

    @Override
    public Long register(ReplyDTO replyDTO) {

        //Board board = Board.builder().bno(replyDTO.getBno()).build();

        Reply reply = modelMapper.map(replyDTO, Reply.class);

        //log.info(reply);
        repository.save(reply);

        return reply.getRno();
    }

    @Override
    public PageResponseDTO<ReplyDTO> removeReply(Long bno, Long rno, PageRequestDTO pageRequestDTO) {

        repository.deleteById(rno);

        return getListOfBoard(bno, pageRequestDTO);
    }

    @Override
    public PageResponseDTO<ReplyDTO> modifyReply(ReplyDTO replyDTO, PageRequestDTO pageRequestDTO) {

        Reply reply = repository.findById(replyDTO.getRno()).orElseThrow(); //없으면 예외 던지기

        reply.setText(replyDTO.getReplyText());

        repository.save(reply);

        Long bno = replyDTO.getBno();

        return getListOfBoard(bno, pageRequestDTO);
    }

}
