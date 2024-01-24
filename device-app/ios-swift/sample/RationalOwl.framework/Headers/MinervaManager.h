//
//  MinervaManager.h
//  ChatClient
//
//  Created by 김정도 on 2015. 7. 30..
//
//

#import <Foundation/Foundation.h>

#import "MinervaDelegate.h"


@interface MinervaManager  : NSObject {
    
@public 
    
@protected
    
@package
    
@private
}

/* class methods */

+ (MinervaManager*) getInstance;


/* API */

/**
 * 단말앱 등록 요청을 한다.
 * 이후 단말앱 등록 결과 DeviceRegisterResultDelegate의 onRegisterResult 콜백이 호출되고 단말 등록 성공시 단말 등록 아이디가 발급된다.
 * @param gateHost 래셔널아울 메시징 게이트로 국가별로 별도 존재 범용 클라우드를 이용할 경우 기본은 'gate.rationalowl.com'이다.
 * @param serviceId 단말앱이 등록할 모바일 서비스의 아이디
 * @param deviceRegName 단말앱이 관리자콘솔에서 표시될 이름으로 앱개발자 및 운영자가 관리자 콘솔에서 확인용으로 이용한다.
 *                      만약 이를 이용하지 않으려면 null을 입력한다.
 */
- (void) registerDevice: (NSString*) gateHost serviceId : (NSString*) serviceId deviceRegName : (NSString*) deviceRegName;


/**
 * 단말앱 등록해제 요청을 한다.
 * 단말앱 등록해제 결과 DeviceRegisterResultDelegate의 onUnregisterResult 콜백이 호출된다.
 * @param serviceId 단말앱이 등록해제할 모바일 서비스의 아이디
 */
- (void) unregisterDevice: (NSString*) serviceId;


/**
 * 단말앱이 등록되어 있는지 확인한다.
 * 단말앱 자동 삭제 모드 설정 후 3주이상 폰을 꺼놓고 방치한 경우 앱 삭제하지 않더라도 자동 등록 해제된다.
 * @param serviceId 서비스 아이디
 * @param deviceRegId 단말 등록 아이디
 *                    - 기존 registerDevice() API결과 발급받은 단말 등록 아이디
 * @retuirn 단말앱 등록 여부
 *        - true: 단말앱이 정상적으로 등록되어 있음
 *        - false: 단말앱이 아직 등록하지 않거나
 *                 단말앱 자동 삭제 모드에서 3주이상 폰을 꺼 놓은 상태로 방치시 자동 등록 해제된 경우
 */
- (BOOL) isDeviceAppRegister: (NSString*) serviceId deviceRegId: (NSString*) deviceRegId;


/**
 * 업스트림메시지를 발신한다.
 * @param data 전달할 데이터로 모바일 서비스 특성에 맞게 json포맷 또는 일반 스트링으로 포맷팅하면 된다.
 * @param serverRegId 데이터를 전달할 앱서버의 등록아이디
 * @return message id
 */
- (NSString*) sendUpstreamMsg: (NSString*) data serverRegId : (NSString*) serverRegId;


/**
 * P2P메시지를 발신한다. 기본 메시지 큐잉 미지원
 * @param data 전달할 데이터로 모바일 서비스 특성에 맞게 json포맷 또는 일반 스트링으로 포맷팅하면 된다.
 * @param devices 데이터를 전달할 단말앱들의 단말등록아이디 목록
 *                    최대 2000대까지 가능
 * @return message id
 */
- (NSString*) sendP2PMsg: (NSString*) data devices : (NSArray*) devices;

/**
 * P2P메시지를 발신한다.
 * @param data 전달할 데이터로 모바일 서비스 특성에 맞게 json포맷 또는 일반 스트링으로 포맷팅하면 된다.
 * @param devices 데이터를 전달할 대상 단말앱들의 단말등록아이디 목록
 *                    최대 2000 단말지정 가능
 * @param supportMsgQ 대상 단말앱이 비활성일 때 미 전달 메시지를 큐잉할지 여부
 *        - false 대상 단말앱이 비활성으로 메시지 수신하지 않더라도 재발송하지 않는다.
 *        - true 미전달 메시지를 메시징 서버에서 큐잉기간(디폴트 3일)동안 큐잉하고 있다가 단말상태가 활성상태가 되면 미전달 메시지를 단말 앱에 전달한다.
 * @param notiTitle
 *            알림 용도로 메시지 전달 시 단말앱이 비활성시 알림 타이틀로 표시할 문자
 * @param notiBody
 *            알림 용도로 메시지 전달 시 단말앱이 비활성시 알림 내용으로 표시할 문자
 *            만약 null이면 data를 알림 창에 표시
 * @return message id
 */
- (NSString*) sendP2PMsg: (NSString*) data devices: (NSArray*) devices supportMsgQ: (BOOL) supportMsgQ notiTitle: (NSString*) notiTitle notiBody: (NSString*) notiBody;



/////////////////////////////////////////
// set and clear Rationalowl delegate
/////////////////////////////////////////

/**
 * 단말앱 등록 요청 및 등록해제 요청 후 결과 수신 시 호출할 Delegate를 지정한다.
 * 해당 Delegate 객체는 단 하나만 지정해야 한다.
 * @param delegate 단말앱 등록/등록해제 결과 콜백 처리를 담당할 Delegate
 *          - 단말앱 등록 결과 수신시 delegate의 onRegisterResult 콜백이 호출된다.
 *          - 단말앱 등록해제 결과 수신시 delegate의 onUnregisterResult 콜백이 호출된다.
 */
- (void) setDeviceRegisterResultDelegate: (id<DeviceRegisterResultDelegate>) delegate;

/**
 * 지정된 단말앱 등록/등록해제 콜백 Delegate를 해제한다.
 */
