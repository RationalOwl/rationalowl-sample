#import "ViewController.h"
#import <RationalOwl/MinervaManager.h>
#import <RationalOwl/Result.h>

@interface ViewController () <DeviceRegisterResultDelegate, MessageDelegate>
@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Set device app register delegate
    MinervaManager *mgr = [MinervaManager getInstance];
    [mgr setDeviceRegisterResultDelegate:self];
    
    // Set message delegate
    [mgr setMessageDelegate:self];
}

- (IBAction)btnReg:(id)sender {
    NSString *gateHost = @"gate.rationalowl.com";  // cloud
    // NSString *gateHost = @"211.239.150.123";
    NSString *serviceId = @"SVC871d16c3-fe28-4f09-ac32-4870d171b067";
    MinervaManager *mgr = [MinervaManager getInstance];
    [mgr registerDevice:gateHost serviceId:serviceId deviceRegName:@"my Ios app"];
}

- (IBAction)btnUnreg:(id)sender {
    NSString *serviceId = @"SVC871d16c3-fe28-4f09-ac32-4870d171b067";
    MinervaManager *mgr = [MinervaManager getInstance];
    [mgr unregisterDevice:serviceId];
}

/////////////////////////////////////////////////////////////
// Device register delegate
/////////////////////////////////////////////////////////////

- (void)onRegisterResult:(int32_t)resultCode resultMsg:(NSString *)resultMsg deviceRegId:(NSString *)deviceRegId {
    NSLog(@"onRegisterResult resultCode = %d resultMsg = %@ deviceRegId = %@", resultCode, resultMsg, deviceRegId);
    
    // Device app registration success!
    // Send deviceRegId to the app server.
    if (resultCode == RESULT_OK) {
        // Handle successful registration
    }
}

- (void)onUnregisterResult:(int32_t)resultCode resultMsg:(NSString *)resultMsg {
    NSLog(@"onUnregisterResult resultCode = %d resultMsg = %@", resultCode, resultMsg);
}

/////////////////////////////////////////////////////////////
// Message delegate
/////////////////////////////////////////////////////////////

/*
 this push message callback is called while below 2 cases
 1. push received while app is foreground
 2. un-delivered push delivered while app launching.
 */
- (void)onPushMsgRecieved:(int32_t)msgSize msgList:(NSArray *)msgList alarmIdx:(int32_t)alarmIdx {
    NSLog(@"onPushMsgRecieved msg size = %d", msgSize);
    
    NSDictionary *msg;
    double sendTime;
    id msgData;
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy/MM/dd HH:mm:ss"];
    
    for (NSDictionary *message in msgList) {
        msg = message;
        // Message sent time
        sendTime = [msg[@"serverTime"] doubleValue];
        NSDate *date = [NSDate dateWithTimeIntervalSince1970:sendTime / 1000];
        msgData = msg[@"data"];
        NSString *customPushStr = [NSString stringWithFormat:@"%@", msgData];
        NSLog(@"push msg = %@", customPushStr);
    }
}

/*
 real time message callbacks
 if you don't use realtime API just ignore it.
 */

// Real-time downstream received.
- (void)onDownstreamMsgRecieved:(NSArray *)msgList {
    NSLog(@"onDownstreamMsgRecieved");
}

// Real-time P2P received.
- (void)onP2PMsgRecieved:(NSArray *)msgList {
    NSLog(@"onP2PMsgRecieved");
}

- (void)onUpstreamMsgResult:(int32_t)resultCode resultMsg:(NSString *)resultMsg msgId:(NSString *)msgId {
    NSLog(@"onMsgRecieved msg upstream message id = %@", msgId);
}

- (void)onP2PMsgResult:(int32_t)resultCode resultMsg:(NSString *)resultMsg msgId:(NSString *)msgId {
    NSLog(@"onMsgRecieved msg p2p message id = %@", msgId);
}

@end
