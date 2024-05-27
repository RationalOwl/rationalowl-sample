import Foundation

class MessageLocalDataSource: JsonLocalDataSource<[DataDef.Message]> {
    private static let fileName = "messages.json"

    static var shared = MessageLocalDataSource()

    let messages: Observable<[DataDef.Message]> = Observable([])
    private var messageIdMappings = [String: DataDef.Message]()

    private let lock = NSLock()

    private init() {
        super.init(Self.fileName)
        loadRecentMessages()
    }

    func addMessage(_ item: DataDef.Message) {
        NSLog("[\(type(of: self))] addMessage(\(item.id))")

        if containsMessage(item.id) { return }

        lock.lock()
        messages.value.append(item)
        messages.value.sort { $0.sentAt > $1.sentAt }
        messageIdMappings[item.id] = item
        lock.unlock()

        messages.notifyObservers()
        save()
    }

    func containsMessage(_ messageId: String) -> Bool {
        lock.lock()
        let result = messageIdMappings.keys.contains(messageId)
        lock.unlock()

        return result
    }

    func getMessage(_ messageId: String) -> DataDef.Message? {
        lock.lock()
        let result = messageIdMappings[messageId]
        lock.unlock()

        return result
    }

    func removeMessage(_ messageId: String) {
        NSLog("[\(type(of: self))] removeMessage(\(messageId))")

        lock.lock()
        messages.value.removeAll { $0.id == messageId }
        lock.unlock()

        messages.notifyObservers()
        save()
    }

    func removeMessages(_ messageIds: Set<String>) {
        NSLog("[\(type(of: self))] removeMessage([\(messageIds.joined(separator: ", "))])")

        lock.lock()
        messages.value.removeAll { messageIds.contains($0.id) }

        for messageId in messageIds {
            messageIdMappings.removeValue(forKey: messageId)
        }
        lock.unlock()

        messages.notifyObservers()
        save()
    }

    func markAsRead(_ messageId: String) {
        NSLog("[\(type(of: self))] setAsRead(\(messageId))")

        lock.lock()
        if let message = messageIdMappings[messageId] {
            message.isRead = true
        }
        lock.unlock()

        messages.notifyObservers()
        save()
    }

    func markAllAsRead() {
        NSLog("[\(type(of: self))] setAllAsRead()")

        lock.lock()
        for message in messages.value {
            message.isRead = true
        }
        lock.unlock()

        messages.notifyObservers()
        save()
    }
    
    func updateDeliveryInfo(_ messageId: String, mAlimtalkSendTime: Int64, mMunjaSendTime: Int64, mMunjaType: Int) -> DataDef.Message? {
        NSLog("[\(type(of: self))] updateDeliveryInfo(\(messageId), mAlimtalkSendTime: \(mAlimtalkSendTime), mMunjaSendTime: \(mMunjaSendTime), mMunjaType: \(mMunjaType))")
        
        if let message = messageIdMappings[messageId] {
            if mAlimtalkSendTime > 0 {
                message.alimTalkSentAt = Date(timeIntervalSince1970: TimeInterval(mAlimtalkSendTime) / 1000)
            }
            
            if mMunjaSendTime > 0 {
                message.munjaSentAt = Date(timeIntervalSince1970: TimeInterval(mMunjaSendTime) / 1000)
            }
            
            message.munjaType = DataDef.MunjaType(rawValue: mMunjaType)
            save()
            
            return message
        }
        
        return nil
    }

    func save() {
        NSLog("[\(type(of: self))] save()")
        value = messages.value
    }

    override func delete() -> Bool {
        let result = super.delete()
        Self.shared = MessageLocalDataSource()
        return result
    }

    private func loadRecentMessages() {
        if let array: [DataDef.Message] = value {
            let now = Date()
            let retainDay: Int = Config.shared?.umsMsgRetainDay ?? 0

            for message in array {
                let days: Int = Calendar.current.dateComponents([.day], from: message.sentAt, to: now).day!

                if days < retainDay {
                    messages.value.append(message)
                    messageIdMappings[message.id] = message
                }
            }

            messages.value.sort { $0.sentAt > $1.sentAt }

            if array.count != messages.value.count {
                save()
            }
        }
    }
}
