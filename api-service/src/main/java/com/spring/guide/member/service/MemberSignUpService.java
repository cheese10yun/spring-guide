package com.spring.guide.member.service;

import com.spring.guide.domain.member.Member;
import com.spring.guide.domain.member.MemberRepository;
import com.spring.guide.member.exception.EmailDuplicateException;
import com.spring.guide.model.Email;
import com.spring.guide.model.Name;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberSignUpService {

    private final MemberRepository memberRepository;

    public Member doSignUp(final Email email, final Name name) {

        if (memberRepository.existsByEmail(email)) {
            throw new EmailDuplicateException(email);
        }

        final Member member = Member.builder()
                .name(name)
                .email(email)
                .build();

        return memberRepository.save(member);
    }

}
