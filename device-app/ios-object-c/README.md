# 래셔널아울 IOS Objective-C 단말앱 샘플
IOS Objective-C 단말앱 샘플은 IOS Objective-C 단말앱 라이브러리에서 제공하는 API를 이용해서 단말앱을 만드는 것을 쉽게 따라할 수 있도록 작성되었고 실제 개발시 해당 소스를 기반으로 보다 쉽게 개발 할 수 있다.

## 개발 전 IOS 설정
[IOS 설정 가이드](https://github.com/RationalOwl/rationalowl-guide/tree/master/device-app/ios-setting)를 통해 개발 전 아래 설정을 먼저 진행해야 한다.

- APNS 발신용 인증서 생성 및 래셔널아울 서비스에 등록
- 프로비저닝 프로파일 설정


## 샘플 프로젝트 설정
1. github에서 샘플코드를 다운받는다.
2. 다운받은 폴더에서 'sample.xcodeproj'파일을 클릭하여 XCode를 실행한다.
 - 프로젝트 루트에 'RationalOwl.framework'파일이 래셔널아울 OS Objective-C 단말앱 라이브러리이다.
 - General > Identify > Bundle Identifier에 developer.apple.com에서 등록한 App ID와 동일한 값을 입력해야 한다.
    

![이미지 이름](./img/project.png)

## 샘플 단말앱 실행

IOS 단말을 Mac에 연결 후 XCode 빌드를 통해 단말에 설치 및 실행한다. 
 - 샘플 단말앱이 APNS 알람을 이용해 시뮬레이션으로 실행되지 않는다.




![이미지 이름](./img/msg_scrn.png)



래셔널아울 관리자콘솔이 제공하는 실시간 모니터링은 서비스 개발 전 단계에서 실시간 데이터의 전달 현황뿐 아니라 개발 단계에서 앱서버와 단말앱의 각 기능 별 성공 여부를 확인할 수 있어 개발속도를 향상시키고 서비스 운영단계에서는 예측 가능성과 서비스 대응력을 높이는 역할을 한다. 샘플앱 개발시 관리자콘솔을 이용해 각 기능별 동작을 확인함으로써 그 편의성을 확인할 수 있을 것이다.

## 샘플 단말앱 UI 구성
샘플 단말앱의 화면 구성을 보려면 XCode에서 Main.storyboard를 클릭하면 확인할 수 있는데 2개의 화면으로 구성된다.

 - 메시지 화면
   - 다운스트림 메시지 수신시 화면에 표시
   - P2P 메시지 수신시 화면에 표시
   - 업스트림 메시지 발신 기능 제공
   - P2P 메시지 발신 기능 제공
 - 단말앱 등록/등록 해제 화면
   - 단말앱 등록 기능 제공
   - 단말앱 등록해제 기능 제공
   - 수/발신 메시지는 TableView에 디스플레이

![이미지 이름](./img/storyboard.png)

 
## 샘플 단말앱 소스 구성
샘플 단말앱의 소스는 세개로 구성된다. 각 소스에서는 래셔널아울 단말앱 개발시 필요한 코드와  래셔널아울 단말앱 API이용법을 제공한다. 

 - AppDelegate
   - 단말앱 구동시 APNS 등록
   - 단말앱 백그라운드 전환시 enterBackground API 호출
   - 단말앱 포그라운드 전환시 becomeActive API 호출
   - APNS콜백에서 호출해야하는 API 

 - RegViewController
   - 단말앱 등록 API 호출
   - 단말앱 등록해제 API 호출
   - 단말앱 등록/등록해제 결과 콜백

- MsgViewController
   - 업스트림 메시지 발신 API 호출
   - 업스트림 메시지 발신 결과 콜백
   - P2P 메시지 발신 API 호출
   - P2P 메시지 발신 결과 콜백
   - 다운스트림 메시지 수신시 콜백
   - P2P 메시지 수신시 콜백


>## 단말앱 등록

샘플 단말앱에서 단말앱을 등록하기 위해서는 다음 절차를 따르면 된다.

1. 샘플앱 초기 메시지 화면에서 '등록화면->'버튼을 클릭 => 등록화면으로 전이
2. 등록화면에서 '단말앱 등록'버튼 클릭


![이미지 이름](./img/reg_scrn.png)


샘플코드에서 registerDevice를 검색하면 아래의 샘플코드를 확인할 수 있다. 


```swift
- (IBAction) regDevice {
    
    NSString* gateHost = inputNameField.text; //gate.rationalowl.com
    MinervaManager* mgr = [MinervaManager getInstance];
    [mgr registerDevice:gateHost serviceId:@"faebcfe844d54d449136491fb253619d" deviceRegName:@"my i pad 1"];
}
    
```

MinervaManager.registerDevice() API의 각 인자별 의미는 다음과 같다.

1. gateHost
 - 래셔널아울 메시징 게이트 서버
 - 국가별로 별도로 존재
 - 무료평가판의 경우 기본 "gate.rationalowl.com"
2. serviceId 
 - 단말앱이 등록하고자하는 모바일 서비스의 아이디
 - 관리자콘솔의 '서비스 > 서비스정보'에서 확인
 - 단말앱이 등록 후 해당 모바일 서비스에 등록한 모든 단말앱 및 앱서버와 실시간 메시지를 수/발신 할 수 있다.
3. deviceRegName 
 - 관리자콘솔에서 단말을 구분하기 위한 용도
 - 사용하지 않을 경우 null로 입력하면 된다.

실행시 registerDevice API의 2번째 서비스 아이디는 실제 래셔널아울 **관리자 콘솔로 등록한 서비스**의 서비스 아이디로 대체해서 실행시키면 단말이 등록되는 것을 관리자 콘솔의 '서비스 > 단말현황'에서 실시간으로 확인할 수 있다.


### 단말앱 등록 결과

registerDevice API 호출 결과 DeviceRegisterResultDelegate 의 onRegisterResult 콜백이 호출된다.

샘플 코드에서 onRegisterResult를 검색하면 RegViewController에서 해당 콜백을 구현함을 확인할 수 있다.

```swift
-(void) onRegisterResult: (int) resultCode resultMsg : (NSString*) resultMsg deviceRegId : (NSString*) deviceRegId {
    NSLog(@"onRegisterResult ");
    
    // device app registration success!
    // send deviceRegId to the app server.
    if(resultCode == RESULT_OK) {
        MinervaManager* mgr = [MinervaManager getInstance];
        [mgr sendUpstreamMsg:@"your device app info including deviceRegId" serverRegId:@"your app server reg id here"];
    }
} 
```

단말앱 등록이 성공되면 발급받은 단말 등록 아이디를 단말앱은 저장 및 관리해야 하고 해당 단말 등록 아이디를 단말앱을 관리 및 통신할 대상 앱서버에게 업스트림 API를 통해 전달해야 한다.
마찬가지로 앱서버는 전달받은 단말 등록 아이디를 저장 및 관리해야 한다.

단말앱 등록 결과 반환받는 값들은 다음과 같다.

 1. 단말 등록 아이디
    - 샘플코드에서 bundle.getString("deviceRegId")로 반환
    - 단말 앱을 구분하는 구분자
    - 단말앱 등록 성공이거나 기등록된 경우 전달받는다.
    - 단말앱 등록 성공일 경우 이를 앱 서버에게 upstream API를 통해 전달해야 한다.
 2. 결과 코드
    - 샘플코드에서 bundle.getInt("resultCode")로 반환
 3. 결과 메시지
    - 샘플코드에서 bundle.getString("resultMsg")로 반환


## 단말앱 등록해제

샘플 단말앱에서 단말앱 등록하기 위해서는 다음 절차를 따르면 된다.

1. 등록화면에서 '단말앱 등록해제'버튼 클릭


샘플코드에서 unregisterDevice 검색하면 아래의 샘플코드를 확인할 수 있다. 

```swift
- (IBAction) unregDevice {
    MinervaManager* mgr = [MinervaManager getInstance];
    [mgr unregisterDevice:@"faebcfe844d54d449136491fb253619d"];
}
```

MinervaManager.unregisterDevice() API의 각 인자별 의미는 다음과 같다.
1. serviceId 
 - 단말앱이 등록해제하고자하는 모바일 서비스의 아이디
 - 관리자콘솔의 '서비스 > 서비스정보'에서 확인가능

간단히 샘플코드에서 MinervaManager.unregisterDevice() API의 서비스 아이디를 실제 래셔널아울 관리자 콘솔로 등록한 서비스 아이디로 대체해서 실행시키면 단말이 등록해제되는 것을 관리자 콘솔의 '서비스 > 단말현황'에서 실시간 모니터링 할 수 있다.


### 단말앱 등록해제 결과

unregisterDevice API 호출 결과 DeviceRegisterResultDelegate 의 onUnregisterResult 콜백이 호출된다.

샘플 코드에서 onUnregisterResult 검색하면 RegViewController에서 해당 콜백을 구현함을 확인할 수 있다.

```swift
-(void) onUnregisterResult: (int) resultCode resultMsg : (NSString*) resultMsg {
    NSLog(@"onUnregisterResult ");
}
```

단말앱 등록해제 결과 단말앱 라이브러리는 단말앱에 다음의 값들을 알려준다.

 1. 단말 등록 아이디
    - 샘플코드에서 bundle.getString("deviceRegId")로 반환
    - 단말 앱을 구분하는 구분자
 2. 결과 코드
    - 샘플코드에서 bundle.getInt("resultCode")로 반환
 3. 결과 메시지
    - 샘플코드에서 bundle.getString("resultMsg")로 반환


## 업스트림 메시지 발신


샘플 단말앱에서 업스트림 메시지를 발신하기 위해서는 다음 절차를 따르면 된다.

1. 샘플앱 메시지 화면에서 'input data'에 발신할 메시지 입력
2. 'UPSTREAM'버튼 클릭


![이미지 이름](./img/msg_scrn.png)


샘플코드에서 sendUpstreamMsg 검색하면 아래의 샘플코드를 확인할 수 있다. 


```swift
#pragma mark -
#pragma mark IBActions
//send message

- (IBAction)sendUpstreamMsg:(id)sender {
    //NSString* svcId = @"d0a83353281e4b678774a0efa44fdd82";
    NSString* serverId = @"1fe45769e24348bfa501c32032958483";
    NSString* msg = inputMessageField.text;
    MinervaManager* mgr = [MinervaManager getInstance];
    [mgr sendUpstreamMsg:msg serverRegId:serverId];
    
    NSString* displayStr = [NSString stringWithFormat:@"=>%@  ", msg];
    //[self.messages insertObject:displayStr atIndex:0];
    [self.messages addObject:displayStr];
    [self.tView reloadData];
    NSIndexPath *topIndexPath = [NSIndexPath indexPathForRow:messages.count-1 inSection:0];
    [self.tView scrollToRowAtIndexPath:topIndexPath
                      atScrollPosition:UITableViewScrollPositionMiddle
                              animated:YES];
}

```

MinervaManager.sendUpstreamMsg() API의 각 인자별 의미는 다음과 같다.

1. msg
 - 전달할 데이터로 모바일 서비스 특성에 맞게 json포맷 또는 일반 스트링으로 포맷팅하면 된다.
2. serverRegId 
 - 데이터를 전달할 앱서버의 등록아이디
 - 관리자콘솔의 '서비스 > 서버현황'에서 확인 가능

샘플코드에서 MinervaManager.sendUpstreamMsg() API의 serverRegId를 실제 등록한 앱서버 등록 아이디로 대체해서 실행시키면 해당 앱서버로 실시간 메시지가 전달되는 것을 확인할 수 있다.
그리고 메시지 전달현황은 관리자 콘솔의 '서비스 > 메시지현황'에서 실시간 모니터링 할 수 있다.

### 업스트림 메시지 발신 결과

unregisterDevice API 호출 결과 MsgResultDelegate 의 onUpstreamMsgResult 콜백이 호출된다.

샘플 코드에서 onUpstreamMsgResult 검색하면 MsgViewController에서 해당 콜백을 구현함을 확인할 수 있다.

```swift
-(void) onUpstreamMsgResult: (int) resultCode resultMsg : (NSString*) resultMsg umi : (NSString*) umi {
    NSLog(@"onUpstreamMsgResult umi = %@", umi);
}
```

단말앱 라이브러리는 단말앱에 다음의 값들을 알려준다.

 1. upstream message id
    - 샘플코드에서 bundle.getString("umi")로 반환
    - minMgr.sendUpstreamMsg(msg, serverRegId)의 반환값과 동일하다.
    - 업스트림 발신 결과와 sendUpstreamMsg() API 호출원을 검증하는 용도
 2. 결과 코드
    - 샘플코드에서 bundle.getInt("resultCode")로 반환
 3. 결과 메시지
    - 샘플코드에서 bundle.getString("resultMsg")로 반환


## P2P 메시지 발신

샘플 단말앱에서 P2P 메시지를 발신하기 위해서는 다음 절차를 따르면 된다.

1. 샘플앱 메시지 화면에서 'input data'에 발신할 메시지 입력
2. 'P2P'버튼 클릭

샘플코드에서 sendP2PMsg 검색하면 아래의 샘플코드를 확인할 수 있다. 


````swift
- (IBAction)sendP2PMsg:(id)sender {
    NSString* msg = inputMessageField.text;
    NSMutableArray* devices = [[NSMutableArray alloc] init];
    [devices addObject:@"cf12c6b3c46e4e318b6e3c77b0590b9d"];
    MinervaManager* mgr = [MinervaManager getInstance];
    [mgr sendP2PMsg:msg devices:devices];
    
    NSString* displayStr = [NSString stringWithFormat:@"=>(p2p)%@  ", msg];
    //[self.messages insertObject:displayStr atIndex:0];
    [self.messages addObject:displayStr];
    [self.tView reloadData];
    NSIndexPath *topIndexPath = [NSIndexPath indexPathForRow:messages.count-1 inSection:0];
    [self.tView scrollToRowAtIndexPath:topIndexPath
                      atScrollPosition:UITableViewScrollPositionMiddle
                              animated:YES];
}

```
sendP2PMsg API의 각 인자별 의미는 다음과 같다.

1. msg
 - 전달할 데이터로 모바일 서비스 특성에 맞게 json포맷 또는 일반 스트링으로 포맷팅하면 된다.
2. destDevices 
 - 데이터를 전달할 대상 단말앱들의 단말등록아이디 목록
 - 최대 2000대까지 가능

샘플코드에서 sendP2PMsg API의 destDevices에 실제 등록한 한개 이상의 단말 등록 아이디로 대체해서 실행시키면 해당 단말앱으로 실시간 메시지가 전달되는 것을 확인할 수 있다.
그리고 메시지 전달현황은 관리자 콘솔의 '서비스 > 메시지현황'에서 실시간 모니터링 할 수 있다.

### P2P 메시지 발신 결과

sendP2PMsg API 호출 결과는  MinervaDelegate.h에 정의된 onP2PMsgResult 콜백이 호출된다. 샘플 코드에서 onP2PMsgResult 검색하면 MsgViewController에서 해당 콜백을 구현함을 확인할 수 있다.

```swift
-(void) onP2PMsgResult: (int) resultCode resultMsg : (NSString*) resultMsg pmi : (NSString*) pmi {
    NSLog(@"onP2PMsgResult pmi = %@", pmi);
}
```

단말앱 라이브러리는 단말앱에 다음의 값들을 알려준다.

 1. P2P message id
    - 샘플코드에서 bundle.getString("pmi")로 반환
    - minMgr.sendP2PMsg() API의 반환값과 동일하다.
    - P2P 메시지 발신 결과와 sendP2PMsg() API 호출원을 검증하는 용도
 2. 결과 코드
    - 샘플코드에서 bundle.getInt("resultCode")로 반환
 3. 결과 메시지
    - 샘플코드에서 bundle.getString("resultMsg")로 반환


## 메시지 수신
단말앱은 앱서버로부터의 다운스트림 메시지와 다른 단말앱으로부터의 P2P 메시지를 수신한다. 

### 다운스트림 메시지 수신

단말앱이 다운스트림 메시지를 수신시 MinervaDelegate.h에 정의된 onDownstreamMsgRecieved 콜백이 호출된다. 샘플 코드에서 onDownstreamMsgRecieved 검색하면 MsgViewController에서 해당 콜백을 구현함을 확인할 수 있다.

```swift
-(void) onDownstreamMsgRecieved: (int) msgSize msgList : (NSArray*) msgList alarmIdx : (int) alarmIdx {
    NSLog(@"onMsgRecieved msg size = %d", msgSize);
    NSDictionary* msg;
    
    NSString* serverRegId;
    long serverTime;
    NSString* msgData;
    
    NSDateFormatter* format = [[NSDateFormatter alloc] init];
    [format setDateFormat:@"yyyy/MM/dd HH:mm:ss"];
    
    for(int i = 0; i < msgSize; i++) {
        msg = msgList[i];
        // message sender(app server)
        serverRegId = msg[@"sender"];
        // message sent time
        serverTime = [msg[@"serverTime"] longValue];
        NSDate* date = [NSDate dateWithTimeIntervalSince1970:(serverTime /1000)];
        msgData = msg[@"data"];
        NSString* displayStr = [NSString stringWithFormat:@"%@  [sent time:%@]", msgData, [format stringFromDate:date]];
        //[self.messages insertObject:displayStr atIndex:0];
        [self.messages addObject:displayStr];
    }
    
    [self.tView reloadData];
    NSIndexPath *topIndexPath = [NSIndexPath indexPathForRow:messages.count-1 inSection:0];
    [self.tView scrollToRowAtIndexPath:topIndexPath
                      atScrollPosition:UITableViewScrollPositionMiddle
                              animated:YES];
}

```
앱서버에서 발신하는 멀티캐스트, 브로드캐스트, 그룹 메시지를 단말앱이 수신시 단말앱 라이브러리는 단말앱에게 다음의 값들을 알려준다.

1. 다운스트림 메시지 갯수    
  - 샘플코드에서 bundle.getInt(MinervaManager.FIELD_MSG_SIZE)로 반환
2. 다운스트림 메시지 목록    
   샘플코드에서 bundle.getString(MinervaManager.FIELD_MSG_LIST)로 반환     
   메시지 목록의 각 메시지는 다음의 값들을 포함한다.
  - 메시지 발신한 앱서버의 서버등록아이디
  - 메시지 데이터
  - 메시지 발신시간
  - 단말앱이 백그라운드시 표시할 알림 타이틀
  - 단말앱이 백그라운드시 표시할 알림 본문

샘플앱이 메시지 수신시 백그라운드일 경우에는 알림이 오고 샘플앱이 포그라운드일 경우 샘플앱의 메시지 면에서 메시지 수신됨을 확인 가능하다.

![이미지 이름](./img/sample_msg.png)



### P2P 메시지 수신

단말앱이 P2P 메시지를 수신시 MinervaDelegate.h에 정의된 onP2PMsgRecieved 콜백이 호출된다. 샘플 코드에서 onP2PMsgRecieved 검색하면 MsgViewController에서 해당 콜백을 구현함을 확인할 수 있다.

```swift
-(void) onP2PMsgRecieved: (int) msgSize msgList : (NSArray*) msgList alarmIdx : (int) alarmIdx {
    NSLog(@"onP2PMsgRecieved msg size = %d", msgSize);
    NSDictionary* msg;
    NSString* sender; //sending device
    long serverTime;
    NSString* msgData;
    NSString* msgId;
    NSDateFormatter* format = [[NSDateFormatter alloc] init];
    [format setDateFormat:@"yyyy/MM/dd HH:mm:ss"];
    
    for(int i = 0; i < msgSize; i++) {
        msg = msgList[i];
        msgId = msg[@"msgId"];
        sender = msg[@"sender"];
        serverTime = [msg[@"serverTime"] longValue];
        NSDate* date = [NSDate dateWithTimeIntervalSince1970:(serverTime /1000)];
        msgData = msg[@"data"];
        NSString* displayStr = [NSString stringWithFormat:@"p2p:%@  [sent time:%@]", msgData, [format stringFromDate:date]];
        //[self.messages insertObject:displayStr atIndex:0];
        [self.messages addObject:displayStr];
    }
    
    ...
}
```
모바일 서비스 내 다른 단말앱에서 발신한 P2P 메시지를 단말앱이 수신시 단말앱 라이브러리는 단말앱에게 다음의 값들을 알려준다.

1. P2P 메시지 갯수    
  - 샘플코드에서 bundle.getInt(MinervaManager.FIELD_MSG_SIZE)로 반환
2. P2P 메시지 목록    
   샘플코드에서 bundle.getString(MinervaManager.FIELD_MSG_LIST)로 반환    
   메시지 목록의 각 메시지는 다음의 값들을 포함한다.
  - 메시지 발신한 단말앱의 단말등록아이디
  - 메시지 데이터
  - 메시지 발신시간
  - 단말앱이 백그라운드시 표시할 알림 타이틀
  - 단말앱이 백그라운드시 표시할 알림 본문
