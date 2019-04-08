
package com.customer.test.data;

import android.content.Context;
import android.util.Log;

import com.rationalowl.minerva.client.android.MinervaManager;
import com.rationalowl.minerva.client.android.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MsgCache {

    public static class PushMsg {
        public String mData;
        public String mSrcTime;
        public String mDestTime;
        public long mElapsedTime;
    }

    private static final String TAG = "MsgCache";

    //property key fields
    private static final String JSON_MSG_KEY = "msg_log";
    private static final String MSG_LOG_FILE = "msg.dat";

    private static MsgCache instance = new MsgCache();

    private ArrayList<PushMsg> mMsgLogList;



    public static MsgCache getInstance() {
        return instance;
    }


    private MsgCache() {
        mMsgLogList = new ArrayList<PushMsg>();
        load();
    }


    public void release() {
        //There's no resource to release.
    }


    public void addMsg(PushMsg msg) {
        mMsgLogList.add(0, msg);
        //mMsgCnt++;
        save();
    }


    public ArrayList<PushMsg> getMsgList() {
        return mMsgLogList;
    }


    public void clearMsgList() {
        mMsgLogList.clear();
        save();
    }


    private void load() {
        boolean isFileExist = false;
        FileReader fr = null;
        File propertyFile = null;
        long fileSize = 0;
        Logger.debug(TAG, "load enter");

        try {
            Context context = MinervaManager.getContext();
            //default       
            File privateDir = context.getFilesDir();
            propertyFile = new File(privateDir, MSG_LOG_FILE);
            fileSize = propertyFile.length();

            if(propertyFile.exists() == true && fileSize > 0) {
                isFileExist = true;
            }
            else {
                propertyFile.createNewFile();
            }

            if(isFileExist == true) {
                fr = new FileReader(propertyFile);
                BufferedReader br = new BufferedReader(fr);

                char[] buf = new char[(int)fileSize];
                char[] tmpBuf = new char[1024 * 1024];
                int read;
                int accumSize = 0;

                while ((read = br.read(tmpBuf)) != -1) {
                    System.arraycopy(tmpBuf, 0, buf, accumSize, read);
                    accumSize += read;

                    //it's just for logging and it's error case
                    if(accumSize > fileSize) {
                        Logger.debug(TAG, "loadData error");
                    }
                }
                //in normal case, after breaking loop, fileSize would be equal to the accumSize.
                //And it's just for logging.
                if(accumSize != fileSize) {
                    Logger.debug(TAG, "loadData error 2");
                }
                br.close();
                fr.close();
                String propertyStr = new String(buf);
                try {
                    JSONObject jsonPrp = new JSONObject(propertyStr);

                    //get event log                    
                    boolean isNull = jsonPrp.isNull(JSON_MSG_KEY);

                    if(isNull == false) {
                        JSONArray evtLogJArr = jsonPrp.getJSONArray(JSON_MSG_KEY);
                        int cnt = evtLogJArr.length();

                        JSONObject jObj;
                        PushMsg msg;

                        for(int i = 0; i < cnt; i++) {
                            jObj = evtLogJArr.getJSONObject(i);

                            String data = jObj.getString("data");
                            String srcTime = jObj.getString("srcTime");
                            String destTime = jObj.getString("destTime");
                            long elapseTime = jObj.getLong("elapseTime");
                            msg = new PushMsg();
                            msg.mData = data;
                            msg.mSrcTime = srcTime;
                            msg.mDestTime = destTime;
                            msg.mElapsedTime = elapseTime;
                            mMsgLogList.add(msg);
                        }
                    }
                    else {
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    private void save() {
        Logger.debug(TAG,"save enter");
        JSONObject jObj = new JSONObject();

        try {

            if(mMsgLogList != null) {
                int cnt = mMsgLogList.size();
                PushMsg msg;
                JSONArray jArr = new JSONArray();
                JSONObject jObjMsg = null;

                for(int i = 0;  i < cnt; i++) {
                    msg = mMsgLogList.get(i);
                    jObjMsg = new JSONObject();
                    jObjMsg.put("data", msg.mData);
                    jObjMsg.put("srcTime", msg.mSrcTime);
                    jObjMsg.put("destTime", msg.mDestTime);
                    jObjMsg.put("elapseTime", msg.mElapsedTime);
                    jArr.put(jObjMsg);
                }
                jObj.put(JSON_MSG_KEY, jArr);
            }

            //finally, save property file.
            Context context = MinervaManager.getContext();
            File privateDir = context.getFilesDir();
            File propertyFile = new File(privateDir, MSG_LOG_FILE);

            FileWriter fw = new FileWriter(propertyFile, false);
            BufferedWriter bw = new BufferedWriter(fw);
            bw = new BufferedWriter(fw);
            //write property data to the file
            bw.write(jObj.toString());
            bw.flush();
            bw.close();
            fw.close();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}