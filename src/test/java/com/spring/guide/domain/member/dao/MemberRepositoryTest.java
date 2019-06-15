package com.spring.guide.domain.member.dao;


import static org.assertj.core.api.Java6Assertions.assertThat;

import com.querydsl.core.types.Predicate;
import com.spring.guide.domain.member.domain.Member;
import com.spring.guide.domain.member.domain.MemberBuilder;
import com.spring.guide.domain.member.domain.QMember;
import com.spring.guide.domain.model.Email;
import com.spring.guide.domain.model.Name;
import com.spring.guide.test.RepositoryTest;
import com.spring.guide.test.setup.model.EmailBuilder;
import com.spring.guide.test.setup.model.NameBuilder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class MemberRepositoryTest extends RepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    private Member saveMember;
    private Email email;

    @Before
    public void setUp() throws Exception {
        final String value = "cheese10yun@gmail.com";
        email = EmailBuilder.build(value);
        final Name name = NameBuilder.build();
        saveMember = memberRepository.save(MemberBuilder.build(email, name));
    }

    @Test
    public void 회원객채생성() {
        assertThat(saveMember.getEmail().getValue()).isEqualTo(email.getValue());
        assertThat(saveMember.getName().getFirst()).isEqualTo("first");
        assertThat(saveMember.getName().getMiddle()).isEqualTo("middle");
        assertThat(saveMember.getName().getLast()).isEqualTo("last");
        assertThat(saveMember.getCreateAt())
            .isGreaterThanOrEqualTo(LocalDateTime.now().minusMinutes(1));
        assertThat(saveMember.getUpdateAt())
            .isGreaterThanOrEqualTo(LocalDateTime.now().minusMinutes(1));
    }

    @Test
    public void findByEmailTest() {
        final Optional<Member> byEmail = memberRepository.findByEmail(email);
        final Member member = byEmail.get();
        assertThat(member.getEmail().getValue()).isEqualTo(email.getValue());
    }

    @Test
    public void existsByEmail_존재하는경우_true() {
        final boolean existsByEmail = memberRepository.existsByEmail(email);
        assertThat(existsByEmail).isTrue();
    }

    @Test
    public void existsByEmail_존재하지않은_경우_false() {
        final boolean existsByEmail = memberRepository.existsByEmail(Email.of("ehdgoanfrhkqortntksdls@asd.com"));
        assertThat(existsByEmail).isFalse();
    }

    @Test
    public void predicate_test() {
        //given
        final QMember qMember = QMember.member;
        final Predicate predicate = qMember.email.eq(Email.of("cheese10yun@gmail.com"));

        //when
        final boolean exists = memberRepository.exists(predicate);

        //then
        assertThat(exists).isTrue();
    }

    @Test
    public void searchByEmail_test() {
        final List<Member> members = memberRepository.searchByEmail(email);
        assertThat(members).contains(saveMember);
        assertThat(members.size()).isGreaterThanOrEqualTo(1);
    }
}