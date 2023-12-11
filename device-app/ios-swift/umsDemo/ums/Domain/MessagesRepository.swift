import RxCocoa
import RxSwift

class MessagesRepository : JsonRepository<[Message]>
{
    private static let fileName = "messages.json"
    
    static let shared = MessagesRepository()
    
    let messages: BehaviorRelay<[Message]> = BehaviorRelay(value: [])
    private var messageIdMappings = [String: Message]()
    
    private let lock = NSLock()
    
    private init()
    {
        super.init(MessagesRepository.fileName)
        
        if let array: [Message] = getValue()
        {
            var list = [Message]()
            
            let now = Date()
            let retainDay: Int = Config.shared?.umsMsgRetainDay ?? 0
            
            for message in array
            {
                let days: Int = Calendar.current.dateComponents([.day], from: message.sentAt, to: now).day!
                
                if days < retainDay
                {
                    list.append(message)
                    messageIdMappings[message.id] = message
                }
            }
            
            messages.accept(list)
            
            if array.count != list.count
            {
                save()
            }
        }
    }
    
    func addMessage(_ item: Message)
    {
        NSLog("[\(type(of: self))] addMessage('\(item.title)')")
        
        if hasMessage(item.id) { return }
        
        lock.lock()
        var newItems = messages.value
        newItems.append(item)
        newItems.sort { $0.sentAt > $1.sentAt }
        messageIdMappings[item.id] = item
        
        messages.accept(newItems)
        lock.unlock()
        
        save()
    }
    
    func hasMessage(_ messageId: String) -> Bool
    {
        lock.lock()
        let result: Bool = messageIdMappings.keys.contains(messageId)
        lock.unlock()
        
        return result
    }
    
    func getMessage(_ messageId: String) -> Message?
    {
        lock.lock()
        let result: Message? = messageIdMappings[messageId]
        lock.unlock()
        
        return result
    }
    
    func removeMessage(_ messageId: String)
    {
        NSLog("[\(type(of: self))] removeMessage(\(messageId))")
        
        lock.lock()
        var newItems = messages.value
        
        if let index = newItems.firstIndex(where: { $0.id == messageId })
        {
            newItems.remove(at: index)
        }
        
        messageIdMappings[messageId] = nil
        
        messages.accept(newItems)
        lock.unlock()
        
        save()
    }
    
    func removeMessages(_ messageIds: [String])
    {
        let messageIdsString = messageIds.joined(separator: ", " )
        NSLog("[\(type(of: self))] removeMessage([\(messageIdsString)])")
        
        lock.lock()
        var newItems = messages.value
        let messageIdSet = Set(messageIds)
        
        newItems.removeAll { messageIdSet.contains($0.id) }
        
        for messageId: String in messageIds
        {
            messageIdMappings[messageId] = nil
        }
        
        messages.accept(newItems)
        lock.unlock()
        
        save()
    }
    
    func setAsRead(_ messageId: String)
    {
        NSLog("[\(type(of: self))] setAsRead(\(messageId))")
        
        lock.lock()
        var newItems = messages.value
        
        if let oldItem = messageIdMappings[messageId]
        {
            var newItem = oldItem
            newItem.isRead = true
            
            if let index = newItems.firstIndex(of: oldItem)
            {
                newItems[index] = newItem
            }
            
            messageIdMappings[messageId] = newItem
        }
        
        messages.accept(newItems)
        lock.unlock()
        
        save()
    }
    
    func setAllAsRead()
    {
        NSLog("[\(type(of: self))] setAllAsRead()")
        
        lock.lock()
        var newItems = [Message]()
        
        for item in messages.value
        {
            var newItem = item
            newItem.isRead = true
            newItems.append(newItem)
            
            messageIdMappings[item.id] = newItem
        }
        
        messages.accept(newItems)
        lock.unlock()
        
        save()
    }
    
    func save()
    {
        NSLog("[\(type(of: self))] save()")
        setValue(messages.value)
    }
}
