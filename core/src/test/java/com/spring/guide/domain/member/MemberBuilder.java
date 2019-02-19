package com.spring.guide.domain.member;

import com.spring.guide.model.Email;
import com.spring.guide.model.Name;


public class MemberBuilder {

    public static Member build() {
        final String value = "cheese10yun@gmail.com";
        final Email email = Email.of(value);
        final Name name = nameBuild();
        final ReferralCode referralCode = ReferralCode.generateCode();
        return createMember(email, name, referralCode);
    }

    public static Member build(Email email, Name name) {
        final ReferralCode referralCode = ReferralCode.generateCode();
        return createMember(email, name, referralCode);
    }

    private static Member createMember(Email email, Name name, ReferralCode referralCode) {
        return Member.builder()
                .email(email)
                .name(name)
                .referralCode(referralCode)
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