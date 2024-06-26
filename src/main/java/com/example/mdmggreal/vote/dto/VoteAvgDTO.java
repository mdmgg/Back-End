package com.example.mdmggreal.vote.dto;

import com.example.mdmggreal.ingameinfo.type.Position;
import com.example.mdmggreal.ingameinfo.type.Tier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteAvgDTO {
    private String championName;
    private Double averageValue;
    private Position position;
    private Tier tier;
}
