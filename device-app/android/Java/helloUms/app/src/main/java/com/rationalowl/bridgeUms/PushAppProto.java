package com.rationalowl.bridgeUms;


import com.fasterxml.jackson.annotation.JsonProperty;


public class PushAppProto {

    public static class PushAppInstallReq {
        @JsonProperty("cId")
        public int mCid;
        @JsonProperty("aId")
        public String mAccountId;

        @JsonProperty("dt")
        public int mDeviceType;
        @JsonProperty("dRId")
        public String mDeviceRegId;
        @JsonProperty("pn")
        public String mPhoneNumber;
        @JsonProperty("auId")
        public String mAppUserId;
        @JsonProperty("n")
        public String mUserName;


        public PushAppInstallReq() {
            mCid = UmsProtocol.APP_INSTALL_CMD_ID;
        }
    }


    public static class PushAppInstallRes {
        @JsonProperty("cId")
        public int mCid;
        @JsonProperty("aId")
        public String mAccountId;
        @JsonProperty("rc")
        public int mResultCode;
        @JsonProperty("cmt")
        public String mComment;

        @JsonProperty("usRid")
        public String mUmsServerRegId;

        public PushAppInstallRes() {
            mCid = UmsProtocol.APP_INSTALL_CMD_ID;
        }
    }


    public static class PushAppUnregUserReq {
        @JsonProperty("cId")
        public int mCid;
        @JsonProperty("aId")
        public String mAccountId;

        @JsonProperty("dRId")
        public String mDeviceRegId;

        public PushAppUnregUserReq() {
            mCid = UmsProtocol.APP_UNREG_USER_CMD_ID;
        }
    }


    public static class PushAppUnregUserRes {
        @JsonProperty("cId")
        public int mCid;
        @JsonProperty("aId")
        public String mAccountId;
        @JsonProperty("rc")
        public int mResultCode;
        @JsonProperty("cmt")
        public String mComment;

        @JsonProperty("dRId")
        public String mDeviceRegId;

        public PushAppUnregUserRes() {
            mCid = UmsProtocol.APP_UNREG_USER_CMD_ID;
        }
    }
}
