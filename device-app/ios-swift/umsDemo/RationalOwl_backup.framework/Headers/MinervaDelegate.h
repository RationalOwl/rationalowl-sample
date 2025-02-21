//
//  MinervaDeligate.h
//  ChatClient
//
//  Created by 김정도 on 2015. 7. 22..
//
//
#import <Foundation/Foundation.h>



@protocol DeviceRegisterResultDelegate <NSObject>

@required



/**
 * 단말앱 등록 결과 호출되는 콜백 함수이다.
 * @param resultCode 결과 코드로 성공시 Result.RESULT_OK 가 반환된다.
 * @param resultMsg 결과 코드인 resultCode에 대한 설명이다.
 * @param deviceRegId 단말앱이 등록이 성공하면 발급받는 단말 등록 아이디이다.
 *               resultCode가 Result.RESULT_OK 이거나 Result.RESULT_DEVICE_ALREADY_REGISTERED일 경우 반환된다.
 *               최초 발급시인 resultCode가 Result.RESULT_OK일 경우 단말앱은 발급받은 단말 등록아이디를 업스트림 API를 통해 앱서버에게 전달해야 한다.
 */
-(void) onRegisterResult: (int) resultCode resultMsg: (NSString*) resultMsg deviceRegId: (NSString*) deviceRegId;


/**
 * 단말앱 등록해제 결과 호출되는 콜백 함수이다.
 * @param resultCode 결과 코드로 성공시 Result.RESULT_OK 가 반환된다.
 * @param resultMsg 결과 코드인 resultCode에 대한 설명이다.
 *               resultCode가 Result.RESULT_OK일 경우 단말앱은 등로해제된 단말 등록 아이디를 앱서버에게 전달해야 한다.
 */
-(void) onUnregisterResult: (int) resultCode resultMsg: (NSString*) resultMsg;

@optional

@end



@protocol MessageDelegate <NSObject>

@required

/**
 * 단말앱이 앱서버로부터 다운스트림 메시지 수신시 호출되는 콜백이다. 단말앱이 포그라운드에서 실시간 다운스트림 메시지를 직접 수신할 경우도 호출되고 APNS 알림을 클릭하거나 사용자가 앱아이콘을 클릭시 미전달된 큐잉된  다운스트림 메시지를 수신할때도 호출된다.
 * @param msgList 수신한 메시지 갯수의 메시지 목록으로 각 메시지는 '메시지 발신 시간/ 메시지 발신한 앱서버 등록 아이디/ 메시지' 세 필드를 가진다.
 */
-(void) onDownstreamMsgRecieved: (NSArray*) msgList;


/**
 * 단말앱이 다른 단말로부터의 P2P 메시지 수신시 호출되는 콜백이다. 단말앱이 포그라운드에서 실시간 P2P 메시지를 직접 수신할 경우도 호출되고 APNS 알림을 클릭하거나 사용자가 앱아이콘을 클릭시 미전달된 큐잉된 P2P 메시지를 수신할때도 호출된다.
 * @param msgList 수신한 메시지 갯수의 메시지 목록으로 각 메시지는 '메시지 발신 시간/ 메시지 발신한 단말앱 등록 아이디/ 메시지' 세 필드를 가진다.
 */
-(void) onP2PMsgRecieved: (NSArray*) msgList;


/**
 * 단말앱이 앱서버로부터 다운스트림 메시지 수신시 호출되는 콜백이다. 단말앱이 포그라운드에서 실시간 다운스트림 메시지를 직접 수신할 경우도 호출되고 APNS 알림을 클릭하거나 사용자가 앱아이콘을 클릭시 미전달된 큐잉된  다운스트림 메시지를 수신할때도 호출된다.
 * @param msgSize 수신한 메시지 갯수
 * @param msgList 수신한 메시지 갯수의 메시지 목록으로 각 메시지는 '메시지 발신 시간/ 메시지 발신한 앱서버 등록 아이디/ 메시지' 세 필드를 가진다.
 * @param alarmIdx 해당 콜백이 호출되는 것이 사용자가 APNS 알림을 클릭해서 수행된 경우 클릭한 APNS 알림과 관련된 메시지 데이터의 인덱스를 가리킨다.
 *                 - 0 이상일 경우 APNS 알림을 클릭해서 호출된 경우로 msgList의 인덱스가 APNS알림 관련 메시지이다.
 *                 - 음수일 경우 APNS 알림을 클릭해서 호출되지 않은 경우.
 */
-(void) onPushMsgRecieved: (int) msgSize msgList: (NSArray*) msgList alarmIdx: (int) alarmIdx;


/**
 * MinervaManager.sendUpstreamMsg() API 호출에 의한 업스트림 메시지 발신 성공 여부를 알려준다.
 * @param resultCode 결과 코드로 성공시 Result.RESULT_OK 가 반환된다.
 * @param resultMsg 결과 코드인 resultCode에 대한 설명이다.
 * @param msgId message id
 */
-(void) onUpstreamMsgResult: (int) resultCode resultMsg: (NSString*) resultMsg msgId: (NSString*) msgId;


/**
 * MinervaManager.sendP2PMsg() API 호출에 의한 P2P 메시지 발신 성공 여부를 알려준다.
 * @param resultCode 결과 코드로 성공시 Result.RESULT_OK 가 반환된다.
 * @param resultMsg 결과 코드인 resultCode에 대한 설명이다.
 * @param msgId msg id
 */
-(void) onP2PMsgResult: (int) resultCode resultMsg: (NSString*) resultMsg msgId: (NSString*) msgId;

@optional

@end




@protocol StorageTransferResultDelegate <NSObject>

@required



/**
 * uploadFile()API 진행 형황을 알려준다.
 * @param filePath
 *            파일 패스
 * @param percent
 *            업로드 진행 퍼센트
 * @param uploadSize
 *            업로드한 파일 사이즈(byte 단위)
 * @param totalSize
 *            전체 파일 사이즈 (byte 단위)
 */

-(void) onUploadProgress: (NSString*) filePath percent: (int) percent uploadSize: (long) uploadSize totalSize: (long) totalSize;


/**
 * uploadFile()API 결과를 알려준다.
 * @param filePath
 *            파일 패스
 * @param resultCode
 *            Result.RESULT_OK : 업로드 성공
 *            그외는 에러
 * @param resultMsg
 *            resultCode의 값에 대한 의미
 */

-(void) onUploadResult: (NSString*) filePath resultCode: (int) resultCode resultMsg: (NSString*) resultMsg;



/**
 * downloadFile()API 진행 형황을 알려준다.
 * @param filePath
 *            파일 패스
 * @param percent
 *            다운로드 진행 퍼센트
 * @param downloadSize
 *            다운로드한 파일 사이즈(byte 단위)
 * @param totalSize
 *            전체 파일 사이즈(byte 단위)
 */

-(void) onDownloadProgress: (NSString*) filePath percent: (int) percent downloadSize: (long) downloadSize totalSize: (long) totalSize;


/**
 * downloadFile()API 결과를 알려준다.
 * @param filePath
 *            다운로드 한 파일이 저장된 파일패스
 *            resultCode가 Result.RESULT_OK 일 경우 filePath에서 파일 작업 가능
 * @param resultCode
 *            Result.RESULT_OK : 다운로드 성공
 *            그외는 에러
 * @param resultMsg
 *            resultCode의 값에 대한 의미
 */

-(void) onDownloadResult: (NSString*) filePath resultCode: (int) resultCode resultMsg: (NSString*) resultMsg;



@optional

@end
