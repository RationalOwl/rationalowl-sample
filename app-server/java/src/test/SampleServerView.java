
package test;


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;


import com.rationalowl.minerva.appserver.AppServerManager;
import com.rationalowl.minerva.appserver.AppServerRegisterResultListener;
import com.rationalowl.minerva.appserver.DeviceGroupListener;
import com.rationalowl.minerva.appserver.MessageListener;
import com.rationalowl.minerva.appserver.Result;


public class SampleServerView extends JFrame implements ActionListener {


    class SimpleRegisterResultListener implements AppServerRegisterResultListener {

        public void onRegisterResult(int resultCode, String resultMsg, String appServerRegId, String appServerRegName) {
            output("RegisterResult:" + resultMsg + "  reg server id = " + appServerRegId + " reg server name = " + appServerRegName);

            if (resultCode == Result.RESULT_OK || resultCode == Result.RESULT_SERVER_REGNAME_ALREADY_REGISTERED) {
                // should save and manage mServerRegName and mServerRegId to the file or db.
            }
        }


        public void onUnregisterResult(int resultCode, String resultMsg) {
            output("UnregisterResult:" + resultMsg);
        }
    }


    class SimpleMessageListener implements MessageListener {

        @Override
        public void onUpstreamMsgReceived(String sender, long sendTime, String msg) {
            output("onUpstreamMsgReceived sender device reg id : " + sender + "send time:" + sendTime + " msg : " + msg);
        }


        @Override
        public void onSendMulticastMsgResult(int resultCode, String resultMsg, String msgId, String requestId) {
            output("onSendMulticastMsgResult: resultCode = " + resultCode + "resultMsg = " + resultMsg + " msgId="+ msgId);
        }


        @Override
        public void onSendBroadcastMsgResult(int resultCode, String resultMsg, String msgId, String requestId) {
            output("onSendBroadcastMsgResult: resultCode = " + resultCode + "resultMsg = " + resultMsg + " msgId="+ msgId);
        }


        @Override
        public void onSendGroupMsgResult(int resultCode, String resultMsg, String msgId, String requestId) {
            output("onSendGroupMsgResult: resultCode = " + resultCode + "resultMsg = " + resultMsg + " msgId="+ msgId);
        }


        @Override
        public void onSendMulticastCustomPushResult(int resultCode, String resultMsg, String msgId, String requestId) {
            output("onSendMulticastCustomPushResult: resultCode = " + resultCode + "resultMsg = " + resultMsg + " msgId="+ msgId);
        }

        @Override
        public void onSendBroadcastCustomPushResult(int resultCode, String resultMsg, String msgId, String requestId) {
            output("onSendBroadcastCustomPushResult: resultCode = "  + resultCode + "resultMsg = " + resultMsg + " msgId="+ msgId);
        }

        @Override
        public void onSendGroupCustomPushResult(int resultCode, String resultMsg, String msgId, String requestId) {
            output("onSendGroupCustomPushResult: resultCode = " + resultCode + "resultMsg = " + resultMsg + " msgId="+ msgId);
        }
    }


    class SimpleDeviceGrpListener implements DeviceGroupListener {

        public void onDeviceGroupCreateResult(int resultCode, String resultMsg, String deviceGrpId, String deviceGrpName, int deviceSize, String desc, ArrayList<String> failedDevices, String requestId) {
            output("onDeviceGroupCreateResult: resultCode = " + resultCode + "resultMsg = " + resultMsg + " requestId="+ requestId);

            if (resultCode == Result.RESULT_OK) {
                // should save and manage deviceGrpId
                output("deviceGrpName =" + deviceGrpName +" deviceGrpId =" + deviceGrpId);
            }
        }


        public void onDeviceGroupAddResult(int resultCode, String resultMsg, String deviceGrpId, int totalDeviceSize, int addedDeviceSize, ArrayList<String> failedDevices, String requestId) {
            output("onDeviceGroupAddResult: resultCode = " + resultCode + "resultMsg = " + resultMsg + " requestId="+ requestId);
        }


        public void onDeviceGroupSubtractResult(int resultCode, String resultMsg, String deviceGrpId, int totalDeviceSize, int subtractDeviceSize, ArrayList<String> failedDevices, String requestId) {
            output("onDeviceGroupSubtractResult: resultCode = " + resultCode + "resultMsg = " + resultMsg + " requestId="+ requestId);
        }


