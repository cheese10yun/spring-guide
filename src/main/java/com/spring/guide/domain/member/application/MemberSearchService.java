package com.spring.guide.domain.member.application;


import com.querydsl.jpa.JPQLQuery;
import com.spring.guide.domain.member.domain.Member;
import com.spring.guide.domain.member.domain.QMember;
import com.spring.guide.domain.member.domain.ReferralCode;
import com.spring.guide.domain.member.dto.MemberExistenceType;
import com.spring.guide.domain.model.Email;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class MemberSearchService extends QuerydslRepositorySupport {

    public MemberSearchService() {
        super(Member.class);
    }

    public boolean isExistTarget(final MemberExistenceType type, final String value) {
        final QMember qMember = QMember.member;
        final JPQLQuery<Member> query;

        switch (type) {
            case EMAIL:
                query = from(qMember)
                        .where(qMember.email.eq(Email.of(value)));
                break;

            case REFERRAL_CODE:
                query = from(qMember)
                        .where(qMember.referralCode.eq(ReferralCode.of(value)));
                break;
            default:
                throw new IllegalArgumentException(String.format("%s is not valid", type.name()));
        }

        final Member member = query.fetchFirst();
        return member != null;
    }
}
