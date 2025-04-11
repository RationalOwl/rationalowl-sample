# Flutter 래셔널아울 플러그인


아래 래셔널아울 Flutter 라이브러리를 통해 Hello World 샘플앱이다.


- 라이브러리명: rationalowl_flutter
- 라이브러리 repository
[![pub package](https://img.shields.io/pub/v/rationalowl_flutter.svg)](https://pub.dev/packages/rationalowl_flutter)



## 개발환경

- 플러터 IOS, 안드로이드 맥북에서 동일한 개발환경으로 진행하는 것을 권장
- 래셔널아울 Flutter 라이브러리 개발환경은 아래와 같다(2025년 4월 10일 기준)
- macos: Sequoia 15.4, Android studio: MeerKat 2024.3.1 patch1, Xcode: 16.3 Flutter SDK: 3.29.2

## 래셔널아울 flutter 라이브러리 get
- pub.dev 레파지토리에 배포된 래셔널아울 flutter 라이브러리명인 rationalowl_flutter을 가져오기 위해 pubspec.yaml 파일에 해당 이름을 기입하고 pubspec.lock파일에 가져올 버전을 명시한다. (2025년 4월 10일 기준 1.1.1 이 최신버전)
- flutter pub get 명령어로 pub.dev 레파지토리에서 라이브러리를 가져온다.


## 사용 방법

플러그인을 사용하려면, `rationalowl_flutter`를 [pubspec.yaml 파일의 종속 항목으로 추가](https://flutter.dev/docs/development/platform-integration/platform-channels)합니다.


## 단말앱 푸시 설정

* APNS 설정은 래셔널아울 네이티브 IOS 와 동일
* FCM 안드로이드 설정은 flutter 안드로이드 FCM 설정방법인 CLI로 설정
    * [FCM CLI 참조](https://firebase.google.com/docs/flutter/setup?platform=android)
    * 특히 소스의 firebase_options.dart FirebaseOptions의 projectId 와 firebase 콘솔에서 생성한 프로젝트 id와 같은지 확인한다.

   