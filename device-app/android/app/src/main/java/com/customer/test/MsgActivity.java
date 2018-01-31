package com.customer.test;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.support.v4.content.LocalBroadcastManager;
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
import com.customer.test.data.MyData;
import com.rationalowl.minerva.client.android.MinervaManager;

import java.util.ArrayList;
import java.util.Calendar;


public class MsgActivity extends Activity implements OnClickListener {
    
    private static final String TAG = "NoticeListActivity";
    
    private static final int MENU_MAIN = Menu.FIRST;
    
    BroadcastReceiver mMsgRecver;
    
    //ListView mLv;    
    //SimpleAdapter mAdapter;
    //private static ArrayList<PushMsg> mMsgList;
    private Button upstreamBtn;
    private Button p2pBtn;
    private EditText et;
    
    
    private MsgListAdapter mListAdapter = null;
    
    ArrayList<String> destDevices;
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
        Log.d(TAG, "onDestroy() enter");
        super.onDestroy();
    }
    
    
    @Override
    protected void onStart() {
        Log.d(TAG, "onStart() enter");
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMsgRecver), new IntentFilter("demo"));
        //this.registerReceiver((mMsgRecver), new IntentFilter("demo"));
        destDevices = new ArrayList<String>();
        destDevices.add("335a4c36c7aa40bfa7144fff30820d82");  // i pad

        //destDevices.add("32835fac9df249479cda877b12409bca");  // jungdo_use_phone
        //destDevices.add("1241670df8694da586605bf431f150a9");  // jungdo_empty_phone(galaxy nexus)
        //destDevices.add("4285e11625ff4e71a94ad799457358a3");  // helpter designer(g6)
        //destDevices.add("8a7540dd28854615b5b17c933a167206");  // helpter ceo(aka)
        //destDevices.add("bc8a9818c02d4e84a6b9cbe6357c0749");  // helpter gpad



        int size = destDevices.size();
        MyData data = MyData.getInstance();
        data.reload();
        String myDeviceId = data.getDeviceRegId();
        
        //remove this device from p2p destiny devices list.
        for(int i = 0; i < size; i++) {
            String devId = destDevices.get(i);
            
            if(devId.equals(myDeviceId)) {
                destDevices.remove(i);
                break;
            }           
        }
    }
    
    
    @Override
    protected void onResume() {
        Log.d(TAG, "onResume() enter");
        super.onResume();          
        mListAdapter.notifyDataSetChanged();
        //setTable();
    }
    
    
    @Override
    public void onStop() {        
        Log.d(TAG, "onStop() enter");
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMsgRecver);
        //this.unregisterReceiver(mMsgRecver);
        //finish();       
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
                String serverId = "513bb7114fc04ffbbd5f7ff6da173f97"; // tta
                //String serverId = "1fe45769e24348bfa501c32032958483"; // jungdo
                //String serverId = "def829b853d046779e2227bdd091653c:null";
                //String serverId = "0bb887049fb04bca924853be0e78f28d"; //gyeong min python
                MinervaManager minMgr = MinervaManager.getInstance();
                //manage umi(upstream message id) to check upstream delivery
                // when ACTION_MINERVA_UPSTREAM_MSG_RESULT fires
                String umi = minMgr.sendUpstreamMsg(msg, serverId);
                // manage umi if you want to check later
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
                //manage pmi(p2p message id) to check p2p delivery
                // when ACTION_MINERVA_P2P_MSG_RESULT fires
                String pmi = minMgr.sendP2PMsg(msg, destDevices);
                // manage pmi if you want to check later
                break;
            default:
                break;
        }        
        
    }
    
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_MAIN, 0, "main");
        return super.onCreateOptionsMenu(menu);
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected enter");
        switch (item.getItemId()) {
            case MENU_MAIN:
                Log.d(TAG, "onOptionsItemSelected 1");
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);       
                return true;            
        }
        return super.onOptionsItemSelected(item);
    }    
    
    /*
    private void setTable() {
        //jungdo_todo : should just update list which are changed. 
        //currently just re-make it to avoid researching time.
        //update view       
        SimpleAdapter adapter = null;        
        MsgDataCache cache = MsgDataCache.getInstance();
        ArrayList<PushMsg> list = cache.getMsgList();               
        
        if(list == null || list.size() == 0) {            
            // create the grid item mapping
            String[] from = new String[] {"rowid"};
            int[] to = new int[] { R.id.item1};
            List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();       
            HashMap<String, String> map = new HashMap<String, String>();            
            map.put("rowid", "���� �޽����� �����ϴ�.");
            fillMaps.add(map);
            mAdapter = new SimpleAdapter(this, fillMaps, R.layout.empty_item, from, to);
        }
        else {            
            HashMap<String, String> map = null;    
            PushMsg msg = null;           
            int listSize = list.size();
            
            if(listSize >= 200) {
                listSize = 1;
                msg = list.get(0);
                list.clear();
                list.add(msg);  
                
                cache.clearMsgList();
                cache.addMsg(msg);
            }
            
            // create the grid item mapping
            String[] from = new String[] {"rowid", "sendTime", "receiveTime", "elapseTime", "msg"};
            int[] to = new int[] { R.id.item1, R.id.item2, R.id.item3, R.id.item4, R.id.item5 };
     
            // prepare the list of all records
            List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();              
            
            
            for(int i = 0; i < listSize; i++) {
                msg = list.get(i);
                map = new HashMap<String, String>();
                map.put("rowid", "" + (i + 1));
                map.put("sendTime", msg.mSrcTime); 
                map.put("receiveTime", msg.mDestTime);        
                map.put("elapseTime", msg.mElapsedTime + "");      
                map.put("msg", msg.mData);                   
                fillMaps.add(map);
            }                   
            // fill in the grid_item layout
            mAdapter = new SimpleAdapter(this, fillMaps, R.layout.grid_item, from, to);
            
        }
        mLv.setAdapter(mAdapter);            
        
    }      
    */
}