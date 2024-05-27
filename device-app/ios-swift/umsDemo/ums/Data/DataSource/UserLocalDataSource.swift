class UserLocalDataSource: JsonLocalDataSource<DataDef.User> {
    private static let fileName = "user.json"

    static let shared = UserLocalDataSource()

    private init() {
        super.init(Self.fileName)
    }

    var user: DataDef.User? {
        get {
            return value
        }
        set {
            value = newValue!
        }
    }
}
