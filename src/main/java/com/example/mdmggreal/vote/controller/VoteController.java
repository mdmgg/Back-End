package com.example.mdmggreal.vote.controller;

import com.example.mdmggreal.global.response.BaseResponse;
import com.example.mdmggreal.post.entity.Post;
import com.example.mdmggreal.vote.dto.VoteSaveDTO;
import com.example.mdmggreal.vote.service.VoteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/vote")
public class VoteController {

    private final VoteService voteService;

    @PostMapping("/save")
    public ResponseEntity<BaseResponse> save(@RequestBody List<VoteSaveDTO> voteSaveDTOS, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("token");
        voteService.saveVotes(voteSaveDTOS, token);
        return ResponseEntity.ok(BaseResponse.from(HttpStatus.OK));
    }

    @GetMapping("/users")
    public ResponseEntity<List<Post>> getVotedPostsByMemberId(HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("token");
        List<Post> votedPosts = voteService.getVotedPostsByMemberId(token);
        return ResponseEntity.ok(votedPosts);
    }

    @GetMapping("/avg")
    public ResponseEntity<List<Map<String, Object>>> getChampionAverages(@RequestParam Long postId) {
        List<Map<String, Object>> averageVotes = voteService.getChampionNamesWithAverageRatioByPostId(postId);
        return ResponseEntity.ok(averageVotes);
    }

}
