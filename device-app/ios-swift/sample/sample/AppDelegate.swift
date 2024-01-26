//
//  AppDelegate.swift
//  sample
//
//  Created by 김정도 on 2018. 1. 26..
//  Copyright © 2018년 Rationalowl. All rights reserved.
//

import UIKit;
import UserNotifications;

import RationalOwl;


@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
    
    var window: UIWindow?
    
    
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
        minMgr.setAppGroup("group.com.rationalowl.sample");
        return true;
    }
    
    
    func applicationWillResignActive(_ application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
        
        let minMgr: MinervaManager = MinervaManager.getInstance();
        minMgr.enterBackground();
    }
    
    func applicationDidEnterBackground(_ application: UIApplication) {
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    }
    
    func applicationWillEnterForeground(_ application: UIApplication) {
        // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
    }
    
    func applicationDidBecomeActive(_ application: UIApplication) {
        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
        
        let minMgr: MinervaManager = MinervaManager.getInstance();
        minMgr.becomeActive();
    }
    
    func applicationWillTerminate(_ application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    }
    
    
    // Called when APNs has assigned the device a unique token
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        
        let token = deviceToken.map { String(format: "%02x", $0) }.joined();
        let minMgr: MinervaManager = MinervaManager.getInstance();
        minMgr.setDeviceToken(token);
        
        // Persist it in your backend in case it's new
    }
    
    
    // Called when APNs failed to register the device for push notifications
    func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
        print("APNs registration failed: \(error)");
    }
    
    
    func userNotificationCenter(_ center: UNUserNotificationCenter,  willPresent notification: UNNotification, withCompletionHandler   completionHandler: @escaping (_ options:   UNNotificationPresentationOptions) -> Void) {
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
    
    
    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        
        // Called to let your app know which action was selected by the user for a given notification.
        let userInfo = response.notification.request.content.userInfo;
        print("\(String(describing: userInfo))");
        
        let minMgr: MinervaManager = MinervaManager.getInstance();
        minMgr.receivedApns(userInfo);
    }
}

