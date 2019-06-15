package com.spring.guide.domain.member.dto;

import com.spring.guide.domain.model.Name;
import javax.validation.Valid;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class MemberProfileUpdate {

    @Valid
    private Name name;

    public MemberProfileUpdate(@Valid Name name) {
        this.name = name;
    }
}
