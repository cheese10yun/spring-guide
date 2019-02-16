package com.spring.guide.domain.member;

import com.spring.guide.model.Email;
import com.spring.guide.utile.setup.model.EmailBuilder;
import com.spring.guide.model.Name;
import com.spring.guide.utile.setup.model.NameBuilder;
import com.spring.guide.utile.RepositoryTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


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
        assertThat(saveMember.getCreateAt()).isAfter(LocalDateTime.now().minusMinutes(1));
        assertThat(saveMember.getUpdateAt()).isAfter(LocalDateTime.now().minusMinutes(1));
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
}