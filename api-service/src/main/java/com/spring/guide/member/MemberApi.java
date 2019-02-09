package com.spring.guide.member;

import com.spring.guide.domain.member.Member;
import com.spring.guide.member.dto.MemberResponse;
import com.spring.guide.member.dto.SignUpRequest;
import com.spring.guide.member.service.MemberSignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("members")
@RequiredArgsConstructor
public class MemberApi {

    private final MemberSignUpService memberSignUpService;

    @PostMapping
    public MemberResponse create(@RequestBody @Valid final SignUpRequest dto) {
        final Member member = memberSignUpService.doSignUp(dto.getEmail(), dto.getName());
        final MemberResponse memberResponse = new MemberResponse(member);
        return memberResponse;
    }
}
