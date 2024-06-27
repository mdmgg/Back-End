package com.example.mdmggreal.ingameinfo.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Tier {
    CHALLENGER("챌린저", 2000L, 1200L),
    GRANDMASTER("그랜드마스터", 1000L, 600L),
    MASTER("마스터", 500L, 300L),
    DIAMOND("다이아몬드", 300L, 180L),
    EMERALD("에메랄드", 200L, 120L),
    PLATINUM("플래티넘", 150L, 90L),
    GOLD("골드" , 100L, 60L),
    SILVER("실버", 50L, 30L),
    BRONZE("브론즈", 30L, 18L),
    IRON("아이언", 1L, 1L),
    UNRANK("언랭", 0L, 0L);
    private final String name;
    private final Long totalVoteCount;
    private final Long correctedVoteCount;


    public static Tier fromName(String name) {
        for (Tier tier : Tier.values()) {
            if (tier.getName().equalsIgnoreCase(name)) {
                return tier;
            }
        }
        throw new IllegalArgumentException("No enum constant with name " + name);
    }
}
