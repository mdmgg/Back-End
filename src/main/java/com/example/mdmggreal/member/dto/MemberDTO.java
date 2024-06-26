package com.example.mdmggreal.member.dto;

import com.example.mdmggreal.ingameinfo.type.Tier;
import com.example.mdmggreal.member.entity.Member;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO  {

    /** 네이버_토큰_ID  */
    private String token;
    /** 이메일  */
    private String email;
    /** 닉네임  */
    private String nickname;
    /** 연령대  */
    private String age;
    /** 성별  */
    private String gender;
    /** 전화번호  */
    private String mobile;
    /** 프로필사진  */
    private String profileImage;
    /**
     * 티어
     */
    private String tier;
    /** 인증여부 */
    private boolean agreeAge;
    private boolean agreeTerms;
    private boolean agreePrivacy;
    private boolean agreePromotion;

    public static MemberDTO from(Member member) {
        return MemberDTO.builder()
                .nickname(member.getNickname())
                .email(member.getEmail())
                .mobile(member.getMobile())
                .tier(member.getTier().getName())
                .build();
    }
}
