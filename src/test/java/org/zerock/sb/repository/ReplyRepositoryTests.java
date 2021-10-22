package org.zerock.sb.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.zerock.sb.entity.Board;
import org.zerock.sb.entity.Reply;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void insertReplyTest() {

        IntStream.rangeClosed(1, 200).forEach(i -> {

            Long bno = (long) (200 - (i % 5)); //200,199,198,197,196

            int replyCount = (i % 5); //0,1,2,3,4

            IntStream.rangeClosed(0, replyCount).forEach(j -> {

                Board board = Board.builder().bno(bno).build();

                Reply reply = Reply.builder()
                        .replyText(("Reply......"))
                        .replyer("replyer..")
                        .board(board)
                        .build();

                replyRepository.save(reply);
            });//inner loop

        });//outer loop
    }

    @Test
    public void testRead() {

        Long rno = 1L;

        Reply reply = replyRepository.findById(rno).get();

        log.info(reply);

    }

    @Test
    public void testByBno() {
        Long bno = 200L;
        List<Reply> replyList = replyRepository.findReplyByBoard_BnoOrderByRno(bno);

        replyList.forEach(reply -> log.info(reply));
    }

    @Test
    public void testListOfBoard() {

        Pageable pageable =
                PageRequest.of(0,10, Sort.by("rno").descending());

        Page<Reply> result = replyRepository.getListByBno(197L, pageable);

        log.info(result.getTotalElements());

        result.get().forEach(reply -> log.info(reply));
    }

    @Test
    public void testReplyCountOfBoard() {

        long bno = 195L;
        int size = 10;

        int count = replyRepository.getReplyCountOfBoard(bno);
        int lastPage = (int)(Math.ceil(count / (double)size));

        /*
        //삼항연산자 대신 쓰는 if문
        if(lastPage == 0) {
            lastPage = 1;
        }
        */

        /*
        //진짜 끝 페이지를 찾는 코드
        int lastEnd = lastPage * size;
        int lastStart = lastEnd - size;

        log.info(lastStart + " : " + lastEnd);
        */

        //댓글 없는 경우 대비 삼항조건문
        Pageable pageable = PageRequest.of(lastPage <= 0 ? 0 : lastPage -1, size);

        Page<Reply> result = replyRepository.getListByBno(bno, pageable);

        log.info("total: " + result.getTotalElements());
        log.info("......." + result.getTotalPages());

        result.get().forEach(reply -> log.info(reply));

    }

    @Test
    public void testdeleteALLReplyByBno() {

        Long bno = 200L;

        replyRepository.deleteALLReplyByBno(bno);
    }

}
