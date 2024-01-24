package com.rationalowl.umsdemo.protocol;

import androidx.annotation.Nullable;

import com.rationalowl.umsdemo.domain.Config;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class UmsClient {
    @Nullable
    private static UmsService instance;
    private static final int UMS_PORT = 36001;

    public static synchronized UmsService getService() {
        if (instance == null) {
            final String baseUrl = Config.getInstance().getUmsHost() + ":" + UMS_PORT;

            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();

            instance = retrofit.create(UmsService.class);
        }

        return instance;
    }
}
