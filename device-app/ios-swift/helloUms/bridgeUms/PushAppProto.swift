class PushAppProto {

    class PushAppAuthNumberReq: Codable {
        // command id
        var cId: Int = UmsProtocol.APP_AUTH_NUMBER_CMD_ID
        // account id
        var aId: String = ""
        // country code
        var cc: String = ""
        // phone number
        var pn: String = ""
        
        // 기본 생성자
        init() {
            self.cId = UmsProtocol.APP_AUTH_NUMBER_CMD_ID
        }
        
        init(aId: String, cc: String, pn: String) {
            self.cId = UmsProtocol.APP_AUTH_NUMBER_CMD_ID
            self.aId = aId
            self.cc = cc
            self.pn = pn
        }
    }


    class PushAppAuthNumberRes: Codable {
        // command id
        var cId: Int = UmsProtocol.APP_AUTH_NUMBER_CMD_ID
        // account id
        var aId: String = ""
        // result code
        var rc: Int = 0
        // comment
        var cmt: String? = nil

        // 기본 생성자
        init() {
            self.cId = UmsProtocol.APP_AUTH_NUMBER_CMD_ID
        }
        
        init(aId: String, rc: Int, cmt: String) {
            self.cId = UmsProtocol.APP_AUTH_NUMBER_CMD_ID
            self.aId = aId
            self.rc = rc
            self.cmt = cmt
        }
    }

    class PushAppVerifyAuthNumberReq: Codable {
        // command id
        var cId: Int = UmsProtocol.APP_VERITY_AUTH_NUMBER_CMD_ID
        // account id
        var aId: String = ""
        // device type  1: android, 2: ios
        var dt: Int = 0
        // phone number
        var pn: String = ""
        // auth number
        var an: String = ""

        // 기본 생성자
        init() {
            self.cId = UmsProtocol.APP_VERITY_AUTH_NUMBER_CMD_ID
        }
        
        init(aId: String, dt: Int, pn: String, an: String) {
            self.cId = UmsProtocol.APP_VERITY_AUTH_NUMBER_CMD_ID
            self.aId = aId
            self.dt = dt
            self.pn = pn
            self.an = an
        }

    }

    class PushAppVerifyAuthNumberRes: Codable {
        // command id
        var cId: Int = UmsProtocol.APP_VERITY_AUTH_NUMBER_CMD_ID
        // account id
        var aId: String = ""
        // result code
        var rc: Int = 0
        // comment
        var cmt: String? = nil

        // 기본 생성자
        init() {
            self.cId = UmsProtocol.APP_VERITY_AUTH_NUMBER_CMD_ID
        }
        
        init(aId: String, rc: Int, cmt: String) {
            self.cId = UmsProtocol.APP_VERITY_AUTH_NUMBER_CMD_ID
            self.aId = aId
            self.rc = rc
            self.cmt = cmt
        }

    }

    class PushAppInstallReq: Codable {
        // command id
        var cId: Int = UmsProtocol.APP_INSTALL_CMD_ID
        // account id
        var aId: String = ""
        // device type  1: android, 2: ios
        var dt: Int = 0
        // device registration id
        var dRId: String = ""
        // phone number
        var pn: String? = nil
        // app user id
        var auId: String? = nil
        // name
        var n: String? = nil

        // 기본 생성자
        init() {
            self.cId = UmsProtocol.APP_INSTALL_CMD_ID
        }
        
        init(aId: String, dt: Int, dRId: String, pn: String, auId: String?, n: String?) {
            self.cId = UmsProtocol.APP_INSTALL_CMD_ID
            self.aId = aId
            self.dt = dt
            self.dRId = dRId
            self.pn = pn
            self.auId = auId
            self.n = n
        }

    }

    class PushAppInstallRes: Codable {
        // command id
        var cId: Int = UmsProtocol.APP_INSTALL_CMD_ID
        // account id
        var aId: String = ""
        // result code
        var rc: Int = 0
        // comment
        var cmt: String? = nil
        // ums server registration id
        var usRid: String = ""

        // 기본 생성자
        init() {
            self.cId = UmsProtocol.APP_INSTALL_CMD_ID
        }
        
        init(aId: String, rc: Int, cmt: String, usRid: String) {
            self.cId = UmsProtocol.APP_INSTALL_CMD_ID
            self.aId = aId
            self.rc = rc
            self.cmt = cmt
            self.usRid = usRid
        }

    }


    class PushAppUnregUserReq: Codable {
        // command id
        var cId: Int = UmsProtocol.APP_UNREG_USER_CMD_ID
        // account id
        var aId: String = ""
        // device registration id
        var dRId: String = ""

        // 기본 생성자
        init() {
            self.cId = UmsProtocol.APP_UNREG_USER_CMD_ID
        }
        
        init(aId: String, dRId: String) {
            self.cId = UmsProtocol.APP_UNREG_USER_CMD_ID
            self.aId = aId
            self.dRId = dRId
        }

    }


    class PushAppUnregUserRes: Codable {
        // command id
        var cId: Int = UmsProtocol.APP_UNREG_USER_CMD_ID
        // account id
        var aId: String = ""
        // result code
        var rc: Int = 0
        // comment
        var cmt: String? = nil
        // device registration id
        var dRId: String = ""

        // 기본 생성자
        init() {
            self.cId = UmsProtocol.APP_UNREG_USER_CMD_ID
        }
        
        init(aId: String, rc: Int, cmt: String, dRId: String) {
            self.cId = UmsProtocol.APP_UNREG_USER_CMD_ID
            self.aId = aId
            self.rc = rc
            self.cmt = cmt
            self.dRId = dRId
        }

    }

    class PushAppMsgReadNoti: Codable {
        // command id
        var cId: Int = UmsProtocol.APP_MSG_READ_NOTI_CMD_ID
        // account id
        var aId: String = ""
        // device registration id
        var dRId: String = ""
        // message id
        var mId: String = ""

        // 기본 생성자
        init() {
            self.cId = UmsProtocol.APP_MSG_READ_NOTI_CMD_ID
        }
        
        init(aId: String, dRId: String, mId: String) {
            self.cId = UmsProtocol.APP_MSG_READ_NOTI_CMD_ID
            self.aId = aId
            self.dRId = dRId
            self.mId = mId
        }

    }

    class PushAppMsgInfoReq: Codable {
        // command id
        var cId: Int = UmsProtocol.APP_MSG_INFO_CMD_ID
        // account id
        var aId: String = ""
        // device registration id
        var dRId: String = ""
        // message id
        var mId: String = ""

        // 기본 생성자
        init() {
            self.cId = UmsProtocol.APP_MSG_INFO_CMD_ID
        }
        
        init(aId: String, dRId: String, mId: String) {
            self.cId = UmsProtocol.APP_MSG_INFO_CMD_ID
            self.aId = aId
            self.dRId = dRId
            self.mId = mId
        }

    }

    class PushAppMsgInfoRes: Codable {
        // command id
        var cId: Int = UmsProtocol.APP_MSG_INFO_CMD_ID
        // account id
        var aId: String = ""
        // result code
        var rc: Int = 0
        // comment
        var cmt: String? = nil
        // message id
        var mId: String = ""
        // alimtalk send time (알림톡 발신 시간)
        var ast: Int64 = 0
        // alimtalk state 알림톡 전달상태 (0: 미발신, 1: 발신요청, 2: 전달성공, 3: 전달실패)
        var ase: Int = 0
        // munja send time (문자 발신 시간)
        var mst: Int64 = 0
        // munja type (11: sms, 12: lms, 13: mms)
        var mt: Int = 0
        // munja state (0: 미발신, 1: 발신요청, 2: 전달성공, 3: 전달실패)
        var ms: Int = 0

        // 기본 생성자
        init() {
            self.cId = UmsProtocol.APP_MSG_INFO_CMD_ID
        }
        
        init(aId: String, rc: Int, cmt: String, mId: String,
                           ast: Int64, ase: Int, mst: Int64, mt: Int, ms: Int) {
            self.cId = UmsProtocol.APP_MSG_INFO_CMD_ID
            self.aId = aId
            self.rc = rc
            self.cmt = cmt
            self.mId = mId
            self.ast = ast
            self.ase = ase
            self.mst = mst
            self.mt = mt
            self.ms = ms
        }

    }

    class PushAppImgDataReq: Codable {
        // command id
        var cId: Int = UmsProtocol.APP_IMG_DATA_CMD_ID
        // account id
        var aId: String = ""
        // message id
        var mId: String = ""
        // image id
        var iId: String = ""

        // 기본 생성자
        init() {
            self.cId = UmsProtocol.APP_IMG_DATA_CMD_ID
        }
        
        init(aId: String, mId: String, iId: String,) {
            self.cId = UmsProtocol.APP_IMG_DATA_CMD_ID
            self.aId = aId
            self.mId = mId
            self.iId = iId
        }

    }

    class PushAppImgDataRes: Codable {
        // command id
        var cId: Int = UmsProtocol.APP_IMG_DATA_CMD_ID
        // account id
        var aId: String = ""
        // result code
        var rc: Int = 0
        // comment
        var cmt: String? = nil
        // image data (base64 인코딩 포맷)
        var imgD: String = ""

        // 기본 생성자
        init() {
            self.cId = UmsProtocol.APP_IMG_DATA_CMD_ID
        }
        
        init(aId: String, rc: Int, cmt: String, imgD: String) {
            self.cId = UmsProtocol.APP_IMG_DATA_CMD_ID
            self.aId = aId
            self.rc = rc
            self.cmt = cmt
            self.imgD = imgD
        }

    }
}
