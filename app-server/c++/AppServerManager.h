#ifndef AppServerManager_H
#define AppServerManager_H

#include <iostream>
#include "AppServerRegisterResultListener.h"
#include "DeviceGroupListener.h"
#include "MessageListener.h"

using namespace std;

/**
 * @brief �ܸ��ۿ���  API �������̽� ����
 * @details �ܸ��ۿ���  API �������̽� ����
 */
class AppServerManager {
 public:
  static AppServerManager* GetInstance() {
    static AppServerManager ins;
    return &ins;
  }

  /**
   * @brief �ۼ����� ��� �� �����Ѵ�.
   * @details
   * �ۼ����� ��� �� �����Ѵ�. ���� �ۼ��� ��Ͻ� �� �ƴ϶� ���Ŀ��� �ۼ���
   * �籸���ø��� ȣ���Ͽ� ���̺귯���� �����ϵ��� �ؾ� �Ѵ�.
   * setRegisterResultListener()�� ����� AppServerRegisterResultListener��
   * �ݹ��� ���� ��� ����� Ȯ���Ѵ�. �ۼ��� ��� ����� ���ųξƿ� �ֿܼ�����
   * �ǽð� Ȯ���� �����ϴ�.
   *
   * @param string serviceId �ۼ����� ����� ��� �� ������ ���� ���̵�
   * @param string regName
   *            �ۼ��� ��� �̸����� ���ųξƿ� ������ ������ �ֿܼ� ǥ�õǴ�
   * �� ������ �̸� �ֿܼ��� �� ������ �����ϴ� ������ �Ѵ�.
   * @param string gateHost �� �� ������ ���� ����� ���ųξƿ� ����Ʈ����
   * @param int gatePort ����Ʈ������ ��Ʈ
   * @return string& requestId
   */
  string& registerAppServer(string serviceId, string regName, string gateHost,
                            int gatePort);

  /**
   * @brief �ۼ����� ��������Ѵ�.
   * @details
   * �ۼ����� ��������Ѵ�. setRegisterResultListener()�� �����
   * AppServerRegisterResultListener�� �ݹ��� ���� ��� ���� ����� Ȯ���Ѵ�.
   * �ۼ��� ��� ���� ����� ���ųξƿ� �ֿܼ����� �ǽð� Ȯ���� �����ϴ�.
   *
   * @param string serviceId �ۼ����� ��ϵ� �� ������ ���� ���̵�
   * @param string serverRegId ��������� �ۼ��� ��� ���̵�
   * @return string& requestId
   */
  string& unregisterAppServer(string serviceId, string serverRegId);

  /**
   * @brief �ܸ� �׷��� �����Ѵ�.
   * @details
   * �ܸ� �׷��� �����Ѵ�. setDeviceGroupListener�� ����� �������� �ݹ��� ����
   * ����� Ȯ���Ѵ�. ���ųξƿ� �ֿܼ����� �ǽð� ��� Ȯ���� �����ϴ�.
   *
   * @param string groupName ������ �ܸ� �׷��
   * @param string groupDesc �ܸ� �׷쿡 ���� ���� - optional �ʵ��
   * null�Է°���
   * @param vector<string> deviceList �ܸ� �׷� ������ �׷� �� ���Խ�ų �ܸ�
   * ��� �ִ� 2000�ܸ� ��ϱ��� ���԰���
   * @return string& requestId
   */
  string& createDeviceGroup(string groupName, string groupDesc,
                            vector<string> deviceList);

  /**
   * @brief �ܸ� �׷쿡 �ܸ��� �߰��Ѵ�.
   * @details
   * �ܸ� �׷쿡 �ܸ��� �߰��Ѵ�. setDeviceGroupListener�� ����� ��������
   * �ݹ��� ���� ����� Ȯ���Ѵ�. ���ųξƿ� �ֿܼ����� �ǽð� ��� Ȯ����
   * �����ϴ�.
   *
   * @param string groupId ��� �ܸ� �׷� ���̵�
   * @param vector<string> deviceList
   *            �ܸ� �׷쿡 �߰��� �ܸ� ��� �ѹ��� �ִ� 2000�ܸ� ��ϱ���
   * ���԰��� �ܸ� �׷� �� �ܸ� ���� �ִ� �鸸 ����� ���� ����
   * @return string& requestId
   */
  string& addDeviceGroup(string groupId, vector<string> deviceList);

