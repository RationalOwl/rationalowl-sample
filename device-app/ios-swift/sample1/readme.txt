1. rationaowl.framework  추가
 - 라이브러리 드래그 드랍으로 추가.
 - target > general tab: sign & embed 로 라이브러리 추가.
2. cocoa pod 적용
 1) pod init
 2) podfile edit
   - pod 'CocoaAsyncSocket' 추가
 * framework에서 추가했는데 또 해야 하나? 튜토리얼 못찾아 간단히 pod로 다시 추가 함.
 3) podfile 내 라이브러리 설치
  - pod install