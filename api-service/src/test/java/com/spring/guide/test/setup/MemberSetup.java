package com.spring.guide.test.setup;


import com.spring.guide.domain.member.Member;
import com.spring.guide.domain.member.MemberRepository;
import com.spring.guide.model.Email;
import com.spring.guide.model.Name;
import com.spring.guide.test.config.TestProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Profile(TestProfile.TEST)
@RequiredArgsConstructor
@Component
public class MemberSetup {

    private final MemberRepository memberRepository;

    public Member save() {
        final Member member = buildMember("yun@test.com");
        return memberRepository.save(member);
    }

    public List<Member> save(int count) {
        List<Member> members = new ArrayList<>();
        IntStream.range(0, count).forEach(i -> members.add(
                memberRepository.save(buildMember(String.format("test00%d@test.com", i))))
        );
        return members;
    }

    public Member build(){
        return buildMember("yun@test.com");
    }

    private Member buildMember(String email) {
        return Member.builder()
                .email(Email.of(email))
                .name(Name.builder()
                        .first("first")
                        .middle("middle")
                        .last("last")
                        .build())
                .build();
    }

}