  /**
   * @brief �ܸ� �׷쿡�� �ܸ��� �����Ѵ�.
   * @details
   * �ܸ� �׷쿡�� �ܸ��� �����Ѵ�. setDeviceGroupListener�� ����� ��������
   * �ݹ��� ���� ����� Ȯ���Ѵ�. ���ųξƿ� �ֿܼ����� �ǽð� ��� Ȯ����
   * �����ϴ�.
   *
   * @param string groupId ��� �ܸ� �׷� ���̵�
   * @param vector<string> deviceList �ܸ� �׷쿡�� ������ �ܸ� ��� �ѹ��� �ִ�
   * 2000�ܸ� ��ϱ��� ���԰���
   * @return string& requestId
   */
  string& subtractDeviceGroup(string groupId, vector<string> deviceList);

  /**
   * @brief �ܸ� �׷��� �����Ѵ�.
   * @details
   * �ܸ� �׷��� �����Ѵ�. setDeviceGroupListener�� ����� �������� �ݹ��� ����
   * ����� Ȯ���Ѵ�. ���ųξƿ� �ֿܼ����� �ǽð� ��� Ȯ���� �����ϴ�.
   *
   * @param string groupId ��� �ܸ� �׷� ���̵�
   * @return string& requestId
   */
  string& deleteDeviceGroup(string groupId);

  /**
   * @brief �Ѵ� �̻��� �ܸ��ۿ� �ٿƮ�� �޽����� �߽��Ѵ�.
   * @details
   * �Ѵ� �̻��� �ܸ��ۿ� �ٿƮ�� �޽����� �߽��Ѵ�. setMsgListener�� �����
   * �������� �ݹ��� ���� ����� Ȯ���Ѵ�. ���ųξƿ� �ֿܼ����� �ǽð� ���
   * Ȯ���� �����ϴ�.
   *
   * @param string data �ܸ��� ������ ������
   * @param vector<string> deviceRegIds �޽����� ������ ��� �ܸ����� �ܸ� ���
   * ���̵� ��� �ִ� 2000 �ܸ� ��� ����
   * @param bool supportMsgQ
   *            �޽��� ť�� ���� ���� true�� ��� �ܸ��� �������� ���� ������
   * ����������� �Ұ��� ��� �⺻ 3�� ���� ���ųξƿ� �޽�¡ ������ �����ϴٰ�
   * 3�� �̳� �ܸ��� ��Ʈ��ũ�� ����� �� ������ �޽�����
   * �����Ѵ�.
   * @param string notiTitle �˸� �뵵�� �޽��� ���� �� �ܸ����� ��Ȱ���� �˸�
   * Ÿ��Ʋ�� ǥ���� ����
   * @param string notiBody �˸� �뵵�� �޽��� ���� �� �ܸ����� ��Ȱ���� �˸�
   * �������� ǥ���� ����
   * @return string& requestId
   */
  string& sendMulticastMsg(string data, vector<string> deviceRegIds,
                           bool supportMsgQ = false, string notiTitle = "",
                           string notiMsg = "");

  /**
   * @brief �� ���񽺿� ��ϵ� ��� �ܸ��ۿ� �ٿƮ�� �޽����� �߽��Ѵ�.
   * @details
   * �� ���񽺿� ��ϵ� ��� �ܸ��ۿ� �ٿƮ�� �޽����� �߽��Ѵ�.
   * setMsgListener�� ����� �������� �ݹ��� ���� ����� Ȯ���Ѵ�. ���ųξƿ�
   * �ֿܼ����� �ǽð� ��� Ȯ���� �����ϴ�.
   *
   * @param string data �ܸ��� ������ ������
   * @param bool supportMsgQ
   *            �޽��� ť�� ���� ���� true�� ��� �ܸ��� �������� ���� ������
   * ����������� �Ұ��� ��� �⺻ 3�� ���� ���ųξƿ� �޽�¡ ������ �����ϴٰ�
   * 3�� �̳� �ܸ��� ��Ʈ��ũ�� ����� �� ������ �޽�����
   * �����Ѵ�.
   * @param string notiTitle �˸� �뵵�� �޽��� ���� �� �ܸ����� ��Ȱ���� �˸�
   * Ÿ��Ʋ�� ǥ���� ����
   * @param string notiBody �˸� �뵵�� �޽��� ���� �� �ܸ����� ��Ȱ���� �˸�
   * �������� ǥ���� ����
   * @return string& requestId
   */
  string& sendBroadcastMsg(string data, bool supportMsgQ = false,
                           string notiTitle = "", string notiMsg = "");

