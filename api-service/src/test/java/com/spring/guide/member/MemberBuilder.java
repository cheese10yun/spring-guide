package com.spring.guide.member;

import com.spring.guide.domain.member.Member;
import com.spring.guide.model.Email;
import com.spring.guide.model.Name;


public class MemberBuilder {

    public static Member build() {
        final String value = "cheese10yun@gmail.com";
        final Email email = Email.of(value);
        final Name name = nameBuild();
        return createMember(email, name);
    }

    public static Member build(Email email, Name name) {
        return createMember(email, name);
    }

    private static Member createMember(Email email, Name name) {
        return Member.builder()
                .email(email)
                .name(name)
                .build();
    }

    private static Name nameBuild() {
        return Name.builder()
                .first("first")
                .middle("middle")
                .last("last")
                .build();
    }


}