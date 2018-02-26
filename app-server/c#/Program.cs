using System;
using System.Threading;
using rationalowl;

class Program
{
        public static string groupId;

    public static string serviceId = "b4a2166853ad4f19acc77b193d89c497";
    public static string serverRegId;

    public static string[] deviceIdList = {
"11459415de7e48d2a21c94773a8dfb4d",
"1531f97adde6490fa63586dcdb3f6a42",
"19dae261a57745fbb258fd343c4ef155",
"20630a1d13f54c4db84bde2b9f0e4e51",
"29f78e16a7284b5197b3a290c73a5186",
"3902ac916564473ca3ea67beb6bd019b",
"42ff2a6e51554d669e378454ef29fcd9",
"4d5a55166e7543519fe6dc2f2c02966b",
"597c2c77081544bb823a7a5e93f98932",
};

    class AppServerRegisterResultImpl : AppServerRegisterResultListener
    {
        public override void onRegisterResult(int resultCode, string resultMsg,
                                    string appServerRegId)
        {
            Console.WriteLine($"onRegisterResult {resultCode}, {resultMsg}, {appServerRegId}");
            Program.serverRegId = appServerRegId;
        }
        public override void onUnregisterResult(int resultCode, string resultMsg)
        {
            Console.WriteLine($"onUnregisterResult {resultCode}, {resultMsg}");
        }
    };
    class DeviceGroupImpl : DeviceGroupListener
    {
        public override void onDeviceGroupCreateResult(int resultCode, string resultMsg,
                                             string deviceGrpId, string deviceGrpName,
                                             int deviceSize, string desc,
                                             string[] failedDevices,
                                             string requestId)
        {
            Console.WriteLine($"onDeviceGroupCreateResult {resultCode}, {resultMsg}, {deviceGrpId}, {deviceGrpName}, {deviceSize}, {desc}, {failedDevices}, {requestId}");
            Program.groupId = deviceGrpId;
        }
        public override void onDeviceGroupAddResult(int resultCode, string resultMsg,
                                          string deviceGrpId, int totalDeviceSize,
                                          int addedDeviceSize,
                                          string[] failedDevices,
                                          string requestId)
        {
            Console.WriteLine($"onDeviceGroupAddResult {resultCode}, {resultMsg}, {deviceGrpId}, {totalDeviceSize}, {addedDeviceSize}, {failedDevices}, {requestId}");
        }
        public override void onDeviceGroupSubtractResult(
            int resultCode, string resultMsg, string deviceGrpId, int totalDeviceSize,
            int subtractDeviceSize, string[] failedDevices, string requestId)
        {
            Console.WriteLine($"onDeviceGroupSubtractResult {resultCode}, {resultMsg}, {deviceGrpId}, {totalDeviceSize}, {subtractDeviceSize}, {failedDevices}, {requestId}");
        }
        public override void onDeviceGroupDeleteResult(int resultCode, string resultMsg,
                                             string deviceGrpId, string requestId)
        {
            Console.WriteLine($"onDeviceGroupDeleteResult {resultCode}, {resultMsg}, {deviceGrpId}, {requestId}");
        }
    };
    class MessageImpl : MessageListener
    {
        public override void onUpstreamMsgReceived(string sender, long sendTime, string msg)
        {
            Console.WriteLine($"onUpstreamMsgReceived {sender}, {sendTime}, {msg}");
        }
        public override void onSendUnicastMsgResult(int resultCode, string resultMsg,
                                          string requestId)
        {
            Console.WriteLine($"onSendUnicastMsgResult {resultCode}, {resultMsg}, {requestId}");
        }
        public override void onSendMulticastMsgResult(int resultCode, string resultMsg,
                                            string requestId)
        {
            Console.WriteLine($"onSendMulticastMsgResult {resultCode}, {resultMsg}, {requestId}");
        }
        public override void onSendBroadcastMsgResult(int resultCode, string resultMsg,
                                            string requestId)
        {
            Console.WriteLine($"onSendBroadcastMsgResult {resultCode}, {resultMsg}, {requestId}");
        }
        public override void onSendGroupMsgResult(int resultCode, string resultMsg,
                                        string requestId)
        {
            Console.WriteLine($"onSendGroupMsgResult {resultCode}, {resultMsg}, {requestId}");
        }
    }

    static void Main(string[] args)
    {
        
        string[] subDeviceList1 = new string[5];
        string[] subDeviceList2 = new string[3];
        Array.Copy(deviceIdList, 0, subDeviceList1, 0, 5);
        Array.Copy(deviceIdList, 5, subDeviceList2, 0, 3);

        AppServerManager.getInstance().setRegisterResultListener(
        new AppServerRegisterResultImpl());
        AppServerManager.getInstance().setDeviceGroupListener(
            new DeviceGroupImpl());
        AppServerManager.getInstance().setMsgListener(new MessageImpl());

        AppServerManager.getInstance().registerAppServer(
            Program.serviceId, "C# App Server Test", "gate.rationalowl.com", 9081);

        // Thread.Sleep(5000);
        // AppServerManager.getInstance().unregisterAppServer(Program.serviceId, Program.serverRegId);

        // Thread.Sleep(5000);
        // AppServerManager.getInstance().createDeviceGroup("groupName",
        // "groupDesc", subDeviceList1);

        // Thread.Sleep(5000);
        // AppServerManager.getInstance().addDeviceGroup(groupId, subDeviceList2);

        // Thread.Sleep(5000);
        // AppServerManager.getInstance().subtractDeviceGroup(groupId, subDeviceList2);

        // Thread.Sleep(5000);
        // AppServerManager.getInstance().deleteDeviceGroup(groupId);

        // Thread.Sleep(5000);
        // AppServerManager.getInstance().createDeviceGroup("groupName", "groupDesc",
        //                                                    subDeviceList1);
        Thread.Sleep(5000);
        AppServerManager.getInstance().sendMulticastMsg("멀티캐스트메시지",
                                                          subDeviceList1);
        // Thread.Sleep(5000);
        // AppServerManager.getInstance().sendGroupMsg("그룹메시지", groupId);

        // Thread.Sleep(5000);
        // AppServerManager.getInstance().sendBroadcastMsg("브로드캐스트메시지");

        Console.ReadLine();
    }
}
