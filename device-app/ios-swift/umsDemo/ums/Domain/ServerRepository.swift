class ServerRepository : JsonRepository<Server>
{
    private static let fileName = "server.json"
    
    static let shared = ServerRepository()
    
    private init()
    {
        super.init(ServerRepository.fileName);
    }
    
    var server: Server?
    {
        get
        {
            return getValue()
        }
        set
        {
            setValue(newValue!)
        }
    }
}