        public void onDeviceGroupDeleteResult(int resultCode, String resultMsg, String deviceGrpId, String requestId) {
            output("onDeviceGroupDeleteResult: resultCode = " + resultCode + "resultMsg = " + resultMsg + " requestId="+ requestId);
        }
    }

    private static final long serialVersionUID = 1L;

    private JTextArea display;

    private JScrollPane displayScroll;

    // reg/unreg buttons
    private JButton regBtn, unRegBtn;

    // device group management buttons
    private JButton createGrpBtn, addToGrpBtn, subtractGrpBtn, deleteGrpBtn;

    // message buttons
    private JButton sendMulticastMsgBtn, sendBroadBtn, sendGrpBtn, sendMulticastPushBtn, sendBroadPushBtn, sendGrpPushBtn;
    private JButton clearBtn;
    JTextField msgField;


    public SampleServerView() {
        super("simple App server");

        // set simple listeners
        AppServerManager serverMgr = AppServerManager.getInstance();
        serverMgr.setRegisterResultListener(new SimpleRegisterResultListener());
        serverMgr.setMsgListener(new SimpleMessageListener());
        serverMgr.setDeviceGroupListener(new SimpleDeviceGrpListener());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        display = new JTextArea();
        displayScroll = new JScrollPane(display);
        DefaultCaret caret = (DefaultCaret) display.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        mainPanel.add(displayScroll, BorderLayout.CENTER);

        JPanel msgPanel = new JPanel();
        msgPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel msgLabel = new JLabel("message");
        msgField = new JTextField(30);
        msgPanel.add(msgLabel);
        msgPanel.add(msgField);

        JPanel urlPanel = new JPanel();
        urlPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        clearBtn = new JButton("clear");
        clearBtn.addActionListener(this);
        urlPanel.add(clearBtn);
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        northPanel.add(msgPanel);
        northPanel.add(Box.createVerticalStrut(2));
        northPanel.add(urlPanel);

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        regBtn = new JButton("register");
        regBtn.addActionListener(this);
        unRegBtn = new JButton("un-register");
        unRegBtn.addActionListener(this);

        // device group management
        createGrpBtn = new JButton("create device group");
        createGrpBtn.addActionListener(this);
        addToGrpBtn = new JButton("add devices to group");
        addToGrpBtn.addActionListener(this);
        subtractGrpBtn = new JButton("remove devices from group");
        subtractGrpBtn.addActionListener(this);
        deleteGrpBtn = new JButton("delete device group");
        deleteGrpBtn.addActionListener(this);

        sendMulticastMsgBtn = new JButton("send multicast");
        sendMulticastMsgBtn.addActionListener(this);
        sendBroadBtn = new JButton("send broadcast");
        sendBroadBtn.addActionListener(this);
        sendGrpBtn = new JButton("send group message");
        sendGrpBtn.addActionListener(this);
        
        sendMulticastPushBtn = new JButton("multicast 푸시");
        sendMulticastPushBtn.addActionListener(this);
        sendBroadPushBtn = new JButton("broadcast 푸시");
        sendBroadPushBtn.addActionListener(this);

        sendGrpPushBtn = new JButton("그룹 푸시");
        sendGrpPushBtn.addActionListener(this);

        btnPanel.add(regBtn);
        btnPanel.add(unRegBtn);
        btnPanel.add(createGrpBtn);
        btnPanel.add(addToGrpBtn);
        btnPanel.add(subtractGrpBtn);
        btnPanel.add(deleteGrpBtn);
        btnPanel.add(sendMulticastMsgBtn);
        btnPanel.add(sendBroadBtn);
        btnPanel.add(sendGrpBtn);
        btnPanel.add(sendMulticastPushBtn);
        btnPanel.add(sendBroadPushBtn);
        btnPanel.add(sendGrpPushBtn);
        getContentPane().add(northPanel, BorderLayout.NORTH);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(btnPanel, BorderLayout.SOUTH);
        setSize(1800, 700);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setVisible(true);
    }


    public void output(final String s) {

        display.append(s + "\n");
    }


