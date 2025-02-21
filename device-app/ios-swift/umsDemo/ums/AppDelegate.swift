import OSLog

import UIKit
import UserNotifications

import RationalOwl

@main
class AppDelegate: UIResponder, UIApplicationDelegate {
    private static let appGroupId = "group.com.rationalowl.umsdemo"
    var window: UIWindow?

    private let userDefaults = UserDefaults(suiteName: appGroupId)!

    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions _: [UIApplication.LaunchOptionsKey: Any]?) -> Bool
    {
        UNUserNotificationCenter.current().delegate = self
        
        application.registerForRemoteNotifications()
            
        let notifyCenter = CFNotificationCenterGetDarwinNotifyCenter()
        let observer = UnsafeRawPointer(Unmanaged.passUnretained(self).toOpaque())
        CFNotificationCenterAddObserver(notifyCenter, observer, handleDarwinNotification, MessageSyncService.newMessagesId as CFString, nil, .deliverImmediately)

        let minMgr: MinervaManager = MinervaManager.getInstance()
        minMgr.setAppGroup(Self.appGroupId)
        minMgr.setMessageDelegate(RoMessageDelegate())

        return true
    }

    func applicationWillResignActive(_: UIApplication) {
        NSLog("[\(type(of: self))] applicationWillResignActive")

        let minMgr: MinervaManager = MinervaManager.getInstance()
        minMgr.enterBackground()

        userDefaults.setValue(false, forKey: "isActive")
    }

    func applicationDidBecomeActive(_: UIApplication) {
        NSLog("[\(type(of: self))] applicationDidBecomeActive")

        let minMgr: MinervaManager = MinervaManager.getInstance()
        minMgr.becomeActive()

        userDefaults.setValue(true, forKey: "isActive")
        MessageSyncService.shared.syncMessages()
    }

    func application(_: UIApplication,
                     configurationForConnecting connectingSceneSession: UISceneSession,
                     options _: UIScene.ConnectionOptions) -> UISceneConfiguration
    {
        return UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
    }

    func application(_: UIApplication,
                     didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data)
    {
        let token = deviceToken.map { String(format: "%02.2hhx", $0) }.joined();

        NSLog("APNs registration token:"+token);
        let minMgr: MinervaManager = MinervaManager.getInstance();
        minMgr.setDeviceToken(token);
    }

    func application(_: UIApplication,
                     didFailToRegisterForRemoteNotificationsWithError error: Error)
    {
        NSLog("APNs registration failed: \(error)");
    }

    func application(_: UIApplication,
                     didReceiveRemoteNotification userInfo: [AnyHashable: Any],
                     fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void)
    {
        // system push is sent by RationalOwl for device app lifecycle check.
        // system push is also silent push.
        // if system push has received, just return.
        if userInfo["SystemPush"] != nil {
            print("system push received!!")
            return
        }

        if let aps = userInfo["aps"] as? NSDictionary {
            // silent push recieved)
            if aps["content-available"] != nil {
                // enable notification delivery tracking
                let minMgr: MinervaManager = MinervaManager.getInstance()
                minMgr.enableNotificationTracking(userInfo, appGroup: MessageSyncService.appGroupId)

                // system push is sent by RationalOwl for device app lifecycle check.
                // system push is also silent push.
                // if system push has received, just return.
                if userInfo["SystemPush"] != nil {
                    NSLog("system push received!!")
                    // do nothing.
                }
                // normal silent push which are sent by your app server.
                // do your logic
                else {
                    NSLog("silent push received!")
                    // do your logic
                }
            }
        }

        completionHandler(.newData)
    }
}

extension AppDelegate: UNUserNotificationCenterDelegate {
    func userNotificationCenter(_: UNUserNotificationCenter,
                                willPresent notification: UNNotification,
                                withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void)
    {
        NSLog("[userNotificationCenter] willPresent: \(notification.request.identifier)")

        completionHandler([.alert, .sound])
    }

    func userNotificationCenter(_: UNUserNotificationCenter,
                                didReceive response: UNNotificationResponse) async
    {
        NSLog("[userNotificationCenter] didReceive")

        let userInfo = response.notification.request.content.userInfo
        let messageId = response.notification.request.identifier

        if userInfo["SystemPush"] != nil {
            print("system push received!!")
            return
        }

        let minMgr = MinervaManager.getInstance()
        minMgr?.receivedApns(userInfo)

        var message = MessageLocalDataSource.shared.getMessage(messageId)

        if message == nil {
            message = DataDef.Message(userInfo)
            MessageLocalDataSource.shared.addMessage(message!)
        }

        let storyboard = UIStoryboard(name: "Message", bundle: nil)
        let viewController = storyboard.instantiateInitialViewController() as! MessageNavigationController
        viewController.message = message

        MessageRepository.shared.markAsRead(message!)

        UIApplication.shared.windows.first?.rootViewController = viewController
        UIApplication.shared.windows.first?.makeKeyAndVisible()
    }
}

func handleDarwinNotification(center _: CFNotificationCenter?, observer _: UnsafeMutableRawPointer?, name: CFNotificationName?, object _: UnsafeRawPointer?, userInfo _: CFDictionary?) {
    if name?.rawValue as String? != MessageSyncService.newMessagesId { return }
    MessageSyncService.shared.syncMessages()
}
