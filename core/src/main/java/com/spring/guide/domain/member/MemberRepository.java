package com.spring.guide.domain.member;

import com.spring.guide.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(Email email);

    boolean existsByEmail(Email email);

    boolean existsByReferralCode(ReferralCode referralCode);

}
