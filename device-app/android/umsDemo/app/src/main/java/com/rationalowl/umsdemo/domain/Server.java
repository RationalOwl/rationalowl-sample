package com.rationalowl.umsdemo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Server {
    private final String regId;

    @JsonCreator
    public Server(@JsonProperty("regId") String regId) {
        this.regId = regId;
    }

    public String getRegId() {
        return regId;
    }
}
