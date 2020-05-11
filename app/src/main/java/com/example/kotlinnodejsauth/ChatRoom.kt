package com.example.kotlinnodejsauth

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinnodejsauth.adapter.ChatAdapter
import com.example.kotlinnodejsauth.data.ChatModel
import com.github.nkzawa.socketio.client.IO
import org.json.JSONException
import org.json.JSONObject
import java.net.URISyntaxException


class ChatRoom : AppCompatActivity(),
    View.OnClickListener {


    private var mRecyclerViewChat: RecyclerView? = null
    private var mSendButton: Button? = null
    private var mEditTextChatBox: EditText? = null
    private var chatAdapter: ChatAdapter? = null
    private val mChatList: ArrayList<ChatModel> = ArrayList()
    private var mSocket: com.github.nkzawa.socketio.client.Socket? = null
    private var isConnected = false
    private var username = "jusang"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)
        mRecyclerViewChat = findViewById(R.id.rv_chat)
        mEditTextChatBox = findViewById(R.id.et_chat_box)
        mSendButton = findViewById(R.id.btn_send)
        mSendButton?.setOnClickListener(this)
        onSocketConnect()
        Log.d("소켓접속",onSocketConnect().toString())
        val linearLayoutManager = LinearLayoutManager(this)
        mRecyclerViewChat?.setLayoutManager(linearLayoutManager)
        chatAdapter = ChatAdapter(mChatList)
        mRecyclerViewChat?.setAdapter(chatAdapter)
    }
        //socket.io 접속
    private fun onSocketConnect() {
        mSocket?.on(com.github.nkzawa.socketio.client.Socket.EVENT_CONNECT, onConnect)
        mSocket?.on(com.github.nkzawa.socketio.client.Socket.EVENT_DISCONNECT, onDisconnected)
        mSocket?.on(com.github.nkzawa.socketio.client.Socket.EVENT_CONNECT_ERROR, onConnectionError)
        mSocket?.on(com.github.nkzawa.socketio.client.Socket.EVENT_CONNECT_TIMEOUT, onConnectionError)
        mSocket?.on("new message", onNewMessage)
        mSocket?.connect()
    }

    //socket.io 접속 종료
    override fun onDestroy() {
        super.onDestroy()
        mSocket?.disconnect()
        mSocket?.off(com.github.nkzawa.socketio.client.Socket.EVENT_CONNECT, onConnect)
        mSocket?.off(com.github.nkzawa.socketio.client.Socket.EVENT_DISCONNECT, onDisconnected)
        mSocket?.off(com.github.nkzawa.socketio.client.Socket.EVENT_CONNECT_ERROR, onConnectionError)
        mSocket?.off(com.github.nkzawa.socketio.client.Socket.EVENT_CONNECT_TIMEOUT, onConnectionError)
        mSocket?.off("new message", onNewMessage)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_send -> { //채팅 입력 버튼 클릭시
                val message =
                    mEditTextChatBox!!.text.toString().trim { it <= ' ' }
                if (message != null && !TextUtils.isEmpty(message)) {
                    mChatList.add(
                        ChatModel(
                            message,
                            username,
                            MESSAGE_SENDER_ME
                        )
                    )
                    mEditTextChatBox!!.setText("")
                    mSocket?.emit("new message", message)
                    if (chatAdapter != null) chatAdapter!!.notifyDataSetChanged()
                }
            }
        }
    }

    private val onConnect: com.github.nkzawa.emitter.Emitter.Listener = com.github.nkzawa.emitter.Emitter.Listener {
        runOnUiThread {
            if (!isConnected) {
                mSocket?.emit("add user",  username)
                Toast.makeText(applicationContext, "Connected", Toast.LENGTH_SHORT)
                    .show()
                isConnected = true
            }
        }
    }
    //채팅 접속 끊겼을시
    private val onDisconnected: com.github.nkzawa.emitter.Emitter.Listener = com.github.nkzawa.emitter.Emitter.Listener {
        runOnUiThread {
            isConnected = false
            Toast.makeText(applicationContext, "접속실패", Toast.LENGTH_SHORT)
                .show()
        }
    }
    //채팅 접속 오류시
    private val onConnectionError: com.github.nkzawa.emitter.Emitter.Listener = com.github.nkzawa.emitter.Emitter.Listener {
        runOnUiThread {
            Toast.makeText(applicationContext, "접속 Error", Toast.LENGTH_SHORT)
                .show()
        }
    }
    //채팅 접속시
    private val onNewMessage: com.github.nkzawa.emitter.Emitter.Listener = object : com.github.nkzawa.emitter.Emitter.Listener{
        override fun call(vararg args: Any) {
            runOnUiThread(Runnable {
                val dataRecieved = args[0] as JSONObject
                val userName: String
                val message: String
                try {
                    userName = dataRecieved.getString("username")
                    message = dataRecieved.getString("message")
                } catch (e: JSONException) {
                    e.printStackTrace()
                    return@Runnable
                }
                Toast.makeText(applicationContext, userName, Toast.LENGTH_SHORT).show()
                mChatList.add(
                    ChatModel(
                        message,
                        userName,
                        MESSAGE_SENDER_OTHER
                    )
                )
                if (chatAdapter != null) chatAdapter!!.notifyDataSetChanged()
            })
        }
    }

    companion object {
        private const val MESSAGE_SENDER_ME = 1
        private const val MESSAGE_SENDER_OTHER = 2
    }
    init {
        try {
            mSocket = IO.socket("https://socket-io-chat.now.sh/")
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }


}