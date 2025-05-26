//
//  NotificationService.m
//  NotiServiceExtension
//
//  Created by Mac on 5/23/25.
//
#import <RationalOwl/MinervaManager.h>

#import "NotificationService.h"

@interface NotificationService ()

@property (nonatomic, strong) void (^contentHandler)(UNNotificationContent *contentToDeliver);
@property (nonatomic, strong) UNMutableNotificationContent *bestAttemptContent;

@end

@implementation NotificationService

- (void)didReceiveNotificationRequest:(UNNotificationRequest *)request withContentHandler:(void (^)(UNNotificationContent * _Nonnull))contentHandler {
    self.contentHandler = contentHandler;
    self.bestAttemptContent = [request.content mutableCopy];
    
    NSDictionary* userInfo = self.bestAttemptContent.userInfo;
    
    // enable notification delivery tracking
    MinervaManager* minMgr = [MinervaManager getInstance];
    [minMgr enableNotificationTracking:userInfo appGroup:@"group.com.rationalowl.hello"];
    
    // Modify the notification content here...
    self.bestAttemptContent.title = userInfo[@"myNotiTitle"];
    self.bestAttemptContent.body = userInfo[@"myNotiBody"];
    
    // draw image from image url
    //NSString* imageUrl = userInfo[@"imageUrl"];
    
    // set custom alarm sound from sound url
    //NSString* soundUrl = userInfo[@"soundUrl"];
    
    self.contentHandler(self.bestAttemptContent);
}

- (void)serviceExtensionTimeWillExpire {
    // Called just before the extension will be terminated by the system.
    // Use this as an opportunity to deliver your "best attempt" at modified content, otherwise the original push payload will be used.
    self.contentHandler(self.bestAttemptContent);
}

@end
