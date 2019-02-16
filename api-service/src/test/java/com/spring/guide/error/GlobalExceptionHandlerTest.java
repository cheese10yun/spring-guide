package com.spring.guide.error;

import com.spring.guide.excpetion.ErrorCode;
import com.spring.guide.test.IntegrationTest;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GlobalExceptionHandlerTest  extends IntegrationTest {


    @Test
    public void handleMethodArgumentTypeMismatchExceptionTest() throws Exception {

        //given

        //when
        final ResultActions resultActions = requestGetMember(0L);

        //then
        resultActions
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("message").value(ErrorCode.METHOD_NOT_ALLOWED.getMessage()))
                .andExpect(jsonPath("status").value(ErrorCode.METHOD_NOT_ALLOWED.getStatus()))
                .andExpect(jsonPath("code").value(ErrorCode.METHOD_NOT_ALLOWED.getCode()))
                .andExpect(jsonPath("errors").isEmpty())
                ;
    }


    private ResultActions requestGetMember(long memberId) throws Exception {
        return mvc.perform(post("/members/{id}", memberId)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print());
    }
}