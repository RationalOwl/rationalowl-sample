# 래셔널아울 Android 단말앱 샘플
Android 단말앱 샘플은 Android 단말앱 라이브러리에서 제공하는 API를 이용해서 단말앱을 만드는 것을 쉽게 따라할 수 있도록 쉽게 작성되었다. 실시간 데이터 발신 API, 실시간 및 푸시알림 수신 콜백에 대해 간단한 사용법을 참조한다.

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


아래는 2023년 12월 기준 샘플앱 앱레벨 build.gradle의 dependencies 부분이다.
최신 사용법은 샘플앱 build.gradle을 참고하면 된다.


```java

dependencies {
    implementation fileTree(include: ['*.jar'], dir: 'libs')
    // RationalOwl library
    implementation files('libs/rationalowl-android-1.4.1.aar')
    // RationalOwl using library
    implementation "androidx.lifecycle:lifecycle-service:2.5.1"
    implementation "androidx.lifecycle:lifecycle-process:2.5.1"

    implementation 'com.fasterxml.jackson.core:jackson-core:2.11.3'
    implementation 'com.fasterxml.jackson.core:jackson-annotations:2.11.3'
    implementation 'com.fasterxml.jackson.core:jackson-databind:2.11.3'

    // FCM library
    implementation 'com.google.firebase:firebase-core:21.1.1'
    implementation 'com.google.firebase:firebase-messaging:23.2.1'
    implementation platform('com.google.firebase:firebase-bom:32.2.3')
    implementation 'com.google.firebase:firebase-messaging-directboot:23.2.1'

    ...
}
```


## FCM(Firebase Cloud Messaging) 적용



[FCM 설정 가이드](https://github.com/RationalOwl/rationalowl-guide/tree/master/device-app/fcm-setting)를 참조하여 아래 FCM 관련 설정을 처리한다.
1) 파이어베이스 콘솔에서 안드로이드 앱을 위한 프로젝트를 생성한다.
2) google-service.json 파일을 다운로드하여 안드로이드 스튜디오에 적용한다. 
3) Firebase Cloud Messaging API(V1) 키관리를 통해 키를 생성한다.
4) 프로젝트 ID 와 '3) 과정'에서 생성한 인증파일(.json)을 래셔널아울울 웹 관리자 화면의 '안드로이드 FCM인증 설정'에서 등록한다.



## 래셔널아울 라이브러리 적용
샘플앱 소스를 안드로이드 스튜디오에서 프로젝트로 import한 후 MinervaManager로 검색하면 래셔널아울 API 사용하는 부분이 모두 검색된다. init, setMsgListener, registerDevice, unregisterDevice, setRegisterResultListener, setMsgListener, setDeviceToken, enableNotificationTracking 의 총 8개 API가 검색되는데 아래 설명을 기준으로 적용하면 된다.


## 래셔널아울 라이브러리 초기화

 MinervaManager.init API가 앱 초기화 부분에서 확인할 수 있다. 샘플앱을 참고하여 초기화 함수 init() API와 메시지 콜백 설정함수인 setMsgListener() API를 앱 초기화 루틴에 추가한다.
 자세한 사용법은 샘플앱 소스 검색을 통해 참고한다. 


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



## 메시지 리스너 등록
MinervaManager.setMsgListener() API를 통해 메시지 콜백 루틴을 지정한다. 해당 콜백은 P2P, Upstream, downstream 과 같은 실시간 메시지에 대한 콜백도 포함되지만 쉬운 설정을 위해 본 문서에서는 푸시메시지 콜백부분만 설명한다. 

FirebaseMessagingService onMessageReceived 콜백은 안드로이드 폰 자체에서 푸시 알림 수신시 호출하는데 반해 아래 MessageListener onMsgReceived 콜백은 앱 실행시 호출한다. 정상적인 경우는 onMsgReceived내에 하나의 메시지가 전달되지만 미전달된 푸시알림이 여러개 존재할 경우 미전달된 푸시알림 목록을 전달한다. 


## API 레퍼런스

솔루션 연동 관련 API는 MinervaManager, DeviceRegisterResultListener, MessageListener 세 인터페이스를 이용하는데 레퍼런스는 아래 링크를 참조한다.

[바로가기](https://guide.rationalowl.com/api-docs/device-app/android/)



## 속성 연동

속성으로 래셔널아울 솔루션 연동을 빨리 적용하기 위한 팁을 제공한다. 

1.  API 호출부 검색
래셔널아울 API 호출은 MinervaManager 클래스에서 호출한다. 따라서 샘플앱을 다운로드 후 개발 Editor에서 MinervaManager.getInstance()을 검색하면 래셔널아울 API호출한 부분을 모두 검색할 수 있다. 안드로이드, IOS 모두 API 호출이 비슷하지만 OS특성상 상이한 부분도 있으므로 각 OS 환경에서 주의가 필요하다.
다음은 안드로이드 환경에서 MinervaManager.getInstance() 검색결과이다. 아래의 7개의 API가 제대로 적용되어 호출되고 있는 지 확인
- init(), setDeviceToken(), setRegisterResultListener(), registerDevice(), unregisterDevice()
- setMsgListener(), enableNotificationTracking()


2.  콜백함수 확인
래셔널아울 콜백함수는 DeviceRegisterResultListener(), MessageListener() 2개의 인터페이스에서 정의한다.

- DeviceRegisterResultListener에서 정의된 콜백들이 정상적으로 호출되는지 확인
    - onRegisterResult: 단말앱 등록 결과 콜백
    - onUnregisterResult: 단말앱 등록해제 결과 콜백
- MessageListener에서 정의된 콜백들이 정상적으로 호출되고 구현했는지 확인
    - onPushMsgRecieved: 1. 앱실행 중 푸시알림 수신시 콜백 호출, 2. 앱실행시 미전달 푸시알림 목록 전달 콜백 호출
    - onP2PMsgRecieved, onDownstreamMsgRecieved, onSendUpstreamMsgResult, onSendP2PMsgResult: 래셔널아울 실시간 데이터 이용시 

