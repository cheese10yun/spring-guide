package com.spring.guide.domain.member;

import com.spring.guide.model.Email;
import com.spring.guide.model.Name;
import com.spring.guide.utile.JpaTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class MemberRepositoryTest extends JpaTest {

    @Autowired
    private MemberRepository memberRepository;

    private Member saveMember;
    private Email email;

    @Before
    public void setUp() throws Exception {
        final String value = "cheese10yun@gmail.com";
        email = Email.of(value);

        final Name name = Name.builder()
                .first("first")
                .middle("middle")
                .last("last")
                .build();

        saveMember = memberRepository.save(MemberBuilder.build(email, name));
    }

    @Test
    public void 회원객채생성() {
        assertThat(saveMember.getEmail().getValue()).isEqualTo(email.getValue());
        assertThat(saveMember.getName().getFirst()).isEqualTo("first");
        assertThat(saveMember.getName().getMiddle()).isEqualTo("middle");
        assertThat(saveMember.getName().getLast()).isEqualTo("last");
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