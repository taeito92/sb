package org.zerock.sb.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.sb.dto.PageRequestDTO;
import org.zerock.sb.entity.Reply;

@SpringBootTest
@Log4j2
public class ReplyServiceTests {

    @Autowired
    private ReplyService replyService;

    @Test
    public void getListOfBoardTest() {
        Long bno = 198L;

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(-1).build();

        log.info(replyService.getListOfBoard(bno, pageRequestDTO));
    }

    @Test
    public void registerTest() {

    }

}
