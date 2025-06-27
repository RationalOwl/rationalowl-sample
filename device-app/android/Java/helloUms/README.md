# 래셔널아울 Android 단말앱 샘플


## 속성 연동

속성으로 래셔널아울 솔루션 연동을 빨리 적용하기 위한 팁을 제공한다.
래셔널UMS는 래셔널아울 API 솔루션 기반으로 개발되어 래셔널아울 기본 연동에 더해 래셔널UMS REST API부분을 추가 연동하면 된다. 아래 1, 2 항목은 기본 래셔널아울 API솔루션 속성 설명이고 3 항목은 UMS 확장 연동 부분이다.

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
    - onPushMsgRecieved: 래셔널아울 푸시알림 이용시
    - onP2PMsgRecieved, onDownstreamMsgRecieved, onSendUpstreamMsgResult, onSendP2PMsgResult: 래셔널아울 실시간 데이터 이용시 

3. 래셔널UMS 연동
- 본 소스는 [안드로이드 HelloWorld 샘플앱](https://github.com/RationalOwl/rationalowl-sample/tree/master/device-app/android/Java/helloWorld) 개발환경에 UMS 연동 부분만 추가한 환경이어서 두 소스 구조를 다운로드 후 소스비교툴로 비교해 보면 UMS 추가할 부분을 소스 차원에서 확인 할 수 있어 추가 작업 부분을 가장 쉽게 알 수 있다.
- 래셔널UMS 단말앱 연동은 REST API 호출을 통해 연동하고 연동을 위해서는 2개의 API호출은 필수이고 [래셔널UMS 단말앱 REST API](https://github.com/RationalOwl/ums/tree/main/개발연동/UMS단말앱RESTAPI) 에서 관련 REST API를 참고한다.
- 샘플앱의 UmsApi클래스에서 UMS 연동을 쉽게 할 수 있도록 연동 API를 제공하며 해당 사용법을 보여주고 있다. 해당 사용법을 실제 앱에서 사용하기를 권고한다.



## 정석 연동

- 래셔널아울 API 솔루션 연동 정석은 [안드로이드 HelloWorld앱 가이드](https://github.com/RationalOwl/rationalowl-sample/tree/master/device-app/android/Java/helloWorld)를 참고한다.
