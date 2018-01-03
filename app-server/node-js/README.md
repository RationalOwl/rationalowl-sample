# 래셔널아울 Node.js 앱서버 샘플

Node.js 앱서버 샘플은 Node.js 앱서버 라이브러리에서 제공하는 API를 이용해서 express 위에서 앱서버를 만드는 것을 쉽게 따라할 수 있도록 작성되었다.

## 참고

본 샘플은 타입스크립트와 ES6의 async/await를 이용해 작성되었다.

## 샘플 프로젝트 설정

1. github에서 샘플 코드를 다운받는다.
2. 의존 모듈을 설치한다

```sh
npm install ## YARN을 이용할 경우는 yarn
```

3. 실행

```sh
npm start
```

래셔널아울 관리자콘솔이 제공하는 실시간 모니터링은 서비스 개발 전 단계에서 실시간 데이터의 전달 현황뿐 아니라 앱서버와 단말의 각 기능 별 성공 여부를 확인할 수 있어 개발속도를 향상시키고 서비스 운영단계에서는 예측 가능성과 서비스 대응력을 높이는 역할을 한다. 샘플 앱 서버 개발시 관리자콘솔을 이용해 각 기능별 동작을 확인함으로써 그 편의성을 확인할 수 있을 것이다.

>## 앱서버 등록

앱서버 등록은 express 서버가 시작할 때 등록이 되도록 구현했다.

```js
app.listen(3000, async () => {
    await AppServerManager.getInstance()
        .registerAppServer(SERVICE_ID, 'sample-node-app-server', 'gate.rationalowl.com', 9081);
    console.log('Example app listening on port 3000!');
});
```

>## 단말그룹 관리

관리자콘솔의 '서비스 > 단말 현황'에 등록된 단말 그룹의 현황을 확인 할 수 있다.

### 단말그룹 생성

/device-group 라우팅에 대해 단말 그룹이 생성되도록 작성했다.

```js
app.post('/device-group', async (req, res) => {
    const name = req.body.name;
    const desc = req.body.desc;
    const devices = req.body.devices;
    try {
        const newGroup = await AppServerManager.getInstance().createDeviceGroup(name, desc, devices);
        res.send(newGroup).end();
    } catch (e) {
        console.error(e);
        res.statusCode = 500;
        res.end();
    }
});
```

curl로 작성된 요청 샘플이다

```sh
curl --request POST \
  --url http://localhost:3000/device-group \
  --header 'content-type: application/json' \
  --data '{"name": "testGroup","desc": "testDesc","devices" : ["35d612418f354c7eba8c6492813ddf2d","386dda6521bf40d58aa935022a5bb144","3b3e4a3ae2d0412ea3d3eaf3f1eda5c5"]}'
```

응답

```json
{
  "resultCode": 1,
  "resultMsg": "작업이  성공 했습니다.",
  "deviceGrpId": "be6d2c0d95a44caea32f2b669148316a",
  "deviceGrpName": "testGroup",
  "deviceSize": 3,
  "desc": "testDesc",
  "failedDevcies": []
}
```

### 단말그룹 내 단말 추가

/device-group/add/:groupId 라우팅에 대해 단말그룹 내 단말이 추가되도록 작성했다.

```js
app.post('/device-group/add/:groupId', async (req, res) => {
    const groupId = req.params.groupId;
    const devices = req.body.devices;
    try {
        const addGrpMsg = await AppServerManager.getInstance().addDeviceGroup(groupId, devices);
        res.send(addGrpMsg).end();
    } catch (e) {
        console.error(e);
        res.statusCode = 500;
        res.end();
    }
});
```

curl로 작성된 요청 샘플이다

```sh
curl --request POST \
  --url http://localhost:3000/device-group/add/be6d2c0d95a44caea32f2b669148316a \
  --header 'content-type: application/json' \
  --data '{"devices" : ["40bbaaba05c44160963d4d3dc3f95178","40f186b7666f413f83fb0754b02d21e4","425e58901bc3485a819a453a85053dff","466280bfa0994f80ae24a70d9343ddcc","4f1c05c6303245508eb2336314149d12","4f742a6a87494efab017b695181d5043"]}'
```

응답

```json
{
  "resultCode": 1,
  "resultMsg": "작업이  성공 했습니다.",
  "deviceGrpId": "be6d2c0d95a44caea32f2b669148316a",
  "totalDeviceSize": 9,
  "addedDeviceSize": 6,
  "failedDevcies": []
}
```

### 단말그룹 내 단말 제거

/device-group/sub/:groupId 라우팅에 대해 단말그룹 내 단말이 제거되도록 작성했다.

```js
app.post('/device-group/sub/:groupId', async (req, res) => {
    const groupId = req.params.groupId;
    const devices = req.body.devices;
    try {
        const subGrpMsg = await AppServerManager.getInstance().subtractDeviceGroup(groupId, devices);
        res.send(subGrpMsg).end();
    } catch (e) {
        console.error(e);
        res.statusCode = 500;
        res.end();
    }
});
```

curl로 작성된 요청 샘플이다

