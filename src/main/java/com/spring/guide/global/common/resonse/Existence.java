package com.spring.guide.global.common.resonse;


import lombok.Getter;

@Getter
public class Existence {

    private boolean existence;

    public Existence(boolean existence) {
        this.existence = existence;
    }
}
