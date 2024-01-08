package com.rationalowl.sample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.rationalowl.minerva.client.android.MessageListener;
import com.rationalowl.minerva.client.android.MinervaManager;
import com.rationalowl.minerva.client.android.util.Logger;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import com.rationalowl.sample.MsgListAdapter.*;

public class MsgActivity extends AppCompatActivity implements View.OnClickListener, MessageListener {

    private static final String TAG = "NoticeListActivity";

    private static final int MENU_ID_REGISTER = Menu.FIRST;

    BroadcastReceiver mMsgRecver;

    private Button upstreamBtn;
    private Button p2pBtn;
    private EditText et;

    private MsgListAdapter mListAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);

        upstreamBtn = (Button)findViewById(R.id.upBtn);
        upstreamBtn.setOnClickListener(this);
        p2pBtn = (Button)findViewById(R.id.p2pBtn);
        p2pBtn.setOnClickListener(this);
        et = (EditText)findViewById(R.id.editText);
        mListAdapter = new MsgListAdapter(this);

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
        int resId = v.getId();

        if(resId == R.id.upBtn) {
            Calendar cal = Calendar.getInstance();
            String curTimeStr = cal.get(Calendar.YEAR) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);

            AppMsg appMsg = new AppMsg();
            appMsg.mData = "=>" + msg;
            appMsg.mSendTime = curTimeStr;

            // display message to the table view
            mListAdapter.addTableList(appMsg);
            mListAdapter.notifyDataSetChanged();

            // send upstream message
            String serverId = "SVR6838f682-3831-4055-8ab2-c7a48cdd8a10"; // hostway sample
            MinervaManager minMgr = MinervaManager.getInstance();
            minMgr.sendUpstreamMsg(msg, serverId);
        }
        else if(resId == R.id.p2pBtn) {
            Calendar cal = Calendar.getInstance();
            String curTimeStr = cal.get(Calendar.YEAR) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
            AppMsg appMsg = new AppMsg();
            appMsg.mData = "=>(p2p)" + msg;
            appMsg.mSendTime = curTimeStr;

            // display message to the table view
            mListAdapter.addTableList(appMsg);
            mListAdapter.notifyDataSetChanged();

            // send p2p message
            MinervaManager minMgr = MinervaManager.getInstance();

            // target device(device registration id) list
            ArrayList<String> destDevices = new ArrayList<String>();
            destDevices.add("5cf16d3529534ed398146e5367e8cce5");  // note 5
            //destDevices.add("1241670df8694da586605bf431f150a9");  // jungdo_empty_phone(galaxy nexus)
            //destDevices.add("4285e11625ff4e71a94ad799457358a3");  // helpter designer(g6)

            // if you want to push to the device apps which are inactive set notification title and notification body.
            minMgr.sendP2PMsg(msg, destDevices);
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
    public void onDownstreamMsgRecieved(ArrayList<JSONObject> msgs) {
        // simply display message to the table view
        addListMsgView(msgs);
    }

    @Override
    public void onP2PMsgRecieved(ArrayList<JSONObject> msgs) {
        // simply display message to the table view
        addListMsgView(msgs);
    }

    @Override
    public void onPushMsgRecieved(ArrayList<JSONObject> msgs) {
        Logger.debug(TAG, "onPushMsgRecieved enter");

        // simply display message to the table view
        addListMsgView(msgs);
    }

    private void addListMsgView(ArrayList<JSONObject> msgs) {
        int msgSize = msgs.size();

        try {
            JSONObject oneMsg = null;
            String data = null, notiTitle = null, notiBody = null;
            long serverTime;
            Calendar cal = Calendar.getInstance();
            String serverTimeStr = null;
            AppMsg appMsg = new AppMsg();

            // recent messages are ordered by message send time descending order [recentest, recent, old, older, oldest...]
            for (int i = 0; i < msgSize; i++) {
                oneMsg = msgs.get(i);
                data = (String) oneMsg.get(MinervaManager.FIELD_MSG_DATA);
                serverTime = (Long) oneMsg.get(MinervaManager.FIELD_MSG_SERVER_TIME);
                cal.setTimeInMillis(serverTime);
                serverTimeStr = cal.get(Calendar.YEAR) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);

                appMsg = new AppMsg();
                appMsg.mData = data;
                appMsg.mSendTime = serverTimeStr;
                mListAdapter.addTableList(appMsg);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        mListAdapter.notifyDataSetChanged();
    }



    @Override
    public void onSendUpstreamMsgResult(int resultCode, String resultMsg, String msgId) {
        Logger.debug(TAG, "onSendUpstreamMsgResult enter");
    }

    @Override
    public void onSendP2PMsgResult(int resultCode, String resultMsg, String msgId) {
        Logger.debug(TAG, "onSendP2PMsgResult enter");
    }
}
