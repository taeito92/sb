package org.zerock.sb.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity//(꼭 변수에 ID값을 정해줘야 함)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Board {

    @Id//Entity는 꼭 식별값이 필요함.(프라이머리키)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String title;
    private String  content;
    private String writer;

    @CreationTimestamp //등록시간 자동관리(하이버네이트)
    private LocalDateTime regDate;

    @UpdateTimestamp//수정시간 자동관리
    private LocalDateTime modDate;

    public void change(String title, String content){//DTO로 변경할 메서드
        this.title = title;
        this.content = content;
    }

}