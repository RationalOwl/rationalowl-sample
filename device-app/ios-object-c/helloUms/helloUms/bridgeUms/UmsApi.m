//
//  UmsApi.m
//  helloUms
//
//  Created by Mac on 5/24/25.
//

#import "UmsApi.h"
#import "PushAppProto.h"
#import "UmsProtocol.h"

@implementation UmsApi


#define UMS_REST_SERVER @"https://linux.rationalowl.kr:36001"

#define UMS_APP_REGISTER_URL UMS_REST_SERVER @"/pushApp/installApp"
#define UMS_APP_UNREGISTER_URL UMS_REST_SERVER @"/pushApp/unregUser"

#define UMS_APP_REQ_AUTH_NUMBER_URL UMS_REST_SERVER @"/pushApp/reqAuthNumber"
#define UMS_APP_VERIFY_AUTH_NUMBER_URL UMS_REST_SERVER @"/pushApp/verifyAuthNumber"
#define UMS_APP_NOTI_READ_URL UMS_REST_SERVER @"/pushApp/notiRead"
#define UMS_APP_MSG_INFO_URL UMS_REST_SERVER @"/pushApp/msgInfo"
#define UMS_APP_IMG_DATA_URL UMS_REST_SERVER @"/pushApp/imgData"


+ (void)callInstallUmsAppWithAccountId:(NSString *)accountId
                            deviceRegId:(NSString *)deviceRegId
                                phoneNum:(NSString *)phoneNum
                                appUserId:(NSString *)appUserId
                                    name:(NSString *)name
                               completion:(void (^)(NSData * _Nullable, NSError * _Nullable))completion {
    NSLog(@"callInstallUmsApp enter");

    PushAppInstallReq *req = [[PushAppInstallReq alloc] init];
    req.aId = accountId;
    req.dt = APP_TYPE_IOS;
    req.dRId = deviceRegId;
    req.pn = phoneNum;
    req.auId = appUserId;
    req.n = name;

    [self postToUrl:UMS_APP_REGISTER_URL withObject:req completion:completion];
}

+ (void)callUnregisterUmsAppWithAccountId:(NSString *)accountId
                              deviceRegId:(NSString *)deviceRegId
                                completion:(void (^)(NSData * _Nullable, NSError * _Nullable))completion {
    NSLog(@"callUnregisterUmsApp enter");

    PushAppUnregUserReq *req = [[PushAppUnregUserReq alloc] init];
    req.aId = accountId;
    req.dRId = deviceRegId;

    [self postToUrl:UMS_APP_UNREGISTER_URL withObject:req completion:completion];
}

+ (void)callReqAuthNumberWithAccountId:(NSString *)accountId
                           countryCode:(NSString *)countryCode
                               phoneNum:(NSString *)phoneNum
                             completion:(void (^)(NSData * _Nullable, NSError * _Nullable))completion {
    NSLog(@"callReqAuthNumber enter");

    PushAppAuthNumberReq *req = [[PushAppAuthNumberReq alloc] init];
    req.aId = accountId;
    req.cc = countryCode;
    req.pn = phoneNum;

    [self postToUrl:UMS_APP_REQ_AUTH_NUMBER_URL withObject:req completion:completion];
}

+ (void)callVerifyAuthNumberWithAccountId:(NSString *)accountId
                                  phoneNum:(NSString *)phoneNum
                                authNumber:(NSString *)authNumber
                                completion:(void (^)(NSData * _Nullable, NSError * _Nullable))completion {
    NSLog(@"callVerifyAuthNumber enter");

    PushAppVerifyAuthNumberReq *req = [[PushAppVerifyAuthNumberReq alloc] init];
    req.aId = accountId;
    req.dt = APP_TYPE_IOS;
    req.pn = phoneNum;
    req.an = authNumber;

    [self postToUrl:UMS_APP_VERIFY_AUTH_NUMBER_URL withObject:req completion:completion];
}

+ (void)callNotifyReadWithAccountId:(NSString *)accountId
                        deviceRegId:(NSString *)deviceRegId
                               msgId:(NSString *)msgId
                          completion:(void (^)(NSData * _Nullable, NSError * _Nullable))completion {
    NSLog(@"callNotifyRead enter");

    PushAppMsgReadNoti *req = [[PushAppMsgReadNoti alloc] init];
    req.aId = accountId;
    req.dRId = deviceRegId;
    req.mId = msgId;

    [self postToUrl:UMS_APP_NOTI_READ_URL withObject:req completion:completion];
}

+ (void)callMsgInfoWithAccountId:(NSString *)accountId
                     deviceRegId:(NSString *)deviceRegId
                            msgId:(NSString *)msgId
                       completion:(void (^)(NSData * _Nullable, NSError * _Nullable))completion {
    NSLog(@"callMsgInfo enter");

    PushAppMsgInfoReq *req = [[PushAppMsgInfoReq alloc] init];
    req.aId = accountId;
    req.dRId = deviceRegId;
    req.mId = msgId;

    [self postToUrl:UMS_APP_MSG_INFO_URL withObject:req completion:completion];
}

+ (void)callImgDataWithAccountId:(NSString *)accountId
                            msgId:(NSString *)msgId
                            imgId:(NSString *)imgId
                       completion:(void (^)(NSData * _Nullable, NSError * _Nullable))completion {
    NSLog(@"callImgData enter");

    PushAppImgDataReq *req = [[PushAppImgDataReq alloc] init];
    req.aId = accountId;
    req.mId = msgId;
    req.iId = imgId;

    [self postToUrl:UMS_APP_IMG_DATA_URL withObject:req completion:completion];
}

+ (void)postToUrl:(NSString *)url
       withObject:(id)obj
        completion:(void (^)(NSData * _Nullable, NSError * _Nullable))completion {
    NSError *error;
    NSData *postData = [NSJSONSerialization dataWithJSONObject:[obj toDictionary] options:0 error:&error];
    if (error) {
        completion(nil, error);
        return;
    }

    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:url]];
    [request setHTTPMethod:@"POST"];
    [request setValue:@"application/json; charset=utf-8" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:postData];

    NSURLSessionDataTask *task = [[NSURLSession sharedSession] dataTaskWithRequest:request completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
        if (error) {
            completion(nil, error);
        } else {
            completion(data, nil);
        }
    }];
    [task resume];
}

@end
