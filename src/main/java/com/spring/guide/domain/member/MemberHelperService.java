package com.spring.guide.domain.member;

import com.spring.guide.domain.member.domain.Member;
import com.spring.guide.domain.member.exception.EmailNotFoundException;
import com.spring.guide.domain.member.exception.MemberNotFoundException;
import com.spring.guide.domain.model.Email;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberHelperService {

    private final MemberRepository memberRepository;

    public Member findById(final Long id) {
        final Optional<Member> member = memberRepository.findById(id);
        member.orElseThrow(() -> new MemberNotFoundException(id));
        return member.get();
    }

    public Member findByEmail(final Email email) {
        final Optional<Member> member = memberRepository.findByEmail(email);
        member.orElseThrow(() -> new EmailNotFoundException(email.getValue()));
        return member.get();
    }

}
