package com.spring.guide.domain.member.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.spring.guide.domain.member.application.MemberProfileService;
import com.spring.guide.domain.member.application.MemberSearchService;
import com.spring.guide.domain.member.application.MemberSignUpService;
import com.spring.guide.domain.member.dao.MemberHelperService;
import com.spring.guide.domain.member.domain.Member;
import com.spring.guide.domain.member.domain.MemberBuilder;
import com.spring.guide.domain.member.dto.SignUpRequest;
import com.spring.guide.domain.member.dto.SignUpRequestBuilder;
import com.spring.guide.domain.model.Email;
import com.spring.guide.domain.model.Name;
import com.spring.guide.test.MockApiTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(MemberApi.class)
public class MemberMockApiTest extends MockApiTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private MemberSignUpService memberSignUpService;

    @MockBean
    private MemberHelperService memberHelperService;

    @MockBean
    private MemberProfileService memberProfileService;

    @MockBean
    private MemberSearchService memberSearchService;


    @Before
    public void setUp() {
        mvc = buildMockMvc(context);
    }


    @Test
    public void 회원가입_성공() throws Exception {
        //given
        final Member member = MemberBuilder.build();
        final Email email = member.getEmail();
        final Name name = member.getName();
        final SignUpRequest dto = SignUpRequestBuilder.build(email, name);

        given(memberSignUpService.doSignUp(any())).willReturn(member);

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
        final Member member = MemberBuilder.build();

        given(memberSignUpService.doSignUp(any())).willReturn(member);

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