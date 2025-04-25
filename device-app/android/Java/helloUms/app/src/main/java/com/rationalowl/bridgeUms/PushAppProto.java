package com.rationalowl.bridgeUms;


import com.fasterxml.jackson.annotation.JsonProperty;


public class PushAppProto {

    public static class PushAppAuthNumberReq {
        @JsonProperty("cId")
        public int mCid;
        @JsonProperty("aId")
        public String mAccountId;

        @JsonProperty("cc")
        public String mCountryCode;
        @JsonProperty("pn")
        public String mPhoneNumber;


        public PushAppAuthNumberReq() {
            mCid = UmsProtocol.APP_AUTH_NUMBER_CMD_ID;
        }
    }


    public static class PushAppAuthNumberRes {
        @JsonProperty("cId")
        public int mCid;
        @JsonProperty("aId")
        public String mAccountId;
        @JsonProperty("rc")
        public int mResultCode;
        @JsonProperty("cmt")
        public String mComment;

        public PushAppAuthNumberRes() {
            mCid = UmsProtocol.APP_AUTH_NUMBER_CMD_ID;
        }
    }


    public static class PushAppVerifyAuthNumberReq {
        @JsonProperty("cId")
        public int mCid;
        @JsonProperty("aId")
        public String mAccountId;
        @JsonProperty("dt")
        public int mDeviceType;

        @JsonProperty("pn")
        public String mPhoneNumber;
        @JsonProperty("an")
        public String mAuthNumber;


        public PushAppVerifyAuthNumberReq() {
            mCid = UmsProtocol.APP_VERITY_AUTH_NUMBER_CMD_ID;
        }
    }


    public static class PushAppVerifyAuthNumberRes {
        @JsonProperty("cId")
        public int mCid;
        @JsonProperty("aId")
        public String mAccountId;
        @JsonProperty("rc")
        public int mResultCode;

        @JsonProperty("cmt")
        public String mComment;

        public PushAppVerifyAuthNumberRes() {
            mCid = UmsProtocol.APP_VERITY_AUTH_NUMBER_CMD_ID;
        }
    }


    public static class PushAppInstallReq {
        @JsonProperty("cId")
        public int mCid;
        @JsonProperty("aId")
        public String mAccountId;

        @JsonProperty("dt")
        public int mDeviceType;
        @JsonProperty("dRId")
        public String mDeviceRegId;

        /* ums fields */
        @JsonProperty("pn")
        public String mPhoneNumber;

        // ums optional
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


    /////// push app job

    public static class PushAppMsgReadNoti {
        @JsonProperty("cId")
        public int mCid;
        @JsonProperty("aId")
        public String mAccountId;

        @JsonProperty("dRId")
        public String mDeviceRegId;
        @JsonProperty("mId")
        public String mMsgId;


        public PushAppMsgReadNoti() {
            mCid = UmsProtocol.APP_MSG_READ_NOTI_CMD_ID;
        }
    }


    public static class PushAppMsgInfoReq {
        @JsonProperty("cId")
        public int mCid;
        @JsonProperty("aId")
        public String mAccountId;
        @JsonProperty("dRId")
        public String mDeviceRegId;
        @JsonProperty("mId")
        public String mMsgId;


        public PushAppMsgInfoReq() {
            mCid = UmsProtocol.APP_MSG_INFO_CMD_ID;
        }
    }


    public static class PushAppMsgInfoRes {
        @JsonProperty("cId")
        public int mCid;
        @JsonProperty("aId")
        public String mAccountId;
        @JsonProperty("rc")
        public int mResultCode;
        @JsonProperty("cmt")
        public String mComment;

        @JsonProperty("mId")
        public String mMsgId;
        @JsonProperty("ast")
        public long mAlimtalkSendTime;
        @JsonProperty("ase")
        public int mAlimtalkState;  // 0: unsend, 1: send request, 2: success, 3: fail
        @JsonProperty("mst")
        public long mMunjaSendTime;
        @JsonProperty("mt")
        public int mMunjaType;      // 12: sms, 13: lms, 14: mms
        @JsonProperty("ms")
        public int mMunjaState;     // 0: unsend, 1: send request, 2: success, 3: fail

        public PushAppMsgInfoRes() {
            mCid = UmsProtocol.APP_MSG_INFO_CMD_ID;
        }
    }


    public static class PushAppImgDataReq {
        @JsonProperty("cId")
        public int mCid;
        @JsonProperty("aId")
        public String mAccountId;
        @JsonProperty("iId")
        public String mImgId;
        @JsonProperty("mId")
        public String mMsgId;


        public PushAppImgDataReq() {
            mCid = UmsProtocol.APP_IMG_DATA_CMD_ID;
        }
    }


    public static class PushAppImgDataRes {
        @JsonProperty("cId")
        public int mCid;
        @JsonProperty("aId")
        public String mAccountId;
        @JsonProperty("rc")
        public int mResultCode;
        @JsonProperty("cmt")
        public String mComment;

        @JsonProperty("imgD")
        public String mImgData;

        public PushAppImgDataRes() {
            mCid = UmsProtocol.APP_IMG_DATA_CMD_ID;
        }
    }
}
