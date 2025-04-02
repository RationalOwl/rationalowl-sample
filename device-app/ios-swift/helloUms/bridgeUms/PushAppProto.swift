

struct PushAppProto {

    struct PushAppInstallReq: Codable {
        // command id
        var cId: Int
        // account id
        var aId: String
        // device type  1: android, 2: ios
        var dt: Int
        // device reg id
        var dRId: String

        /* optional fields */
        // phone number
        var pn: String?
        // app user id
        var auId: String?
        // app user name
        var n: String?

        init() {
            self.cId = UmsProtocol.APP_INSTALL_CMD_ID
            self.aId = ""
            self.dt = 0
            self.dRId = ""
            self.pn = ""
            self.auId = ""
            self.n = ""
        }
    }

    struct PushAppInstallRes: Codable {
        // command id
        var cId: Int
        // account id
        var aId: String
        // result code 1: success else: error
        var rc: Int
        // comment
        var cmt: String?
        // ums server reg id
        var usRid: String

        init() {
            self.cId = UmsProtocol.APP_INSTALL_CMD_ID
            self.aId = ""
            self.rc = 0
            self.cmt = ""
            self.usRid = ""
        }
    }

    struct PushAppUnregUserReq: Codable {
        // command id
        var cId: Int
        // account id
        var aId: String
        // device reg id
        var dRId: String

        init() {
            self.cId = UmsProtocol.APP_UNREG_USER_CMD_ID
            self.aId = ""
            self.dRId = ""
        }
    }

    struct PushAppUnregUserRes: Codable {
        // command id
        var cId: Int
        // account id
        var aId: String
        // result code
        var rc: Int
        // comment
        var cmt: String?
        // device reg id
        var dRId: String?

        init() {
            self.cId = UmsProtocol.APP_UNREG_USER_CMD_ID
            self.aId = ""
            self.rc = 0
            self.cmt = ""
            self.dRId = ""
        }
    }
}
