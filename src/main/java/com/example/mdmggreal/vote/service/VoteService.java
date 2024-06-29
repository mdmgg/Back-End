package com.example.mdmggreal.vote.service;

import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.exception.ErrorCode;
import com.example.mdmggreal.ingameinfo.entity.InGameInfo;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repository.MemberRepository;
import com.example.mdmggreal.post.entity.Post;
import com.example.mdmggreal.vote.dto.VoteAvgDTO;
import com.example.mdmggreal.vote.dto.VoteSaveDTO;
import com.example.mdmggreal.vote.entity.Vote;
import com.example.mdmggreal.vote.repository.VoteQueryRepository;
import com.example.mdmggreal.vote.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.mdmggreal.global.exception.ErrorCode.INVALID_USER_ID;
import static com.example.mdmggreal.global.exception.ErrorCode.VOTE_ALREADY_EXISTS;

@Service
@AllArgsConstructor
public class VoteService {

    private final MemberRepository memberRepository;

    private final VoteRepository voteRepository;
    private final VoteQueryRepository voteQueryRepository;

    public List<Vote> saveVotes(List<VoteSaveDTO> voteSaveDTOS, Long memberId, Long postId) {
        Member member = getMemberByMemberId(memberId);
        boolean isVote = voteQueryRepository.existsVoteByMemberId(postId, member.getId());
        if (isVote) {
            throw new CustomException(VOTE_ALREADY_EXISTS);
        }

        List<Vote> votes = voteSaveDTOS.stream()
                .map(voteSaveDTO -> convertToEntity(voteSaveDTO, memberId))
                .collect(Collectors.toList());
        return voteRepository.saveAll(votes);
    }

    public List<VoteAvgDTO> getChampionNamesWithAverageRatioByPostId(Long postId) {
        List<Object[]> results = voteRepository.findChampionNamesWithAverageRatioByPostId(postId);
        List<VoteAvgDTO> averageVotes = new ArrayList<>();

        for (Object[] result : results) {
            InGameInfo inGameInfo = (InGameInfo) result[0];
            Double average = (Double) result[1];

            VoteAvgDTO dto = new VoteAvgDTO(
                    inGameInfo.getChampionName(), average, inGameInfo.getPosition(), inGameInfo.getTier());
            averageVotes.add(dto);
        }
        return averageVotes;
    }

    public List<Post> getVotedPostsByMemberId(Long memberId) {
        Member member = getMemberByMemberId(memberId);
        List<Vote> votes = voteRepository.findByMemberId(member.getId());
        return votes.stream()
                .map(Vote::getInGameInfo)
                .filter(Objects::nonNull)
                .map(InGameInfo::getPost)
                .collect(Collectors.toList());
    }


    public Vote convertToEntity(VoteSaveDTO voteSaveDTO, Long memberId) {
        Member member = getMemberByMemberId(memberId);
        InGameInfo inGameInfo = new InGameInfo(voteSaveDTO.getIngameInfoId());
        return Vote.builder()
                .ratio(voteSaveDTO.getRatio())
                .memberId(member.getId())
                .inGameInfo(inGameInfo)
                .build();
    }

    private Member getMemberByMemberId(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(INVALID_USER_ID)
        );
    }
}
