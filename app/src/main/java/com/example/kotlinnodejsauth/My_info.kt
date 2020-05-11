package com.example.kotlinnodejsauth

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.kotlinnodejsauth.Retrofit.INodeJS
import com.example.kotlinnodejsauth.Retrofit.RetrofitClient
import com.example.kotlinnodejsauth.data.User
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_my_info.*

class My_info : AppCompatActivity() {
    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable()

    var User_id: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_info)

        //Init API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)

        //쉐어드에 저장된 내 정보 불러오기.
        val pref = getSharedPreferences("UserId", Context.MODE_PRIVATE)
        var userID = pref.getString("id", 0.toString())
        val pref1 = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        var userEmail = pref1.getString(userID, null)
        var gson = Gson()
        var UserDTO = gson.fromJson(userEmail, User::class.java)
        User_id = UserDTO.email
        UserInfo(UserDTO.email)


        // 이름
        myInfo_name.text = UserDTO.name.toString()
        // 이메일
        myInfo_email.text = UserDTO.email.toString()



        // 이름 변경
        name.setOnClickListener {
            var intent = Intent(this, modify_name::class.java)
            startActivity(intent)
        }
        // 이메일 변경


        // 비밀번호 변경
        modify_password.setOnClickListener {
            var intent = Intent(this, com.example.kotlinnodejsauth.modify_password::class.java)
            startActivity(intent)
        }
        // 회원 탈퇴하기
        membership_withdrawal.setOnClickListener {
            var dialog = AlertDialog.Builder(this)
            dialog.setTitle("회원 탈퇴하기")
            dialog.setMessage("회원을 탈퇴하시겠습니까?")


            fun toast_p() {
                compositeDisposable.add(myAPI.delete_user(
                     UserDTO.id, UserDTO.email
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { message ->
                        if (message.contains("회원을 탈퇴하였습니다.")) {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                            finishAffinity()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            System.exit(0)
                        } else {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    })
            }

            fun toast_n() {
            }

            var dialog_listener = object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE ->
                            toast_p()
                        DialogInterface.BUTTON_NEGATIVE ->
                            toast_n()
                    }
                }
            }
            dialog.setPositiveButton("탈퇴하기", dialog_listener)
            dialog.setNegativeButton("취소", dialog_listener)
            dialog.show()
        }
    }
    private fun UserInfo(id: String?) {
        compositeDisposable.add(myAPI.userInfo(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { message ->
                Log.d("userinfo", message.toString())

                //쉐어드에 저장된 내 정보 불러오기.
                val pref = getSharedPreferences("UserId", Context.MODE_PRIVATE)
                var userID = pref.getString("id", 0.toString())
                val pref1 = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
                val ed = pref1.edit()
                ed.putString(userID, message.toString())
                ed.apply()
                val pref2 = getSharedPreferences("UserId", Context.MODE_PRIVATE)
                var userID1 = pref2.getString("id", 0.toString())
                val pref3 = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
                var userEmail = pref3.getString(userID1, null)
                var gson = Gson()
                var UserDTO = gson.fromJson(userEmail, User::class.java)
                User_id = UserDTO.email
                UserInfo(UserDTO.email)
                // 이름
                myInfo_name.text = UserDTO.name.toString()
                // 이메일
                myInfo_email.text = UserDTO.email.toString()
            })
    }

    override fun onPause() {
        UserInfo(User_id)
        super.onPause()
    }
}