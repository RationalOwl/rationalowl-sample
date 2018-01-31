package com.customer.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.customer.test.data.MsgCache;
import com.customer.test.data.MsgCache.PushMsg;

import java.util.List;


//public class MsgListAdapter extends ArrayAdapter<PushMsg> {
public class MsgListAdapter extends BaseAdapter {


    private static class MsgHolder {
        public TextView msgView;
        public TextView timeView;
    }
    private LayoutInflater mInflater;
    
    private List<PushMsg> mMsgList;
    //private Context context;


    public MsgListAdapter(Context ctx) {
        //super(ctx, R.layout.row_layout);
        //this.mMsgList = msgList;
        
        // Cache the LayoutInflate to avoid asking for a new one each time.
        mInflater = LayoutInflater.from(ctx);
        
        MsgCache cache = MsgCache.getInstance();
        mMsgList = cache.getMsgList();                 
        
        //this.context = ctx;
    }
    
    
    public void addUpstream(String msg) {
        
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        MsgHolder holder = new MsgHolder(); 
        String datePre = "발신시간:";
        
        // First let's verify the convertView is not null 
        if (convertView == null) { 
            // This a new view we inflate the new layout 
            
            v = mInflater.inflate(R.layout.row_layout, null);
            
            // Now we can fill the layout with the right values 
            TextView msgTxt = (TextView) v.findViewById(R.id.msg);
            TextView dateTxt = (TextView) v.findViewById(R.id.date);
            holder.msgView = msgTxt; 
            holder.timeView = dateTxt; 
            v.setTag(holder); 
        } 
        else {
            holder = (MsgHolder) v.getTag();
        }
        
        PushMsg p = mMsgList.get(position); 
        holder.msgView.setText(p.mData); 
        holder.timeView.setText(datePre + p.mSrcTime); 
        return v;         
    }


    @Override
    public int getCount() {
        if (mMsgList != null)
        {
            int msgSize = mMsgList.size();
            
            if(msgSize > 300) {
                MsgCache cache = MsgCache.getInstance();
                cache.clearMsgList();

                msgSize = mMsgList.size();                
            }           
            return msgSize;
        }
        return 0;
    }


    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return mMsgList.get(arg0);
    }


    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }
}
