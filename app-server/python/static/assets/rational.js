$(function () {
    class RationalMessage {
        constructor(action, body) {
            this.action = action;
            this.body = body;
        }

        toJsonString() {
            return JSON.stringify({ action: this.action, body: this.body });
        }
    }
    var ws_scheme = window.location.protocol == "https:" ? "wss" : "ws";
    var chatsock = new ReconnectingWebSocket(ws_scheme + '://' + window.location.host);

    const onMessageBuffer = [];

    chatsock.onopen = function (message) {
        console.log('[onopen]');
    };
    chatsock.onmessage = function (message) {
        console.log('[onmessage]', message.data);
        onMessageBuffer.push(message.data);
        $("#receivedMessage").append(`<tr><td>${message.data}</td></tr>`);
    };



    $("#registerAppServer").on("submit", function (event) {
        const message = {
            serviceId: $('#serviceId').val(),
            regName: $('#regName').val(),
        }
        chatsock.send(new RationalMessage('registerAppServer', message).toJsonString());
        return false;
    });
    $("#createDeviceGroup").on("submit", function (event) {
        const message = {
            groupName: $('#groupName').val(),
            groupDesc: $('#groupDesc').val(),
            deviceList: eval($('#creatDeviceList').val()),
        }
        chatsock.send(new RationalMessage('createDeviceGroup', message).toJsonString());
        return false;
    });
    $("#addDeviceGroup").on("submit", function (event) {
        const message = {
            groupId: $('#addGroupId').val(),
            deviceList: eval($('#addDeviceList').val()),
        }
        chatsock.send(new RationalMessage('addDeviceGroup', message).toJsonString());
        return false;
    });
    $("#subtractDeviceGroup").on("submit", function (event) {
        const message = {
            groupId: $('#subGroupId').val(),
            deviceList: eval($('#subDeviceList').val()),
        }
        chatsock.send(new RationalMessage('subtractDeviceGroup', message).toJsonString());
        return false;
    });
    $("#deleteDeviceGroup").on("submit", function (event) {
        const message = {
            groupId: $('#deleteGroupId').val(),
        }
        chatsock.send(new RationalMessage('deleteDeviceGroup', message).toJsonString());
        return false;
    });
    $("#sendMulticastMsg").on("submit", function (event) {
        const message = {
            message: $('#multicastMessage').val(),
            deviceList: eval($('#multicastDeviceList').val()),
        }
        chatsock.send(new RationalMessage('sendMulticastMsg', message).toJsonString());
        return false;
    });
    $("#sendBroadcastMsg").on("submit", function (event) {
        const message = {
            message: $('#boardcastMessage').val(),
        }
        chatsock.send(new RationalMessage('sendBroadcastMsg', message).toJsonString());
        return false;
    });
    $("#sendGroupMsg").on("submit", function (event) {
        const message = {
            message: $('#groupMessage').val(),
            groupId: $('#groupGroupId').val(),
        }
        chatsock.send(new RationalMessage('sendGroupMsg', message).toJsonString());
        return false;
    });


});