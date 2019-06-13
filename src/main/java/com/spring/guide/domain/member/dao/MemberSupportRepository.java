package com.spring.guide.domain.member.dao;

import com.spring.guide.domain.member.domain.Member;
import com.spring.guide.domain.model.Email;
import java.util.List;

public interface MemberSupportRepository {

  List<Member> searchByEmail(Email email);
}
