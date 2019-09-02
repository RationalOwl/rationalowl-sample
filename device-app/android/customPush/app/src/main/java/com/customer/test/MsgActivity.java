package com.customer.test;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.customer.test.data.MsgCache;
import com.customer.test.data.MsgCache.PushMsg;
import com.rationalowl.minerva.client.android.MessageListener;
import com.rationalowl.minerva.client.android.MinervaManager;
import com.rationalowl.minerva.client.android.util.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


public class MsgActivity extends AppCompatActivity implements OnClickListener, MessageListener {
    
    private static final String TAG = "NoticeListActivity";

    private static final int MENU_ID_REGISTER = Menu.FIRST;
    
    BroadcastReceiver mMsgRecver;
    
    //ListView mLv;    
    //SimpleAdapter mAdapter;
    //private static ArrayList<PushMsg> mMsgList;
    private Button upstreamBtn;
    private Button p2pBtn;
    private EditText et;
    
    private MsgListAdapter mListAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msg_list3);

        upstreamBtn = (Button)findViewById(R.id.upBtn);
        upstreamBtn.setOnClickListener(this);
        p2pBtn = (Button)findViewById(R.id.p2pBtn);
        p2pBtn.setOnClickListener(this);
        et = (EditText)findViewById(R.id.editText);
        
        //ArrayList<PushMsg> list = cache.getMsgList();          
        mListAdapter = new MsgListAdapter(this);
      
        
        //setListAdapter(mListAdapter);        
        ListView lv = (ListView)findViewById(R.id.list);
        lv.setAdapter(mListAdapter);
       
        

        mMsgRecver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //String s = intent.getStringExtra("msg");
                // do something here.
                //setTable();
                mListAdapter.notifyDataSetChanged();
            }
        };
        
    }    
    

    @Override
    public void onDestroy() {        
        Logger.debug(TAG, "onDestroy() enter");
        super.onDestroy();
    }
    
    
    @Override
    protected void onStart() {
        Logger.debug(TAG, "onStart() enter");
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMsgRecver), new IntentFilter("demo"));
    }


    @Override
    public void onStop() {
        Logger.debug(TAG, "onStop() enter");
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMsgRecver);
        //this.unregisterReceiver(mMsgRecver);
        //finish();
    }



    @Override
    protected void onResume() {
        Logger.debug(TAG, "onResume() enter");
        super.onResume();          
        mListAdapter.notifyDataSetChanged();

        // set message callback listener at onResume()
        MinervaManager minMgr = MinervaManager.getInstance();
        minMgr.setMsgListener(this);
    }



    @Override
    public void onPause() {
        Logger.debug(TAG, "onPause() enter");
        super.onPause();

        // clear message callback listener at onPause()
        MinervaManager minMgr = MinervaManager.getInstance();
        minMgr.clearMsgListener();
    }


    public void onClick(View v) {
        String msg = et.getText().toString();
        
        switch (v.getId()) {
            case R.id.upBtn: {
                Calendar cal = Calendar.getInstance();
                String curTimeStr = cal.get(Calendar.YEAR) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
               
                MsgCache cache = MsgCache.getInstance();
                PushMsg pushMsg = new PushMsg();
                pushMsg.mData = "=>" + msg;
                pushMsg.mSrcTime = curTimeStr;
                pushMsg.mDestTime = curTimeStr;
                pushMsg.mElapsedTime = 0;
                cache.addMsg(pushMsg);
                
                mListAdapter.notifyDataSetChanged();
                String serverId = "0e38fe0b46c9411294c41ef3c424bc6b"; // aws dev
                //String serverId = "2ff3da1fb1af44b599876725ed46eb92"; // hostway sample
                //String serverId = "0bb887049fb04bca924853be0e78f28d"; //gyeong min python
                MinervaManager minMgr = MinervaManager.getInstance();
                //manage umi(upstream message id) to check upstream delivery
                String umi = minMgr.sendUpstreamMsg(msg, serverId);
                break;
            }
            case R.id.p2pBtn:                
                Calendar cal = Calendar.getInstance();
                String curTimeStr = cal.get(Calendar.YEAR) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
               
                MsgCache cache = MsgCache.getInstance();
                PushMsg pushMsg = new PushMsg();
                pushMsg.mData = "=>(p2p)" + msg;
                pushMsg.mSrcTime = curTimeStr;
                pushMsg.mDestTime = curTimeStr;
                pushMsg.mElapsedTime = 0;
                cache.addMsg(pushMsg);

                mListAdapter.notifyDataSetChanged();          

                MinervaManager minMgr = MinervaManager.getInstance();

                // target device(device registration id) list
                ArrayList<String> destDevices = new ArrayList<String>();
                //destDevices.add("50b0020d9ba2405d88b1d967839d696e");  // i pad
                //destDevices.add("2b9f6349d5f4432a9baa77e01353303a");  // galaxy nexus
                destDevices.add("64b0c4832c79497cb8d7aeee39e16bda");  // note 5
                //destDevices.add("1241670df8694da586605bf431f150a9");  // jungdo_empty_phone(galaxy nexus)
                //destDevices.add("4285e11625ff4e71a94ad799457358a3");  // helpter designer(g6)
                //destDevices.add("8a7540dd28854615b5b17c933a167206");  // helpter ceo(aka)
                //destDevices.add("bc8a9818c02d4e84a6b9cbe6357c0749");  // helpter gpad

                // if you want to push to the device apps which are inactive set notification title and notification body.
                String pmi = minMgr.sendP2PMsg(msg, destDevices, true, "noti title", "noti body");
                // String pmi = minMgr.sendP2PMsg(msg, destDevices);
                // manage pmi if you want to check later
                break;
            default:
                break;
        }        
        
    }
    
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ID_REGISTER, 0, "register device app");
        return super.onCreateOptionsMenu(menu);
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Logger.debug(TAG, "onOptionsItemSelected enter");
        switch (item.getItemId()) {
            case MENU_ID_REGISTER:
                Logger.debug(TAG, "onOptionsItemSelected 1");
                Intent intent = new Intent(this, RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;            
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMsgReceived(ArrayList<JSONObject> msgs) {
        Logger.debug(TAG, "onMsgReceived enter");

        int msgSize = msgs.size();

        try {
            JSONObject oneMsg = null;
            int msgType;
            String sender = null;
            String data = null, notiTitle = null, notiBody = null;
            long serverTime;
            long curTime = System.currentTimeMillis();
            long elapseTime;
            Calendar cal = Calendar.getInstance();
            String curTimeStr = cal.get(Calendar.YEAR) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
            String serverTimeStr = null;
            MsgCache cache = MsgCache.getInstance();
            PushMsg pushMsg = new PushMsg();

            // recent messages are ordered by message send time descending order [recentest, recent, old, older, oldest...]
            // this sample app treat old message first.
            for (int i = msgSize - 1; i >= 0; i--) {
                oneMsg = msgs.get(i);
                msgType = (int) oneMsg.get(MinervaManager.FIELD_MSG_TYPE);  // 1(downstream), 2(p2p)
                sender = (String) oneMsg.get(MinervaManager.FIELD_MSG_SENDER);
                data = (String) oneMsg.get(MinervaManager.FIELD_MSG_DATA);

                if(oneMsg.has(MinervaManager.FIELD_ENCRYPTED_MSG)) {
                    data = "(encryped)" + data;
                }
                serverTime = (Long) oneMsg.get(MinervaManager.FIELD_MSG_SERVER_TIME);

                // optional fields
                if(oneMsg.has(MinervaManager.FIELD_MSG_NOTI_TITLE)) {
                    notiTitle = (String) oneMsg.get(MinervaManager.FIELD_MSG_NOTI_TITLE);
                }

                if(oneMsg.has(MinervaManager.FIELD_MSG_NOTI_BODY)) {
                    notiBody = (String) oneMsg.get(MinervaManager.FIELD_MSG_NOTI_BODY);
                }
                curTime = System.currentTimeMillis();
                elapseTime = curTime - serverTime;
                cal.setTimeInMillis(serverTime);
                serverTimeStr = cal.get(Calendar.YEAR) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);

                pushMsg = new PushMsg();
                pushMsg.mData = data;
                pushMsg.mSrcTime = serverTimeStr;
                pushMsg.mDestTime = curTimeStr;
                pushMsg.mElapsedTime = elapseTime;
                cache.addMsg(pushMsg);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        mListAdapter.notifyDataSetChanged();
    }


    @Override
    public void onSendUpstreamMsgResult(int resultCode, String resultMsg, String requestId) {
        Logger.debug(TAG, "onSendUpstreamMsgResult enter");
    }

    @Override
    public void onSendP2PMsgResult(int resultCode, String resultMsg, String requestId) {
        Logger.debug(TAG, "onSendP2PMsgResult enter");
    }
}