package com.spring.guide.member;

import com.spring.guide.domain.member.Member;
import com.spring.guide.excpetion.ErrorCode;
import com.spring.guide.member.dto.MemberProfileUpdate;
import com.spring.guide.member.dto.SignUpRequest;
import com.spring.guide.member.dto.SignUpRequestBuilder;
import com.spring.guide.model.Email;
import com.spring.guide.model.Name;
import com.spring.guide.test.IntegrationTest;
import com.spring.guide.test.setup.domain.MemberSetup;
import com.spring.guide.test.setup.request.MemberProfileUpdateBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class MemberApiTest extends IntegrationTest {

    @Autowired
    private MemberSetup memberSetup;

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

    @Test
    public void 회원조회() throws Exception {
        //given
        final Member member = memberSetup.save();

        //when
        final ResultActions resultActions = requestGetMember(member.getId());

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("email.value").value(member.getEmail().getValue()))
                .andExpect(jsonPath("email.host").value(member.getEmail().getHost()))
                .andExpect(jsonPath("email.id").value(member.getEmail().getId()))
                .andExpect(jsonPath("name.first").value(member.getName().getFirst()))
                .andExpect(jsonPath("name.middle").value(member.getName().getMiddle()))
                .andExpect(jsonPath("name.last").value(member.getName().getLast()))
                .andExpect(jsonPath("name.fullName").value(member.getName().getFullName()))
        ;
    }

    @Test
    public void 회원조회_회원이없는경우() throws Exception {
        //given

        //when
        final ResultActions resultActions = requestGetMember(0L);

        //then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(ErrorCode.ENTITY_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("status").value(ErrorCode.ENTITY_NOT_FOUND.getStatus()))
                .andExpect(jsonPath("code").value(ErrorCode.ENTITY_NOT_FOUND.getCode()))
                .andExpect(jsonPath("errors").isEmpty())
        ;

    }

    @Test
    public void 회원_프로필_업데이트() throws Exception {
        //given
        final Member member = memberSetup.save();
        final MemberProfileUpdate dto = MemberProfileUpdateBuilder.build();

        //when

        final ResultActions resultActions = requestUpdateProfile(dto, member.getId());

        //then
        resultActions
                .andExpect(status().isOk());

    }

    private ResultActions requestUpdateProfile(final MemberProfileUpdate dto, final long id) throws Exception {
        return mvc.perform(MockMvcRequestBuilders.put("/members/{id}/profile", id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print());
    }

    private ResultActions requestGetMember(long memberId) throws Exception {
        return mvc.perform(get("/members/{id}", memberId)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print());
    }

    private ResultActions requestSignUp(SignUpRequest dto) throws Exception {
        return mvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print());
    }
}