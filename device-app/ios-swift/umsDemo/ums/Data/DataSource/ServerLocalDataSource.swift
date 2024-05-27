class ServerLocalDataSource: JsonLocalDataSource<DataDef.Server> {
    private static let fileName = "server.json"

    static let shared = ServerLocalDataSource()

    private init() {
        super.init(Self.fileName)
    }

    var server: DataDef.Server? {
        get {
            return value
        }
        set {
            value = newValue!
        }
    }
}
