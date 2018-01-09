
package com.customer.test.data;

import android.content.Context;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rationalowl.minerva.client.android.MinervaManager;
import com.rationalowl.minerva.client.android.util.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class MyData {
    private static final String TAG = "PropertyInfoFetcher";


    // private static class ServicePkgPair {
    //     public String mServiceId;
    //     public String mPkgName;
    // }


    public static final String DATA_FILE = "mydata.dat";


    //property key fields       
    private static final String JSON_DEVICE_REG_ID_KEY = "device_reg_id";



    private static MyData instance = new MyData();

    private String mDeviceRegId;

    private ObjectMapper mMapper;
    private Map<String, Object> mJsonPrp;


    public static MyData getInstance() {
        return instance;
    }

    private MyData() {
        mMapper = new ObjectMapper();
        mJsonPrp = new LinkedHashMap<String,Object>();
        load();
    }


    public void release() {
        //There's no resource to release.
    }


    public void reload() {
        mJsonPrp = new LinkedHashMap<String,Object>();
        load();
    }


    public void setDeviceRegId(String deviceRegId) {
        mDeviceRegId = deviceRegId;
        mJsonPrp.put(JSON_DEVICE_REG_ID_KEY, deviceRegId);
        save();
    }


    public String getDeviceRegId() {
        return mDeviceRegId;
    }



    private void load() {
        Logger.debug(TAG, "load enter");
        boolean isFileExist = false;
        FileReader fr = null;
        File propertyFile = null;
        long fileSize = 0;

        try {
            Context context = MinervaManager.getContext();
            File dataDir = context.getFilesDir();
            propertyFile = new File(dataDir, DATA_FILE);

            if(propertyFile.exists() == false) {
                boolean test = propertyFile.createNewFile();
                Logger.debug(TAG, "" + test);
            }
            fileSize = propertyFile.length();

            if(fileSize > 0) {
                isFileExist = true;
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
                        Logger.debug(TAG,  "loadData error");
                    }
                }
                //in normal case, after breaking loop, fileSize would be equal to the accumSize.
                //And it's just for logging.
                if(accumSize != fileSize) {
                    Logger.debug(TAG,  "loadData error 2");
                }
                br.close();
                fr.close();
                String propertyStr = new String(buf);
                propertyStr.trim();

                boolean isContain = false;
                mJsonPrp = mMapper.readValue(propertyStr, new TypeReference<Map<String, Object>>() {});

                //get device registration id
                isContain = mJsonPrp.containsKey(JSON_DEVICE_REG_ID_KEY);

                if(isContain == true) {
                    mDeviceRegId = (String)mJsonPrp.get(JSON_DEVICE_REG_ID_KEY);
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
        Logger.debug(TAG, "save enter");

        try {
            String jsonStr = mMapper.writeValueAsString(mJsonPrp);

            //save to the data file
            Context context = MinervaManager.getContext();
            File dataDir = context.getFilesDir();
            File propertyFile = new File(dataDir, DATA_FILE);
            FileWriter fw = new FileWriter(propertyFile, false);
            BufferedWriter bw = new BufferedWriter(fw);

            //write property data to the file            
            bw.write(jsonStr);
            bw.flush();
            bw.close();
            fw.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}