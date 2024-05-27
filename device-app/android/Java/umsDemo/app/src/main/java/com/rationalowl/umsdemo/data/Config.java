package com.rationalowl.umsdemo.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.rationalowl.minerva.client.android.MinervaManager;

import org.ini4j.Ini;

import java.io.IOException;
import java.io.InputStream;

public class Config {
    private static final String TAG = "Config";

    private static Config instance;

    /* rationalowl config */
    private String roGateHost, roSvcId;

    /* ums config */
    private String umsHost, umsAccountId;
    private int umsMsgRetainDay;

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }

        return instance;
    }

    private Config() {
        final Context context = MinervaManager.getContext();
        final AssetManager assets = context.getResources().getAssets();

        try {
            try (InputStream stream = assets.open("config.ini")) {
                final Ini ini = new Ini(stream);

                /* rational owl fields */
                roGateHost = loadString(ini, "rational_owl", "gate_host");
                roSvcId = loadString(ini, "rational_owl", "svc_id");

                /* ums */
                umsHost = loadString(ini, "ums", "ums_host");
                umsAccountId = loadString(ini, "ums", "account_id");
                umsMsgRetainDay = loadInt(ini, "ums", "msg_retain_day");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* rationalowl config */
    public String getRoGateHost() {
        return roGateHost;
    }

    public String getRoSvcId() {
        return roSvcId;
    }

    /* ums config */
    public String getUmsHost() {
        return umsHost;
    }

    public String getUmsAccountId() {
        return umsAccountId;
    }

    public int getUmsMsgRetainDay() {
        return umsMsgRetainDay;
    }

    private String loadString(Ini ini, String section, String key) {
        final String value = ini.get(section, key);

        if (value == null) {
            Log.e(TAG, "[" + section + "] " + key + " is not set");
            System.exit(1);
        }

        return value;
    }

    private int loadInt(Ini ini, String section, String key) {
        final String value = loadString(ini, section, key);
        return Integer.parseInt(value);
    }
}
