package org.zerock.sb.entity;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity//(꼭 변수에 ID값을 정해줘야 함)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long fno;

    @ManyToOne
    private Diary diary;

    @ManyToOne
    private Member member;

    private int score;

    @CreationTimestamp
    private LocalDateTime regDate;



}
