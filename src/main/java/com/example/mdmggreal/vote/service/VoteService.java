package com.example.mdmggreal.vote.service;

import com.example.mdmggreal.ingameinfo.entity.InGameInfo;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.service.MemberService;
import com.example.mdmggreal.vote.dto.VoteDTO;
import com.example.mdmggreal.vote.entity.Vote;
import com.example.mdmggreal.vote.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VoteService {

    private final MemberService memberService;

    private final VoteRepository voteRepository;

    public List<VoteAvgDTO> getChampionNamesWithAverageRatioByPostId(Long postId) {
        List<Object[]> results = voteRepository.findChampionNamesWithAverageRatioByPostId(postId);
        List<VoteAvgDTO> averageVotes = new ArrayList<>();

        for(Object[] result : results) {
            InGameInfo inGameInfo = (InGameInfo)result[0];
            Double average = (Double)result[1];

            VoteAvgDTO dto = new VoteAvgDTO(inGameInfo.getChampionName(), average);
            averageVotes.add(dto);
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
        Member member = getMember(mobile);
        List<Vote> votes = voteSaveDTOS.stream()
                .map(voteSaveDTO -> convertToEntity(voteSaveDTO, mobile))
                .collect(Collectors.toList());
        return voteRepository.saveAll(votes);
    }

    public Vote convertToEntity(VoteSaveDTO voteSaveDTO, String token) {
        Member member = getMember(mobile);
        InGameInfo inGameInfo = new InGameInfo(voteDTO.getIngameInfoId());
        return Vote.builder()
                .ratio(voteDTO.getRatio())
                .memberId(member.getId())
                .inGameInfo(inGameInfo)
                .build();
    }
    private Member getMember(String mobile) {
        return memberRepository.findByMobile(mobile).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_USER_ID)
        );
    }
}
