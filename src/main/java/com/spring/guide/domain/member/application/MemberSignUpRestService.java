package com.spring.guide.domain.member.application;

import com.spring.guide.domain.member.domain.Member;
import com.spring.guide.domain.member.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class MemberSignUpRestService {

  private final RestTemplate localTestTemplate;

  public Member requestSginUp(final SignUpRequest dto) {
    final ResponseEntity<Member> response = localTestTemplate
        .postForEntity("/members", dto, Member.class);
    return response.getBody();
  }
}
