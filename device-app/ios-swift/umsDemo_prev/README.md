


## 개발 전 IOS 설정
[IOS 설정 가이드](https://github.com/RationalOwl/rationalowl-guide/tree/master/device-app/ios-apns-p8)를 통해 개발 전 아래 설정을 먼저 진행해야 한다.

- APNS 발신용 인증키(.p8) 발급
- 래셔널UMS 기반으로 앱 개발시 [래셔널UMS 웹관리자 가이드](https://github.com/RationalOwl/ums/tree/main/web_admin) 의 '푸시앱 인증서 설정' 파트를 참조하여 발급한 p8 인증키를 등록한다.


## 샘플 프로젝트 설정
1. github에서 샘플코드를 다운받는다.
2. 다운받은 폴더에서 'RationalOwlUMSDemo.xcworkspace'파일을 클릭하여 XCode를 실행한다.
 - 샘플앱을 XCode로 실행하면 실행가능한 상태로 설정들이 되어 있다.
 - 프로젝트 루트에 'RationalOwl.framework'파일이 래셔널아울 OS Objective-C 단말앱 라이브러리이다.
 - 'sample-Bridging-Header.h'파일이 Objective-C 단말앱 라이브러리를 Swift에서 사용할 수 있게 해 준다.
 - General > Identify > Bundle Identifier에 developer.apple.com에서 등록한 App ID와 동일한 값을 입력해야 한다.
 - 기타 앱 설정은 'sample1' 샘플 참조

## 주의사항
- Apns 인증키 발급시 애플개발자 계정에서 입력한 번들아이디와 xCode 샘플프로젝트에서 설정한 번들아이디가 동일해야 함 
- IOS 리치 노티피케이션 설정을 위해 Service Extension 설정해야 함.
- 메인앱과 Service Extension 간 데이터 공유를 위해 App Group 설정


## 래셔널아울 라이브러리 적용
- 샘플앱 소스를 안드로이드 스튜디오에서 프로젝트로 import한 후 MinervaManager로 검색하면 래셔널아울 API 사용하는 부분이 모두 검색된다. 
- registerDevice, unregisterDevice, setDeviceRegisterResultDelegate, enableNotificationTracking, setMessageDelegate, setDeviceToken, setAppGroup, enterBackground, becomeActive, receivedApns 의 총 10개 API가 검색되는데 아래 설명을 기준으로 적용하면 된다.
- 샘플소스를 참고하여 카피후 필요에 따라 추가/수정한다.


## 래셔널아울 라이브러리 초기화

 앱 초기화 루틴을 AppDelegate didFinishLaunchingWithOptions 콜백 내에서 MinervaManager.setAppGroup(), MinervaManager.setMessageDelegate() API를 호출한다.


```java
let manager = MinervaManager.getInstance()
manager.setAppGroup(MessageSyncService.appGroupId)
manager.setMessageDelegate(RoMessageDelegate())

```

## 앱 라이프사이클 설정
- AppDelegate applicationWillResignActive 콜백 내에서 MinervaManager.enterBackground() API를 호출한다.
- AppDelegate applicationDidBecomeActive 콜백 내에서 MinervaManager.becomeActive() API를 호출한다.
- SceneDelegate를 이용할 경우 SceneDelegate의 sceneDidBecomeActive 콜백 내에서 MinervaManager.becomeActive() API를 호출한다.
- SceneDelegate를 이용할 경우 SceneDelegate의 sceneWillResignActive 콜백 내에서 MinervaManager.enterBackground() API를 호출한다.
- 자세한 사용법은 샘플앱 소스 검색을 통해 참고한다. 



## 단말앱 등록 & 등록해제

MinervaManager.registerDevice API는 단말앱 등록 API로 해당 API가 호출해야 앱에서 래셔널아울 푸시알림 수신 및 실시간 데이터 수/발신 API를 이용할 있다. registerDevice API 실행결과는 onRegisterResult() 콜백함수를 통해 알수 있다. 해당 API와 콜백은 앱 사용자 등록 루틴에 추가하면 된다. 

반대로 unregisterDevice API는 단말앱 등록해제 API이다. 해당 API 결과는 onUnregisterResult 콜백함수를 통해 알수 있다. 해당 API와 콜백은 앱 사용자 회원 탈퇴 루틴에 추가하면 된다. 

콜백처리루틴은 MinervaManager.setDeviceRegisterResultDelegate() API를 통해 지정하면 된다.

자세한 사용법은 샘플앱 소스 검색을 통해 참고한다. 

## 최신 APNS 토큰 설정

- AppDelegate didRegisterForRemoteNotificationsWithDeviceToken 콜백 내에서 MinervaManager.setDeviceToken() API를 호출한다.

- 자세한 사용법은 샘플앱 소스 검색을 통해 참고한다.

## 푸시 알림 표시

- 래셔널아울 푸시알림은 기본 리치 노티피케이션이다. 따라서 Service Extension의 didReceive 콜백을 통해 푸시 알림 수신을 처리한다. 
- AppDelegate의 didReceiveRemoteNotification 콜백에서도 처리한다. 
- AppDelegate의 didReceive 콜백에서 receivedApns() API를 호출한다.
- 샘플 코드의 기본틀을 그대로 카피하여 사용하되 자신의 앱이 원하는 형태의 알림은 // Modify the notification content here... 주석 부분에서 원하는 형태로 표현하는 것을 권고한다.

1. Service Extension의 didReceive 콜백


```java
override func didReceive(_ request: UNNotificationRequest,
                             withContentHandler contentHandler: @escaping (UNNotificationContent) -> Void)
{
    self.contentHandler = contentHandler
    bestAttemptContent = (request.content.mutableCopy() as? UNMutableNotificationContent)
    
    if let bestAttemptContent = bestAttemptContent
    {
        // Called to let your app know which action was selected by the user for a given notification.
        var userInfo = bestAttemptContent.userInfo
        debugPrint(userInfo)
        
        // enable notification delivery tracking
        let manager = MinervaManager.getInstance()!
        manager.enableNotificationTracking(userInfo, appGroup: NotificationService.appGroupId)
        
        // system push is sent by RationalOwl for device app lifecycle check.
        // system push is also silent push.
        // if system push has received, just return.
        if userInfo["SystemPush"] != nil
        {
            print("system push received!!")
            return
        }
        
        userInfo["received-at"] = Date().timeIntervalSince1970
        userInfo["show-notification"] = userDefaults.bool(forKey: "isActive")
        
        if let userInfoJson = try? JSONSerialization.data(withJSONObject: userInfo)
        {
            var messages = userDefaults.array(forKey: NotificationService.newMessagesKey) as? [Data] ?? []
            messages.append(userInfoJson)
            userDefaults.set(messages, forKey: NotificationService.newMessagesKey)
            
            CFNotificationCenterPostNotification(CFNotificationCenterGetDarwinNotifyCenter(),
                                                  CFNotificationName(NotificationService.newMessagesId as CFString), nil, nil, true)
        }
        
        // Modify the notification content here...
        if userInfo["title"] != nil
        {
            bestAttemptContent.title = userInfo["title"] as! String
        }
        
        if userInfo["body"] != nil
        {
            bestAttemptContent.body = userInfo["body"] as! String
        }
        
        contentHandler(bestAttemptContent)
    }
}
```

2. AppDelegate의 didReceiveRemoteNotification 콜백


```java
func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable: Any])
{
    // system push is sent by RationalOwl for device app lifecycle check.
    // system push is also silent push.
    // if system push has received, just return.
    if userInfo["SystemPush"] != nil
    {
        print("system push received!!")
        return;
    }
    
    if let aps = userInfo["aps"] as? NSDictionary
    {
        // silent push recieved
        if aps["content-available"] != nil
        {
            // enable notification delivery tracking
            manager.enableNotificationTracking(userInfo, appGroup: MessageSyncService.appGroupId)
        }
    }
}

```


3. AppDelegate의 didReceiveRemoteNotification 콜백


```java
func userNotificationCenter(_ center: UNUserNotificationCenter,
                                didReceive response: UNNotificationResponse) async
{
    NSLog("[userNotificationCenter] didReceive")
    
    let userInfo = response.notification.request.content.userInfo
    let messageId = response.notification.request.identifier
    
    if userInfo["SystemPush"] != nil
    {
        print("system push received!!")
        return
    }
    
    manager.receivedApns(userInfo)
    
    var message = repository.getMessage(messageId)
    
    if message == nil
    {
        message = Message(userInfo)
        repository.addMessage(message!)
    }
    
    let storyboard = UIStoryboard(name: "Message", bundle: nil)
    let viewController = storyboard.instantiateInitialViewController() as! MessageNavigationController
    viewController.message = message
    
    MessageReadReceiptService.shared.markAsRead(message!.id)
    
    UIApplication.shared.windows.first?.rootViewController = viewController
    UIApplication.shared.windows.first?.makeKeyAndVisible()
}

```

## 메시지 리스너 등록

MinervaManager.setMessageDelegate() API를 통해 메시지 콜백 루틴을 지정한다. 해당 콜백은 P2P, Upstream, downstream 과 같은 실시간 메시지에 대한 콜백도 포함되지만 쉬운 설정을 위해 본 문서에서는 푸시메시지 콜백부분만 설명한다. 

MessageDelegate onMsgReceived 콜백은 앱 실행시 호출한다. 정상적인 경우는 onMsgReceived내에 하나의 메시지가 전달되지만 미전달된 푸시알림이 여러개 존재할 경우 미전달된 푸시알림 목록을 전달한다. 

자세한 사용법은 소스코드 검색을 통해 확인한다.


## 통합메시지솔루션 연동
래셔널UMS 솔루션은 래셔널아울 솔루션 기반으로 개발된 통합메시지 솔루션이다. 단말앱과 래셔널UMS과 통합은 REST API 호출로 이루어진다.


## 단말앱 등록

- 단말앱 회원 가입 루틴에서 호출한다.
- 래셔널아울 등록 API 호출 결과 콜백 onRegisterResult() 내에서 결과값이 성공일 경우 호출하는 것이 효율적이다.
- 샘플코드소스에서 'pushApp/installApp' 로 검색하여 사용 예시 참조


### REST API

 - Method: post
 - url: https://umsDomain:36001/pushApp/installApp
 - post parameter
 
```java
{
    "cId":303,                      // command id
    "sId":"myRationalOwlServiceId", // service id
    "aId":"myAccountId",            // account id
    "dt": 1,                        // device type 1: android, 2: ios
    "dRId":"myDeviceRegId",         // device registration id
    "pn":"01022221111",             // phone number    
    "auId":"app user id"            // app user id    
    "n":"홍길동"                     // name (app user name)
}
```
 - post parameter 설명
    - cId : (command id) 303
    - sId : (service id) 래셔널아울 서비스 아이디
    - aId : (account id) 래셔널UMS 계정 아이디
    - dt : (device type) 1: android, 2: ios
    - dRId : (device registration id) 래셔널아울 단말등록 아이디, onRegisterResult 콜백내에서 전달받음.
    - pn : (phone number) 앱이 설치된 폰의 전화번호
    - auId: (app user id) 앱 설치시 입력한 앱 사용자 아이디(optional)
    - n: (app user name) 앱 설치시 입력한 앱 사용자 이름

- response
 
```java
{
    "cId":303,                      // command id
    "aId":"myAccountId",            // account id
    "rc":1,  
    "cmt":"정상처리"
}
```
 - response parameter 설명
    - rc : (result code) 결과값
        - 1: 성공, -102: 기등록된 상태에서 api 호출시
        - 등록 성공시 웹 관리자 화면에서 모니터링 지원 시작
    - cmt : (comment) api 호출결과 부연 설명


## 푸시알림 수신확인 전달

- 사용자가 푸시알림 수신확인시 이를 통합메시지 솔루션에 전달한다.
    - 사용자가 앱을 실행후 메시지 목록에서 특정 메시지를 클릭후 확인시
    - 사용자가 푸시알림 수신 후 푸시알림 아이콘 클릭 후 메시지 확인시
- 해당 API를 호출해야 수신확인 전달 정보가 웹 관리자를 통한 실시간 모니터링과 실시간 트래킹 콜백에 반영된다.


### REST API

 - Method: post
 - url: https://umsDomain:36001/pushApp/notiRead
 - post parameter
 
```java
{
    "cId":311,                      // command id
    "r":1,                          // service id
    "aId":"myAccountId",            // device type 1: android, 2: ios
    "dRId":"myDeviceRegId",         // device registration id
    "pn":"01022221111",             // phone number    
    "mId":"messageId"               // message id    
}
```
 - post parameter 설명
    - cId : (command id) 
    - r : (request) 1 로 고정
    - aId : (account id) 래셔널UMS 계정 아이디    
    - dRId : (device registration id) 래셔널아울 단말등록 아이디, onRegisterResult 콜백내에서 전달받음.
    - pn : (phone number) 앱이 설치된 폰의 전화번호
    - mId: (message id) 푸시알림 수신 콜백에 전달된 메시지 아이디        
        - 메시지 구분자
- response
 
```java
ok
```
 - response parameter 설명
    - json포맷이 아닌 단순 텍스트 ok가 반환된다.



## 대체 메시지 전달정보 확인

- 푸시알림 대체 메시지 전환 발신시 대체 메시지(알림톡, SMS/LMS/MMS) 전달 정보를 확인한다.
- 대체 메시지 미 사용시 본 API 호출 불필요


### REST API

 - Method: post
 - url: https://umsDomain:36001/pushApp/msgInfo
 - post parameter
 
```java
{
    "cId":312,                      // command id
    "aId":"myAccountId",            // account id
    "pn":"01022221111",             // phone number    
    "dRId":"myDeviceRegId",         // device registration id
    "mId":"messageId"               // message id
}
```
 - post parameter 설명
    - cId : (command id) 
    - aId : (account id) 래셔널UMS 계정 아이디
    - dRId : (device registration id) 래셔널아울 단말등록 아이디, onRegisterResult 콜백내에서 전달받음.
    - pn : (phone number) 앱이 설치된 폰의 전화번호
    - mId: (message) 메시지 전달 현황을 알고자하는 메시지의 아이디

- response
 
```java
{
    "cId":303,                      // command id
    "aId":"myAccountId",            // account id
    "rc":1,  
    "cmt":"정상처리",
    "mId":"messageId",
    "ast":1123232224,
    "as":2,
    "mst":212122122,
    "mt":2,
    "ms":12
}
```
 - response parameter 설명
    - rc : (result code) 결과값
    - cmt : (comment) api 호출결과 부연 설명
    - mId : (message id)
    - ast : (alimtalk send time) 알림톡 발신 시간
        - 1970년 1월1일 0시 기준 밀리초 단위 시간 차
    - as : (alimtalk state) 알림톡 전달 상태
        - 0 : 미발신
        - 1 : 발신요청
        - 2 : 전달성공
        - 3 : 전달실패
    - mst : (munja send time) 문자 발신 시간
        - 1970년 1월1일 0시 기준 밀리초 단위 시간 차
    - ms : (munja state) 문자 전달 상태
        - 0 : 미발신
        - 1 : 발신요청
        - 2 : 전달성공
        - 3 : 전달실패
    - mt : (munja type) 문자종류
        - 12 : sms
        - 13 : lms
        - 14 : mms

## 첨부 이미지 데이터 확인

- 이미지 첨부 발신시 첨부한 이미지 데이터를 확인한다.
- base64 인코딩된 이미지 데이터를 확인한다.


### REST API

 - Method: post
 - url: https://umsDomain:36001/pushApp/imgData
 - post parameter
 
```java
{
    "cId":313,                      // command id
    "aId":"myAccountId",            // account id
    "iId":"adsfdasf",               // image id    
    "mId":"messageId"               // message id
}
```
 - post parameter 설명
    - cId : (command id) 
    - aId : (account id) 래셔널UMS 계정 아이디    
    - pn : (phone number) 앱이 설치된 폰의 전화번호
    - mId: (message) 이미지가 첨부된 메시지의 아이디
        - 메시지 아이디는 메시지 수신 콜백으로 전달된다.
    - iId: (image id) 확인하고자 하는 이미지의 아이디
        - 이미지 아이디는 메시지 수신 콜백으로 전달된다.

- response
 
```java
{
    "cId":313,                      // command id
    "aId":"myAccountId",            // account id
    "rc":1,  
    "cmt":"정상처리",
    "imgD":"imageData"
}
```
 - response parameter 설명    
    - imgD : (image data)
        - 확인하고자하는 이미지 데이터
        - base64 인코딩된 포맷


## 푸시 알림 관리

푸시알림은 FirebaseMessagingService onMessageReceived 콜백이나 MessageListener onMsgReceived 콜백을 통해 전달된다. 특히 MessageListener onMsgReceived 콜백에서 미전달 푸시알림까지 전달하기 때문에 불필요하게 앱서버에서 메시지 목록을 가져오는 동작이 앱서버의 과부하를 야기하는 경우가 왕왕 발생하기 때문이다. 그리고 두 콜백에서 메시지 내용뿐 아니라 발신시간, 메시지아이디, 이미지 첨부시 이미지 아이디 정보를 모두 포함하기 때문에 단말앱에서는 푸시알림 발신시간을 기준으로 푸시알림 정보를 폰내 파일에 앱 성격에 따라 최대 한달 혹은 1년 단위로 저장하여 관리하기를 권장한다.