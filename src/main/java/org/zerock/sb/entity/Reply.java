package org.zerock.sb.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "board") //연관관계는 ToString은 하지 않도록 exclude 필요
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    private String replyText;

    private String replyer;

    //단방향 참조
    //관계형성
    @ManyToOne(fetch = FetchType.LAZY) //지연로딩
    private Board board;

    @CreationTimestamp
    private LocalDateTime replyDate;

    //reply text만 변경할 것이므로 따로 메서드로 정의
    public void setText(String replyText) {
        this.replyText = replyText;
    }
}
