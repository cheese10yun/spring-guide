package com.spring.guide.domain.member.dao;

import static org.assertj.core.api.Java6Assertions.assertThat;

import com.spring.guide.domain.member.application.MemberSearchService;
import com.spring.guide.domain.member.domain.Member;
import com.spring.guide.domain.member.domain.ReferralCode;
import com.spring.guide.domain.member.dto.MemberExistenceType;
import com.spring.guide.domain.model.Email;
import com.spring.guide.test.IntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class MemberSearchServiceTest extends IntegrationTest {

    @Autowired
    private MemberSearchService memberSearchService;

    @Test
    public void isExistTarget_이메일_있는_경우_return_true() {
        //given
        final Member member = memberSetup.save();
        final String email = member.getEmail().getValue();

        //when
        final boolean existTarget = memberSearchService.isExistTarget(MemberExistenceType.EMAIL, email);

        //then
        assertThat(existTarget).isTrue();
    }

    @Test
    public void isExistTarget_이메일_없는_경우_return_true() {
        //given
        final String email = Email.of("notExist@email.com").getValue();

        //when
        final boolean existTarget = memberSearchService.isExistTarget(MemberExistenceType.EMAIL, email);

        //then
        assertThat(existTarget).isFalse();
    }

    @Test
    public void isExistTarget_추천인_있는_경우_return_true() {
        //given
        final Member member = memberSetup.save();
        final String code = member.getReferralCode().getValue();

        //when
        final boolean existTarget = memberSearchService.isExistTarget(MemberExistenceType.REFERRAL_CODE, code);

        //then
        assertThat(existTarget).isTrue();
    }

    @Test
    public void isExistTarget_추천인_없는_경우_return_false() {
        //given
        final String code = ReferralCode.generateCode().getValue();

        //when
        final boolean existTarget = memberSearchService.isExistTarget(MemberExistenceType.REFERRAL_CODE, code);

        //then
        assertThat(existTarget).isFalse();
    }


}