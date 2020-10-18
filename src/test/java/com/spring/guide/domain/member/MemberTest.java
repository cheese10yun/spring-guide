package com.spring.guide.domain.member;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.spring.guide.domain.member.domain.Member;
import com.spring.guide.domain.member.domain.MemberBuilder;
import com.spring.guide.domain.model.Email;
import com.spring.guide.domain.model.Name;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.Test;


public class MemberTest {

    @Test
    public void 회원객체_테스트() {

        final String value = "cheese10yun@gmail.com";
        final Email email = Email.of(value);

        final Name name = Name.builder()
                .first("first")
                .middle("middle")
                .last("last")
                .build();

        final Member member = MemberBuilder.build(email, name);

        assertThat(member.getEmail().getValue()).isEqualTo(value);
        assertThat(member.getName().getFirst()).isEqualTo("first");
        assertThat(member.getName().getMiddle()).isEqualTo("middle");
        assertThat(member.getName().getLast()).isEqualTo("last");
    }

    @Test
    public void updateProfile() {

        final Member member = MemberBuilder.build();
        final Name name = Name.builder()
                .first("fff")
                .middle("mmm")
                .last("lll")
                .build();

        member.updateProfile(name);

        assertThat(member.getName().getFirst()).isEqualTo(name.getFirst());
        assertThat(member.getName().getFullName()).isEqualTo(name.getFullName());
        assertThat(member.getName().getMiddle()).isEqualTo(name.getMiddle());
        assertThat(member.getName().getLast()).isEqualTo(name.getLast());
    }

    @Test
    public void toString_test() {
        final Member member = MemberBuilder.build();
        final String memberToString = member.toString();

        assertThat(memberToString).contains(
                member.getEmail().getValue(),
                member.getName().getFirst(),
                member.getName().getLast(),
                member.getName().getMiddle()
        );
    }

    @Test
    public void asdasd() {

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final String text = "2016-03-04 11:30:40";

        final LocalDateTime now = LocalDateTime.parse(text, formatter);
        final LocalDate localDate = LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth());

        final Date date = Date.valueOf(localDate);

        System.out.println(date);



    }

    @Test
    public void name() {
        final String text = "2016-03-04 11:30:40";
        final String substring = text.substring(0, 10);
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        final LocalDate now = LocalDate.parse(substring, formatter);
        System.out.println(now);



        System.out.println(substring);
    }

    @Test
    public void asdasds() {
        final LocalDate date = LocalDate.of(2020, 12, 12);
        date.plusDays(1);




        final Date date1 = Date.valueOf("2019-12-12");
        System.out.printf(date1.toString());
    }

    @Test
    public void adasdasdasd() {

        Date date =  Date.valueOf("2019-12-12");



    }
}