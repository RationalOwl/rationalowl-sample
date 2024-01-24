package com.rationalowl.hello

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.rationalowl.minerva.client.android.DeviceRegisterResultListener
import com.rationalowl.minerva.client.android.MinervaManager
import com.rationalowl.minerva.client.android.Result
import com.rationalowl.minerva.client.android.util.Logger

class MainActivity : AppCompatActivity(), View.OnClickListener, DeviceRegisterResultListener {
    internal inner class SimpleReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            mMsg = intent.getStringArrayExtra(MESSAGE_FROM_RATIONALOWL_MSG_LISTENER_KEY)
            if (mMsg != null) {
                val msgId = mMsg!![0]
                val body = mMsg!![1]
                val title = mMsg!![2]
                val imgId = mMsg!![3]
                val strB = StringBuilder()
                strB.append(
                    """
    push from Rationalowl message listener 
    
    """.trimIndent()
                )
                strB.append("message id: $msgId\r\n")
                strB.append("message id: $msgId\r\n")
                strB.append("title: $title\r\n")
                strB.append("msg: $body\r\n")
                strB.append("image id: $imgId\r\n")
                msgTxt!!.text = strB.toString()
            }
        }
    }

    private var msgTxt: TextView? = null
    private var mDeviceRegId: String? = null
    private var mMsg: Array<String>? = null
    private var mReceiver: SimpleReceiver? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val regBtn = findViewById<View>(R.id.btn_reg) as Button
        val unreglBtn = findViewById<View>(R.id.btn_unreg) as Button
        msgTxt = findViewById<View>(R.id.txt_msg) as TextView
        regBtn.setOnClickListener(this)
        unreglBtn.setOnClickListener(this)

        // simply update message from rationalowl msg listener.
        mReceiver = SimpleReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(MESSAGE_FROM_RATIONALOWL_MSG_LISTENER_ACTION)
        registerReceiver(mReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Broadcast 등록 해제
        unregisterReceiver(mReceiver)
    }

    override fun onResume() {
        Logger.debug(TAG, "onResume() enter")
        super.onResume()
        mMsg = intent.getStringArrayExtra(MESSAGE_FROM_FIREBASE_SERVICE_KEY)
        if (mMsg != null) {
            val msgId = mMsg!![0]
            val body = mMsg!![1]
            val title = mMsg!![2]
            val imgId = mMsg!![3]
            val strB = StringBuilder()
            strB.append(
                """
    push from firebase messaging service 
    
    """.trimIndent()
            )
            strB.append("message id: $msgId\r\n")
            strB.append("title: $title\r\n")
            strB.append("msg: $body\r\n")
            strB.append("image id: $imgId\r\n")
            msgTxt!!.text = strB.toString()
        }
    }

    override fun onClick(v: View) {
        val resId = v.id
        if (resId == R.id.btn_reg) {
            // sometimes, FCM onNewToken() callback not called,
            // So, before registering we need to call it explicitly.
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task: Task<String?> ->
                if (!task.isSuccessful) {
                    Log.e(TAG, "Fetching FCM registration token failed", task.exception)
                    Toast.makeText(this, "FCM Token fetch fail", Toast.LENGTH_SHORT).show()
                    return@addOnCompleteListener
                }
                // Get recent FCM token
                val fcmToken = task.result
                val mgr = MinervaManager.getInstance()
                mgr.setDeviceToken(fcmToken)

                // after setDeviceToken API, call register API.
                mgr.setRegisterResultListener(this)
                mgr.registerDevice(
                    "211.239.150.113",
                    "SVC871d16c3-fe28-4f09-ac32-4870d171b067",
                    "hello world android"
                )
            }
        } else if (resId == R.id.btn_unreg) {
            val mgr = MinervaManager.getInstance()
            mgr.setRegisterResultListener(this)
            mgr.unregisterDevice("SVC871d16c3-fe28-4f09-ac32-4870d171b067") //aws dev gate
        }
    }

    override fun onRegisterResult(resultCode: Int, resultMsg: String, deviceRegId: String) {
        Logger.debug(TAG, "onRegisterResult $resultCode")
        val msg = resultMsg + "registration id : " + deviceRegId
        //yes registration has completed successfully!
        if (resultCode == Result.RESULT_OK || resultCode == Result.RESULT_DEVICE_ALREADY_REGISTERED) {
            // save deviceRegId to local file
            // and send deviceRegId to app server
            mDeviceRegId = deviceRegId
            Toast.makeText(this, "단말앱 등록 성공", Toast.LENGTH_LONG).show()
        } else if (resultCode == Result.RESULT_DEVICE_ALREADY_REGISTERED) {
            // already registered.
            mDeviceRegId = deviceRegId
            Toast.makeText(this, "단말앱 기등록됨", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "단말앱 등록 에러:$resultMsg", Toast.LENGTH_LONG).show()
            //error occurred while registering device app.
        }
        Log.d(TAG, msg)
    }

    override fun onUnregisterResult(resultCode: Int, resultMsg: String) {

        //yes unregistration has completed successfully!
        if (resultCode == Result.RESULT_OK) {
        } else {
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        const val MESSAGE_FROM_FIREBASE_SERVICE_KEY = "messageFromFirebaseMessagingService"
        const val MESSAGE_FROM_RATIONALOWL_MSG_LISTENER_KEY = "messageFromRationalowlMsgListener"
        const val MESSAGE_FROM_RATIONALOWL_MSG_LISTENER_ACTION =
            "messageFromRationalowlMsgListenerAction"
    }
}