package com.example.mdmggreal.vote.service;

import com.example.mdmggreal.ingameinfo.entity.InGameInfo;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.service.MemberService;
import com.example.mdmggreal.post.entity.Post;
import com.example.mdmggreal.vote.dto.VoteSaveDTO;
import com.example.mdmggreal.vote.entity.Vote;
import com.example.mdmggreal.vote.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VoteService {

    private final MemberService memberService;
    private final VoteRepository voteRepository;

    public List<Map<String, Object>> getChampionNamesWithAverageRatioByPostId(Long postId) {
        List<Object[]> results = voteRepository.findChampionNamesWithAverageRatioByPostId(postId);
        List<Map<String, Object>> averageVotes = new ArrayList<>();

        for(Object[] result : results) {
            InGameInfo inGameInfo = (InGameInfo)result[0];
            Double average = (Double)result[1];

            Map<String, Object> map = new HashMap<>();
            map.put("championName", inGameInfo.getChampionName());
            map.put("averageValue", average);

            averageVotes.add(map);
        }
        return averageVotes;
    }

    public List<Post> getVotedPostsByMemberId(String token) {
        Member memberByToken = memberService.getMemberByToken(token);
        List<Vote> votes = voteRepository.findByMemberId(memberByToken.getId());
        return votes.stream()
                .map(Vote::getInGameInfo)
                .filter(Objects::nonNull)
                .map(InGameInfo::getPost)
                .collect(Collectors.toList());
    }

    public List<Vote> saveVotes(List<VoteSaveDTO> voteSaveDTOS, String token) {
        List<Vote> votes = voteSaveDTOS.stream()
                .map(voteSaveDTO -> convertToEntity(voteSaveDTO, token))
                .collect(Collectors.toList());
        return voteRepository.saveAll(votes);
    }

    public Vote convertToEntity(VoteSaveDTO voteSaveDTO, String token) {
        Member memberByToken = memberService.getMemberByToken(token);
        InGameInfo inGameInfo = new InGameInfo(voteSaveDTO.getIngameInfoId());

        return Vote.builder()
                .ratio(voteSaveDTO.getRatio())
                .memberId(memberByToken.getId())
                .inGameInfo(inGameInfo)
                .build();
    }
}
