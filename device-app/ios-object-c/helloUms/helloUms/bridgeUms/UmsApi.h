//
//  UmsApi.h
//  helloUms
//
//  Created by Mac on 5/24/25.
//

#import <Foundation/Foundation.h>


@interface UmsApi : NSObject

+ (void)callInstallUmsAppWithAccountId:(NSString *)accountId
                         deviceRegId:(NSString *)deviceRegId
                            phoneNum:(NSString *)phoneNum
                          appUserId:(NSString *)appUserId
                               name:(NSString *)name
                          completion:(void (^)(NSData *data, NSError *error))completion;

+ (void)callUnregisterUmsAppWithAccountId:(NSString *)accountId
                             deviceRegId:(NSString *)deviceRegId
                                completion:(void (^)(NSData *data, NSError *error))completion;

+ (void)callReqAuthNumberWithAccountId:(NSString *)accountId
                          countryCode:(NSString *)countryCode
                             phoneNum:(NSString *)phoneNum
                           completion:(void (^)(NSData *data, NSError *error))completion;

+ (void)callVerifyAuthNumberWithAccountId:(NSString *)accountId
                                 phoneNum:(NSString *)phoneNum
                               authNumber:(NSString *)authNumber
                               completion:(void (^)(NSData *data, NSError *error))completion;

+ (void)callNotifyReadWithAccountId:(NSString *)accountId
                        deviceRegId:(NSString *)deviceRegId
                               msgId:(NSString *)msgId
                          completion:(void (^)(NSData *data, NSError *error))completion;

+ (void)callMsgInfoWithAccountId:(NSString *)accountId
                    deviceRegId:(NSString *)deviceRegId
                           msgId:(NSString *)msgId
                     completion:(void (^)(NSData *data, NSError *error))completion;

+ (void)callImgDataWithAccountId:(NSString *)accountId
                           msgId:(NSString *)msgId
                           imgId:(NSString *)imgId
                     completion:(void (^)(NSData *data, NSError *error))completion;

@end