    public void clearText() {
        display.setText("");
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        // app server reg/unreg
        if (src == regBtn) {
            regServer();
        }
        else if (src == unRegBtn) {
            unregServer();
        }

        // send message
        else if (src == sendMulticastMsgBtn) {
            sendMulticastMsg();
        }
        else if (src == sendBroadBtn) {
            sendBroadcastMsg();
        }
        else if (src == sendGrpBtn) {
            sendGrpMsg();
        }
        
        //send custom push
        else if (e.getSource() == sendMulticastPushBtn) {
            sendMulticastPush();
        }
        else if (e.getSource() == sendBroadPushBtn) {
            sendBroadcastPush();
        }       
        else if (e.getSource() == sendGrpPushBtn) {
            sendGrpMsgPush();
        }   
        
        // device group mananement
        else if (src == createGrpBtn) {
            createDeviceGroup();
        }
        else if (src == addToGrpBtn) {
            addDevicesToGroup();
        }
        else if (src == subtractGrpBtn) {
            subtractDevicesFromGroup();
        }
        else if (src == deleteGrpBtn) {
            deleteGroup();
        }
        
        else if (src == clearBtn) {
            clearText();
        }
    }


    private void regServer() {
        AppServerManager serverMgr = AppServerManager.getInstance();
        // edit service id as yours
        //String serviceId = "9bd4db31dbaa4897ad9aa81c3e7e183a"; // cloud
        String serviceId = "afab0b12c8f44c00860195446032933d";
        //String appServerRegName = "app server registraion name1";
        String appServerRegName = "샘플앱서버11";
        //String gateHost = "gate.rationalowl.com";
        String gateHost = "211.239.150.123";
        int gatePort = 9081;   
        serverMgr.registerAppServer(serviceId, appServerRegName, gateHost, gatePort);
        output("Register request");
    }


    private void unregServer() {
        AppServerManager serverMgr = AppServerManager.getInstance();
        String serviceId = "9bd4db31dbaa4897ad9aa81c3e7e183a";
        String serverRegId = "75d0c3e10cff47ec81c807ea4de5f25b";
        //String serverRegId = "your app server registration Id";
        serverMgr.unregisterAppServer(serviceId, serverRegId);
        output("unregServer request");
    }


    

    private void sendMulticastMsg() {
        
        // data format is json string 
        // and jackson json library has used.
        String data = msgField.getText();
            
        // target device registration id
        ArrayList<String> targetDevices = new ArrayList<String>();
        targetDevices.add("1453ff0bc4c54a5ab31fb9e85d667757");
        //targetDevices.add("3b04be2c545d491d8323539bcc1a0176");
        //targetDevices.add("7661ad50f28842658e0bcbb5549a15fd");
        AppServerManager serverMgr = AppServerManager.getInstance();
        String requestId = serverMgr.sendMulticastMsg(data, targetDevices);
        output("sendMulticastMsg Msg :" + data + "requestId = " + requestId);
         
    }


    private void sendBroadcastMsg() {
        String data = msgField.getText();
        AppServerManager serverMgr = AppServerManager.getInstance();
        String requestId = serverMgr.sendBroadcastMsg(data);
        output("sendBroadcastMsg Msg :" + data + "requestId = " + requestId);
    }


