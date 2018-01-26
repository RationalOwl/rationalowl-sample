#ifndef AppServerManager_H
#define AppServerManager_H

#include <iostream>
#include "AppServerRegisterResultListener.h"
#include "DeviceGroupListener.h"
#include "MessageListener.h"

using namespace std;

/**
 * @brief 단말앱에게  API 인터페이스 제공
 * @details 단말앱에게  API 인터페이스 제공
 */
class AppServerManager {
 public:
  static AppServerManager* GetInstance() {
    static AppServerManager ins;
    return &ins;
  }

  /**
   * @brief 앱서버를 등록 및 구동한다.
   * @details
   * 앱서버를 등록 및 구동한다. 최초 앱서버 등록시 뿐 아니라 이후에도 앱서버
   * 재구동시마다 호출하여 라이브러리가 구동하도록 해야 한다.
   * setRegisterResultListener()로 등록한 AppServerRegisterResultListener의
   * 콜백을 통해 등록 결과를 확인한다. 앱서버 등록 결과는 래셔널아울 콘솔에서도
   * 실시간 확인이 가능하다.
   *
   * @param string serviceId 앱서버가 등록할 대상 고객 서비스의 서비스 아이디
   * @param string regName
   *            앱서버 등록 이름으로 래셔널아울 서비스의 관리자 콘솔에 표시되는
   * 고객 서버의 이름 콘솔에서 앱 서버를 구분하는 역할을 한다.
   * @param string gateHost 고객 앱 서버와 가장 가까운 래셔널아울 게이트서버
   * @param int gatePort 게이트서버의 포트
   * @return string& requestId
   */
  string& registerAppServer(string serviceId, string regName, string gateHost,
                            int gatePort);

  /**
   * @brief 앱서버를 등록해제한다.
   * @details
   * 앱서버를 등록해제한다. setRegisterResultListener()로 등록한
   * AppServerRegisterResultListener의 콜백을 통해 등록 헤제 결과를 확인한다.
   * 앱서버 등록 해제 결과는 래셔널아울 콘솔에서도 실시간 확인이 가능하다.
   *
   * @param string serviceId 앱서버가 등록된 고객 서비스의 서비스 아이디
   * @param string serverRegId 등록해제할 앱서버 등록 아이디
   * @return string& requestId
   */
  string& unregisterAppServer(string serviceId, string serverRegId);

  /**
   * @brief 단말 그룹을 생성한다.
   * @details
   * 단말 그룹을 생성한다. setDeviceGroupListener로 등록한 리스너의 콜백을 통해
   * 결과를 확인한다. 래셔널아울 콘솔에서도 실시간 결과 확인이 가능하다.
   *
   * @param string groupName 생성할 단말 그룹명
   * @param string groupDesc 단말 그룹에 대한 설명 - optional 필드로
   * null입력가능
   * @param vector<string> deviceList 단말 그룹 생성시 그룹 내 포함시킬 단말
   * 목록 최대 2000단말 목록까지 포함가능
   * @return string& requestId
   */
  string& createDeviceGroup(string groupName, string groupDesc,
                            vector<string> deviceList);

  /**
   * @brief 단말 그룹에 단말을 추가한다.
   * @details
   * 단말 그룹에 단말을 추가한다. setDeviceGroupListener로 등록한 리스너의
   * 콜백을 통해 결과를 확인한다. 래셔널아울 콘솔에서도 실시간 결과 확인이
   * 가능하다.
   *
   * @param string groupId 대상 단말 그룹 아이디
   * @param vector<string> deviceList
   *            단말 그룹에 추가할 단말 목록 한번에 최대 2000단말 목록까지
   * 포함가능 단말 그룹 내 단말 수는 최대 백만 대까지 포함 가능
   * @return string& requestId
   */
  string& addDeviceGroup(string groupId, vector<string> deviceList);

