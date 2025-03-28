import Foundation
import UserNotifications

class MessageSyncService {
    static let shared = MessageSyncService()

    static let appGroupId = "group.com.rationalowl.umsdemo"
    static let newMessagesId = "com.rationalowl.umsdemo.newMessages"
    static let newMessagesKey = "newMessages"

    private let userDefaults = UserDefaults(suiteName: appGroupId)!
    private let repository = MessageLocalDataSource.shared

    private init() {}

    func syncMessages() {
        guard let messagesData = userDefaults.array(forKey: Self.newMessagesKey) as? [Data] else { return }
        var latestToNofity: DataDef.Message?

        for messageData in messagesData {
            if let map = try? JSONSerialization.jsonObject(with: messageData, options: .mutableContainers) as? [String: Any] {
                let receivedAt: TimeInterval? = map["received-at"] as? TimeInterval
                let message = DataDef.Message(map, receivedAt: receivedAt != nil ? Date(timeIntervalSince1970: receivedAt!) : Date())

                repository.addMessage(message)
                repository.save()

                if map["show-notification"] as? Bool == true {
                    if latestToNofity == nil || message.sentAt > latestToNofity!.sentAt {
                        latestToNofity = message
                    }
                }
            }
        }

        if latestToNofity != nil {
            showNotification(latestToNofity!)
        }

        userDefaults.removeObject(forKey: Self.newMessagesKey)
    }

    func showNotification(_ message: DataDef.Message) {
        NSLog("[\(type(of: self))] showNotification(\(message.id))")

        let content = UNMutableNotificationContent()
        content.title = message.title
        content.body = message.body
        content.sound = UNNotificationSound.default
        content.badge = 1

        let request = UNNotificationRequest(identifier: message.id, content: content, trigger: nil)
        UNUserNotificationCenter.current().add(request, withCompletionHandler: nil)
    }
}
