package com.spring.guide.member;

import com.spring.guide.domain.member.Member;
import com.spring.guide.member.dto.SignUpRequest;
import com.spring.guide.member.dto.SignUpRequestBuilder;
import com.spring.guide.model.Email;
import com.spring.guide.model.Name;
import com.spring.guide.utile.IntegrationTest;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberApiTest extends IntegrationTest {

    @Test
    public void 회원가입_성공() throws Exception {
        //given
        final Member member = MemberBuilder.build();
        final Email email = member.getEmail();
        final Name name = member.getName();
        final SignUpRequest dto = SignUpRequestBuilder.build(email, name);

        //when
        final ResultActions resultActions = requestSignUp(dto);

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("email.value").value(email.getValue()))
                .andExpect(jsonPath("email.host").value(email.getHost()))
                .andExpect(jsonPath("email.id").value(email.getId()))
                .andExpect(jsonPath("name.first").value(name.getFirst()))
                .andExpect(jsonPath("name.middle").value(name.getMiddle()))
                .andExpect(jsonPath("name.last").value(name.getLast()))
                .andExpect(jsonPath("name.fullName").value(name.getFullName()))
        ;
    }

    @Test
    public void 회원가입_유효하지않은_입력값() throws Exception {
        //given
        final Email email = Email.of("asdasd@d");
        final Name name = Name.builder().build();

        final SignUpRequest dto = SignUpRequestBuilder.build(email, name);

        //when
        final ResultActions resultActions = requestSignUp(dto);

        //then
        resultActions
                .andExpect(status().isBadRequest())
        ;

    }

    private ResultActions requestSignUp(SignUpRequest dto) throws Exception {
        return mvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print());
    }
}