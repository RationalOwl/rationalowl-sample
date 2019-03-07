//
//  NotificationService.swift
//  NotificationServiceExtension
//
//  Created by 김정도 on 04/03/2019.
//  Copyright © 2019 Rationalowl. All rights reserved.
//

import UserNotifications;
import RationalOwl;


class NotificationService: UNNotificationServiceExtension {

    var contentHandler: ((UNNotificationContent) -> Void)?
    var bestAttemptContent: UNMutableNotificationContent?

    override func didReceive(_ request: UNNotificationRequest, withContentHandler contentHandler: @escaping (UNNotificationContent) -> Void) {
        self.contentHandler = contentHandler
        bestAttemptContent = (request.content.mutableCopy() as? UNMutableNotificationContent)
        
        if let bestAttemptContent = bestAttemptContent {
            
            // Called to let your app know which action was selected by the user for a given notification.
            let userInfo = bestAttemptContent.userInfo;
            print("\(String(describing: userInfo))");
            
            // enable notification delivery tracking
            let minMgr: MinervaManager = MinervaManager.getInstance();
            minMgr.enableNotificationTracking(userInfo, appGroup: "group.com.rationalowl.sample");
            
            // Modify the notification content here...
            bestAttemptContent.title = userInfo["myNotiTitle"] as! String;
            bestAttemptContent.body = userInfo["myNotiBody"] as! String;
            
            // draw image from image url
            //NSString* imageUrl = userInfo[@"imageUrl"];
            
            // set custom alarm sound from sound url
            //NSString* soundUrl = userInfo[@"soundUrl"];
            
            contentHandler(bestAttemptContent)
        }
    }
    
    override func serviceExtensionTimeWillExpire() {
        // Called just before the extension will be terminated by the system.
        // Use this as an opportunity to deliver your "best attempt" at modified content, otherwise the original push payload will be used.
        if let contentHandler = contentHandler, let bestAttemptContent =  bestAttemptContent {
            contentHandler(bestAttemptContent)
        }
    }

}