package org.zerock.sb.repository;

import com.google.common.collect.Sets;
import lombok.extern.log4j.Log4j2;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.sb.dto.DiaryDTO;
import org.zerock.sb.entity.DiaryPicture;
import org.zerock.sb.entity.Diary;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class DiaryRepositoryTests {

    @Autowired
    DiaryRepository diaryRepository;

    @Autowired
    ModelMapper modelMapper;

    @Test
    public void testInsert() {

        IntStream.rangeClosed(1, 5).forEach(i -> {

            Set<String> tags = IntStream.rangeClosed(1, 3).mapToObj(j -> i + "_tag_" + j)
                    .collect(Collectors.toSet());

            Set<DiaryPicture> pictures = IntStream.rangeClosed(1, 3).mapToObj(j -> {
                DiaryPicture picture = DiaryPicture.builder()
                        .uuid(UUID.randomUUID().toString())
                        .fileName("img" + j + ".jpg")
                        .savePath("2021/10/18")
                        .idx(j)
                        .build();

                return picture;
            }).collect(Collectors.toSet());

            Diary diary = Diary.builder()
                    .title("sample..." + i)
                    .content("dummy..." + i)
                    .writer("user" + i)
                    .tags(tags)
                    .pictures(pictures)
                    .build();

            diaryRepository.save(diary);
        });
    }


    //@Transactional //select 2번 가능
    @Test
    public void testSelectOne() {

        Long bno = 9L;

        Optional<Diary> optionalDiary = diaryRepository.findById(bno);

        Diary diary = optionalDiary.orElseThrow(); //예외처리

        log.info(diary);
        log.info(diary.getTags());
        log.info(diary.getPictures());

    }

    @Test
    public void testPaging1() {

        Pageable pageable = PageRequest.of(0,10, Sort.by("dno").descending());

        Page<Diary> result = diaryRepository.findAll(pageable);

        result.get().forEach(diary -> {
            /*
            log.info(diary);
            log.info(diary.getTags());
            log.info(diary.getPictures());
             */
        });
    }

    @Test
    public void testSelectOne2() {

        Long bno = 1L;

        Optional<Diary> optionalDiary = diaryRepository.findById(bno);

        Diary diary = optionalDiary.orElseThrow(); //예외처리

        DiaryDTO diaryDTO = modelMapper.map(diary ,DiaryDTO.class);

        // log.info(diaryDTO);
    }

    @Test
    public void testSearchTag() {
        String tag = "1";
        Pageable pageable = PageRequest.of(0,10, Sort.by("dno").descending());

        Page<Diary> result = diaryRepository.searchTags(tag, pageable);

        result.get().forEach(diary -> {
            log.info(diary);
            log.info(diary.getTags());
            log.info(diary.getPictures());
            log.info("--------------------");
        });
    }

    @Test
    public void testDelete() {

        Long dno = 3L;

        diaryRepository.deleteById(dno);
    }

    @Commit
    @Transactional
    @Test
    public void testUpdate() {

        Long dno = 1L;

        Set<String> updateTags = Sets.newHashSet("aaa", "bbb", "ccc");

        Set<DiaryPicture> updatePictures =
                IntStream.rangeClosed(5, 10).mapToObj(i -> {
                    DiaryPicture diaryPicture = DiaryPicture.builder()
                            .uuid(UUID.randomUUID().toString())
                            .savePath("2021/10/19")
                            .fileName("Test" + i + ".jpg")
                            .idx(i)
                            .build();

                    return diaryPicture;
                }).collect(Collectors.toSet());

        Optional<Diary> optionalDiary = diaryRepository.findById(dno);

        Diary diary = optionalDiary.orElseThrow();

        diary.setTitle("updated title 1");
        diary.setContent("updated content 1");
        diary.setTags(updateTags);
        diary.setPictures(updatePictures);

        diaryRepository.save(diary);


    }

    @Test
    public void findWithFavoriteCountTest() {
        Pageable pageable = PageRequest.of(0,10, Sort.by("dno").descending());

        Page<Object[]> result = diaryRepository.findWithFavoriteCount(pageable);

        for (Object[] objects : result.getContent()) { //result.getContent는 List타입으로 가져옴
            log.info(Arrays.toString(objects));
        }
    }

    @Test
    public void listHardTest() {

        Pageable pageable = PageRequest.of(0,10, Sort.by("dno").descending());

        diaryRepository.getSearchList(pageable);
    }



}



