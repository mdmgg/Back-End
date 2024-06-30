package com.example.mdmggreal.batch.config;

import com.example.mdmggreal.alarm.service.PostAlarmService;
import com.example.mdmggreal.post.entity.Post;
import com.example.mdmggreal.post.repository.PostRepository;
import com.example.mdmggreal.vote.entity.Vote;
import com.example.mdmggreal.vote.repository.VoteQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class PostBatchConfig extends DefaultBatchConfiguration {
    private final PostRepository postRepository;
    private final VoteQueryRepository voteQueryRepository;
    private final PostAlarmService postAlarmService;

    @Bean
    public Job updatePostJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("updatePostJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(updatePostStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step updatePostStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("updatePostStep", jobRepository)
                .tasklet(updatePostTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Tasklet updatePostTasklet() {
        return (contribution, chunkContext) -> {
            LocalDateTime now = LocalDateTime.now();
            List<Post> postList = postRepository.findByEndDateTimeBefore(now);
            processPostsAfterEndDate(postList);
            return RepeatStatus.FINISHED;
        };
    }

    private void processPostsAfterEndDate(List<Post> postList) {
        if (!postList.isEmpty()) {
            for (Post post : postList) {
                post.editStatus();
                addPostAlarm(post);
            }
        }
    }

    private void addPostAlarm(Post post) {
        List<Vote> voteList = voteQueryRepository.getVoteListByPostId(post.getId());
        if (!voteList.isEmpty()) {
            for (Vote vote : voteList) {
                postAlarmService.addAlarm(post, vote.getMemberId());
            }
        }

    }
}