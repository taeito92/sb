package org.zerock.sb.entity;

import lombok.*;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Member {

    @Id
    private String mid; //email일 수도 있다고 인지

    private String mpw;

    private String mname;

    @ElementCollection(fetch = FetchType.LAZY) //ElementCollection은 보통 Set으로 설계
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();

    public void changePassword(String password){
        this.mpw = password;
    }

}

