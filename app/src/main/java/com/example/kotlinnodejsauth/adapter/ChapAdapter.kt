package com.example.kotlinnodejsauth.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinnodejsauth.R
import com.example.kotlinnodejsauth.data.ChatModel

import java.util.ArrayList


class ChatAdapter(private val mChatList: ArrayList<ChatModel>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): ChatViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        val view: View =
            layoutInflater.inflate(R.layout.item_chat_box, viewGroup, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(
        chatViewHolder: ChatViewHolder,
        i: Int
    ) {
        when (mChatList[i].getSender()) {
            1 -> {
                chatViewHolder.mTextViewRight.setText(mChatList[i].getMessage())
                chatViewHolder.mTextViewRight.visibility = View.VISIBLE
            }
            2 -> {
                chatViewHolder.mTextViewLeft.setText(mChatList[i].getMessage())
                chatViewHolder.mTextViewLeft.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemCount(): Int {
        return mChatList.size
    }

    inner class ChatViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var mTextViewLeft: TextView
        var mTextViewRight: TextView

        init {
            mTextViewLeft =
                itemView.findViewById<View>(R.id.tv_chat_message_left) as TextView
            mTextViewRight =
                itemView.findViewById<View>(R.id.tv_chat_message_right) as TextView
        }
    }

}