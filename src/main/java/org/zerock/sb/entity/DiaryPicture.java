package org.zerock.sb.entity;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable //이 어노테이션을 걸면 element로 처리
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "uuid")
public class DiaryPicture implements Comparable<DiaryPicture>{

    private String uuid;
    private String fileName;
    private String savePath;
    private int idx; //조회 시 순번

    @Override
    public int compareTo(DiaryPicture o) {
        return o.idx - this.idx; // 음수,0,양수
    }
}