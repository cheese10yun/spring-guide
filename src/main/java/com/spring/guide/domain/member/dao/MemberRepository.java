package com.spring.guide.domain.member.dao;

import com.spring.guide.domain.member.domain.Member;
import com.spring.guide.domain.member.domain.ReferralCode;
import com.spring.guide.domain.model.Email;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(Email email);

    boolean existsByEmail(Email email);

    boolean existsByReferralCode(ReferralCode referralCode);

}
