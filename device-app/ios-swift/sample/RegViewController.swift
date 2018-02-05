//
//  RegViewController.swift
//  sample
//
//  Created by 김정도 on 2018. 1. 27..
//  Copyright © 2018년 Rationalowl. All rights reserved.
//

import UIKit

class RegViewController: UIViewController, DeviceRegisterResultDelegate {
    
    
    
    @IBOutlet weak var regDeviceView: UIView!;
    @IBOutlet weak var inputNameField: UITextField!;
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    /////////////////////////////////////////////////////////////////
    // IBActions
    /////////////////////////////////////////////////////////////////
    
    @IBAction func regDevice() {
        let serviceId: String = "faebcfe844d54d449136491fb253619d";
        let gateHost: String = inputNameField.text!;
        let mgr: MinervaManager = MinervaManager.getInstance();
        mgr.registerDevice(gateHost, serviceId: serviceId, deviceRegName: "my I phone");
    }
    
    
    @IBAction func unregDevice() {
        let serviceId: String = "faebcfe844d54d449136491fb253619d";
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
            //let mgr: MinervaManager = MinervaManager.getInstance();
            //mgr.sendUpstreamMsg("send deviceRegId to the app server", serverRegId: "app server reg id");
            
        }
    }
    
    func onUnregisterResult(_ resultCode: Int32, resultMsg: String!) {
        print("onUnregisterResult resultCode = \(resultCode) resultMsg = \(resultMsg)")
    }
}

