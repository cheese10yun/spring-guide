package com.spring.guide.domain.coupon.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.spring.guide.domain.coupon.domain.Coupon;
import com.spring.guide.domain.coupon.domain.CouponCode;
import com.spring.guide.global.error.exception.ErrorCode;
import com.spring.guide.test.IntegrationTest;
import com.spring.guide.test.setup.domain.CouponSetup;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

public class CouponApiTest extends IntegrationTest {

    @Autowired
    private CouponSetup couponSetup;

    @Test
    public void 쿠폰_조회_by_code() throws Exception {

        //given
        final Coupon coupon = couponSetup.save();

        //when
        final ResultActions resultActions = requestGetCouponByCode(coupon.getCode());

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("code.value").exists())
                .andExpect(jsonPath("discount").exists())
                .andExpect(jsonPath("expirationDate").exists())
                .andExpect(jsonPath("expiration").value(false))
        ;

    }

    @Test
    public void 쿠폰_조회_없는_경우() throws Exception {
        //given
        final CouponCode code = CouponCode.generateCode();

        //when
        final ResultActions resultActions = requestGetCouponByCode(code);

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
    public void 쿠폰_used() throws Exception {
        //given
        final Coupon coupon = couponSetup.save();

        //when
        final ResultActions resultActions = requestUseCouponByCode(coupon.getCode());

        //then
        resultActions
                .andExpect(status().isOk())
        ;

    }

    private ResultActions requestGetCouponByCode(final CouponCode code) throws Exception {
        return mvc.perform(get("/coupons/{code}", code.getValue())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print());
    }

    private ResultActions requestUseCouponByCode(final CouponCode code) throws Exception {
        return mvc.perform(put("/coupons/{code}", code.getValue())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print());
    }

}