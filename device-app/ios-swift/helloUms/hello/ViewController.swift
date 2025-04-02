//
//  ViewController.swift
//  hello
//
//  Created by Mac on 2023/12/21.
//

import UIKit;

import RationalOwl;



class ViewController: UIViewController , DeviceRegisterResultDelegate, MessageDelegate {
    
    var mDeviceRegId: String?
    

    override func viewDidLoad() {
        super.viewDidLoad()
        
        // set device app register delegate
        let mgr: MinervaManager = MinervaManager.getInstance();
        mgr.setDeviceRegisterResultDelegate(self);
        
        // set message delegate
        mgr.setMessageDelegate(self);
    }


    @IBAction func btnReg(_ sender: Any) {
        //let gateHost: String = "gate.rationalowl.com";  // cloud
        let gateHost: String = "dev.rationalowl.com";
        let serviceId: String = "adbce9791c2c46cca10aa527a86dd6e7";
        let mgr: MinervaManager = MinervaManager.getInstance();
        mgr.registerDevice(gateHost, serviceId: serviceId, deviceRegName: "my Ios helloUms app");
    }
    
    
    @IBAction func btnUnreg(_ sender: Any) {
        let serviceId: String = "adbce9791c2c46cca10aa527a86dd6e7";
        let mgr: MinervaManager = MinervaManager.getInstance();
        mgr.unregisterDevice(serviceId);
    }
    
    
    /////////////////////////////////////////////////////////////////
    // device register delegate
    /////////////////////////////////////////////////////////////////
    
    
    func onRegisterResult(_ resultCode: Int32, resultMsg: String!, deviceRegId: String!) {
        print("onRegisterResult \(resultCode)")
        let msg = "\(resultMsg) registration id: \(deviceRegId)"
        print(msg)
        
        // registration has completed successfully!
        if resultCode == RESULT_OK || resultCode == RESULT_DEVICE_ALREADY_REGISTERED {
            print("rationalOwl register success!!!")
            
            // save deviceRegId to local file
            // and send deviceRegId to app server
            let fDeviceRegId = deviceRegId
            
            // call rationalums rest api
            UmsApi.callInstallUmsApp(accountId: "923a0aac-abf4-493d-9f9d-787569ac53bc", deviceRegId: deviceRegId, phoneNum: nil, appUserId: nil, name: nil) { data, response, error in
                
                if let error = error {
                    print("Error Occurred: \(error)")
                    return
                }
                
                print(NSString(data: data!, encoding: NSUTF8StringEncoding))
                
                guard let data = data else { return }
                do {
                    let res = try JSONDecoder().decode(PushAppProto.PushAppInstallRes.self, from: data)
                    
                    if res.rc == RESULT_OK {
                        self.mDeviceRegId = fDeviceRegId
                        print("단말앱 등록 성공")
                    }
                    else {
                        print("resCode: \(res.rc) comment: \(res.cmt)")
                    }
                }
                catch {
                    print("Error decoding response: \(error)")
                }
            }
        }
        else {
            // registration error has occurred!
            print("단말앱 등록 에러: \(String(describing: resultMsg))")
        }
    }
    
    func onUnregisterResult(_ resultCode: Int32, resultMsg: String!) {
        // rationalowl unregistration has completed successfully!
        if resultCode == RESULT_OK {
            // call rationalums rest api
            UmsApi.callUnregisterUmsApp(accountId: "923a0aac-abf4-493d-9f9d-787569ac53bc", deviceRegId: mDeviceRegId!) { data, response, error in
                
                if let error = error {
                    print("Error Occurred: \(error)")
                    return
                }
                
                guard let data = data else { return }
                do {
                    let res = try JSONDecoder().decode(PushAppProto.PushAppUnregUserRes.self, from: data)
                    
                    if res.rc == RESULT_OK {
                        self.mDeviceRegId = nil
                        print("단말앱 해제 성공")
                    }
                    else {
                        print("resCode: \(res.rc) comment: \(res.cmt)")
                    }
                }
                catch {
                    print("Error decoding response: \(error)")
                }
            }
        }
        else {
            // registration error has occurred!
            print("단말앱 해제 에러: \(String(describing: resultMsg))")
        }
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

