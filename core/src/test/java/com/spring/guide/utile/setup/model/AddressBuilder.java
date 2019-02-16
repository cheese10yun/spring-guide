package com.spring.guide.utile.setup.model;

import com.spring.guide.model.Address;

public class AddressBuilder {

    public static Address build() {
        return Address.builder()
                .county("county")
                .city("city")
                .state("state")
                .zipCode("zipCode")
                .build();
    }

    public static Address build(String county, String city, String state, String zipCode) {
        return Address.builder()
                .county(county)
                .city(city)
                .state(state)
                .zipCode(zipCode)
                .build();
    }

}