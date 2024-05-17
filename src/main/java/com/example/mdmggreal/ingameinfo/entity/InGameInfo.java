package com.example.mdmggreal.ingameinfo.entity;

import com.example.mdmggreal.global.entity.BaseEntity;
import com.example.mdmggreal.ingameinfo.dto.request.InGameInfoRequest;
import com.example.mdmggreal.ingameinfo.type.Position;
import com.example.mdmggreal.ingameinfo.type.Tier;
import com.example.mdmggreal.post.entity.Post;
import com.example.mdmggreal.vote.entity.Vote;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class InGameInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "in_game_info_id")
    private Long id;
    private String championName;
    @Enumerated(STRING)
    private Tier tier;
    private Position position;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "inGameInfo")
    private List<Vote> votes;


    public static InGameInfo of(InGameInfoRequest request, Post post) {

        return InGameInfo.builder()
                .championName(request.championName())
                .tier(request.tier())
                .position(request.position())
                .post(post)
                .build();
    }

    public InGameInfo(Long id) {
        this.id = id;
    }


}
