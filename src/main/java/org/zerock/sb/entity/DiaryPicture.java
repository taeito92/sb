package org.zerock.sb.entity;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable // 걸어주면 엘리먼트 컬렉션으로 처리 가능
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of="uuid")
public class DiaryPicture implements Comparable<DiaryPicture> {

    private String uuid;
    private String fileName;
    private String savePath;
    private int idx;


    @Override
    public int compareTo(DiaryPicture o) {
        return this.idx - o.idx;
    }
}
