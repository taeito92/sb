package org.zerock.sb.entity;

import lombok.*;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity//(꼭 변수에 ID값을 정해줘야 함)
@Table(name = "tbl_diary")
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
    private String  content;
    private String writer;

    @CreationTimestamp //등록시간 자동관리(하이버네이트)
    private LocalDateTime regDate;

    @UpdateTimestamp//수정시간 자동관
    private LocalDateTime modDate;

    // 실행하면 태그 테이블도 만들어지고 외래키도 자동으로 걸림, 게시글을 수정하면 태그들도 같이 지워졌다가 다시 들어간다.
    // 그래서 엔티티가 아니라 다이어리에 강력하게 묶여있는..
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "tbl_diary_tag")
    @Fetch(value = FetchMode.JOIN)
    @BatchSize(size = 50)
    @Builder.Default
    private Collection<String> tags = new HashSet<>();

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTags(Collection<String> tags) {
        this.tags = tags;
    }

    public void setPictures(Set<DiaryPicture> pictures) {
        this.pictures = pictures;
    }

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "tbl_diary_picture")
    @Fetch(value = FetchMode.JOIN)
    @BatchSize(size = 50)
    private Set<DiaryPicture> pictures;
}
