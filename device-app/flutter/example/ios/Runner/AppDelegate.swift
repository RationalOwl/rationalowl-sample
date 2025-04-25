import Flutter
import UIKit

import flutter_local_notifications
import RationalOwl

@main
@objc class AppDelegate: FlutterAppDelegate {
    override func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {
        GeneratedPluginRegistrant.register(with: self)

        if #available(iOS 10.0, *) {
            UNUserNotificationCenter.current().delegate = self as UNUserNotificationCenterDelegate
        }
        // to notification delivery tracking enable
        // 1. call setAppGroup API at the container app(main app).
        // 2. call enableNotificationTracking() API at the service extension
        //let minMgr: MinervaManager = MinervaManager.getInstance();
        //minMgr.setAppGroup("group.com.rationalowl.flutterexample");
        return super.application(application, didFinishLaunchingWithOptions: launchOptions)
    }

    override func application(_: UIApplication,
                              didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data)
    {
        let token = deviceToken.map { String(format: "%02.2hhx", $0) }.joined()
        NSLog("APNs token2: \(token)")

        let minMgr = MinervaManager.getInstance()!
        minMgr.setDeviceToken(token)
    }
}
