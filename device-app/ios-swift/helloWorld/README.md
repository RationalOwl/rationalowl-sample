


## 개발 전 IOS 설정
[IOS 설정 가이드](https://github.com/RationalOwl/rationalowl-guide/tree/master/device-app/ios-apns-p8)를 통해 개발 전 아래 설정을 먼저 진행해야 한다.

- APNS 발신용 인증키(.p8) 발급
- 래셔널UMS 기반으로 앱 개발시 [래셔널UMS 웹관리자 가이드](https://github.com/RationalOwl/ums/tree/main/web_admin) 의 '푸시앱 인증서 설정' 파트를 참조하여 발급한 p8 인증키를 등록한다.


## 샘플 프로젝트 개요
 -  github에서 샘플코드를 다운받는다.
 -  다운받은 폴더에서 'RationalOwlUMSDemo.xcworkspace'파일을 클릭하여 XCode를 실행한다.
    - 2023년 12월 개발환경 기준
    - 샘플앱을 XCode로 실행하면 실행가능한 상태로 설정들이 되어 있다.
    - 프로젝트 루트에 'RationalOwl.framework'파일이 래셔널아울 OS Objective-C 단말앱 라이브러리이다.
    - 'sample-Bridging-Header.h'파일이 Objective-C 단말앱 라이브러리를 Swift에서 사용할 수 있게 해 준다. 
 - 래셔널아울 Push API를 적용한 간단한 hello world 앱
    - 래셔널아울 실시간 API와 Push API를 모두 적용한 간단한 앱은 [Sample1](https://github.com/RationalOwl/rationalowl-sample/tree/master/device-app/ios-swift/sample1) 샘플 참조
 - 앱 실행시 푸시 수신시 단순히 xcode 콘솔에만 표시
 

## 주의사항
- IOS 리치 노티피케이션 설정을 위해 Service Extension 설정해야 함.
    - xcode : File > New > Target > Notification Service Extension 클릭.
    - 샘플 NotiServiceExtension 참고
- 메인앱과 Service Extension 간 데이터 공유를 위해 App Group 설정
    - group + bundle ID 형식    
    - 메인앱, Service Extension 두 곳 모두 설정
        - target > hello > Signing & Capabilities > '+ Capability' 클릭 > App Groups 클릭
        - 샘플 group.com.rationalowl.hello 참고
- Podfile 에 'CocoaAsyncSocket' 라이브러리 추가되어 있는지 확인
    - 샘플 Podfile 참고
- 래셔널아울 IOS 라이브러리 RationalOwl.framework 파일 메인앱, Service Extension 두 군데 추가
    - 샘플 프로젝트 참조


## 래셔널아울 라이브러리 적용
- 샘플앱 소스를 안드로이드 스튜디오에서 프로젝트로 import한 후 MinervaManager로 검색하면 래셔널아울 API 사용하는 부분이 모두 검색된다. 
- registerDevice, unregisterDevice, setDeviceRegisterResultDelegate, enableNotificationTracking, setMessageDelegate, setDeviceToken, setAppGroup, enterBackground, becomeActive, receivedApns 의 총 10개 API가 검색된다.
- API 검색을 통해 API 사용법과 의미 파악이 가능하나 자세한 설명은 [Sample1](https://github.com/RationalOwl/rationalowl-sample/tree/master/device-app/ios-swift/sample1) 참조
- 샘플소스를 참고하여 카피후 필요에 따라 추가/수정한다.
