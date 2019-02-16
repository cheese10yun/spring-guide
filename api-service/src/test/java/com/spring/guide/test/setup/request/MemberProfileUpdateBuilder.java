package com.spring.guide.test.setup.request;

import com.spring.guide.member.dto.MemberProfileUpdate;
import com.spring.guide.model.Name;
import com.spring.guide.test.setup.model.NameBuilder;


public class MemberProfileUpdateBuilder {

    public static MemberProfileUpdate build() {
        final Name name = NameBuilder.build("qqwew", "asdxca", "adwwd");
        return new MemberProfileUpdate(name);
    }
}
