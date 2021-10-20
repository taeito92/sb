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
@ToString(exclude = "board") // 보드는 뺴고 투스트링하세요~
public class Reply {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long rno;

    private String replyText;

    private String replyer;

    //lazy 이것이 나주엥 실제로 보드가 필요할때까지 안가져오는 것 - 지연로딩
    @ManyToOne(fetch = FetchType.LAZY) // 이거를 빼면 어떤 관계인지 알 수 없기 때문에 선언을 꼭해줘야해 제일 중요
    private Board board; // 마이바티즈와 차이 이전에는 bno로 걸었지만 이제는 아니야

    @CreationTimestamp
    private LocalDateTime replyDate;

    public void setText(String text){
        this.replyText = text;
    }
}
