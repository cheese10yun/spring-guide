package com.spring.guide.domain.member.dao;

import com.spring.guide.domain.member.domain.Member;
import com.spring.guide.domain.member.domain.QMember;
import com.spring.guide.domain.model.Email;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class MemberSupportRepositoryImpl extends QuerydslRepositorySupport implements
    MemberSupportRepository {

  public MemberSupportRepositoryImpl() {
    super(Member.class);
  }

  @Override
  public List<Member> searchByEmail(final Email email) {

    final QMember qMember = QMember.member;

    return from(qMember)
        .where(qMember.email.value.like(email.getValue() + "%"))
        .fetch();
  }

}
