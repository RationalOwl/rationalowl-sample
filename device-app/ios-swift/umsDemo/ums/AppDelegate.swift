import OSLog

import UIKit
import UserNotifications

import FirebaseCore
import FirebaseMessaging
import RationalOwl

@main
class AppDelegate: UIResponder, UIApplicationDelegate
{
    private static let appGroupId = "group.com.rationalowl.umsdemo"
    var window: UIWindow?
    
    private let userDefaults = UserDefaults(suiteName: appGroupId)!
    private let repository = MessagesRepository.shared
    private let manager = MinervaManager.getInstance()!
    
    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool
    {
        UNUserNotificationCenter.current().delegate = self
        
        if #available(iOS 10, *)
        {
            UNUserNotificationCenter.current().requestAuthorization(options: [.badge, .alert, .sound]){ (granted, error) in }
            application.registerForRemoteNotifications()
        }
        else
        {
            UIApplication.shared.registerUserNotificationSettings(UIUserNotificationSettings(types: [.sound, .alert, .badge], categories: nil))
            UIApplication.shared.registerForRemoteNotifications()
        }
        
        let notifyCenter = CFNotificationCenterGetDarwinNotifyCenter()
        let observer = UnsafeRawPointer(Unmanaged.passUnretained(self).toOpaque())
        CFNotificationCenterAddObserver(notifyCenter, observer, handleDarwinNotification, MessageSyncService.newMessagesId as CFString, nil, .deliverImmediately)
        
        manager.setAppGroup(MessageSyncService.appGroupId)
        manager.setMessageDelegate(RoMessageDelegate())
        return true
    }
    
    func applicationWillResignActive(_ application: UIApplication)
    {
        NSLog("[\(type(of: self))] applicationWillResignActive")
        manager.enterBackground()
        userDefaults.setValue(false, forKey: "isActive")
    }
    
    func applicationDidBecomeActive(_ application: UIApplication)
    {
        NSLog("[\(type(of: self))] applicationDidBecomeActive")
        manager.becomeActive()
        userDefaults.setValue(true, forKey: "isActive")
        
        MessageSyncService.shared.syncMessages()
    }
    
    func application(_ application: UIApplication,
                     configurationForConnecting connectingSceneSession: UISceneSession,
                     options: UIScene.ConnectionOptions) -> UISceneConfiguration
    {
        return UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
    }
    
    func registerForPushNotifications()
    {
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge]) {
            [weak self] granted, error in
            
            NSLog("Permission granted: \(granted)")
            guard granted else { return }
            
            self?.getNotificationSettings()
        }
    }
    
    func getNotificationSettings()
    {
        UNUserNotificationCenter.current().getNotificationSettings { settings in
            NSLog("Notification settings: \(settings)")
            guard settings.authorizationStatus == .authorized else { return }
            DispatchQueue.main.async {
                UIApplication.shared.registerForRemoteNotifications()
            }
        }
    }
    
    func application( _ application: UIApplication,
                      didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data)
    {
        let token = deviceToken.map { String(format: "%02.2hhx", $0) }.joined()
        manager.setDeviceToken(token)
    }
    
    func application(_ application: UIApplication,
                     didFailToRegisterForRemoteNotificationsWithError error: Error)
    {
        NSLog("APNs registration failed: \(error)")
    }
    
    
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any], fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {

        NSLog("User Info : %@", userInfo);
        let aps: Dictionary<String, Any> = userInfo["aps"] as! Dictionary<String, Any>;
        
        // silent push recieved
        if (aps["content-available"] != nil) {
            // enable notification delivery tracking
            let minMgr: MinervaManager = MinervaManager.getInstance();
            minMgr.enableNotificationTracking(userInfo, appGroup: "group.com.rationalowl.hello")

            // system push is sent by RationalOwl for device app lifecycle check.
            // system push is also silent push.
            // if system push has received, just return.
            if (userInfo["SystemPush"] != nil) {
                NSLog("system push received!!");
                // do nothing.
            }
            // normal silent push which are sent by your app server.
            // do your logic
            else {
                NSLog("silent push received!");
                // do your logic
            }
        }
        completionHandler(.newData)
    }
}

extension AppDelegate: UNUserNotificationCenterDelegate
{
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                willPresent notification: UNNotification,
                                withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void)
    {
        NSLog("[userNotificationCenter] willPresent: \(notification.request.identifier)")
        
        completionHandler([.alert, .sound])
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                didReceive response: UNNotificationResponse) async
    {
        NSLog("[userNotificationCenter] didReceive")
        
        let userInfo = response.notification.request.content.userInfo
        let messageId = response.notification.request.identifier
        
        if userInfo["SystemPush"] != nil
        {
            print("system push received!!")
            return
        }
        
        manager.receivedApns(userInfo)
        
        var message = repository.getMessage(messageId)
        
        if message == nil
        {
            message = Message(userInfo)
            repository.addMessage(message!)
        }
        
        let storyboard = UIStoryboard(name: "Message", bundle: nil)
        let viewController = storyboard.instantiateInitialViewController() as! MessageNavigationController
        viewController.message = message
        
        MessageReadReceiptService.shared.markAsRead(message!.id)
        
        UIApplication.shared.windows.first?.rootViewController = viewController
        UIApplication.shared.windows.first?.makeKeyAndVisible()
    }
}

func handleDarwinNotification(center: CFNotificationCenter?, observer: UnsafeMutableRawPointer?, name: CFNotificationName?, object: UnsafeRawPointer?, userInfo: CFDictionary?) -> Void
{
    if name?.rawValue as String? != MessageSyncService.newMessagesId { return }
    MessageSyncService.shared.syncMessages()
}
