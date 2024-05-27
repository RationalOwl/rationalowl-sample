import RationalOwl
import UserNotifications

class NotificationService: UNNotificationServiceExtension {
    private static let appGroupId = "group.com.rationalowl.umsdemo"
    private static let newMessagesId = "com.rationalowl.umsdemo.newMessages"
    private static let newMessagesKey = "newMessages"

    private var contentHandler: ((UNNotificationContent) -> Void)?
    private var bestAttemptContent: UNMutableNotificationContent?

    private let userDefaults = UserDefaults(suiteName: appGroupId)!

    override func didReceive(_ request: UNNotificationRequest,
                             withContentHandler contentHandler: @escaping (UNNotificationContent) -> Void)
    {
        self.contentHandler = contentHandler
        bestAttemptContent = (request.content.mutableCopy() as? UNMutableNotificationContent)

        if let bestAttemptContent = bestAttemptContent {
            // Called to let your app know which action was selected by the user for a given notification.
            var userInfo = bestAttemptContent.userInfo
            debugPrint(userInfo)

            // enable notification delivery tracking
            let minMgr = MinervaManager.getInstance()
            minMgr?.enableNotificationTracking(userInfo, appGroup: Self.appGroupId)

            // system push is sent by RationalOwl for device app lifecycle check.
            // system push is also silent push.
            // if system push has received, just return.
            if userInfo["SystemPush"] != nil {
                print("system push received!!")
                return
            }

            userInfo["received-at"] = Date().timeIntervalSince1970
            userInfo["show-notification"] = userDefaults.bool(forKey: "isActive")

            if let userInfoJson = try? JSONSerialization.data(withJSONObject: userInfo) {
                var messages = userDefaults.array(forKey: Self.newMessagesKey) as? [Data] ?? []
                messages.append(userInfoJson)
                userDefaults.set(messages, forKey: Self.newMessagesKey)

                CFNotificationCenterPostNotification(CFNotificationCenterGetDarwinNotifyCenter(),
                                                     CFNotificationName(Self.newMessagesId as CFString), nil, nil, true)
            }

            // Modify the notification content here...
            if userInfo["title"] != nil {
                bestAttemptContent.title = userInfo["title"] as! String
            }

            if userInfo["body"] != nil {
                bestAttemptContent.body = userInfo["body"] as! String
            }

            contentHandler(bestAttemptContent)
        }
    }

    override func serviceExtensionTimeWillExpire() {
        // Called just before the extension will be terminated by the system.
        // Use this as an opportunity to deliver your "best attempt" at modified content, otherwise the original push payload will be used.
        if let contentHandler = contentHandler, let bestAttemptContent = bestAttemptContent {
            contentHandler(bestAttemptContent)
        }
    }
}
