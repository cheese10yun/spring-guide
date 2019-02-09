package com.spring.guide.model;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

public class EmailTest {

    @Test
    public void Email_test() {
        final String value = "cheese10yun@gmail.com";
        final Email email = Email.of(value);
        assertThat(email.getValue(), is(value));
        assertThat(email.getId(), is("cheese10yun"));
        assertThat(email.getHost(), is("gmail.com"));
    }

    @Test
    public void Email_Invalidation() {
        final String value = "cheese1gmail.com";
        final Email email = Email.of(value);
        assertThat(email.getValue(), is(value));
        assertThat(email.getHost(), is(nullValue()));
        assertThat(email.getId(), is(nullValue()));
    }

    @Test
    public void Email_Invalidation_test() {
        final String value = "chees@e1gmailm";
        final Email email = Email.of(value);
        assertThat(email.getValue(), is(value));
        assertThat(email.getId(), is("chees"));
        assertThat(email.getHost(), is("e1gmailm"));
    }

    @Test
    public void Email_Invalidation_test_01() {
        final String value = "@asd.com";
        final Email email = Email.of(value);
        assertThat(email.getValue(), is(value));
        assertThat(email.getId(), is(""));
        assertThat(email.getHost(), is("asd.com"));
    }
}