package com.example.mdmggreal.member.entity;

import com.example.mdmggreal.global.entity.BaseEntity;
import com.example.mdmggreal.ingameinfo.type.Tier;
import com.example.mdmggreal.member.dto.MemberDTO;
import com.example.mdmggreal.member.dto.request.SignUpRequest;
import com.example.mdmggreal.member.type.Agree;
import com.example.mdmggreal.member.type.OAuthProvider;
import com.example.mdmggreal.member.type.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.example.mdmggreal.member.type.OAuthProvider.NAVER;
import static com.example.mdmggreal.member.type.Role.USER;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id")
    private Long id;

    /** 이메일  */
    @Column(nullable = false)
    private String email;

    /** 닉네임  */
    @Column(nullable = false)
    private String nickname;

    /**
     * 휴대전화번호
     * 일단 null 로 두고, 추후 전화번호 인증 추가되면 로직추가하기
     */
    private String mobile;

    /** 프로필사진  */
    @Column(nullable = false)
    private String profileImage;

    /** 티어  */
    private Tier tier;

    /** 인증 */
    @Enumerated(STRING)
    private Role role;

    /** 포인트  */
    private Integer point;

    @Embedded
    private Agree agree;

    /** SNS 가입 경로  */
    private OAuthProvider oAuthProvider;

    public static Member from(MemberDTO memberDTO) {
        return Member.builder()
                .email(memberDTO.getEmail())
                .nickname(memberDTO.getNickname())
                .mobile(memberDTO.getMobile())
                .profileImage(memberDTO.getProfileImage())
                .agree(Agree.builder()
                        .agreeAge(memberDTO.isAgreeAge())
                        .agreeTerms(memberDTO.isAgreeTerms())
                        .agreePrivacy(memberDTO.isAgreePrivacy())
                        .agreePromotion(memberDTO.isAgreePromotion())
                        .build())
                .role(USER)
                .tier(Tier.IRON)
                .oAuthProvider(NAVER)
                .build();
    }

    /**
     * 네이버 로그인
     */
    public static Member from(SignUpRequest request) {
        return Member.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .mobile(null)
                .profileImage(request.getProfileImage())
                .agree(Agree.builder()
                        .agreeAge(request.getAgrees().isAgreeAge())
                        .agreeTerms(request.getAgrees().isAgreeTerms())
                        .agreePrivacy(request.getAgrees().isAgreePrivacy())
                        .agreePromotion(request.getAgrees().isAgreePromotion())
                        .build())
                .role(USER)
                .tier(Tier.IRON)
                .oAuthProvider(NAVER)
                .build();
    }
}
