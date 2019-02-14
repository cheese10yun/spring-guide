package com.spring.guide.member.service;

import com.spring.guide.domain.member.Member;
import com.spring.guide.domain.member.MemberRepository;
import com.spring.guide.member.dto.SignUpRequest;
import com.spring.guide.member.exception.EmailDuplicateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberSignUpService {

    private final MemberRepository memberRepository;

    public Member doSignUp(final SignUpRequest dto) {

        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new EmailDuplicateException(dto.getEmail());
        }

        return memberRepository.save(dto.toEntity());
    }

}
