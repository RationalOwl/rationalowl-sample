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
        let gateHost: String = "211.239.150.113";
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
        print("onRegisterResult resultCode = \(resultCode) resultMsg = \(resultMsg) deviceRegId = \(deviceRegId)")
        
        // device app registration success!
        // send deviceRegId to the app server.
        if(resultCode == RESULT_OK) {
            let mgr: MinervaManager = MinervaManager.getInstance();
            mgr.sendUpstreamMsg("send deviceRegId to the app server", serverRegId: "app server reg id");
            
        }
    }
    
    func onUnregisterResult(_ resultCode: Int32, resultMsg: String!) {
        print("onUnregisterResult resultCode = \(resultCode) resultMsg = \(resultMsg)")
    }
    
    
    /////////////////////////////////////////////////////////////////
    // message delegate
    /////////////////////////////////////////////////////////////////
    
    func onDownstreamMsgRecieved(_ msgSize: Int32, msgList: [Any]!, alarmIdx: Int32) {
        print("onMsgRecieved msg size = \(msgSize)")
        var msg: Dictionary<String, Any>;
        // downstream msg type
        // 1(realtime downstream),   3[custom push: 1) un-delivered push msg list delivered after app launch, 2) push while app fore-ground]
        var msgType: Int;
        // sender's registration id
        var sender: String;
        // msgData sould be simple string or dictionary(json string on custom push)
        var msgData: Any?;
        let dateFormatter = DateFormatter();
        dateFormatter.dateFormat = "yyyy/MM/dd HH:mm:ss";
        
        for i in 0..<msgList.count {
            msg = msgList[i] as! Dictionary<String, Any>;
            // 1(realtime downstream),  3[custom push: 1) un-delivered push while app launch, 2) push while app fore-ground]
            msgType = msg["type"] as! Int;
            // message sender(app server)'s app server registraion id
            sender = msg["sender"] as! String;
            msgData = msg["data"];
            
            switch (msgType) {
                // realtime downstream received
                case 1:
                    // hello app don't use realtime msg
                    break;
                
                // realtime p2p msg received
                case 2:
                    // hello app don't use realtime msg
                    break;
                
                // custom push received
                case 3:
                    // When push received while app foreground, simplay print msg.
                    let customPushStr: String = (String(describing: msgData));
                    // custom push format can be any fields app need.
                    // RationalUms Demo format
                    /*
                    {
                        "mId": "message id here",
                        "title": "message title here",
                        "body": "message body here",
                        "ii": "image id here"
                        "st": "(message) send time"
                        }
                    */
                    // hello world simply print custom push string.
                    print("push msg = \(customPushStr)")
                    break;
                
                default:
                    break;
                
            }
        }
    }
    
    
    func onP2PMsgRecieved(_ msgSize: Int32, msgList: [Any]!, alarmIdx: Int32) {
        print("onMsgRecieved msg size = \(msgSize)");
    }
    
    
    func onUpstreamMsgResult(_ resultCode: Int32, resultMsg: String!, msgId: String!) {
        print("onMsgRecieved msg upstream message id = " + msgId);
    }
    
    
    func onP2PMsgResult(_ resultCode: Int32, resultMsg: String!, msgId: String!) {
        print("onMsgRecieved msg p2p message id = " + msgId);
    }
}

