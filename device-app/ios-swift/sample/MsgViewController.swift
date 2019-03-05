//
//  MsgViewController.swift
//  sample
//
//  Created by 김정도 on 2018. 1. 26..
//  Copyright © 2018년 Rationalowl. All rights reserved.
//

import UIKit;



class MsgViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, MessageDelegate {
   
    
    @IBOutlet weak var msgListView: UIView!;
    @IBOutlet weak var inputMessageField: UITextField!
    @IBOutlet weak var tVew: UITableView!;
    
    var messages: Array<String>!;
    
    override func viewDidLoad() {
        super.viewDidLoad();
        
        messages = Array <String>();
        
        tVew.delegate = self;
        tVew.dataSource = self;
        
        // set message delegate
        let mgr: MinervaManager = MinervaManager.getInstance();
        mgr.setMessageDelegate(self);
        
        // Do any additional setup after loading the view, typically from a nib.
    }
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning();
        // Dispose of any resources that can be recreated.
    }
    
    /////////////////////////////////////////////////////////////////
    // IBActions
    /////////////////////////////////////////////////////////////////
    
    @IBAction func sendUpstreamMsg() {
        let serverId: String = "e2c5f56d6cda41279c3a4ad8549052bf";
        let msg: String = inputMessageField.text!;
        let mgr: MinervaManager = MinervaManager.getInstance();
        mgr.sendUpstreamMsg(msg, serverRegId:serverId);
        
        let displayStr: String = "=>\(msg)";
        self.messages.append(displayStr);
        self.tVew.reloadData();
        
        let topIndexPath = IndexPath(row: messages.count-1, section: 0)
        self.tVew.scrollToRow(at: topIndexPath, at: UITableView.ScrollPosition.middle, animated: true);
    }
    
    
    @IBAction func sendP2PstreamMsg() {
        let msg: String = inputMessageField.text!;
        let devices: Array<String> = ["a03e1768d6ab411b930eb6dd6b5048b4"];
        let mgr: MinervaManager = MinervaManager.getInstance();
        mgr.sendP2PMsg(msg, devices:devices);
        
        let displayStr: String = "=>(p2p)\(msg)";
        self.messages.append(displayStr);
        self.tVew.reloadData();
        let topIndexPath = IndexPath(row: messages.count-1, section: 0)
        self.tVew.scrollToRow(at: topIndexPath, at: UITableView.ScrollPosition.middle, animated: true);
    }
    
    
    /////////////////////////////////////////////////////////////////
    // message delegate
    /////////////////////////////////////////////////////////////////
    
    func onDownstreamMsgRecieved(_ msgSize: Int32, msgList: [Any]!, alarmIdx: Int32) {
        print("onMsgRecieved msg size = \(msgSize)")
        var msg: Dictionary<String, Any>;
        // who send message
        var sender: String;
        var sendTime: Double;
        var msgData: String;
        let dateFormatter = DateFormatter();
        dateFormatter.dateFormat = "yyyy/MM/dd HH:mm:ss";
        
        for i in 0..<msgList.count {
            msg = msgList[i] as! Dictionary<String, Any>;
            // message sender(app server)'s app server registraion id
            sender = msg["sender"] as! String;
            // message sent time
            sendTime = msg["serverTime"] as! Double;
            let date = Date(timeIntervalSince1970: sendTime/1000);
            msgData = msg["data"] as! String;
            let displayStr: String = "\(msgData)   \(dateFormatter.string(from: date))";
            self.messages.append(displayStr);
        }
        
        self.tVew.reloadData();
        let topIndexPath = IndexPath(row: messages.count-1, section: 0)
        self.tVew.scrollToRow(at: topIndexPath, at: UITableView.ScrollPosition.middle, animated: true);
    }
    
    
    func onP2PMsgRecieved(_ msgSize: Int32, msgList: [Any]!, alarmIdx: Int32) {
        print("onMsgRecieved msg size = \(msgSize)")
        var msg: Dictionary<String, Any>;
        var sender: String;
        var sendTime: Double;
        var msgData: String;
        let dateFormatter = DateFormatter();
        dateFormatter.dateFormat = "yyyy/MM/dd HH:mm:ss";
        
        for i in 0..<msgList.count {
            msg = msgList[i] as! Dictionary<String, Any>;
            // message sender(device app)'s device app registraion id
            sender = msg["sender"] as! String;
            // message sent time
            sendTime = msg["serverTime"] as! Double;
            let date = Date(timeIntervalSince1970: sendTime/1000);
            msgData = msg["data"] as! String;
            let displayStr: String = "p2p:\(msgData)   \(dateFormatter.string(from: date))";
            self.messages.append(displayStr);
        }
        
        self.tVew.reloadData();
        let topIndexPath = IndexPath(row: messages.count-1, section: 0)
        self.tVew.scrollToRow(at: topIndexPath, at: UITableView.ScrollPosition.middle, animated: true);
    }
    
    
    func onUpstreamMsgResult(_ resultCode: Int32, resultMsg: String!, umi: String!) {
        print("onMsgRecieved msg upstream message id = \(umi)")
    }
    
    
    func onP2PMsgResult(_ resultCode: Int32, resultMsg: String!, pmi: String!) {
        print("onMsgRecieved msg p2p message id = \(pmi)")
    }
    
    
    /////////////////////////////////////////////////////////////////
    // UITableViewDataSource
    /////////////////////////////////////////////////////////////////
    
    func numberOfSectionsInTableView(in tableView: UITableView) -> Int {
        return 1
    }
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return messages.count;
    }
    
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let str: String = messages[indexPath.row];
        //let str: String = messages.object(at: indexPath.row) as! String;
        let cell: UITableViewCell = UITableViewCell(style: UITableViewCell.CellStyle.subtitle, reuseIdentifier: "Cell");
        //let cell: UITableViewCell = tableView.dequeueReusableCell(withIdentifier: "ChatCellIdentifier")!;
        cell.textLabel?.text = str;
        return cell;
    }
}

