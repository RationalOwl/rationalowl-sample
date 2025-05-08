public enum PushAppProto {
    public struct PushAppAuthNumberReq: Codable {
        enum CodingKeys: String, CodingKey {
            case mCid = "cId"
            case mAccountId = "aId"
            case mCountryCode = "cc"
            case mPhoneNumber = "pn"
        }

        public var mCid: Int = UmsProtocol.APP_AUTH_NUMBER_CMD_ID
        public var mAccountId: String

        public var mCountryCode: String
        public var mPhoneNumber: String
    }

    public struct PushAppAuthNumberRes: Decodable {
        enum CodingKeys: String, CodingKey {
            case mCid = "cId"
            case mAccountId = "aId"
            case mResultCode = "rc"
            case mComment = "cmt"
        }

        public var mCid: Int = UmsProtocol.APP_AUTH_NUMBER_CMD_ID
        public var mAccountId: String
        public var mResultCode: Int
        public var mComment: String?
    }

    public struct PushAppVerifyAuthNumberReq: Codable {
        enum CodingKeys: String, CodingKey {
            case mCid = "cId"
            case mAccountId = "aId"
            case mDeviceType = "dt"
            case mPhoneNumber = "pn"
            case mAuthNumber = "an"
        }

        public var mCid: Int = UmsProtocol.APP_VERITY_AUTH_NUMBER_CMD_ID
        public var mAccountId: String
        public var mDeviceType: Int

        public var mPhoneNumber: String
        public var mAuthNumber: String
    }

    public struct PushAppVerifyAuthNumberRes: Decodable {
        enum CodingKeys: String, CodingKey {
            case mCid = "cId"
            case mAccountId = "aId"
            case mResultCode = "rc"
            case mComment = "cmt"
        }

        public var mCid: Int = UmsProtocol.APP_VERITY_AUTH_NUMBER_CMD_ID
        public var mAccountId: String
        public var mResultCode: Int

        public var mComment: String?
    }

    public struct PushAppInstallReq: Codable {
        enum CodingKeys: String, CodingKey {
            case mCid = "cId"
            case mAccountId = "aId"
            case mDeviceType = "dt"
            case mDeviceRegId = "dRId"
            case mPhoneNumber = "pn"
            case mAppUserId = "auId"
            case mUserName = "n"
        }

        public var mCid: Int = UmsProtocol.APP_INSTALL_CMD_ID
        public var mAccountId: String

        public var mDeviceType: Int
        public var mDeviceRegId: String
        public var mPhoneNumber: String
        public var mAppUserId: String
        public var mUserName: String
    }

    public struct PushAppInstallRes: Decodable {
        enum CodingKeys: String, CodingKey {
            case mCid = "cId"
            case mAccountId = "aId"
            case mResultCode = "rc"
            case mComment = "cmt"
            case mUmsServerRegId = "usRid"
        }

        public var mCid: Int = UmsProtocol.APP_INSTALL_CMD_ID
        public var mAccountId: String
        public var mResultCode: Int
        public var mComment: String?

        public var mUmsServerRegId: String?
    }

    public struct PushAppUnregUserReq: Codable {
        enum CodingKeys: String, CodingKey {
            case mCid = "cId"
            case mAccountId = "aId"
            case mDeviceRegId = "dRId"
        }

        public var mCid: Int = UmsProtocol.APP_UNREG_USER_CMD_ID
        public var mAccountId: String

        public var mDeviceRegId: String
    }

    public struct PushAppUnregUserRes: Decodable {
        enum CodingKeys: String, CodingKey {
            case mCid = "cId"
            case mAccountId = "aId"
            case mResultCode = "rc"
            case mComment = "cmt"
            case mDeviceRegId = "dRId"
        }

        public var mCid: Int = UmsProtocol.APP_UNREG_USER_CMD_ID
        public var mAccountId: String
        public var mResultCode: Int
        public var mComment: String?

        public var mDeviceRegId: String
    }

    /////// push app job

    public struct PushAppMsgReadNoti: Codable {
        enum CodingKeys: String, CodingKey {
            case mCid = "cId"
            case mAccountId = "aId"
            case mDeviceRegId = "dRId"
            case mMsgId = "mId"
        }
        public var mCid: Int = UmsProtocol.APP_MSG_READ_NOTI_CMD_ID
        public var mAccountId: String
        public var mDeviceRegId: String
        public var mMsgId: String
    }

    public struct PushAppMsgInfoReq: Codable {
        enum CodingKeys: String, CodingKey {
            case mCid = "cId"
            case mAccountId = "aId"
            case mDeviceRegId = "dRId"
            case mMsgId = "mId"
        }

        public var mCid: Int = UmsProtocol.APP_MSG_INFO_CMD_ID

        public var mAccountId: String
        public var mDeviceRegId: String

        public var mMsgId: String
    }

    public struct PushAppMsgInfoRes: Decodable {
        enum CodingKeys: String, CodingKey {
            case mCid = "cId"
            case mAccountId = "aId"
            case mResultCode = "rc"
            case mComment = "cmt"
            case mMsgId = "mId"
            case mAlimtalkSendTime = "ast"
            case mAlimtalkState = "ase"
            case mMunjaSendTime = "mst"
            case mMunjaType = "mt"
            case mMunjaState = "ms"
        }

        public var mCid: Int = UmsProtocol.APP_MSG_INFO_CMD_ID
        public var mAccountId: String
        public var mResultCode: Int
        public var mComment: String?

        public var mMsgId: String
        public var mAlimtalkSendTime: Int64
        public var mAlimtalkState: Int // 0: unsend, 1: send request, 2: success, 3: fail
        public var mMunjaSendTime: Int64
        public var mMunjaType: Int // 12: sms, 13: lms, 14: mms
        public var mMunjaState: Int
    }

    public struct PushAppImgDataReq: Codable {
        enum CodingKeys: String, CodingKey {
            case mCid = "cId"
            case mAccountId = "aId"
            case mImgId = "iId"
            case mMsgId = "mId"
        }

        public var mCid: Int = UmsProtocol.APP_IMG_DATA_CMD_ID
        public var mAccountId: String
        public var mImgId: String
        public var mMsgId: String
    }

    public struct PushAppImgDataRes: Decodable {
        enum CodingKeys: String, CodingKey {
            case mCid = "cId"
            case mAccountId = "aId"
            case mResultCode = "rc"
            case mComment = "cmt"
            case mImgData = "imgD"
        }

        public var mCid: Int = UmsProtocol.APP_IMG_DATA_CMD_ID
        public var mAccountId: String
        public var mResultCode: Int
        public var mComment: String?

        public var mImgData: String
    }
}
