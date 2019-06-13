package com.spring.guide.domain.member.dao;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.spring.guide.domain.member.domain.Member;
import com.spring.guide.domain.member.domain.MemberBuilder;
import com.spring.guide.domain.member.exception.EmailNotFoundException;
import com.spring.guide.domain.member.exception.MemberNotFoundException;
import com.spring.guide.test.MockTest;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class MemberFindDaoTest extends MockTest {

    private Member member;

    @Before
    public void setUp() throws Exception {
        member = MemberBuilder.build();
    }

    @InjectMocks
    private MemberFindDao memberFindDao;

    @Mock
    private MemberRepository memberRepository;

    @Test
    public void findById_존재하는_경우() {
        //given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        //when
        final Member member = memberFindDao.findById(anyLong());

        //then
        assertThat(member).isNotNull();
    }

    @Test(expected = MemberNotFoundException.class)
    public void findById_존재하지않은_경우() {
        //given
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        memberFindDao.findById(anyLong());
    }

    @Test
    public void findBy_Email_존재하는_경우() {
        //given
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));

        //when
        final Member member = memberFindDao.findByEmail(any());

        //then
        assertThat(member).isNotNull();
    }

    @Test(expected = EmailNotFoundException.class)
    public void findBy_Email_존재하지않은_경우() {
        //given
        given(memberRepository.findByEmail(any())).willReturn(Optional.empty());

        //when
        memberFindDao.findByEmail(member.getEmail());
    }

    @Test
    public void try_test() {

        try {
            // 비지니스 로직 수행...
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}