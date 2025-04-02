

struct PushAppProto {

    struct PushAppInstallReq: Codable {
        var cId: Int
        var aId: String
        var dt: Int
        var dRId: String
        var pn: String?
        var auId: String?
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
        var cId: Int
        var aId: String
        var rc: Int
        var cmt: String?
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
        var cId: Int
        var aId: String
        var dRId: String

        init() {
            self.cId = UmsProtocol.APP_UNREG_USER_CMD_ID
            self.aId = ""
            self.dRId = ""
        }
    }

    struct PushAppUnregUserRes: Codable {
        var cId: Int
        var aId: String
        var rc: Int
        var cmt: String?
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
