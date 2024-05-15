package com.example.mdmggreal.member.controller;

import com.example.mdmggreal.member.dto.MemberDTO;
import com.example.mdmggreal.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.example.mdmggreal.global.exception.ErrorCode.NICKNAME_ALREADY_EXISTS;

@RestController
@RequestMapping("/api/users")
@SessionAttributes("token") // 세션에 memberDTO 속성을 추가
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /*
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody MemberDTO memberDTO) {
        try {
            memberService.signup(memberDTO);
            return ResponseEntity.ok("회원가입이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("회원가입에 실패했습니다. " + e.getMessage());
        }
    }

    /*
     * 닉네임 중복 체크
     */
    @GetMapping("/check")
    public ResponseEntity<?> checkNickname(@RequestParam String nickname) {
        if (memberService.isNicknameAvailable(nickname)) {
            return ResponseEntity.ok("닉네임을 사용할 수 있습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(NICKNAME_ALREADY_EXISTS);
        }
    }

    /*
     * 네이버 로그인 정보 콜백
     */
    @GetMapping("/callback")
    public JSONObject callback(HttpServletRequest request) throws Exception {
        MemberDTO memberDTO = memberService.getNaverInfo(request.getParameter("code"));

        String isMemberYn = "N";
        String isMobileYn = "N";

        if(memberService.isTokenExist(memberDTO.getToken())) {
            isMemberYn = "Y";
        }

        if (memberService.isMobileExist(memberDTO.getMobile())) {
            isMobileYn = "Y";
        }

        Map<String, Object> response = new HashMap<>();
        response.put("member", memberDTO);
        response.put("isMemberYn", isMemberYn);
        response.put("isMobileYn", isMobileYn);

        return new JSONObject(response);
    }

    /*
     * 로그아웃 구현
     */
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate(); // 세션 삭제
        return ResponseEntity.ok("로그아웃되었습니다.");
    }

}
