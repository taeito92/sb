package org.zerock.sb.entity;

import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_diary") //table 이름 직접 설정
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"tags", "pictures"})
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dno;

    private String title;

    private String content;

    private String writer;

    @CreationTimestamp
    private LocalDateTime regDate;

    @UpdateTimestamp
    private LocalDateTime modDate;

    @ElementCollection(fetch = FetchType.LAZY) //종속적인 관계에는 ElementCollection을 줌 -> FK 자동 설정
    @CollectionTable(name = "tbl_diary_tags")
    //Join Fetch라고 함 -> N+1 문제 해결 방안
    @Fetch(value = FetchMode.JOIN) // -> 지연로딩임에도 Join으로 가져옴, OneToMany에도 사용가능
    @BatchSize(size = 50)
    @Builder.Default
    private Set<String> tags = new HashSet<>();

    @ElementCollection(fetch = FetchType.LAZY) //값개체
    @CollectionTable(name = "tbl_diary_picture")
    @Fetch(value = FetchMode.JOIN)
    @BatchSize(size = 50)
    private Set<DiaryPicture> pictures;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public void setPictures(Set<DiaryPicture> pictures) {
        this.pictures = pictures;
    }
}
