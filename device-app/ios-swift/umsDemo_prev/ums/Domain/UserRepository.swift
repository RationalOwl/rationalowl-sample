class UserRepository : JsonRepository<User>
{
    private static let fileName = "user.json"
    
    static let shared = UserRepository()
    
    private init()
    {
        super.init(UserRepository.fileName);
    }
    
    var user: User?
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
