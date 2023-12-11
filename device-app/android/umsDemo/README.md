# 통합메시지 Android 단말앱 샘플
래셔널아울 솔루션을 기반으로 개발한 통합메시지 솔루션 안드로이드 데모앱이다.

## 샘플 프로젝트 설정
1. github에서 샘플코드를 다운받는다.
2. 안드로이드 스튜디오를 실행한다.
3. 'File > New > Import Project..'로 샘플앱 프로젝트를 오픈한다.

## 단말앱 라이브러리 적용 확인

래셔널아울 안드로이드 단말앱 라이브러리 적용을 확인한다.

1. 래셔널아울 라이브러리를 적용한다.
    - 아래 build.gradle의 dependencies에서 // RationalOwl library 부분
2. 래셔널아울 이용 라이브러리를 적용한다.
    - 아래 build.gradle의 dependencies에서 // RationalOwl using library 부분   
3. FCM 라이브러리를 적용한다.
    - 가장 최신 라이브러리는 샘플코드의 lib 폴더에 적용된다.
    - 가장 최신 라이브러리 적용법은 샘플코드 앱 레벨 build.gradle을 참조하기 바란다.    


아래는 샘플앱 앱레벨 build.gradle의 dependencies 부분이다. 래셔널아울 라이브러리와 래셔널아울 라이브러리에서 사용하는 필수 라이브러리(life cycle lib, jackson json lib)을 추가한다. 자세한 사항은 소스 내 build.gradle 파일을 참조한다.


```java

dependencies {
    // RationalOwl library and dependencies
    implementation files('libs/rationalowl-android-1.3.6.aar')

    // life cycle lib
    implementation "androidx.lifecycle:lifecycle-service:2.6.2"
    implementation "androidx.lifecycle:lifecycle-process:2.6.2"

    // jackson json lib
    implementation group: 'com.fasterxml.jackson.core', name: 'jackson-core', version: '2.15.2'
    implementation group: 'com.fasterxml.jackson.core', name: 'jackson-annotations', version: '2.15.2'
    implementation group: 'com.fasterxml.jackson.core', name: 'jackson-databind', version: '2.15.2'

    ...
}
```


## FCM(Firebase Cloud Messaging) 적용



