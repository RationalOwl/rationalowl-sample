package com.rationalowl.umsdemo.domain;

import androidx.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    // rationalowl device app id
    private final String regId;
    private final String phoneCountryCode, phoneNumber;
    private final String name;
    @Nullable
    private final String userId;
    private final Date joinedAt;

    @JsonCreator
    public User(@JsonProperty("regId") String regId,
                @JsonProperty("phoneCountryCode") String phoneCountryCode,
                @JsonProperty("phoneNumber") String phoneNumber,
                @JsonProperty("name") String name,
                @JsonProperty("userId") @Nullable String userId) {
        this.regId = regId;
        this.phoneCountryCode = phoneCountryCode;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.userId = userId;
        this.joinedAt = new Date();
    }

    public String getRegId() {
        return regId;
    }

    public String getPhoneCountryCode() {
        return phoneCountryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public String getUserId() {
        return userId;
    }

    public Date getJoinedAt() {
        return joinedAt;
    }
}