  /**
   * @brief 단말 그룹에서 단말을 제거한다.
   * @details
   * 단말 그룹에서 단말을 제거한다. setDeviceGroupListener로 등록한 리스너의
   * 콜백을 통해 결과를 확인한다. 래셔널아울 콘솔에서도 실시간 결과 확인이
   * 가능하다.
   *
   * @param string groupId 대상 단말 그룹 아이디
   * @param vector<string> deviceList 단말 그룹에서 제거할 단말 목록 한번에 최대
   * 2000단말 목록까지 포함가능
   * @return string& requestId
   */
  string& subtractDeviceGroup(string groupId, vector<string> deviceList);

  /**
   * @brief 단말 그룹을 삭제한다.
   * @details
   * 단말 그룹을 삭제한다. setDeviceGroupListener로 등록한 리스너의 콜백을 통해
   * 결과를 확인한다. 래셔널아울 콘솔에서도 실시간 결과 확인이 가능하다.
   *
   * @param string groupId 대상 단말 그룹 아이디
   * @return string& requestId
   */
  string& deleteDeviceGroup(string groupId);

  /**
   * @brief 한대 이상의 단말앱에 다운스트림 메시지를 발신한다.
   * @details
   * 한대 이상의 단말앱에 다운스트림 메시지를 발신한다. setMsgListener로 등록한
   * 리스너의 콜백을 통해 결과를 확인한다. 래셔널아울 콘솔에서도 실시간 결과
   * 확인이 가능하다.
   *
   * @param string data 단말에 전달할 데이터
   * @param vector<string> deviceRegIds 메시지를 전달할 대상 단말앱의 단말 등록
   * 아이디 목록 최대 2000 단말 목록 제한
   * @param bool supportMsgQ
   *            메시지 큐잉 지원 여부 true일 경우 단말이 전원꺼짐 등의 이유로
   * 데이터통신이 불가할 경우 기본 3일 동안 래셔널아울 메시징 서버가 보관하다가
   * 3일 이내 단말이 네트워크에 연결될 때 미전달 메시지를
   * 전달한다.
   * @param string notiTitle 알림 용도로 메시지 전달 시 단말앱이 비활성시 알림
   * 타이틀로 표시할 문자
   * @param string notiBody 알림 용도로 메시지 전달 시 단말앱이 비활성시 알림
   * 내용으로 표시할 문자
   * @return string& requestId
   */
  string& sendMulticastMsg(string data, vector<string> deviceRegIds,
                           bool supportMsgQ = false, string notiTitle = "",
                           string notiMsg = "");

  /**
   * @brief 고객 서비스에 등록된 모든 단말앱에 다운스트림 메시지를 발신한다.
   * @details
   * 고객 서비스에 등록된 모든 단말앱에 다운스트림 메시지를 발신한다.
   * setMsgListener로 등록한 리스너의 콜백을 통해 결과를 확인한다. 래셔널아울
   * 콘솔에서도 실시간 결과 확인이 가능하다.
   *
   * @param string data 단말에 전달할 데이터
   * @param bool supportMsgQ
   *            메시지 큐잉 지원 여부 true일 경우 단말이 전원꺼짐 등의 이유로
   * 데이터통신이 불가할 경우 기본 3일 동안 래셔널아울 메시징 서버가 보관하다가
   * 3일 이내 단말이 네트워크에 연결될 때 미전달 메시지를
   * 전달한다.
   * @param string notiTitle 알림 용도로 메시지 전달 시 단말앱이 비활성시 알림
   * 타이틀로 표시할 문자
   * @param string notiBody 알림 용도로 메시지 전달 시 단말앱이 비활성시 알림
   * 내용으로 표시할 문자
   * @return string& requestId
   */
  string& sendBroadcastMsg(string data, bool supportMsgQ = false,
                           string notiTitle = "", string notiMsg = "");

