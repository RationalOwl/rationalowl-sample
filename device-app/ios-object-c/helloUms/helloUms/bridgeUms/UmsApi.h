//
//  UmsApi.h
//  helloUms
//
//  Created by Mac on 5/24/25.
//

#import <Foundation/Foundation.h>


@interface UmsApi : NSObject


////////////////////////////////////////////////////////////////////
// RationalUms 연동시 반드시 호출해야 하는 2개의 rest api
////////////////////////////////////////////////////////////////////


// hello ums 샘플앱에서는 필수 api 2개의 사용을 보여준다.
// 옵션 api의 사용법도 동일하기 때문에 앱에서 원하는 기능을 추가하여 사용할 수 있다.

/**
* 단말앱을 등록한다.
*
* @param accountId (필수)앱 서비스에 부여되는 아이디
* @param deviceRegId (필수)단말앱 아이디
* @param phoneNum (옵션)사용자 폰번호(푸시 알림 미 전달시 문자, 알림톡으로 수신하고자 할때)
* @param appUserId (옵션)앱 사용자 아이디
* @param name (옵션)앱 사용자 이름
* @param completion (필수)API 결과 콜백
*/
+ (void)callInstallUmsApp:(NSString *)accountId
                         deviceRegId:(NSString *)deviceRegId
                            phoneNum:(NSString *)phoneNum
                          appUserId:(NSString *)appUserId
                               name:(NSString *)name
                          completion:(void (^)(NSData *data, NSError *error))completion;


/**
* 단말앱을 탈퇴한다.
*
* @param accountId (필수)앱 서비스에 부여되는 아이디
* @param deviceRegId (필수)단말앱 아이디
* @param completion (필수)API 결과 콜백
*/
+ (void)callUnregisterUmsApp:(NSString *)accountId
                             deviceRegId:(NSString *)deviceRegId
                                completion:(void (^)(NSData *data, NSError *error))completion;


////////////////////////////////////////////////////////////////////
// optional APIs
////////////////////////////////////////////////////////////////////


/**
* 사용자 전화번호 검증을 위한 보안번호를 요청한다.
* 이후 SMS로 보안번호가 날아온다.
* 앱 가입시 사용자가 폰번호를 잘못 기입하는 확률이 최소 1프로 이상으로 폰번호를 통한 문자, 알림톡 통합 이용시 반드시
* 해당 API를 통해 정확한 폰번호 입력을 권고한다.
*
* @param accountId (필수)앱 서비스에 부여되는 아이디
* @param countryCode (필수) 전화번호 국가코드 (대한민국 82)
* @param phoneNum (필수)사용자 폰번호
* @param completion (필수)API 결과 콜백
*/
+ (void)callReqAuthNumber:(NSString *)accountId
                          countryCode:(NSString *)countryCode
                             phoneNum:(NSString *)phoneNum
                           completion:(void (^)(NSData *data, NSError *error))completion;


/**
* callReqAuthNumber 호출결과 SMS로 전달받은 보안번호를 검증한다.
*
* @param accountId (필수)앱 서비스에 부여되는 아이디
* @param phoneNum (필수)사용자 폰번호
* @param authNumber (필수)callReqAuthNumber 호출결과 SMS로 전달받은 보안번호
* @param completion (필수)API 결과 콜백
*/
+ (void)callVerifyAuthNumber:(NSString *)accountId
                                 phoneNum:(NSString *)phoneNum
                               authNumber:(NSString *)authNumber
                               completion:(void (^)(NSData *data, NSError *error))completion;


/**
* 사용자 수신확인시 수신확인했음을 알려준다.
* 해당 API호출로 실시간 모니터링, 발신내역, 통계 등에 수신확인 정보가 실시간 반영된다.
*
* @param accountId (필수)앱 서비스에 부여되는 아이디
* @param deviceRegId (필수)단말앱 아이디
* @param msgId (필수) 푸시알림 수신시 mId 필드에 기입된 메시지 아이디
* @param completion (필수)API 결과 콜백
*/
+ (void)callNotifyRead:(NSString *)accountId
                        deviceRegId:(NSString *)deviceRegId
                               msgId:(NSString *)msgId
                          completion:(void (^)(NSData *data, NSError *error))completion;


 /**
  * 푸시알림 외 메시지 전달 정보를 요청한다.
  * 전달정보는 알림톡 발신시간, 알림톡 전달상태(전달성공, 실패), 문자 발신시간, 발신한 문자종류(sms/lms/mms), 문자전달 상태(전달성공, 실패)이다.
  *
  * @param accountId (필수)앱 서비스에 부여되는 아이디
  * @param deviceRegId (필수)단말앱 아이디
  * @param msgId (필수) 푸시알림 수신시 mId 필드에 기입된 메시지 아이디
  * @param completion (필수)API 결과 콜백
  */
+ (void)callMsgInfo:(NSString *)accountId
                    deviceRegId:(NSString *)deviceRegId
                           msgId:(NSString *)msgId
                     completion:(void (^)(NSData *data, NSError *error))completion;


/**
 * 이미지를 첨부한 알림 발신시 푸시알림에 ii 필드에 이미지 아이디가 세팅되어 오는데 해당 아이디로 이미지 데이터를 요청한다.
 *
 * @param accountId (필수)앱 서비스에 부여되는 아이디
 * @param msgId (필수) 푸시알림 수신시 mId 필드에 기입된 메시지 아이디
 * @param imgId (필수) 푸시알림 수신시 ii 필드에 기입된 이미지 아이디
 * @param completion (필수)API 결과 콜백
 */
+ (void)callImgData:(NSString *)accountId
                           msgId:(NSString *)msgId
                           imgId:(NSString *)imgId
                     completion:(void (^)(NSData *data, NSError *error))completion;

@end
