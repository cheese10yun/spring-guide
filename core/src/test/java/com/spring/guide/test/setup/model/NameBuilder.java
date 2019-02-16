package com.spring.guide.test.setup.model;

import com.spring.guide.model.Name;

public class NameBuilder {

    public static Name build() {
        return Name.builder()
                .first("first")
                .middle("middle")
                .last("last")
                .build();
    }

    public static Name build(String first, String middle, String last) {
        return Name.builder()
                .first(first)
                .middle(middle)
                .last(last)
                .build();
    }


}