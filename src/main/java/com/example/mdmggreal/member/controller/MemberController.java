package com.example.mdmggreal.member.controller;

import com.example.mdmggreal.member.dto.request.SignUpRequest;
import com.example.mdmggreal.member.dto.response.NicknameCheckResponse;
import com.example.mdmggreal.member.dto.response.SignUpResponse;
import com.example.mdmggreal.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /*
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signup(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(memberService.signUp(request));
    }

    /*
     * 닉네임 중복 체크
     */
    @GetMapping("/nicknamecheck")
    public ResponseEntity<NicknameCheckResponse> nicknameCheck(@RequestParam("nickname") String nickname) {
        Boolean check = memberService.checkNickname(nickname);
        return ResponseEntity.ok(NicknameCheckResponse.of(HttpStatus.OK, check));
    }
}