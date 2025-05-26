#import "AppDelegate.h"
#import <RationalOwl/MinervaManager.h>

@interface AppDelegate () <UNUserNotificationCenterDelegate>
@end

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    UNUserNotificationCenter *center = [UNUserNotificationCenter currentNotificationCenter];
    center.delegate = self;
    [center requestAuthorizationWithOptions:(UNAuthorizationOptionSound | UNAuthorizationOptionAlert | UNAuthorizationOptionBadge) completionHandler:^(BOOL granted, NSError * _Nullable error) {
        if(!error) {
            dispatch_async(dispatch_get_main_queue(), ^{
                [[UIApplication sharedApplication] registerForRemoteNotifications];
            });
        }
    }];
    
    // to notification delivery tracking
    // call setAppGroup API at the container app(main app).
    MinervaManager* minMgr = [MinervaManager getInstance];
    [minMgr setAppGroup:@"group.com.rationalowl.hello"];
    
    return YES;
}

- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
    
    NSLog(@"devToken=%@",deviceToken);
    NSUInteger length = deviceToken.length;
    
    if (length == 0) {
        return;
    }
    const unsigned char* buffer = deviceToken.bytes;
    NSMutableString* hexString  = [NSMutableString stringWithCapacity:(length * 2)];
    for (int i = 0; i < length; ++i) {
        [hexString appendFormat:@"%02x", buffer[i]];
    }
    NSString* token = [hexString copy];
    MinervaManager* minMgr = [MinervaManager getInstance];
    [minMgr setDeviceToken:token];
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler {
    completionHandler(UIBackgroundFetchResultNewData);
}

// MARK: UNUserNotificationCenterDelegate
- (void)userNotificationCenter:(UNUserNotificationCenter *)center didReceiveNotificationResponse:(UNNotificationResponse *)response withCompletionHandler:(void (^)(void))completionHandler {
    NSLog(@"[userNotificationCenter] didReceive");
    NSDictionary *userInfo = response.notification.request.content.userInfo;
    
    MinervaManager *minMgr = [MinervaManager getInstance];
    [minMgr receivedApns:userInfo];
    
    completionHandler();
}

// MARK: UISceneSession Lifecycle
- (UISceneConfiguration *)application:(UIApplication *)application configurationForConnectingSceneSession:(UISceneSession *)connectingSceneSession options:(UISceneConnectionOptions *)options {
    return [[UISceneConfiguration alloc] initWithName:@"Default Configuration" sessionRole:connectingSceneSession.role];
}

- (void)application:(UIApplication *)application didDiscardSceneSessions:(NSSet<UISceneSession *> *)sceneSessions {
    // Handle discarded scene sessions
}

@end
