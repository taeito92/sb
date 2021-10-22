package org.zerock.sb.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.sb.dto.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class DiaryServiceTests {

    @Autowired
    DiaryService diaryService;

    @Test
    public void registerTest() {

        List<String> tags = IntStream.rangeClosed(1, 3).mapToObj(j -> "tag_" + j)
                .collect(Collectors.toList());

        List<DiaryPictureDTO> pictures = IntStream.rangeClosed(1, 3).mapToObj(j -> {
                    DiaryPictureDTO picture = DiaryPictureDTO.builder()
                            .uuid(UUID.randomUUID().toString())
                            .fileName("img" + j + ".jpg")
                            .savePath("2021/10/18")
                            .idx(j)
                            .build();

                    return picture;
                }).collect(Collectors.toList());

        DiaryDTO diaryDTO = DiaryDTO.builder()
                .title("title....")
                .content("content...")
                .writer("user")
                .tags(tags)
                .pictures(pictures)
                .build();

        diaryService.register(diaryDTO);

    }

    @Transactional(readOnly = true) //Entity와 DTO 객체간의 괴리 때문에 delete, insert가 반복할 때 사용
    @Test
    public void readTest() {

        Long dno = 1L;

        DiaryDTO diaryDTO = diaryService.read(dno);

        log.info(diaryDTO);
    }

    @Test
    public void listTest() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();

        log.info(diaryService.getList(pageRequestDTO));

    }

    @Test
    public void listWithFavoriteTest() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();

        PageResponseDTO<DiaryListDTO> responseDTO = diaryService.getListWithFavorite(pageRequestDTO);

        //log.info(responseDTO);

    }

}
