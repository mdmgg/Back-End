package com.example.mdmggreal.alarm.entity;

import com.example.mdmggreal.alarm.dto.AlarmDTO;
import com.example.mdmggreal.global.entity.BaseEntity;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.post.entity.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static java.lang.Boolean.FALSE;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostAlarm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    private Boolean isRead;
    private String alarmContents;

    public static PostAlarm from(AlarmDTO alarmDTO, Member member, Post post) {
        return PostAlarm.builder()
                .member(member)
                .post(post)
                .alarmContents(alarmDTO.getAlarmContents())
                .isRead(FALSE)
                .build();

    }


    public void editIsRead() {
        this.isRead = true;
    }
}
