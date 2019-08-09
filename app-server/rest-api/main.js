let channelUrl = null;

const registerServerRun = () => {
    const serviceId = document.getElementById('register-service-id').value;
    const registerName = document.getElementById('register-name').value;
    const response = sendHttpRequest('http://gate.rationalowl.com:8006/server/register/', {
        serviceId, registerName
    });
    channelUrl = JSON.parse(response).channelServer;
};

const unregisterServerRun = () => {
    const serviceId = document.getElementById('unregister-service-id').value;
    const serverRegId = document.getElementById('unregister-id').value;
    sendHttpRequest('http://gate.rationalowl.com:8006/server/unregister/', {
        serviceId, serverRegId
    });
};

////////////////////////////////////////////
// send realtime data 
////////////////////////////////////////////

const multicastRun = () => {
    const serviceId = document.getElementById('multicast-service-id').value;
    const serverRegId = document.getElementById('multicast-register-id').value;
    const queuing = Number(document.getElementById('multicast-queueing').value);
    const data = document.getElementById('multicast-data').value;
    const deviceRegIds = document.getElementById('multicast-devices').value.replace(/\s/g, '').split(/,/g);
    const notiTitle = document.getElementById('multicast-noti-title').value;
    const notiBody = document.getElementById('multicast-noti-body').value;
    const notiSound = document.getElementById('multicast-noti-sound').value;

    sendHttpRequest(`http://${channelUrl}/downstream/multicast/`, {
        serviceId, serverRegId, queuing, data, deviceRegIds, notiTitle, notiBody, notiSound
    });
};

const broadcastRun = () => {
    const serviceId = document.getElementById('broadcast-service-id').value;[]
    const serverRegId = document.getElementById('broadcast-register-id').value;
    const queuing = Number(document.getElementById('broadcast-queueing').value);
    const data = document.getElementById('broadcast-data').value;
    const notiTitle = document.getElementById('broadcast-noti-title').value;
    const notiBody = document.getElementById('broadcast-noti-body').value;
    const notiSound = document.getElementById('broadcast-noti-sound').value;

    sendHttpRequest(`http://${channelUrl}/downstream/broadcast/`, {
        serviceId, serverRegId, queuing, data, notiTitle, notiBody, notiSound
    });
};

const groupmsgRun = () => {
    const serviceId = document.getElementById('groupmsg-service-id').value;
    const serverRegId = document.getElementById('groupmsg-register-id').value;
    const queuing = Number(document.getElementById('groupmsg-queueing').value);
    const data = document.getElementById('groupmsg-data').value;
    const groupId = document.getElementById('groupmsg-group-id').value;
    const notiTitle = document.getElementById('groupmsg-noti-title').value;
    const notiBody = document.getElementById('groupmsg-noti-body').value;
    const notiSound = document.getElementById('groupmsg-noti-sound').value;

    sendHttpRequest(`http://${channelUrl}/downstream/group/`, {
        serviceId, serverRegId, queuing, data, groupId, notiTitle, notiBody, notiSound
    });
};


////////////////////////////////////////////
// send custom push
////////////////////////////////////////////
const multicastPushRun = () => {
    const serviceId = document.getElementById('multicast-push-service-id').value;
    const serverRegId = document.getElementById('multicast-push-register-id').value;
    const deviceRegIds = document.getElementById('multicast-push-devices').value.replace(/\s/g, '').split(/,/g);
    const encrypt = Number(document.getElementById('multicast-push-encrypt').value);
    const customField1 = document.getElementById('multicast-push-custom-field1').value;
    const customField2 = document.getElementById('multicast-push-custom-field2').value;
    const customField3 = document.getElementById('multicast-push-custom-field3').value;

    const data = {customField1, customField2, customField3};

    sendHttpRequest(`http://${channelUrl}/custompush/multicast/`, {
        serviceId, serverRegId, deviceRegIds, encrypt, data
    });
};

