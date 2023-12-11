package com.rationalowl.umsdemo.domain;

import android.content.Context;
import android.content.res.AssetManager;

import com.rationalowl.minerva.client.android.MinervaManager;
import com.rationalowl.minerva.client.android.util.Logger;

import org.ini4j.Ini;

import java.io.IOException;
import java.io.InputStream;

public class Config {
    private static final String TAG = "Config";
    private static final Config instance = new Config();

    /* rationalowl config */
    private String roGateHost;
    private String roSvcId;

    /* ums config */
    private String mUmsHost;
    private String mUmsAccountId;
    private int umsMsgRetainDay;

    public static Config getInstance() {
        return instance;
    }

    private Config() {
        final Context context = MinervaManager.getContext();
        final AssetManager assetManager = context.getResources().getAssets();

        // 서비스 이용약관
        try {
            final InputStream stream = assetManager.open("config.ini");
            final Ini ini = new Ini(stream);

            /* rational owl fields */
            roGateHost = ini.get("rational_owl", "gate_host");

            if (roGateHost == null) {
                Logger.error(TAG, "roGateHost should be set!!");
                System.exit(1);
                return;
            }

            roSvcId = ini.get("rational_owl", "svc_id");

            if (roSvcId == null) {
                Logger.error(TAG, "roSvcId should be set!!");
                System.exit(1);
                return;
            }

            /* ums */
            mUmsHost = ini.get("ums", "ums_host");

            if (mUmsHost == null) {
                Logger.error(TAG, "umsHost should be set!!");
                System.exit(1);
                return;
            }

            mUmsAccountId = ini.get("ums", "account_id");

            if (mUmsAccountId == null) {
                Logger.error(TAG, "umsAccountId should be set!!");
                System.exit(1);
                return;
            }

            final String umsMsgRetainDayStr = ini.get("ums", "msg_retain_day");

            if (umsMsgRetainDayStr == null) {
                Logger.error(TAG, "umsMsgRetainDayStr should be set!!");
                System.exit(1);
                return;
            }

            umsMsgRetainDay = Integer.parseInt(umsMsgRetainDayStr);
            stream.close();
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
        return mUmsHost;
    }

    public String getUmsAccountId() {
        return mUmsAccountId;
    }

    public int getUmsMsgRetainDay() {
        return umsMsgRetainDay;
    }
}
