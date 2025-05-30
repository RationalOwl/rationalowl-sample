//
//  ViewController.swift
//  hello
//
//  Created by Mac on 2023/12/21.
//

import UIKit;

import RationalOwl;

class ViewController: UIViewController , DeviceRegisterResultDelegate, MessageDelegate {
    
    

    override func viewDidLoad() {
        super.viewDidLoad()
        
        // set device app register delegate
        let mgr: MinervaManager = MinervaManager.getInstance();
        mgr.setDeviceRegisterResultDelegate(self);
        
        // set message delegate
        mgr.setMessageDelegate(self);
    }


    @IBAction func btnReg(_ sender: Any) {
        let gateHost: String = "gate.rationalowl.com";  // cloud
        // let gateHost: String = "211.239.150.123";
        let serviceId: String = "SVC871d16c3-fe28-4f09-ac32-4870d171b067";
        let mgr: MinervaManager = MinervaManager.getInstance();
        mgr.registerDevice(gateHost, serviceId: serviceId, deviceRegName: "my Ios app");
    }
    
    
    @IBAction func btnUnreg(_ sender: Any) {
        let serviceId: String = "SVC871d16c3-fe28-4f09-ac32-4870d171b067";
        let mgr: MinervaManager = MinervaManager.getInstance();
        mgr.unregisterDevice(serviceId);
    }
    
    
    /////////////////////////////////////////////////////////////////
    // device register delegate
    /////////////////////////////////////////////////////////////////
    
    func onRegisterResult(_ resultCode: Int32, resultMsg: String!, deviceRegId: String!) {
        print("onRegisterResult resultCode = \(resultCode) resultMsg = \(String(describing: resultMsg)) deviceRegId = \(String(describing: deviceRegId))")
        
        // device app registration success!
        // send deviceRegId to the app server.
        if(resultCode == RESULT_OK) {
                        
        }
    }
    
    func onUnregisterResult(_ resultCode: Int32, resultMsg: String!) {
        print("onUnregisterResult resultCode = \(resultCode) resultMsg = \(String(describing: resultMsg))")
    }
    
    
    /////////////////////////////////////////////////////////////////
    // message delegate
    /////////////////////////////////////////////////////////////////
    
    //realtime downstream received.
    func onDownstreamMsgRecieved(_ msgList: [Any]!) {
        // hello world app don't treat realtime message
        print("onDownstreamMsgRecieved");
    }
    
    // realtime p2p received
    func onP2PMsgRecieved(_ msgList: [Any]!) {
        // hello world app don't treat realtime message
        print("onP2PMsgRecieved");
    }
    
    // while app running this callback called when push arried,
    // If there's some un-delivered push message exist, this callback delivered un-delivered push list.
    func onPushMsgRecieved(_ msgSize: Int32, msgList: [Any]!, alarmIdx: Int32) {
        print("onPushMsgRecieved msg size = \(msgSize)")
        var msg: Dictionary<String, Any>;
        // msg send time
        var sendTime: Double;
        // msgData sould be simple string or dictionary(json string on custom push)
        var msgData: Any?;
        let dateFormatter = DateFormatter();
        dateFormatter.dateFormat = "yyyy/MM/dd HH:mm:ss";
        
        for i in 0..<msgList.count {
            msg = msgList[i] as! Dictionary<String, Any>;
            // message sent time
            sendTime = msg["serverTime"] as! Double;
            let date = Date(timeIntervalSince1970: sendTime/1000);
            msgData = msg["data"];
            let customPushStr: String = (String(describing: msgData));
            print("push msg = \(customPushStr)")
        }
    }
    
    
    func onUpstreamMsgResult(_ resultCode: Int32, resultMsg: String!, msgId: String!) {
        print("onMsgRecieved msg upstream message id = " + msgId);
    }
    
    
    func onP2PMsgResult(_ resultCode: Int32, resultMsg: String!, msgId: String!) {
        print("onMsgRecieved msg p2p message id = " + msgId);
    }
}