- (void) clearDeviceRegisterResultDelegate;

/**
 * 실시간 메시지 발신 후 발신 결과나 실시간 메시지 수신 시 호출할 Delegate를 지정한다.
 * 해당 Delegate 객체는 단 하나만 지정해야 한다.
 * @param delegate 메시지 관련 콜백 처리를 담당할 Delegate
 *         - 앱서버로부터 다운스트림 메시지 수신시 delegate의 onDownstreamMsgRecieved 콜백이 호출된다.
 *         - 다른 단말앱으로부터 P2P 메시지 수신신 delegate의 onP2PMsgRecieved 콜백이 호출된다.
 *         - 앱서버에게 업스트림 메시지 발신 후 발신 결과 delegate의 onUpstreamMsgResult 콜백이 호출된다.
 *         - 다른 단말앱에게 P2P 메시지 발신 후 발신 결과 delegate의 onP2PMsgResult 콜백이 호출된다.
 */
- (void) setMessageDelegate: (id<MessageDelegate>) delegate;

/**
 * 지정된 메시지 콜백 Delegate를 해제한다.
 */
- (void) clearMessageDelegate;


/////////////////////////////////////////
// life cycle delegate
/////////////////////////////////////////

/**
 * 단말앱이 액티브 상태가 됨을 단말 라이브러리에게 알린다.
 * AppDelegate 의 applicationDidBecomeActive 함수 내에서 API 호출을 해야 한다
 */
- (void) becomeActive;


/**
 * 단말앱이 백그라운드 상태로 전이됨을 단말 라이브러리에게 알린다.
 * AppDelegate 의 applicationWillResignActive 함수 내에서 API 호출을 해야 한다
 */
- (void) enterBackground;

/////////////////////////////////////////
// APNS delegate
/////////////////////////////////////////

/**
 * 단말앱이 APNS 단말 토큰을 수신했음을 단말 라이브러리에게 알린다.
 * AppDelegate 의 didRegisterForRemoteNotificationsWithDeviceToken 함수 내에서 API 호출을 해야 한다
 */
- (void) setDeviceToken: (NSString *)token;

/**
 * 단말앱이 APNS 알림을 수신했음을 단말 라이브러리에게 알린다.
 * AppDelegate 의 didReceiveNotificationResponse 함수 내에서 API 호출을 해야 한다
 */
- (void) receivedApns: (NSDictionary *)userInfo;


/////////////////////////////////////////
// APNS Notification Service Extension group
/////////////////////////////////////////

/**
 * 설정한 앱그룹을 라이브러리에게 알린다.
 * Notification tracking을 이용하기 위해서는 반드시 컨테이너 앱(메인 앱)에서 setAppGroup을 호출해야 한다.
 * 1. 컨테이너 앱(메인 앱) 에서 setAppGroup API 호출하고
 * 2. Service Extension에서 enableNotificationTracking API 호출
 */
- (void) setAppGroup: (NSString *)appGroup;


/////////////////////////////////////////
// Notification Service Extension API
/////////////////////////////////////////

/**
 * Service Extension에서 알림 수신시 호출하면 푸시 알림 수신 여부 실시간 트래킹을 지원한다.
 *
 * @param data
 *            커스텀 푸시 데이터 키:값 쌍
 */
- (void) enableNotificationTracking: (NSDictionary*) data appGroup: (NSString*) appGroup;



//////////////////////////////////////////////////////////////////////////////
// File storage
//////////////////////////////////////////////////////////////////////////////


#ifdef STORAGE_SUPPORT

/**
 * 파일 스토리지 전송  결과를 처리할 Delegate를 지정한다.
 *
 * @param delegate
 *            uploadFile()과 downloadFile() API를 통해 진행되는 파일업로드 및 다운로드 진행결과 처리를 담당할 Delegate
 */

- (void) setStorageTransferDelegate: (id<StorageTransferResultDelegate>) delegate;


/**
 * 파일 스토리지 전송  결과를 처리할 Delegate를 해제한다.
 */
- (void) clearStorageTransferResultDelegate;


/**
 * 파일을 파일 스토리지에 업로드한다. setStorageTransferListener()로 등록한
 * StorageTransferListener의 콜백을 통해 업로드 진행률과 업로드 결과를 확인한다.
 *
 * @param storageHost
 *            대상 스토리지 호스트  ex) storage.myhost.com 또는  111.111.11.11
 * @param localFilePath
 *            업로드할 로컬 파일
 * @param storageDirPath
 *            업로드할 스토리지 디렉토리 패스, 디렉토리 구분자 "/"
 *            ex) "/", "/video", "/image","/image/sampleImg"
 * @return request id
 */



- (NSString*) uploadFile: (NSString*) storageHost localFilePath : (NSString*) localFilePath storageDirPath : (NSString*) storageDirPath;

/**
 * 파일스토리지서버에서 파일을 다운로드하여 로컬 파일경로에 저장한다. setStorageTransferListener()로 등록한
 * StorageTransferListener의 콜백을 통해 다운로드 진행률과 다운로드 결과를 확인한다.
 *
 * @param storageHost
 *            대상 스토리지 호스트  ex) storage.myhost.com 또는  111.111.11.11
 *
 * @param storageFilePath
 *            다운로드할 스토리지 파일패스: storageDirPath + "/" + 파일 명
 *            ex) /images/img1.jpg
 * @param downDirPath
 *            다운로드한 파일을 저장할 디렉토리 파일 패스
 * @param saveFileName
 *            확장자를 포함한 저장할 파일명
 * @return request id
 */

- (NSString*) downloadFile: (NSString*) storageHost storageFilePath : (NSString*) storageFilePath downDirPath : (NSString*) downDirPath saveFileName : (NSString*) saveFileName;

#endif

@end