const broadcastPushRun = () => {
    const serviceId = document.getElementById('broadcast-service-id').value;[]
    const serverRegId = document.getElementById('broadcast-register-id').value;
    const encrypt = Number(document.getElementById('broadcast-push-encrypt').value);
    const customField1 = document.getElementById('broadcast-push-custom-field1').value;
    const customField2 = document.getElementById('broadcast-push-custom-field2').value;
    const customField3 = document.getElementById('broadcast-push-custom-field3').value;

    const data = {customField1, customField2, customField3};

    sendHttpRequest(`http://${channelUrl}/custompush/broadcast/`, {
        serviceId, serverRegId, encrypt, data
    });
};

const groupmsgPushRun = () => {
    const serviceId = document.getElementById('group-push-service-id').value;
    const serverRegId = document.getElementById('group-push-register-id').value;
    const groupId = document.getElementById('group-push-group-id').value;
    const encrypt = Number(document.getElementById('group-push-encrypt').value);
    const customField1 = document.getElementById('group-push-custom-field1').value;
    const customField2 = document.getElementById('group-push-custom-field2').value;
    const customField3 = document.getElementById('group-push-custom-field3').value;

    const data = {customField1, customField2, customField3};

    sendHttpRequest(`http://${channelUrl}/custompush/group/`, {
        serviceId, serverRegId, groupId, encrypt, data
    });
};

const retryPushRun = () => {
    const serviceId = document.getElementById('retry-push-service-id').value;
    const serverRegId = document.getElementById('retry-push-register-id').value;
    const msgId = document.getElementById('retry-push-msg-id').value;
    const retryCondition = Number(document.getElementById('retry-condition').value);

    sendHttpRequest(`http://${channelUrl}/custompush/retry/`, {
        serviceId, serverRegId, msgId, retryCondition
    });
};


////////////////////////////////////////////
// device group management
////////////////////////////////////////////

const deviceGroupCreateRun = () => {
    const serviceId = document.getElementById('device-group-create-service-id').value;
    const serverRegId = document.getElementById('device-group-create-register-id').value;
    const groupName = document.getElementById('device-group-create-name').value;
    const deviceList = document.getElementById('device-group-create-devices').value.replace(/\s/g, '').split(/,/g);
    const description = document.getElementById('device-group-create-description').value;

    sendHttpRequest(`http://${channelUrl}/deviceGroup/create/`, {
        serviceId, serverRegId, groupName, deviceList, description
    });
};

const deviceGroupAddRun = () => {
    const serviceId = document.getElementById('device-group-add-service-id').value;
    const serverRegId = document.getElementById('device-group-add-register-id').value;
    const groupId = document.getElementById('device-group-add-id').value;
    const deviceList = document.getElementById('device-group-add-devices').value.replace(/\s/g, '').split(/,/g);

    sendHttpRequest(`http://${channelUrl}/deviceGroup/add/`, {
        serviceId, serverRegId, groupId, deviceList
    });
};

const deviceGroupSubtractRun = () => {
    const serviceId = document.getElementById('device-group-subtract-service-id').value;
    const serverRegId = document.getElementById('device-group-subtract-register-id').value;
    const groupId = document.getElementById('device-group-subtract-id').value;
    const deviceList = document.getElementById('device-group-subtract-devices').value.replace(/\s/g, '').split(/,/g);

    sendHttpRequest(`http://${channelUrl}/deviceGroup/subtract/`, {
        serviceId, serverRegId, groupId, deviceList
    });
};

const deviceGroupRemoveRun = () => {
    const serviceId = document.getElementById('device-group-remove-service-id').value;
    const serverRegId = document.getElementById('device-group-remove-register-id').value;
    const groupId = document.getElementById('device-group-remove-id').value;

    sendHttpRequest(`http://${channelUrl}/deviceGroup/remove/`, {
        serviceId, serverRegId, groupId
    });
};

const sendHttpRequest = (url, data) => {
    console.log(JSON.stringify(data));    

    const request = new XMLHttpRequest();
    request.open("POST", url, false);
    request.setRequestHeader("Content-Type", "application/json");
    request.send(JSON.stringify(data));    
    document.getElementById('response-box').style.visibility = 'visible';
    document.getElementById('response-status').innerText = request.statusText;
    document.getElementById('response-body').innerText = request.responseText;

    return request.responseText;
};