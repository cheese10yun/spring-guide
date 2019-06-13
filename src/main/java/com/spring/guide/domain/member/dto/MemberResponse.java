package com.spring.guide.domain.member.dto;

import com.spring.guide.domain.member.domain.Member;
import com.spring.guide.domain.model.Email;
import com.spring.guide.domain.model.Name;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponse {

    private Email email;

    private Name name;

    public MemberResponse(final Member member) {
        this.email = member.getEmail();
        this.name = member.getName();
    }
}
