package org.zerock.sb.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "uuid") //uuid값이 같으면 같은 객체다
public class DiaryPictureDTO {

    private String uuid;
    private String fileName;
    private String savePath;
    private int idx;

    public String getLink(){
        return savePath +"/s_"+uuid+"_"+fileName;
    }
}
