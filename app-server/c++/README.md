# ���ųξƿ� C++ �ۼ��� ����
C++ �ۼ��� ������ C++ �ۼ��� ���̺귯������ �����ϴ� API�� �̿��ؼ��� �����̴�.


## ����
�� ���������� Boost ���̺귯���� �κ������� ���ǰ� �ִ�.
Cmake ������Ʈ�� �����Ǿ���.
Visual Studio Code�� �ۼ��Ǿ���.
Visual Studio 2017 Community Edition, Windows 10���� ����Ǿ���.

## ���� ������Ʈ ����
1. github���� ���� �ڵ带 �ٿ�޴´�.
2. [CMake](https://cmake.org/download/)�� ��ġ�Ѵ�. 
3. Visual Studio Code�� Ȯ�����α׷��� C/C++, CMake, CMake Tools�� ��ġ�Ѵ�.
4. cmakelists.txt ���� Boost ��θ� �ڽ��� ȯ�濡 �°� �����Ѵ�.
5. Visual Studio Code�� ����ȷ�Ʈ���� CMake: Build �� �����Ͽ� �����Ѵ�.
6. build/cpp_app_server_test.exe�� �����Ѵ�.

���ųξƿ� �������ܼ��� �����ϴ� �ǽð� ����͸��� ���� ���� �� �ܰ迡�� �ǽð� �������� ���� ��Ȳ�� �ƴ϶� �ۼ����� �ܸ��� �� ��� �� ���� ���θ� Ȯ���� �� �־� ���߼ӵ��� ����Ű�� ���� ��ܰ迡���� ���� ���ɼ��� ���� �������� ���̴� ������ �Ѵ�. ���� �� ���� ���߽� �������ܼ��� �̿��� �� ��ɺ� ������ Ȯ�������ν� �� ���Ǽ��� Ȯ���� �� ���� ���̴�.

>## �ۼ��� ���/���� 

## �ۼ��� ���/���� ��� �ݹ� ���
���� �ڵ忡�� setRegisterResultListener()�� �˻��ϸ� �Ʒ��� ���� �ڵ带 Ȯ���� �� �ִ�. �ش� API�� �ۼ��� �ʱ�ȭ ��ƾ�� �߰��Ͽ� �ۼ��� ���/���� API ȣ�� �� ��� �����ʸ� �����Ѵ�. 

```cpp
AppServerManager::GetInstance()->setRegisterResultListener(
    new AppServerRegisterResultImpl());
AppServerManager::GetInstance()->setDeviceGroupListener(
    new DeviceGroupImpl());
AppServerManager::GetInstance()->setMsgListener(new MessageImpl());
```
1. setRegisterResultListener() API�� ���ڷ� AppServerRegisterResultImpl �����Ͽ���.


## �ۼ��� ���
���� �ڵ忡�� registerAppServer()�� �˻��ϸ� �Ʒ��� ���� �ڵ带 Ȯ���� �� �ִ�.

```cpp
AppServerManager::GetInstance()->registerAppServer(
    serviceId, u8"Cpp Test Server", "gate.rationalowl.com", 9081);
```
1. registerAppServer() API�� ù° ������ serviceId�� ����ϰ��� �ϴ� ������ ���񽺾��̵��̴�. ���� ���̵�� ���ųξƿ� �ܼ��� '���� > ���� ���� > ����ID'���� Ȯ���Ѵ�. �̸� ī���Ͽ� serviceId�� �Է��Ѵ�.

![�̹��� �̸�](./img/svc_info.png)

2. �ι�° ������ appServerRegName���� ����ϰ��� �ϴ� �ۼ����� ��� �̸����� ���ųξƿ� �ֿܼ��� �ۼ����� �����ϴ� ������ �Ѵ�. ����� �ϳ��� ���� ������ �ۼ����� ���� ���� ���� 10������ ����� �����ϴ�. �� �������� �����ϴ� ���� ������ 'īī����'������ ���� 5���� �ۼ����� ��ϵǾ� �ְ� ���� �� �� ���� ��Ȳ�� �ܼ��� '���� > ���� ��Ȳ'���� Ȯ�� �����ϴ�. �ۼ��� ��Ȳ�� ���� ���� �� '��������̸�'�ʵ尡 �װ��̴�. 

![�̹��� �̸�](./img/app_server.png)
3. ����° ������ gateHost�� ������ �������� �ڽ��� ���񽺰� ���ư��� ȣ��Ʈ�� ���� ����� ���ųξƿ� �޽�¡ ������ ���� ����Ʈ�� �ȴ�. ���� ���ǰ� ���� Ŭ���� ������� ��� �⺻ 'gate.rationalowl.com'�� �Է��ϸ� �ȴ�.

4. �׹�° ������ gatePort�� ����Ʈ ������ ��Ʈ�� 9081�� �Է��Ѵ�.

5. ���þۼ����� �����ϸ� ���� �ڵ��� registerAppServer()�� ȣ���ϰ� �ǰ� �� ��� �ݹ��� SimpleRegisterResultListener Ŭ������ onRegisterResult()�� ȣ��ȴ�. ���� �ݹ鿡���� ���� ��� ����� ��� ��� �߱޵� ������Ͼ��̵� ���â�� ���÷����Ѵ�.

```cpp
class AppServerRegisterResultImpl : public AppServerRegisterResultListener {
 public:
  const void onRegisterResult(int resultCode, string resultMsg,
                              string appServerRegId) {
    cout << "++onRegisterResult" << endl;
    string resultMessage =
        boost::locale::conv::from_utf<char>(resultMsg, "EUC-KR");
    cout << resultCode << " " << resultMessage << " " << appServerRegId << endl;
  }
  const void onUnregisterResult(int resultCode, string resultMsg) {
    cout << "++onUnregisterResult" << endl;
    string resultMessage =
        boost::locale::conv::from_utf<char>(resultMsg, "EUC-KR");
    cout << resultCode << " " << resultMessage << endl;
  }
};
```

> resultCode�� Result.RESULT_OK�� ���� ���� ��Ͻ� ���� �߱޵� �ۼ������ ���̵��̰� 
Result::RESULT_SERVER_REGNAME_ALREADY_REGISTERED �� �̹� ��ϵ� ���� �ۼ����� �籸���� ����̰� �̶��� appServerRegId(�ۼ������ ���̵�)�� Ȯ���� ���� ������ ������ Ȯ���� �� �ִ�. ���� �ۼ������ �̸��� ����/������ �ؾ��ϰ� �ۼ��� �籸���� registerAppServer() ȣ��ÿ� �ش� ���� ����Ǿ�� �ȵȴ�. ���� �ٸ� ���� �Է��ϸ� ���ο� �ۼ��� ��� ���̵� �߱޵ǰ� �ش� �ۼ����� ���񽺿� ���� �߰��Ǵ� ������ �����Ѵ�.

���ųξƿ� ������ �ܼ��� �ۼ��� ���¿� ���� �ǽð� ����͸��� �����Ѵ�. �ۼ��� ��Ͻ� ���ųξƿ� ������ �ܼ��� '���� > ���� ��Ȳ'���� �ǽð� Ȯ�� �����ϴ�.

![�̹��� �̸�](./img/console_reg_result.png)

## �ۼ��� ��� ����
���� �ڵ忡�� unregisterAppServer()�� �˻��ϸ� �Ʒ��� ���� �ڵ带 Ȯ���� �� �ִ�.

```cpp
AppServerManager::GetInstance()->unregisterAppServer(serviceId, serverRegId);
```
1. unregisterAppServer() API�� ù° ������ serviceId�� ���񽺾��̵��̴�. 
2. �ι�° ������ serverRegId�� ���� �ڵ尡 �ռ� �ۼ��� ��� ��� �߱޹��� �ۼ��� ��� ���̵��̴�. 
3. ���� �ڵ忡�� �� ���ڸ� �Է� �� ���þۼ����� �����ϸ� ���� �ڵ��� unregServer()�� ȣ���ϰ� �ǰ� �� ��� �ݹ��� AppServerRegisterResultImpl Ŭ������ onUnregisterResult()�ݹ��� ȣ��ȴ�. ���� �ݹ鿡���� ���� ��� ���� ����� ���â�� ���÷����Ѵ�. �ۼ����� ��� ���� �Ǹ� �ۼ����� ���̻� �ܸ��۵�� �ǽð� �޽����� �ְ� ���� �� ���� ���ųξƿ� ������ �ֿܼ����� �ۼ��� ������ �������.


>## �ܸ��׷� ����

�������ܼ��� '���� > �ܸ� ��Ȳ'�� ��ϵ� �ܸ� �׷��� ��Ȳ�� Ȯ�� �� �� �ִ�.

![�̹��� �̸�](./img/console_device_grp.png)


## �ܸ��׷� ���� ��� �ݹ� ���
���� �ڵ忡�� setDeviceGroupListener()�� �˻��ϸ� �Ʒ��� ���� �ڵ带 Ȯ���� �� �ִ�. �ش� API�� �ۼ��� �ʱ�ȭ ��ƾ�� �߰��Ͽ� �ܸ��׷� ���� API ȣ�� �� ��� �����ʸ� �����Ѵ�. 

```cpp
AppServerManager::GetInstance()->setRegisterResultListener(
    new AppServerRegisterResultImpl());
AppServerManager::GetInstance()->setDeviceGroupListener(
    new DeviceGroupImpl());
AppServerManager::GetInstance()->setMsgListener(new MessageImpl());

```
1. setDeviceGroupListener() API�� ���ڷ� DeviceGroupImpl �����Ͽ���.


## �ܸ��׷� ����

���� �ڵ忡�� createDeviceGroup()�� �˻��ϸ� �Ʒ��� ���� �ڵ带 Ȯ���� �� �ִ�.


```cpp
AppServerManager::GetInstance()->createDeviceGroup("groupName", "groupDesc",
                                                    subDeviceList1);
```
1. createDeviceGroup() API�� ù° ������ groupName�� ������ �ֿܼ��� '�ܸ��׷��'���� Ȯ���� �� �ֵ���  readable�� �ܸ� �׷� �������̴�.
2. �ι�° ������ groupDesc�� �ܸ� �׷��� �����ϴ� �����̴�. �Է����� ���� �� null�� ������ �ȴ�.
3. devices�� �ܸ� �׷� ���̵� ������� �ܸ� �׷� ������ �׷쿡 ������ �ܸ� ��� ���̵� �����Ѵ�.
5. ���þۼ����� �����ϸ� createDeviceGroup()�� ȣ���ϰ� �ǰ� �� ��� �ݹ��� DeviceGroupImpl Ŭ������ onDeviceGroupCreateResult()�� ȣ��ȴ�. 

```cpp
class DeviceGroupImpl : public DeviceGroupListener {
 public:
  const void onDeviceGroupCreateResult(int resultCode, string resultMsg,
                                       string deviceGrpId, string deviceGrpName,
                                       int deviceSize, string desc,
                                       vector<string> failedDevices,
                                       string requestId) {
    GroupId = deviceGrpId;
    cout << "++onDeviceGroupCreateResult" << endl;
    cout << resultCode << " " << deviceGrpId << " " << deviceGrpName << " "
         << deviceSize << " " << desc << " " << requestId << endl;
  }
  ....
}
```

6. ������ �ܼ��� '���� > �ܸ� ��Ȳ'���� �ܸ� �׷��� ���� �ǽð����� �ܸ� �׷��� �����Ǵ� ���� Ȯ�� �� �� �ִ�.

![�̹��� �̸�](./img/console_create_device_grp.png)


## �ܸ��׷� �� �ܸ� �߰�

���� �ڵ忡�� addDeviceGroup()�� �˻��ϸ� �Ʒ��� ���� �ڵ带 Ȯ���� �� �ִ�.


```cpp
AppServerManager::GetInstance()->addDeviceGroup(GroupId, subDeviceList2);
```
1. addDevicesToGroup() API�� ù° ������ groupId�� �ܸ��� �߰��ϰ��� �ϴ� ��� �ܸ��׷��� ���̵��̴�. �ܸ��׷� ���� ������ onDeviceGroupCreateResult() �ݹ� ���� ���ڷ� �Ѿ�� deviceGrpId�� ���� �ִ´�.
2. �ι�° ������ devices�� �ܸ� �׷쿡�� �߰��ϰ��� �ϴ� �ܸ����� �ܸ���� ���̵� ����̴�.
3. ���þۼ����� �����Ͽ� 'add devices to group'��ư�� Ŭ���ϸ� ���� �ڵ��� addDeviceGroup()�� ȣ���ϰ� �ǰ� �� ��� �ݹ��� SimpleDeviceGrpListener Ŭ������ onDeviceGroupAddResult()�� ȣ��ȴ�. 

```cpp
class DeviceGroupImpl : public DeviceGroupListener {
 public:
  const void onDeviceGroupAddResult(int resultCode, string resultMsg,
                                    string deviceGrpId, int totalDeviceSize,
                                    int addedDeviceSize,
                                    vector<string> failedDevices,
                                    string requestId) {
    cout << "++onDeviceGroupAddResult" << endl;
    cout << resultCode << " " << deviceGrpId << " " << totalDeviceSize << " "
         << addedDeviceSize << " " << requestId << endl;
  }
  ...
}
```

4. ������ �ܼ��� '���� > �ܸ� ��Ȳ'���� �ܸ� �׷��� ���� �ܸ� �׷쳻 �ܸ� ���� 5�뿡�� 10��� �þ ���� Ȯ�� �� �� �ִ�.
![�̹��� �̸�](./img/console_add_device_grp.png)

## �ܸ��׷� �� �ܸ� ����

���� �ڵ忡�� subtractDeviceGroup()�� �˻��ϸ� �Ʒ��� ���� �ڵ带 Ȯ���� �� �ִ�.


```cpp
AppServerManager::GetInstance()->subtractDeviceGroup(GroupId, subDeviceList2);
```
1. subtractDeviceGroup() API�� ù° ������ groupId�� �ܸ��� �����ϰ��� �ϴ� ��� �ܸ��׷��� ���̵��̴�. �ܸ��׷� ���� ������ onDeviceGroupCreateResult() �ݹ� ���� ���ڷ� �Ѿ�� deviceGrpId�� ���� �ִ´�.
2. �ι�° ������ devices�� �ܸ� �׷쿡�� �����ϰ��� �ϴ� �ܸ��� �ܸ���� ���̵� ����̴�.
3. ���þۼ����� �����ϸ� ���� �ڵ��� subtractDeviceGroup()�� ȣ���ϰ� �ǰ� �� ��� �ݹ��� DeviceGroupImpl Ŭ������ onDeviceGroupSubtractResult()�� ȣ��ȴ�. 

```cpp
class DeviceGroupImpl : public DeviceGroupListener {
 public:
  const void onDeviceGroupSubtractResult(
      int resultCode, string resultMsg, string deviceGrpId, int totalDeviceSize,
      int subtractDeviceSize, vector<string> failedDevices, string requestId) {
    cout << "++onDeviceGroupSubtractResult" << endl;
    cout << resultCode << " " << deviceGrpId << " " << totalDeviceSize << " "
         << subtractDeviceSize << " " << requestId << endl;
  }
  ...
}
```

4. ������ �ܼ��� '���� > �ܸ� ��Ȳ'���� �ܸ� �׷��� ���� �ܸ� �׷쳻 �ܸ� ���� 10�뿡�� 7��� �پ�� ���� Ȯ�� �� �� �ִ�.
![�̹��� �̸�](./img/console_subtract_device_grp.png)


## �ܸ��׷� ����

���� �ڵ忡�� deleteDeviceGroup()�� �˻��ϸ� �Ʒ��� ���� �ڵ带 Ȯ���� �� �ִ�.

```cpp
AppServerManager::GetInstance()->deleteDeviceGroup(GroupId);
```
1. deleteDeviceGroup() API�� ù° ������ groupId�� �����ϰ��� �ϴ� ��� �ܸ��׷��� ���̵��̴�. �ܸ��׷� ���� ������ onDeviceGroupCreateResult() �ݹ� ���� ���ڷ� �Ѿ�� deviceGrpId�� ���� �ִ´�.
2. ���þۼ����� �����ϸ� ���� �ڵ��� deleteDeviceGroup()�� ȣ���ϰ� �ǰ� �� ��� �ݹ��� DeviceGroupImpl Ŭ������ onDeviceGroupDeleteResult()�� ȣ��ȴ�. 

```cpp
class DeviceGroupImpl : public DeviceGroupListener {
 public:
  const void onDeviceGroupDeleteResult(int resultCode, string resultMsg,
                                       string deviceGrpId, string requestId) {
    cout << "++onDeviceGroupDeleteResult" << endl;
    cout << resultCode << " " << deviceGrpId << " " << requestId << endl;
  }
  ...
}
```

3. ������ �ܼ��� '���� > �ܸ� ��Ȳ'���� ���õ��𿡼� ������ '�ܸ��׷�1' �ܸ��׷��� ������ ���� Ȯ���� �� �ִ�.
![�̹��� �̸�](./img/console_delete_device_grp.png)


>## �ǽð� �޽��� ��/�߽�

�������ܼ��� '���� > �޽��� ��Ȳ'���� �ǽð� �޽��� ���� ����͸��� �����ϴ�.

![�̹��� �̸�](./img/console_msg.png)

## �ǽð� �޽��� ��/�߽� ��� �ݹ� ���
���� �ڵ忡�� setMsgListener()�� �˻��ϸ� �Ʒ��� ���� �ڵ带 Ȯ���� �� �ִ�. �ش� API�� �ۼ��� �ʱ�ȭ ��ƾ�� �߰��Ͽ� �޽��� ��/�߽Ž� ȣ��Ǵ� �����ʸ� �����Ѵ�. 

```cpp
AppServerManager::GetInstance()->setRegisterResultListener(
    new AppServerRegisterResultImpl());
AppServerManager::GetInstance()->setDeviceGroupListener(
    new DeviceGroupImpl());
AppServerManager::GetInstance()->setMsgListener(new MessageImpl());
```
1. setMsgListener() API�� ���ڷ� MessageImpl �����Ͽ���.


## ��Ƽĳ��Ʈ �߽�

���� �ڵ忡�� sendMulticastMsg()�� �˻��ϸ� �Ʒ��� ���� �ڵ带 Ȯ���� �� �ִ�. �����ڵ忡���� 3���� �ܸ��� JSON������
�����͸� �߽��Ѵ�. 


```cpp
AppServerManager::GetInstance()->sendMulticastMsg(u8"��Ƽĳ��Ʈ�޽���",
                                                subDeviceList1);
```
1. sendMulticastMsg() API�� ù° ������ jsonStr�� �ܸ��� ������ json������ ��Ʈ�� �������̴�.
2. �ι�° ������ groutargetDevices�� �����͸� ������ ��� �ܸ��� �ܸ� ��� ���̵� ����̴�.
3. ���þۼ����� �����ϸ� ���� �ڵ��� sendMulticastMsg()�� ȣ���ϰ� �ǰ� �� �߽� ����� �ݹ��� MessageImpl Ŭ������ onSendMulticastMsgResult()�� ȣ��ȴ�. �ش� �ݹ��� �޽��� �߽��� �����ߴ��� ���θ� Ȯ���ϴ� �뵵�� �����Ѵ�.

```cpp
class MessageImpl : public MessageListener {
 public:
  const void onSendMulticastMsgResult(int resultCode, string resultMsg,
                                      string requestId) {
    cout << "++onSendMulticastMsgResult" << endl;
    cout << resultCode << " " << requestId << endl;
  }
  ...
}
```

4. ������ �ܼ��� '���� > �޽��� ��Ȳ'���� �ǽð����� �޽��� ������Ȳ�� Ȯ�� �� �� �ִ�.

![�̹��� �̸�](./img/console_multicast.png)


## ��ε�ĳ��Ʈ �߽�

���� �ڵ忡�� sendBroadcastMsg()�� �˻��ϸ� �Ʒ��� ���� �ڵ带 Ȯ���� �� �ִ�.


```cpp
AppServerManager::GetInstance()->sendBroadcastMsg(u8"��ε�ĳ��Ʈ�޽���");
```
1. sendBroadcastMsg() API�� ù° ������ data�� �ܼ��� ��Ʈ�� �������̴�.
2. ���þۼ����� �����Ͽ� 'send broadcast'��ư�� Ŭ���ϸ� ���� �ڵ��� sendBroadcastMsg()�� ȣ���ϰ� �ǰ� �� �߽� ����� �ݹ��� MessageImpl Ŭ������ onSendBroadcastMsgResult()�� ȣ��ȴ�. �ش� �ݹ��� �޽��� �߽��� �����ߴ��� ���θ� Ȯ���ϴ� �뵵�� �����Ѵ�.

```cpp
class MessageImpl : public MessageListener {
 public:
  const void onSendBroadcastMsgResult(int resultCode, string resultMsg,
                                      string requestId) {
    cout << "++onSendBroadcastMsgResult" << endl;
    cout << resultCode << " " << requestId << endl;
  }
  ...
}
```

3. ������ �ܼ��� '���� > �޽��� ��Ȳ'���� �ǽð����� �޽��� ������Ȳ�� Ȯ�� �� �� �ִ�. �ֿܼ��� ���� 2001 �ܸ�, ������ �ܸ��� 20000�ܸ��̴�. 

![�̹��� �̸�](./img/console_broadcast.png)

'���� > �ܸ� ��Ȳ'���� ���񽺿� ��ϵ� ��� �ܸ��� 22,001���̰� �� �� Ȱ������(��������)�� 2,001��� �޽��� ���޷��� ��ġ���� �� �� �ִ�. ����� �ٿƮ���� �⺻ 3�ϰ� ť���� �����ϸ� �ش� �Ⱓ ���� �ܸ����� Ȱ�����°� �Ǹ� ������ �޽����� �ٷ� �����Ѵ�.
![�̹��� �̸�](./img/console_broadcast_device.png)



## �׷�޽��� �߽�


���� �ڵ忡�� sendGroupMsg()�� �˻��ϸ� �Ʒ��� ���� �ڵ带 Ȯ���� �� �ִ�. 


```cpp
AppServerManager::GetInstance()->sendGroupMsg(u8"�׷�޽���", GroupId);
```
1. sendMulticastMsg() API�� ù° ������ data�� �ܸ��� ������ ��Ʈ�� �������̴�.
2. �ι�° ������ deviceGroupId���� �����͸� ������ �ܸ� �׷� ���̵� �Է��Ѵ�.
3. ���þۼ����� �����ϸ� ���� �ڵ��� sendGroupMsg()�� ȣ���ϰ� �ǰ� �� �߽� ����� �ݹ��� MessageImpl Ŭ������ onSendGroupMsgResult()�� ȣ��ȴ�. �ش� �ݹ��� �޽��� �߽��� �����ߴ��� ���θ� Ȯ���ϴ� �뵵�� �����Ѵ�.

```cpp
class MessageImpl : public MessageListener {
 public:
  const void onSendBroadcastMsgResult(int resultCode, string resultMsg,
                                      string requestId) {
    cout << "++onSendBroadcastMsgResult" << endl;
    cout << resultCode << " " << requestId << endl;
  }
  ...
}
```

6. ������ �ܼ��� '���� > �޽��� ��Ȳ'���� �ǽð����� �޽��� ������Ȳ�� Ȯ�� �� �� �ִ�. 

![�̹��� �̸�](./img/console_grp_msg.png)

>## ����Ʈ�� �޽��� ����

�����ڵ忡�� onUpstreamMsgReceived�� �˻��ϸ� �Ʒ� �ڵ带 Ȯ���� �� �ִ�. 

```cpp
class MessageImpl : public MessageListener {
 public:
  const void onUpstreamMsgReceived(string sender, long sendTime, string msg) {
    cout << "++onUpstreamMsgReceived" << endl;
    cout << sender << " " << sendTime << " " << msg << endl;
  }
  ...
}
```
�ۼ����� ����Ʈ�� �޽����� �����ϸ� onUpstreamMsgReceived() �ݹ��� ȣ��ȴ�.