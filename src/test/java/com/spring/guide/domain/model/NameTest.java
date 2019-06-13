package com.spring.guide.domain.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class NameTest {

    @Test
    public void getFullName_isFullName_ReturnFullName() {
        final Name name = Name.builder()
                .first("first")
                .middle("middle")
                .last("last")
                .build();
        final String fullName = name.getFullName();
        assertThat(fullName, is("first middle last"));
    }

    @Test
    public void getFullName_WithoutMiddle_ReturnMiddleNameIsNull() {
        final Name name = Name.builder()
                .first("first")
                .middle("")
                .last("last")
                .build();
        final String fullName = name.getFullName();
        assertThat(fullName, is("first last"));
        assertThat(name.getMiddle(), is(nullValue()));
    }

    @Test
    public void getFullName_MiddleNameIsNull_ReturnMiddleNameIsNull() {
        final Name name = Name.builder()
                .first("first")
                .middle("")
                .last("last")
                .build();
        final String fullName = name.getFullName();
        assertThat(fullName, is("first last"));
        assertThat(name.getMiddle(), is(nullValue()));
    }

}