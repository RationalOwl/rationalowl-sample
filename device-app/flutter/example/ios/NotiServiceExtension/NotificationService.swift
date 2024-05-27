import RationalOwl
import UserNotifications

class NotificationService: UNNotificationServiceExtension {
    private static let appGroup = "group.com.rationalowl.flutterexample"

    var contentHandler: ((UNNotificationContent) -> Void)?
    var bestAttemptContent: UNMutableNotificationContent?

    override func didReceive(_ request: UNNotificationRequest,
                             withContentHandler contentHandler: @escaping (UNNotificationContent) -> Void)
    {
        self.contentHandler = contentHandler
        bestAttemptContent = (request.content.mutableCopy() as? UNMutableNotificationContent)

        if let bestAttemptContent = bestAttemptContent {
            let userInfo = bestAttemptContent.userInfo
            print("userInfo: \(String(describing: userInfo))")

            let minMgr = MinervaManager.getInstance()!
            minMgr.enableNotificationTracking(userInfo, appGroup: Self.appGroup)

            if userInfo["notiTitle"] != nil {
                bestAttemptContent.title = userInfo["notiTitle"] as! String
            }
            if userInfo["notiBody"] != nil {
                bestAttemptContent.body = userInfo["notiBody"] as! String
            }

            contentHandler(bestAttemptContent)
        }
    }

    override func serviceExtensionTimeWillExpire() {
        if let contentHandler = contentHandler, let bestAttemptContent = bestAttemptContent {
            contentHandler(bestAttemptContent)
        }
    }
}
