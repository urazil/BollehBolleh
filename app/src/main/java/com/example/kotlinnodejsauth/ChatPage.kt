package com.example.kotlinnodejsauth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kotlinnodejsauth.data.User
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_chat_page.*

class ChatPage : Fragment() {





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_chat_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
          //shared preference 로그인 정보 가져오기
        val pref = context!!.getSharedPreferences("UserId", Context.MODE_PRIVATE)
        var userID = pref.getString("id", 0.toString())
        val pref1 = context!!.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        var userEmail = pref1.getString(userID, null)
        var gson = Gson()
        var UserDTO = gson.fromJson(userEmail, User::class.java)


        personname.text = UserDTO.name.toString()

        //채팅방 입장
       chat_enter.setOnClickListener {
           val intent = Intent(context, ChatRoom::class.java)
           intent.putExtra("name", personname.text.toString())
           startActivity(intent)
       }


    }


}