[FCM 설정 가이드](https://github.com/RationalOwl/rationalowl-guide/tree/master/device-app/fcm-setting)를 참조하여 아래 FCM 관련 설정을 처리한다.
1) 파이어베이스 콘솔에서 안드로이드 앱을 위한 프로젝트를 생성한다.
2) google-service.json 파일을 다운로드하여 안드로이드 스튜디오에 적용한다. 
3) 서버키를 카피한다.
4) 래셔널UMS 기반으로 앱 개발시 [래셔널UMS 웹관리자 가이드](https://github.com/RationalOwl/ums/tree/main/web_admin) 의 '푸시앱 인증서 설정' 파트를 참조하여 안드로이드 서버키를 등록한다.



## 래셔널아울 라이브러리 적용
샘플앱 소스를 안드로이드 스튜디오에서 프로젝트로 import한 후 MinervaManager로 검색하면 래셔널아울 API 사용하는 부분이 모두 검색된다. init, setMsgListener, registerDevice, unregisterDevice, setRegisterResultListener, setMsgListener, setDeviceToken, enableNotificationTracking 의 총 8개 API가 검색되는데 아래 설명을 기준으로 적용하면 된다.


## 래셔널아울 라이브러리 초기화

 MinervaManager.init API가 앱 초기화 부분에서 확인할 수 있다. 샘플앱을 참고하여 초기화 함수 init() API와 메시지 콜백 설정함수인 setMsgListener() API를 앱 초기화 루틴에 추가한다.


```java
// Firebase init api
FirebaseApp.initializeApp(context);

final Context context = getApplicationContext();
MinervaManager.init(context);

// set rationalowl msg listener
final MinervaManager mgr = MinervaManager.getInstance();
mgr.setMsgListener(new RoMessageListener());

```

## 단말앱 등록 & 등록해제

MinervaManager.registerDevice API는 단말앱 등록 API로 해당 API가 호출해야 앱에서 래셔널아울 푸시알림 수신 및 실시간 데이터 수/발신 API를 이용할 있다. registerDevice API 실행결과는 onRegisterResult() 콜백함수를 통해 알수 있다. 해당 API와 콜백은 앱 사용자 등록 루틴에 추가하면 된다. 

반대로 unregisterDevice API는 단말앱 등록해제 API이다. 해당 API 결과는 onUnregisterResult 콜백함수를 통해 알수 있다. 해당 API와 콜백은 앱 사용자 회원 탈퇴 루틴에 추가하면 된다. 

콜백처리루틴은 MinervaManager.setRegisterResultListener() API를 통해 지정하면 된다.

자세한 사용법은 샘플앱 소스 검색을 통해 참고한다. 



## 최신 FCM 토큰 설정

MinervaManager.setDeviceToken() API를 통해 최신 FCM 토큰을 업데이트한다. 아래 두 군데에서 해당 API를 호출해야 한다.

1. FirebaseMessagingService에서 토큰 갱신시 호출되는 콜백 함수 내
  - FirebaseMessagingService onNewToken()콜백 내에서

2. 단말앱 등록 루틴인 MinervaManager.registerDevice API 호출부에서 호출

자세한 사용법은 샘플앱 소스 검색을 통해 참고한다.

## 푸시 알림 표시

푸시알림이 폰에 전달시 FirebaseMessagingService onMessageReceived 콜백이 호출된다. 해당 콜백내 아래 코드를 카피하여 그대로 복사한다. 그리고 알림 표시방법은 앱 성격에 따라 handleMessage(data) 내에서 구현하면 된다. 
handleMessage(data)의 data에는 푸시알림 메시지 뿐 아니라 발신시간, 메시지아이디, 이미지 첨부시 이미지 아이디 정보도 포함된다. 자세한 사용법은 샘플앱 소스 검색을 통해 참고한다.


```java
public class NotificationService extends FirebaseMessagingService {

    ...

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Logger.debug(TAG, "onMessageReceived enter");
        final Map<String, String> data = remoteMessage.getData();

        // set notification  delivery tracking
        manager.enableNotificationTracking(data);

        // silent push received.
        if (data.containsKey("silent")) {
            // system push is sent by RationalOwl for device app lifecycle check.
            // system push is also silent push.
            // if system push has received, just return.
            if (data.containsKey("SystemPush")) {
                Logger.debug(TAG, "System push received!");
                return;
            }
            // normal silent push which are sent by your app server.
            // do your logic
            else {
                Logger.debug(TAG, "your app server sent silent push");
                // do your logic
            }
        }
        // it is normal custom push not silent push.
        // do your logic here
        else {
            // make your custom notification UI
            handleMessage(data);
        }
    }
}
```




## 메시지 리스너 등록

MinervaManager.setMsgListener() API를 통해 메시지 콜백 루틴을 지정한다. 해당 콜백은 P2P, Upstream, downstream 과 같은 실시간 메시지에 대한 콜백도 포함되지만 쉬운 설정을 위해 본 문서에서는 푸시메시지 콜백부분만 설명한다. 

FirebaseMessagingService onMessageReceived 콜백은 안드로이드 폰 자체에서 푸시 알림 수신시 호출하는데 반해 아래 MessageListener onMsgReceived 콜백은 앱 실행시 호출한다. 정상적인 경우는 onMsgReceived내에 하나의 메시지가 전달되지만 미전달된 푸시알림이 여러개 존재할 경우 미전달된 푸시알림 목록을 전달한다. 



```java
public class RoMessageListener implements MessageListener {
    ...

    @Override
    public void onMsgReceived(ArrayList<JSONObject> objects) {
        // receive un-delivered push msg in the queuing period.
        Log.d(TAG, "onMsgReceived enter");

        final List<Message> messages = new ArrayList<>();

        // recent messages are ordered by message send time descending order 
        // [recentest, recent, old, older, oldest...]
        // treat old message first.
        for (JSONObject json : objects) {
            // this app just use custom push and user data only.
            try {
                // 1 (realtime: downstream), 2 (realtime: p2p), 3(custom push)
                final int type = (int) json.get(MinervaManager.FIELD_MSG_TYPE);  
                final String data = (String) json.get(MinervaManager.FIELD_MSG_DATA);

                if (type == MESSAGE_TYPE_CUSTOM) {
                    final Map<String, String> map = mapper.readValue(data, mapType);

                    final Message message = new Message(map);
                    if (repository.hasMessage(message.getId())) continue;

                    MessagesRepository.getInstance().addMessage(message);
                    messages.add(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // only the latest msg notification alert.
        if (!messages.isEmpty()) {
            messages.sort((m1, m2) -> m2.getSentAt().compareTo(m1.getSentAt()));
            NotificationService.showNotification(messages.get(0));
        }
    }
}
```


## 푸시 알림 관리

푸시알림은 FirebaseMessagingService onMessageReceived 콜백이나 MessageListener onMsgReceived 콜백을 통해 전달된다. 특히 MessageListener onMsgReceived 콜백에서 미전달 푸시알림까지 전달하기 때문에 불필요하게 앱서버에서 메시지 목록을 가져오는 동작이 앱서버의 과부하를 야기하는 경우가 왕왕 발생하기 때문이다. 그리고 두 콜백에서 메시지 내용뿐 아니라 발신시간, 메시지아이디, 이미지 첨부시 이미지 아이디 정보를 모두 포함하기 때문에 단말앱에서는 푸시알림 발신시간을 기준으로 푸시알림 정보를 폰내 파일에 앱 성격에 따라 최대 한달 혹은 1년 단위로 저장하여 관리하기를 권장한다.