package com.spring.guide.domain.member;

import com.spring.guide.model.Email;
import com.spring.guide.model.Name;
import org.assertj.core.api.Java6Assertions;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class MemberTest {

    @Test
    public void 회원객체_테스트() {

        final String value = "cheese10yun@gmail.com";
        final Email email = Email.of(value);

        final Name name = Name.builder()
                .first("first")
                .middle("middle")
                .last("last")
                .build();

        final Member member = MemberBuilder.build(email, name);

        assertThat(member.getEmail().getValue()).isEqualTo(value);
        assertThat(member.getName().getFirst()).isEqualTo("first");
        assertThat(member.getName().getMiddle()).isEqualTo("middle");
        assertThat(member.getName().getLast()).isEqualTo("last");
    }

    @Test
    public void updateProfile() {

        final Member member = MemberBuilder.build();
        final Name name = Name.builder()
                .first("fff")
                .middle("mmm")
                .last("lll")
                .build();

        member.updateProfile(name);

        assertThat(member.getName().getFirst()).isEqualTo(name.getFirst());
        assertThat(member.getName().getFullName()).isEqualTo(name.getFullName());
        assertThat(member.getName().getMiddle()).isEqualTo(name.getMiddle());
        assertThat(member.getName().getLast()).isEqualTo(name.getLast());
    }

    @Test
    public void toString_test() {
        final Member member = MemberBuilder.build();
        final String memberToString = member.toString();

        assertThat(memberToString).contains(
                member.getEmail().getValue(),
                member.getName().getFirst(),
                member.getName().getLast(),
                member.getName().getMiddle()
        );
    }
}