package com.spring.guide.member.service;

import com.spring.guide.domain.member.Member;
import com.spring.guide.domain.member.MemberHelperService;
import com.spring.guide.member.dto.MemberProfileUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberProfileService {

    private final MemberHelperService memberHelperService;

    public Member update(final long memberId, final MemberProfileUpdate dto) {
        final Member member = memberHelperService.findById(memberId);
        member.updateProfile(dto.getName());
        return member;
    }

}
