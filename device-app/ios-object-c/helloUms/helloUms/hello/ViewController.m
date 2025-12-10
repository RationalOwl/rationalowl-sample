#import "ViewController.h"
#import <RationalOwl/MinervaManager.h>
#import <RationalOwl/Result.h>

#import "ViewController.h"

#import "UmsApi.h"
#import "PushAppProto.h"

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    MinervaManager *mgr = [MinervaManager getInstance];
    [mgr setDeviceRegisterResultDelegate:self];
    [mgr setMessageDelegate:self];
}


- (IBAction)btnReg:(id)sender {
    // airkorea
    NSString *gateHost = @"210.99.81.117";
    NSString *serviceId = @"SVC1829c8fd-920e-444d-805d-ef04e96c4811"; //@"696c
    //NSString *gateHost = @"linux.rationalowl.kr"; //@"ums.rationalowl.com"; //
    //NSString *serviceId = @"SVCf3173aae-d166-4199-b266-3aa78f601508"; //@"696c8a6aa7e44903b02e3948714d3887";
    MinervaManager *mgr = [MinervaManager getInstance];
    [mgr registerDevice:gateHost
              serviceId:serviceId
         deviceRegName:@"my Ios helloUms app"];
}

- (IBAction)btnUnreg:(id)sender {
    // airkorea
    NSString *serviceId = @"SVC1829c8fd-920e-444d-805d-ef04e96c4811";
    //NSString *serviceId = @"SVCf3173aae-d166-4199-b266-3aa78f601508";
    
    MinervaManager *mgr = [MinervaManager getInstance];
    [mgr unregisterDevice:serviceId];
}

#pragma mark - DeviceRegisterResultDelegate

- (void)onRegisterResult:(int32_t)resultCode
              resultMsg:(NSString *)resultMsg
            deviceRegId:(NSString *)deviceRegId {

    NSLog(@"onRegisterResult %d", resultCode);
    NSString *msg = [NSString stringWithFormat:@"%@ registration id: %@", resultMsg, deviceRegId];
    NSLog(@"%@", msg);

    if (resultCode == RESULT_OK || resultCode == RESULT_DEVICE_ALREADY_REGISTERED) {
        NSLog(@"rationalOwl register success!!!");
        //NSString *fDeviceRegId = deviceRegId;
        // airkorea
        [UmsApi callInstallUmsApp:@"9139c4e4-4fa3-47b1-bc6b-f4921fc54214"
                                   deviceRegId:deviceRegId
                                      phoneNum:nil
                                    appUserId:nil
                                           name:nil
                                    completion:^(NSData *data, NSError *error) {
            if (error) {
                NSLog(@"API 호출 실패: %@", error.localizedDescription);
                return;
            }

            NSString *responseString = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
            NSLog(@"응답 데이터: %@", responseString);

            NSError *jsonError = nil;
            NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data options:0 error:&jsonError];
            if (jsonError) {
                NSLog(@"디코딩 오류: %@", jsonError.localizedDescription);
                return;
            }

            PushAppInstallRes *res = [[PushAppInstallRes alloc] initWithDictionary:jsonDict];
            if (res.rc == RESULT_OK) {
                self.mDeviceRegId = deviceRegId;
                NSLog(@"단말앱 등록 성공");
            } else {
                NSLog(@"에러 코드: %d, 사유: %@", res.rc, res.cmt);
            }
        }];
    } else {
        NSLog(@"단말앱 등록 에러: %@", resultMsg);
    }
}

- (void)onUnregisterResult:(int32_t)resultCode resultMsg:(NSString *)resultMsg {
    if (resultCode == RESULT_OK) {
        if (!self.mDeviceRegId) {
            NSLog(@"등록 아이디 없음");
            return;
        }
        // airkorea
        [UmsApi callUnregisterUmsApp:@"9139c4e4-4fa3-47b1-bc6b-f4921fc54214"
                                     deviceRegId:self.mDeviceRegId
                                      completion:^(NSData *data, NSError *error) {
            if (error) {
                NSLog(@"API 호출 실패: %@", error.localizedDescription);
                return;
            }

            NSError *jsonError = nil;
            NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data options:0 error:&jsonError];
            if (jsonError) {
                NSLog(@"디코딩 오류: %@", jsonError.localizedDescription);
                return;
            }

            PushAppUnregUserRes *res = [[PushAppUnregUserRes alloc] initWithDictionary:jsonDict];
            if (res.rc == RESULT_OK) {
                self.mDeviceRegId = nil;
                NSLog(@"단말앱 해제 성공");
            } else {
                NSLog(@"에러 코드: %d, 사유: %@", res.rc, res.cmt);
            }
        }];
    } else {
        NSLog(@"단말앱 해제 에러: %@", resultMsg);
    }
}

#pragma mark - MessageDelegate

- (void)onDownstreamMsgRecieved:(NSArray *)msgList {
    NSLog(@"onDownstreamMsgRecieved");
}

- (void)onP2PMsgRecieved:(NSArray *)msgList {
    NSLog(@"onP2PMsgRecieved");
}

- (void)onPushMsgRecieved:(int32_t)msgSize msgList:(NSArray *)msgList alarmIdx:(int32_t)alarmIdx {
    NSLog(@"onPushMsgRecieved msg size = %d", msgSize);
    
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy/MM/dd HH:mm:ss"];
    
    for (NSDictionary *msg in msgList) {
        //double sendTime = [msg[@"serverTime"] doubleValue];
        //NSDate *date = [NSDate dateWithTimeIntervalSince1970:sendTime / 1000];
        id msgData = msg[@"data"];
        NSString *customPushStr = [NSString stringWithFormat:@"%@", msgData];
        NSLog(@"push msg = %@", customPushStr);
    }
}

- (void)onUpstreamMsgResult:(int32_t)resultCode resultMsg:(NSString *)resultMsg msgId:(NSString *)msgId {
    NSLog(@"onMsgRecieved msg upstream message id = %@", msgId);
}

- (void)onP2PMsgResult:(int32_t)resultCode resultMsg:(NSString *)resultMsg msgId:(NSString *)msgId {
    NSLog(@"onMsgRecieved msg p2p message id = %@", msgId);
}

@end
