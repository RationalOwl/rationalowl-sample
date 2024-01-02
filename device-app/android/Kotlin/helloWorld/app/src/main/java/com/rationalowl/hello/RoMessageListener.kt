package com.rationalowl.hello

import android.content.Intent
import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.rationalowl.minerva.client.android.MessageListener
import com.rationalowl.minerva.client.android.MinervaManager
import com.rationalowl.minerva.client.android.util.Logger
import org.json.JSONObject

class RoMessageListener : MessageListener {
    override fun onMsgReceived(msgs: ArrayList<JSONObject>) {
        Log.d(TAG, "onMsgReceived enter")
        val msgSize = msgs.size
        Log.d(TAG, "$msgSize message received")
        try {
            val oneMsg: JSONObject? = null
            var msgType: Int
            var sender: String? = null
            var data: String? = null
            var customPush: Map<String, String>? = null

            // recent messages are ordered by message send time descending order [recentest, recent, old, older, ... oldest]
            for (json in msgs) {
                // message type
                msgType = json[MinervaManager.FIELD_MSG_TYPE] as Int // 1 (realtime: downstream), 2 (realtime: p2p), 3(custom push)
                // message sender (sender registration id)
                sender = json[MinervaManager.FIELD_MSG_SENDER] as String
                // data
                data = json[MinervaManager.FIELD_MSG_DATA] as String
                Log.d(TAG, "message type:$msgType")
                Log.d(TAG, "message sender:$sender")
                // custom data formatted json format
                Log.d(TAG, "message :$data")
                when (msgType) {
                    // realtime downstream
                    1,
                    // realtime p2p
                    2 -> {}
                    3 -> {

                        // custom push format can be any fields you want.
                        // RationalUms Demo format
                        /*
                        {
                            "mId": "message id here",
                            "title": "message title here",
                            "body": "message body here",
                            "ii": "image id here"
                            "st": "(message) send time"
                        }
                        */
                        // if multiple custom push received, we just notify recentest push only.
                        if (customPush == null) {

                            customPush = mapper.readValue(data)
                            var msgId: String? = null
                            var body: String? = null
                            var title: String? = null
                            var imageId: String? = null
                            // mandatory fields
                            msgId = customPush?.get("mId")
                            body = customPush?.get("body")

                            // optional fields
                            if (customPush!!.containsKey("title")) {
                                title = customPush?.get("title")
                            }
                            if (customPush.containsKey("ii")) {
                                imageId = customPush?.get("ii")
                            }
                            val extra = arrayOfNulls<String>(4)
                            extra[0] = msgId
                            extra[1] = body
                            extra[2] = title
                            extra[3] = imageId
                            val context = MinervaManager.getContext()
                            val intent =
                                Intent(MainActivity.Companion.MESSAGE_FROM_RATIONALOWL_MSG_LISTENER_ACTION)
                            intent.putExtra(
                                MainActivity.Companion.MESSAGE_FROM_RATIONALOWL_MSG_LISTENER_KEY,
                                extra
                            )
                            context.sendBroadcast(intent)
                        }
                    }

                    else -> {}
                }
            }
            if (customPush != null) {
                MyFirebaseMessagingService.Companion.showCustomNotification(customPush)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onSendUpstreamMsgResult(resultCode: Int, resultMsg: String, msgId: String) {
        Logger.debug(TAG, "onSendUpstreamMsgResult enter")
    }

    override fun onSendP2PMsgResult(resultCode: Int, resultMsg: String, msgId: String) {
        Logger.debug(TAG, "onSendP2PMsgResult enter")
    }

    companion object {
        private const val TAG = "RoMsgListener"
        private val mapper = jacksonObjectMapper()
    }
}