# 래셔널아울 Android 단말앱 샘플


## 속성 연동

속성으로 래셔널아울 솔루션 연동을 빨리 적용하기 위한 팁을 제공한다. 

1.  API 호출부 검색
래셔널아울 API 호출은 MinervaManager 클래스에서 호출한다. 따라서 샘플앱을 다운로드 후 개발 Editor에서 MinervaManager.getInstance()을 검색하면 래셔널아울 API호출한 부분을 모두 검색할 수 있다. 안드로이드, IOS 모두 API 호출이 비슷하지만 OS특성상 상이한 부분도 있으므로 각 OS 환경에서 주의가 필요하다.
다음은 안드로이드 환경에서 MinervaManager.getInstance() 검색결과이다. 아래의 7개의 API가 제대로 적용되어 호출되고 있는 지 확인인
- init(), setDeviceToken(), setRegisterResultListener(), registerDevice(), unregisterDevice()
- setMsgListener(), enableNotificationTracking()


2.  콜백함수 확인
래셔널아울 콜백함수는 DeviceRegisterResultListener(), MessageListener() 2개의 인터페이스에서 정의한다.

- DeviceRegisterResultListener에서 정의된 콜백들이 정상적으로 호출되는지 확인
    - onRegisterResult: 단말앱 등록 결과 콜백
    - onUnregisterResult: 단말앱 등록해제 결과 콜백
- MessageListener에서 정의된 콜백들이 정상적으로 호출되고 구현했는지 확인
    - onPushMsgRecieved: 래셔널아울 푸시알림 이용시
    - onP2PMsgRecieved, onPushMsgRecieved, onSendUpstreamMsgResult, onSendP2PMsgResult: 래셔널아울 실시간 데이터 이용시 


## 정석 연동

[안드로이드 HelloWorld앱 가이드](https://github.com/RationalOwl/rationalowl-sample/tree/master/device-app/android/Java/helloWorld)를 참고


