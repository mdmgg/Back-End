package com.example.mdmggreal.member.service;

import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.exception.ErrorCode;
import com.example.mdmggreal.global.security.CustomUserInfoDto;
import com.example.mdmggreal.global.security.JwtUtil;
import com.example.mdmggreal.member.dto.request.SignUpRequest;
import com.example.mdmggreal.member.dto.response.SignUpResponse;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repository.MemberRepository;
import com.example.mdmggreal.oauth.dto.AuthTokens;
import com.example.mdmggreal.vote.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final VoteService voteService;
    private final JwtUtil jwtUtil;

    public SignUpResponse signUp(SignUpRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }
        Member savedMember = memberRepository.save(Member.from(request));

        AuthTokens createdTokens = jwtUtil.createTokens(CustomUserInfoDto.of(savedMember));

        return SignUpResponse.from(savedMember, createdTokens);
    }

    public Boolean checkNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            return true;
        } else {
            return false;
        }
    }

    public void updateMemberPoint(Member member, long point) {

    }

    public void updateMemberTier(Member member) {
        /*
        1. 회원이 투표한 정보중 과실 비율을 가장 크게 한 리스트 목록을 가져온다
        2. 가져온 투표와 선택한 챔피언에 대해서 그 게시글의 과실 비율이 가장 높은 정보를 가져와서 챔피언 이름과 과실을 맞췃는지 확인
        3. 그렇게 해서 총 맞춘 판결의 수를 가져온다
        4. 참여판 재판의 수는 투표를 group by postId 로 해서 가져온다
         */
    }


}
