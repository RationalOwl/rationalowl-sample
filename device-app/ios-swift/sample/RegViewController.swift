//
//  RegViewController.swift
//  sample
//
//  Created by 김정도 on 2018. 1. 27..
//  Copyright © 2018년 Rationalowl. All rights reserved.
//

import UIKit;

import RationalOwl;



class RegViewController: UIViewController, DeviceRegisterResultDelegate {
    
    
    
    @IBOutlet weak var regDeviceView: UIView!;
    @IBOutlet weak var inputNameField: UITextField!;
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        //inputNameField.text = "gate.rationalowl.com";
        inputNameField.text = "211.239.150.123"; // aws dev env
        
        
        let minMgr: MinervaManager = MinervaManager.getInstance();
        minMgr.setDeviceRegisterResultDelegate(self);
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    /////////////////////////////////////////////////////////////////
    // IBActions
    /////////////////////////////////////////////////////////////////
    
    @IBAction func regDevice() {
        let serviceId: String = "SVCea9fe858-a49e-4e73-a42a-55668f825e0b";
        let gateHost: String = inputNameField.text!;
        let mgr: MinervaManager = MinervaManager.getInstance();
        mgr.registerDevice(gateHost, serviceId: serviceId, deviceRegName: "my I phone");
    }
    
    
    @IBAction func unregDevice() {
        let serviceId: String = "SVCea9fe858-a49e-4e73-a42a-55668f825e0b";
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
}

