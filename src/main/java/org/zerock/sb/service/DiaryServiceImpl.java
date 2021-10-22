package org.zerock.sb.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.sb.dto.DiaryDTO;
import org.zerock.sb.dto.DiaryListDTO;
import org.zerock.sb.dto.PageRequestDTO;
import org.zerock.sb.dto.PageResponseDTO;
import org.zerock.sb.entity.Diary;
import org.zerock.sb.repository.DiaryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;
    private final ModelMapper modelMapper;

    @Override
    public Long register(DiaryDTO diaryDTO) {

        Diary diary = modelMapper.map(diaryDTO, Diary.class);

        /*
        log.info(diary);
        log.info(diary.getTags());
        log.info(diary.getPictures());
        */


        diaryRepository.save(diary);

        return diary.getDno();
    }

    @Override
    public DiaryDTO read(Long dno) {

        Optional<Diary> optionalDiary = diaryRepository.findById(dno);

        Diary diary = optionalDiary.orElseThrow(); //예외처리

        DiaryDTO diaryDTO = modelMapper.map(diary, DiaryDTO.class);

        return diaryDTO;
    }

    @Override
    public PageResponseDTO<DiaryDTO> getList(PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("dno").descending());

        Page<Diary> result = diaryRepository.findAll(pageable);

        List<DiaryDTO> dtoList = result.get().map(
                diary -> modelMapper.map(diary, DiaryDTO.class)).collect(Collectors.toList());

        long totalCount = result.getTotalElements();

        return new PageResponseDTO<>(pageRequestDTO, (int) totalCount, dtoList);
    }

    @Override
    public PageResponseDTO<DiaryListDTO> getListWithFavorite(PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("dno").descending());

        Page<Object[]> result = diaryRepository.findWithFavoriteCount(pageable);

        long totalCount = result.getTotalElements();

        List<DiaryListDTO> dtoList = result.get().map(objects -> {
            Object[] arr = (Object[])objects;
            Diary diary = (Diary)arr[0];
            long totalScore = (long)arr[1];

            /*
            log.info("-----------------------------------------------");
            log.info(diary);
            log.info(totalScore);
             */

            DiaryListDTO diaryListDTO = modelMapper.map(diary, DiaryListDTO.class); //필요한 부분만 copy
            diaryListDTO.setTotalScore((int)totalScore);

            return diaryListDTO;

        }).collect(Collectors.toList());

        return new PageResponseDTO<>(pageRequestDTO, (int)totalCount, dtoList);
    }

    @Override
    public void modify(DiaryDTO diaryDTO) {
        Optional<Diary> result = diaryRepository.findById(diaryDTO.getDno());

        if (result.isEmpty()) {
            throw new RuntimeException("NOT FOUND");
        }
        Diary diary = result.get();
        diary.setTitle(diary.getTitle());
        diary.setContent(diary.getContent());
        diary.setTags(diary.getTags());
        diary.setPictures(diary.getPictures());

        diaryRepository.save(diary);
    }

    @Override
    public void remove(Long dno) {
        diaryRepository.deleteById(dno);
    }
}