```sh
curl --request POST \
  --url http://localhost:3000/device-group/sub/be6d2c0d95a44caea32f2b669148316a \
  --header 'content-type: application/json' \
  --data '{"devices" : ["40bbaaba05c44160963d4d3dc3f95178","40f186b7666f413f83fb0754b02d21e4","4f742a6a87494efab017b695181d5043"]}'
```

응답

```json
{
  "resultCode": 1,
  "resultMsg": "작업이  성공 했습니다.",
  "deviceGrpId": "be6d2c0d95a44caea32f2b669148316a",
  "totalDeviceSize": 6,
  "subtractDeviceSize": 3,
  "failedDevcies": []
}
```

### 단말그룹 삭제

/device-group/sub/:groupId 라우팅에 대해 단말 그룹이 제거되도록 작성했다.

```js
app.delete('/device-group/:groupId', async (req, res) => {
    const groupId = req.params.groupId;
    try {
        const deleteGrpMsg = await AppServerManager.getInstance().deleteDeviceGroup(groupId);
        res.send(deleteGrpMsg).end();
    } catch (e) {
        console.error(e);
        res.statusCode = 500;
        res.end();
    }
});
```

curl로 작성된 요청 샘플이다

```sh
curl --request DELETE \
  --url http://localhost:3000/device-group/be6d2c0d95a44caea32f2b669148316a \
  --header 'content-type: application/json'
```

응답

```json
{
  "resultCode": 1,
  "resultMsg": "작업이  성공 했습니다.",
  "deviceGrpId": "be6d2c0d95a44caea32f2b669148316a"
}
```

>## 실시간 메시지 수/발신

관리자콘솔의 '서비스 > 메시지 현황'에서 실시간 메시지 전달 모니터링이 가능하다.

### 멀티캐스트 발신

/message/multi 라우팅에 대해 메시지가 멀티캐스트방식으로 전달되도록 구현했다.

```js
app.post('/message/multi', async (req, res) => {
    const message = req.body.message;
    const devices = req.body.devices;
    try {
        const multicastMsg = await AppServerManager.getInstance().sendMulticastMsg(message, devices, false);
        res.send(multicastMsg).end();
    } catch (e) {
        console.log('error');
        console.error(e);
        res.statusCode = 500;
        res.end();
    }
});
```

curl로 작성된 요청 샘플이다

```sh
curl --request POST \
  --url http://localhost:3000/message/multi \
  --header 'content-type: application/json' \
  --data '{"message" : "testMessage","devices" : ["35d612418f354c7eba8c6492813ddf2d","386dda6521bf40d58aa935022a5bb144","3b3e4a3ae2d0412ea3d3eaf3f1eda5c5"]}'
```

응답

```json
{
  "resultCode": 1,
  "resultMsg": "작업이  성공 했습니다."
}
```

### 브로드캐스트 발신

/message/broad 라우팅에 대해 메시지가 브로드캐스트방식으로 전달되도록 구현했다.

```js
app.post('/message/broad', async (req, res) => {
    const message = req.body.message;
    try {
        const broadcastMsg = await AppServerManager.getInstance().sendBroadcastMsg(message, false);
        res.send(broadcastMsg).end();
    } catch (e) {
        console.error(e);
        res.statusCode = 500;
        res.end();
    }
});
```

curl로 작성된 요청 샘플이다

```sh
curl --request POST \
  --url http://localhost:3000/message/broad \
  --header 'content-type: application/json' \
  --data '{"message" : "testMessage"}'
```

응답

```json
{
  "resultCode": 1,
  "resultMsg": "작업이  성공 했습니다."
}
```

### 그룹 메시지 발신

/message/group 라우팅에 대해 메시지가 해당 그룹의 단말에게 전달되도록 구현했다.

```js
app.post('/message/group', async (req, res) => {
    const message = req.body.message;
    const groupId = req.body.groupId;
    try {
        const groupMsg = await AppServerManager.getInstance().sendGroupMsg(message, groupId, false);
        res.send(groupMsg).end();
    } catch (e) {
        console.error(e);
        res.statusCode = 500;
    }
});
```

curl로 작성된 요청 샘플이다

```sh
curl --request POST \
  --url http://localhost:3000/message/group \
  --header 'content-type: application/json' \
  --data '{"message" : "testMessage","groupId" : "be6d2c0d95a44caea32f2b669148316a"}'
```

응답

```json
{
  "resultCode": 1,
  "resultMsg": "작업이  성공 했습니다."
}
```

### 업스트림 메시지 수신

메시지 수신과 같은 경우는 HTTP 프로토콜의 제약으로 인해 REST API를 만들 수 없으므로 아래 예제로 대체한다.

단말에서 업스트림 메시지를 보내면 앱 서버는 아래와 같은 코드로 수신 할 수 있다.

```js
const listnener: UpstreamMessageListenerType = (result) => {
    console.log(result.sender, result.serverTime, result.data);
};

AppServerManager.getInstance().addReceivedUpstreamMsgListener(listnener);
```

리스너의 삭제는 아래와 같다.

```js
AppServerManager.getInstance().removeReceivedUpstreamMsgListener(listnener);
```