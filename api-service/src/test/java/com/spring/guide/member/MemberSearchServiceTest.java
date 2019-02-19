package com.spring.guide.member;

import com.spring.guide.domain.member.Member;
import com.spring.guide.domain.member.ReferralCode;
import com.spring.guide.member.type.MemberExistenceType;
import com.spring.guide.model.Email;
import com.spring.guide.test.IntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Java6Assertions.assertThat;


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