package com.example.mdmggreal.vote.service;

import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.exception.ErrorCode;
import com.example.mdmggreal.ingameinfo.entity.InGameInfo;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repository.MemberRepository;
import com.example.mdmggreal.member.service.MemberService;
import com.example.mdmggreal.post.entity.Post;
import com.example.mdmggreal.post.repository.PostQueryRepository;
import com.example.mdmggreal.post.repository.PostRepository;
import com.example.mdmggreal.vote.dto.VoteAvgDTO;
import com.example.mdmggreal.vote.dto.VoteSaveDTO;
import com.example.mdmggreal.vote.entity.Vote;
import com.example.mdmggreal.vote.repository.VoteQueryRepository;
import com.example.mdmggreal.vote.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.mdmggreal.global.exception.ErrorCode.VOTE_ALREADY_EXISTS;

@Service
@AllArgsConstructor
public class VoteService {

    private final MemberRepository memberRepository;
    private final VoteRepository voteRepository;
    private final VoteQueryRepository voteQueryRepository;
    private final PostRepository postRepository;
    private final PostQueryRepository postQueryRepository;
    private final MemberService memberService;

    public List<Vote> saveVotes(List<VoteSaveDTO> voteSaveDTOS, String mobile, Long postId) {
        Member member = getMemberByMobile(mobile);
        checkIfVoteExists(postId, member.getId());

        List<Vote> votes = voteSaveDTOS.stream()
                .map(voteSaveDTO -> convertToEntity(voteSaveDTO, member))
                .collect(Collectors.toList());
        return voteRepository.saveAll(votes);
    }

    public List<VoteAvgDTO> getChampionNamesWithAverageRatioByPostId(Long postId) {
        return voteRepository.findChampionNamesWithAverageRatioByPostId(postId).stream()
                .map(this::convertToVoteAvgDTO)
                .collect(Collectors.toList());
    }

    public List<Post> getVotedPostsByMemberId(String mobile) {
        Member member = getMemberByMobile(mobile);
        return voteRepository.findByMemberId(member.getId()).stream()
                .map(Vote::getInGameInfo)
                .filter(Objects::nonNull)
                .map(InGameInfo::getPost)
                .collect(Collectors.toList());
    }

    public List<Vote> getHighRatioVotesByMemberId(Long memberId) {
        List<Vote> voteList = voteQueryRepository.findHighRatioVotesByMemberId(memberId);
        int collectCount = calculateCollectCount(voteList);

        Member member = getMemberById(memberId);
        member.editTier(collectCount, voteList.size());
        return voteList;
    }

    private int calculateCollectCount(List<Vote> voteList) {
        int collectCount = 0;
        for (Vote vote : voteList) {
            Post post = postQueryRepository.findByVoteId(vote.getId());
            Double topRatioByPostId = voteQueryRepository.findTopRatioByPostId(post.getId());
            if (vote.getRatio() >= topRatioByPostId) {
                collectCount += 1;
            }
        }
        return collectCount;
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_USER_ID)
        );
    }

    private Member getMemberByMobile(String mobile) {
        return memberRepository.findByMobile(mobile).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_USER_ID)
        );
    }

    private void checkIfVoteExists(Long postId, Long memberId) {
        if (voteQueryRepository.existsVoteByMemberId(postId, memberId)) {
            throw new CustomException(VOTE_ALREADY_EXISTS);
        }
    }

    private VoteAvgDTO convertToVoteAvgDTO(Object[] result) {
        InGameInfo inGameInfo = (InGameInfo) result[0];
        Double average = (Double) result[1];
        return new VoteAvgDTO(
                inGameInfo.getChampionName(), average, inGameInfo.getPosition(), inGameInfo.getTier()
        );
    }

    private Vote convertToEntity(VoteSaveDTO voteSaveDTO, Member member) {
        InGameInfo inGameInfo = new InGameInfo(voteSaveDTO.getIngameInfoId());
        return Vote.builder()
                .ratio(voteSaveDTO.getRatio())
                .memberId(member.getId())
                .inGameInfo(inGameInfo)
                .build();
    }
}
