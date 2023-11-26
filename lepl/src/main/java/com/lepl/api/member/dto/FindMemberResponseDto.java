package com.lepl.api.member.dto;

import lombok.Getter;

@Getter
public class FindMemberResponseDto {
    private Long id;
    private String nickname;
    private Long level;

    public FindMemberResponseDto(Long memberId, String nickname, Long level) {
        this.id = memberId;
        this.nickname = nickname;
        this.level = level;
    }
}