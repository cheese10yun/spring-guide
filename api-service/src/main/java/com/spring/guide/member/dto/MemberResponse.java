package com.spring.guide.member.dto;

import com.spring.guide.domain.member.Member;
import com.spring.guide.model.Email;
import com.spring.guide.model.Name;
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
