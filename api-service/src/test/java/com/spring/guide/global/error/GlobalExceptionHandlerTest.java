package com.spring.guide.global.error;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.spring.guide.excpetion.ErrorCode;
import com.spring.guide.test.IntegrationTest;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

public class GlobalExceptionHandlerTest extends IntegrationTest {


    @Test
    public void 지원하지않은_http_method_호출_했을경우() throws Exception {

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

    @Test
    public void enum_타입_일치하지_않을_경우() throws Exception {

        final String typeValue = "asd";
        final ResultActions resultActions = requestExistenceTarget(typeValue);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(ErrorCode.INVALID_TYPE_VALUE.getMessage()))
                .andExpect(jsonPath("status").value(ErrorCode.INVALID_TYPE_VALUE.getStatus()))
                .andExpect(jsonPath("code").value(ErrorCode.INVALID_TYPE_VALUE.getCode()))
                .andExpect(jsonPath("errors[0].field").value("type"))
                .andExpect(jsonPath("errors[0].value").value(typeValue))
                .andExpect(jsonPath("errors[0].reason").value("typeMismatch"))
        ;

    }

    private ResultActions requestExistenceTarget(String type) throws Exception {
        return mvc.perform(get("/members/existence")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("type", type)
                .param("value", ""))
                .andDo(print());
    }


    private ResultActions requestGetMember(long memberId) throws Exception {
        return mvc.perform(post("/members/{id}", memberId)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print());
    }
}