    private void sendGrpMsg() {
        String data = msgField.getText();
        String deviceGroupId = "29426ab58f2349d4a01e77856d856841";
        AppServerManager serverMgr = AppServerManager.getInstance();
        String requestId = serverMgr.sendGroupMsg(data, deviceGroupId);
        output("sendGrpMsg Msg :" + data + "requestId = " + requestId);
    }
        
    
    private void sendMulticastPush() {
        
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("myNotiTitle", "커스텀 알람 타이틀");
        data.put("myNotiBody", "이미지url로 나만의 알림을 표현하세요!");
        data.put("imageUrl", "http://your.image.com/image1.jpg");
        data.put("soundUrl", "http://your.sound.com/sound1.wav");   
        
        ArrayList<String> targetDevices = new ArrayList<String>();
        targetDevices.add("1453ff0bc4c54a5ab31fb9e85d667757");
        
        AppServerManager serverMgr = AppServerManager.getInstance();
        serverMgr.sendMulticastCustomPush(data, targetDevices);
        output("broadcast push :" + data.toString());
    }

    
    private void sendBroadcastPush() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("myNotiTitle", "커스텀 알람 타이틀");
        data.put("myNotiBody", "이미지url로 나만의 알림을 표현하세요!");
        data.put("imageUrl", "http://your.image.com/image1.jpg");
        data.put("soundUrl", "http://your.sound.com/sound1.wav");  
        AppServerManager serverMgr = AppServerManager.getInstance();
        serverMgr.sendBroadcastCustomPush(data);
        // same as serverMgr.sendBroadcastMsg(data);
        output("broadcast push :" + data.toString());
    }
    
    
    private void sendGrpMsgPush() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("myNotiTitle", "커스텀 알람 타이틀");
        data.put("myNotiBody", "이미지url로 나만의 알림을 표현하세요!");
        data.put("imageUrl", "http://your.image.com/image1.jpg");
        data.put("soundUrl", "http://your.sound.com/sound1.wav");  
        String deviceGroupId = "29426ab58f2349d4a01e77856d856841";
        AppServerManager serverMgr = AppServerManager.getInstance();
        String requestId = serverMgr.sendGroupCustomPush(data, deviceGroupId);
        output("sendGrpMsg Msg :" + data + "requestId = " + requestId);
    }
    
    
    private void createDeviceGroup() {
        AppServerManager serverMgr = AppServerManager.getInstance();
        // device group name which will be displayed in the console
        String groupName = "단말그룹 1";
        // device group description
        String groupDesc = "단말그룹 1입니다.";
        // devices which should be grouped.
        // maximum devices.size() should be below 2,000 in the
        // createDeviceGroup() API.
        ArrayList<String> devices = new ArrayList<String>();
        // edit device registration id as yours        
        devices.add("be64c16df4e44e84a1bfb85624133a7e");
        devices.add("09876d41ac964d1295b28fcaa3f08744");
        devices.add("5e4520bb31284957987c97b4eae01b0f");
        devices.add("f38a2e95f94c4dea90c797edb19dcd34");
        devices.add("dc9f257af2454ee089419b42bdd8b902");
        String requestId = serverMgr.createDeviceGroup(groupName, groupDesc, devices);
        output("createDeviceGroup request requestId = " + requestId);
    }


    private void addDevicesToGroup() {
        AppServerManager serverMgr = AppServerManager.getInstance();
        // group id which is generated by onDeviceGroupCreateResult() callback.
        String groupId = "a22b830c17af452a84e0b93a4ac2815f";

        // devices which should be added to the device group
        // maximum devices.size() should be below 2,000 in the addDeviceGroup() API.
        ArrayList<String> devices = new ArrayList<String>();
        devices.add("27af1a1cb5454b1caf5ccfd95d2c5b6f");
        devices.add("3b04be2c545d491d8323539bcc1a0176");
        devices.add("7661ad50f28842658e0bcbb5549a15fd");
        devices.add("bafb976f957a499c9d82025455fd34f9");
        devices.add("f63e97e3344f4e4b860308fe0756e236");
        String requestId = serverMgr.addDeviceGroup(groupId, devices);
        output("addDevicesToGroup request requestId = " + requestId);
    }


    private void subtractDevicesFromGroup() {
        AppServerManager serverMgr = AppServerManager.getInstance();
        // group id which is generated by onDeviceGroupCreateResult() callback.
        String groupId = "a22b830c17af452a84e0b93a4ac2815f";

        // devices which should be removed from the device group
        // maximum devices.size() should be below 2,000 in the subtractDeviceGroup() API.
        ArrayList<String> devices = new ArrayList<String>();
        devices.add("27af1a1cb5454b1caf5ccfd95d2c5b6f");
        devices.add("3b04be2c545d491d8323539bcc1a0176");
        devices.add("7661ad50f28842658e0bcbb5549a15fd");
        String requestId = serverMgr.subtractDeviceGroup(groupId, devices);
        output("subtractDevicesFromGroup requestId = " + requestId);
    }


    private void deleteGroup() {
        AppServerManager serverMgr = AppServerManager.getInstance();
        // group id which is generated by onDeviceGroupCreateResult() callback.
        String groupId = "a22b830c17af452a84e0b93a4ac2815f";
        String requestId = serverMgr.deleteDeviceGroup(groupId);
        output("deleteGroup requestId = " + requestId);
    }

}