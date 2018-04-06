package com.customer.test;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.customer.test.data.MsgCache;
import com.customer.test.data.MsgCache.PushMsg;
import com.rationalowl.minerva.client.android.MinervaManager;

import java.util.ArrayList;

public class EventLogListAdapter extends BaseAdapter {

    private static final String TAG = "EventLogListAdapter";
    
    LayoutInflater mInflater;
    boolean isNewIcon = false;

    
    public EventLogListAdapter(){
        Log.d(TAG, "constructor enter");
        Context context = MinervaManager.getContext();//Service1App.getContext();
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    

    public int getCount() {
        MsgCache cache = MsgCache.getInstance();
        ArrayList<PushMsg> list = cache.getMsgList();
        
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    
    public Object getItem(int position) {
        MsgCache cache = MsgCache.getInstance();
        ArrayList<PushMsg> list = cache.getMsgList();
        
        if(list != null) {
            return list.get(position);
        }
        return null;
    }

    
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    
    
    public View getView(int position, View convertView, ViewGroup parent) {
        MsgCache cache = MsgCache.getInstance();
        ArrayList<PushMsg> list = cache.getMsgList();
        PushMsg curData = list.get(position);
        //int newCnt = appMgr.getNewEventLogCnt();
        ViewHolder h;

        if(curData == null) {
            return null;
        }

        if(convertView == null) {                        
            convertView = mInflater.inflate(R.layout.msg_list, parent, false);
            //h = new ViewHolder();
            //h.iv_new = (ImageView)convertView.findViewById(R.id.img_new);                  
            //h.tv_date = (TextView)convertView.findViewById(R.id.txt_date);
            //h.tv_data = (TextView)convertView.findViewById(R.id.txt_data);     
        }
        else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            h = (ViewHolder) convertView.getTag();
        }        
        h = new ViewHolder();  
        //h.tv_date = (TextView)convertView.findViewById(R.id.txt_date);
        //h.tv_data = (TextView)convertView.findViewById(R.id.txt_data);                    
        String date = curData.mData;
        String srcTime = curData.mSrcTime;
        String destTime = curData.mDestTime;
        long elapseTime = curData.mElapsedTime;
        h.tv_date.setText(date);
        h.tv_srtTime.setText(srcTime);        
        h.tv_destTime.setText(destTime);        
        h.tv_elapsedTime.setText(elapseTime + "");        
        return convertView;


    }
    

    static class ViewHolder {                     
        TextView tv_date;
        TextView tv_srtTime;
        TextView tv_destTime;
        TextView tv_elapsedTime;
    }    
}