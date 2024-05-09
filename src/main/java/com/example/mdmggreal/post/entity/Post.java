package com.example.mdmggreal.post.entity;

import com.example.mdmggreal.base.entity.BaseEntity;
import com.example.mdmggreal.entity.Member;
import com.example.mdmggreal.post.dto.PostAddRequest;
import com.example.mdmggreal.post.entity.type.VideoType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

//    @ManyToOne
//    @JoinColumn(content = "member_id")
//    private Member member;

    @Embedded
    private Video video;
    private String title;
    private String content;
    private String thumbnailURL;
    private Long viewCount;

    public static Post of(PostAddRequest request,String thumbnailURL, String videoUrl) {
        return Post.builder()
//                .member(member)
                .title(request.title())
                .content(request.content())
                .thumbnailURL(thumbnailURL)
                .video(Video.of(videoUrl, request.type()))
                .viewCount(0L)
                .build();
    }


}