  /**
   * @brief �ܸ� �׷쿡 ��ϵ� �ܸ��ۿ� �ٿƮ�� �޽����� �߽��Ѵ�.
   * 발신?��?��.
   * @details
   * �ܸ� �׷쿡 ��ϵ� �ܸ��ۿ� �ٿƮ�� �޽����� �߽��Ѵ�. setMsgListener��
   * ����� �������� �ݹ��� ���� ����� Ȯ���Ѵ�. ���ųξƿ� �ֿܼ����� �ǽð�
   * ��� Ȯ���� �����ϴ�.
   *
   * @param string data �ܸ��� ������ ������
   * @param string deviceGroupId �ܸ� �׷� ���̵�
   * @param bool supportMsgQ
   *            �޽��� ť�� ���� ���� true�� ��� �ܸ��� �������� ���� ������
   * ����������� �Ұ��� ��� �⺻ 3�� ���� ���ųξƿ� �޽�¡ ������ �����ϴٰ�
   * 3�� �̳� �ܸ��� ��Ʈ��ũ�� ����� �� ������ �޽�����
   * �����Ѵ�.
   * @param string notiTitle �˸� �뵵�� �޽��� ���� �� �ܸ����� ��Ȱ���� �˸�
   * Ÿ��Ʋ�� ǥ���� ����
   * @param string notiBody �˸� �뵵�� �޽��� ���� �� �ܸ����� ��Ȱ���� �˸�
   * �������� ǥ���� ����
   * @return string& requestId
   */
  string& sendGroupMsg(string data, string deviceGroupId,
                       bool supportMsgQ = false, string notiTitle = "",
                       string notiMsg = "");

  /**
   * @brief �ܸ� ��� ����� ó���� �����ʸ� ����Ѵ�.
   * @details �ܸ� ��� ����� ó���� �����ʸ� ����Ѵ�.
   *
   * @param AppServerRegisterResultListener* listener
   *     registerAppServer API�� ���� ��Ͽ�û�� ����� ó���� ������ ����
   *            registerAppServer()API ȣ��� listener�� onRegisterResult()
   * �ݹ鿡�� ����� Ȯ���Ѵ�.
   */
  void setRegisterResultListener(AppServerRegisterResultListener* listener);

  /**
   * @brief �ۼ������� ����� �ܸ� ��� �����ʸ� �����Ѵ�. ���� listener��
   * �ݹ��� ȣ����� �ʴ´�.
   * @details �ۼ������� ����� �ܸ� ��� �����ʸ� �����Ѵ�. ���� listener��
   * �ݹ��� ȣ����� �ʴ´�.
   *
   */
  void clearRegisterResultListener();

  /**
   * @brief �ٿƮ�� �޽��� �߽� ����� ����Ʈ�� �޽��� ������ ó���� �����ʸ�
   * ����Ѵ�.
   * @details �ٿƮ�� �޽��� �߽� ����� ����Ʈ�� �޽��� ������ ó����
   * �����ʸ� ����Ѵ�.
   *
   * @param MessageListener* listener �ٿƮ�� �޽��� �߽� ����� ����Ʈ��
   * �޽��� ���Ž� ó���� ������
   */
  void setMsgListener(MessageListener* listener);

  /**
   * @brief �ۼ������� ����� �޽��� �����ʸ� �����Ѵ�. ���� �޽��� ��������
   * �ݹ��� ȣ����� �ʴ´�.
   * @details �ۼ������� ����� �޽��� �����ʸ� �����Ѵ�. ���� �޽��� ��������
   * �ݹ��� ȣ����� �ʴ´�.
   *
   */
  void clearMsgListener();

  /**
   * @brief �ܸ� �׷� ���� APIȣ���� ����� ó���� �����ʸ� ����Ѵ�.
   * @details �ܸ� �׷� ���� APIȣ���� ����� ó���� �����ʸ� ����Ѵ�.
   *
   * @param DeviceGroupListener* listener �ܸ� �׷� ���� API����� ó���� ������
   */
  void setDeviceGroupListener(DeviceGroupListener* listener);

  /**
   * @brief �ۼ������� ����� �ܸ��׷���� �����ʸ� �����Ѵ�. ���� ��������
   * �ݹ��� ȣ����� �ʴ´�.
   * @details �ۼ������� ����� �ܸ��׷���� �����ʸ� �����Ѵ�. ���� ��������
   * �ݹ��� ȣ����� �ʴ´�.
   *
   */
  void clearDeviceGroupListener();

 private:
  AppServerManager() {}
  AppServerManager(const AppServerManager& other) {}
  ~AppServerManager() {}
};

#endif