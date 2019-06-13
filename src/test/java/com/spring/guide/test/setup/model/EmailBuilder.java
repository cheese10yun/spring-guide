package com.spring.guide.test.setup.model;

import com.spring.guide.domain.model.Email;

public class EmailBuilder {

    public static Email build() {
        return Email.of("yun@test.com");
    }

    public static Email build(final String email) {
        return Email.of(email);
    }


}
