//
//  AppDelegate.swift
//  hello
//
//  Created by Mac on 2023/12/21.
//

import UIKit

import RationalOwl

@main
class AppDelegate: UIResponder, UIApplicationDelegate, UNUserNotificationCenterDelegate {



    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        // iOS 10 support
        if #available(iOS 10, *) {
            UNUserNotificationCenter.current().requestAuthorization(options:[.badge, .alert, .sound]){ (granted, error) in };
            application.registerForRemoteNotifications();
        }
        else {
            UIApplication.shared.registerUserNotificationSettings(UIUserNotificationSettings(types: [.sound, .alert, .badge], categories: nil))
            UIApplication.shared.registerForRemoteNotifications()
        }
        // to notification delivery tracking enable
        // 1. call setAppGroup API at the container app(main app).
        // 2. call enableNotificationTracking() API at the service extension
        let minMgr: MinervaManager = MinervaManager.getInstance();
        minMgr.setAppGroup("group.com.rationalowl.hello");
        return true
    }
    
    
    func application( _ application: UIApplication,
                      didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        let token = deviceToken.map { String(format: "%02.2hhx", $0) }.joined();
        let minMgr: MinervaManager = MinervaManager.getInstance();
        minMgr.setDeviceToken(token);
    }
    
    
    // MARK: UNUserNotificationCenterDelegate
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                didReceive response: UNNotificationResponse) async
    {
        NSLog("[userNotificationCenter] didReceive")
        let userInfo = response.notification.request.content.userInfo;
        let minMgr: MinervaManager = MinervaManager.getInstance();
        minMgr.receivedApns(userInfo);
    }
    

    // MARK: UISceneSession Lifecycle
    func application(_ application: UIApplication, configurationForConnecting connectingSceneSession: UISceneSession, options: UIScene.ConnectionOptions) -> UISceneConfiguration {
        // Called when a new scene session is being created.
        // Use this method to select a configuration to create the new scene with.
        
        return UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
    }

    func application(_ application: UIApplication, didDiscardSceneSessions sceneSessions: Set<UISceneSession>) {
        // Called when the user discards a scene session.
        // If any sessions were discarded while the application was not running, this will be called shortly after application:didFinishLaunchingWithOptions.
        // Use this method to release any resources that were specific to the discarded scenes, as they will not return.
    }


}