  /**
   * @brief 단말 그룹에 등록된 단말앱에 다운스트림 메시지를 발신한다.
   * 諛쒖떊?븳?떎.
   * @details
   * 단말 그룹에 등록된 단말앱에 다운스트림 메시지를 발신한다. setMsgListener로
   * 등록한 리스너의 콜백을 통해 결과를 확인한다. 래셔널아울 콘솔에서도 실시간
   * 결과 확인이 가능하다.
   *
   * @param string data 단말에 전달할 데이터
   * @param string deviceGroupId 단말 그룹 아이디
   * @param bool supportMsgQ
   *            메시지 큐잉 지원 여부 true일 경우 단말이 전원꺼짐 등의 이유로
   * 데이터통신이 불가할 경우 기본 3일 동안 래셔널아울 메시징 서버가 보관하다가
   * 3일 이내 단말이 네트워크에 연결될 때 미전달 메시지를
   * 전달한다.
   * @param string notiTitle 알림 용도로 메시지 전달 시 단말앱이 비활성시 알림
   * 타이틀로 표시할 문자
   * @param string notiBody 알림 용도로 메시지 전달 시 단말앱이 비활성시 알림
   * 내용으로 표시할 문자
   * @return string& requestId
   */
  string& sendGroupMsg(string data, string deviceGroupId,
                       bool supportMsgQ = false, string notiTitle = "",
                       string notiMsg = "");

  /**
   * @brief 단말 등록 결과를 처리할 리스너를 등록한다.
   * @details 단말 등록 결과를 처리할 리스너를 등록한다.
   *
   * @param AppServerRegisterResultListener* listener
   *     registerAppServer API를 통해 등록요청한 결과를 처리할 리스너 이후
   *            registerAppServer()API 호출시 listener의 onRegisterResult()
   * 콜백에서 결과를 확인한다.
   */
  void setRegisterResultListener(AppServerRegisterResultListener* listener);

  /**
   * @brief 앱서버에서 등록한 단말 등록 리스너를 해제한다. 이후 listener의
   * 콜백이 호출되지 않는다.
   * @details 앱서버에서 등록한 단말 등록 리스너를 해제한다. 이후 listener의
   * 콜백이 호출되지 않는다.
   *
   */
  void clearRegisterResultListener();

  /**
   * @brief 다운스트림 메시지 발신 결과와 업스트림 메시지 수신을 처리할 리스너를
   * 등록한다.
   * @details 다운스트림 메시지 발신 결과와 업스트림 메시지 수신을 처리할
   * 리스너를 등록한다.
   *
   * @param MessageListener* listener 다운스트림 메시지 발신 결과와 업스트림
   * 메시지 수신시 처리할 리스너
   */
  void setMsgListener(MessageListener* listener);

  /**
   * @brief 앱서버에서 등록한 메시지 리스너를 해제한다. 이후 메시지 리스너의
   * 콜백이 호출되지 않는다.
   * @details 앱서버에서 등록한 메시지 리스너를 해제한다. 이후 메시지 리스너의
   * 콜백이 호출되지 않는다.
   *
   */
  void clearMsgListener();

  /**
   * @brief 단말 그룹 관리 API호출후 결과를 처리할 리스너를 등록한다.
   * @details 단말 그룹 관리 API호출후 결과를 처리할 리스너를 등록한다.
   *
   * @param DeviceGroupListener* listener 단말 그룹 관리 API결과를 처리할 리스너
   */
  void setDeviceGroupListener(DeviceGroupListener* listener);

  /**
   * @brief 앱서버에서 등록한 단말그룹관리 리스너를 해제한다. 이후 리스너의
   * 콜백이 호출되지 않는다.
   * @details 앱서버에서 등록한 단말그룹관리 리스너를 해제한다. 이후 리스너의
   * 콜백이 호출되지 않는다.
   *
   */
  void clearDeviceGroupListener();

 private:
  AppServerManager() {}
  AppServerManager(const AppServerManager& other) {}
  ~AppServerManager() {}
};

#endif