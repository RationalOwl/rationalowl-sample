//
//  PushAppProto.h
//  helloUms
//
//  Created by Mac on 5/24/25.
//

#import <Foundation/Foundation.h>

@class PushAppAuthNumberReq;
@class PushAppAuthNumberRes;
@class PushAppVerifyAuthNumberReq;
@class PushAppVerifyAuthNumberRes;
@class PushAppInstallReq;
@class PushAppInstallRes;
@class PushAppUnregUserReq;
@class PushAppUnregUserRes;
@class PushAppMsgReadNoti;
@class PushAppMsgInfoReq;
@class PushAppMsgInfoRes;
@class PushAppImgDataReq;
@class PushAppImgDataRes;

@interface PushAppAuthNumberReq : NSObject
@property int cId;    // command id
@property NSString *aId; // account id
@property NSString *cc;  // country code
@property NSString *pn;  // phone number
- (instancetype)init;
- (instancetype)initWithDictionary:(NSDictionary *)dict;
- (NSDictionary *)toDictionary;
@end

@interface PushAppAuthNumberRes : NSObject
@property int cId;   // command id
@property NSString *aId;  // account id
@property int rc;   // result code
@property NSString *cmt;  // comment
- (instancetype)init;
- (instancetype)initWithDictionary:(NSDictionary *)dict;
- (NSDictionary *)toDictionary;
@end

@interface PushAppVerifyAuthNumberReq : NSObject
@property int cId;   // command id
@property NSString *aId;  // account id
@property int dt;    // device type 1: andoid, 2: ios
@property NSString *pn;  // phone number
@property NSString *an;  // auth number
- (instancetype)init;
- (instancetype)initWithDictionary:(NSDictionary *)dict;
- (NSDictionary *)toDictionary;
@end

@interface PushAppVerifyAuthNumberRes : NSObject
@property int cId;   // command id
@property NSString *aId;  // account id
@property int rc;   // result code
@property NSString *cmt;  // comment
- (instancetype)init;
- (instancetype)initWithDictionary:(NSDictionary *)dict;
- (NSDictionary *)toDictionary;
@end

@interface PushAppInstallReq : NSObject
@property int cId;   // command id
@property NSString *aId;  // account id
@property int dt;    // device type  1: android, 2: ios
@property NSString *dRId;  // device registration id
@property NSString *pn;    // phone number
@property NSString *auId;  // app user id
@property NSString *n;     // name
- (instancetype)init;
- (instancetype)initWithDictionary:(NSDictionary *)dict;
- (NSDictionary *)toDictionary;
@end

@interface PushAppInstallRes : NSObject
@property int cId;   // command id
@property NSString *aId;  // account id
@property int rc;   // result code
@property NSString *cmt;   // comment
@property NSString *usRid; // ums server registration id
- (instancetype)init;
- (instancetype)initWithDictionary:(NSDictionary *)dict;
- (NSDictionary *)toDictionary;
@end

@interface PushAppUnregUserReq : NSObject
@property int cId;   // command id
@property NSString *aId;  // account id
@property NSString *dRId; // device registration id
- (instancetype)init;
- (instancetype)initWithDictionary:(NSDictionary *)dict;
- (NSDictionary *)toDictionary;
@end

@interface PushAppUnregUserRes : NSObject
@property int cId;   // command id
@property NSString *aId;  // account id
@property int rc;   // result code
@property NSString *cmt;  // comment
@property NSString *dRId; // device registration id
- (instancetype)init;
- (instancetype)initWithDictionary:(NSDictionary *)dict;
- (NSDictionary *)toDictionary;
@end

@interface PushAppMsgReadNoti : NSObject
@property int cId;   // command id
@property NSString *aId;  // account id
@property NSString *dRId; // device registration id
@property NSString *mId;  // message id
- (instancetype)init;
- (instancetype)initWithDictionary:(NSDictionary *)dict;
- (NSDictionary *)toDictionary;
@end

@interface PushAppMsgInfoReq : NSObject
@property int cId;   // command id
@property NSString *aId;  // account id
@property NSString *dRId; // device registration id
@property NSString *mId;  // message id
- (instancetype)init;
- (instancetype)initWithDictionary:(NSDictionary *)dict;
- (NSDictionary *)toDictionary;
@end

@interface PushAppMsgInfoRes : NSObject
@property int cId;   // command id
@property NSString *aId;  // account id
@property int rc;   // result code
@property NSString *cmt;  // comment
@property NSString *mId;  // message id
@property long long ast;  // alimtalk send time (알림톡 발신 시간)
@property int ase;        // alimtalk state 알림톡 전달상태 (0: 미발신, 1: 발신요청, 2: 전달성공, 3: 전달실패)
@property long long mst;  // munja send time (문자 발신 시간)
@property int mt;         // munja type (11: sms, 12: lms, 13: mms)
@property int ms;         // munja state (0: 미발신, 1: 발신요청, 2: 전달성공, 3: 전달실패)
- (instancetype)init;
- (instancetype)initWithDictionary:(NSDictionary *)dict;
- (NSDictionary *)toDictionary;
@end

@interface PushAppImgDataReq : NSObject
@property int cId;   // command id
@property NSString *aId;  // account id
@property NSString *mId;  // message id
@property NSString *iId;  // image id
- (instancetype)init;
- (instancetype)initWithDictionary:(NSDictionary *)dict;
- (NSDictionary *)toDictionary;
@end

@interface PushAppImgDataRes : NSObject
@property int cId;   // command id
@property NSString *aId;  // account id
@property int rc;   // result code
@property NSString *cmt;  // comment
@property NSString *imgD; // image data (base64 인코딩 포맷)
- (instancetype)init;
- (instancetype)initWithDictionary:(NSDictionary *)dict;
- (NSDictionary *)toDictionary;
@end
