package com.example.kotlinnodejsauth

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.kotlinnodejsauth.Retrofit.INodeJS
import com.example.kotlinnodejsauth.Retrofit.RetrofitClient
import com.example.kotlinnodejsauth.data.User
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_modify_password.*

class modify_password : AppCompatActivity() {
    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.7f
        window.attributes = layoutParams
        setContentView(R.layout.activity_modify_password)

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

       //비밀번호 수정 버튼 클릭시 (비밀번호 정규식 사용해서 바꿔야함)
        modify_password_button.setOnClickListener {
            if (!editText_password.text.isEmpty()) {
                val passMatches = Regex("^(?=.*[a-zA-Z0-9])(?=.*[!@#\$%^*+=-]).{8,20}$")
                if (editText_password.text.toString().matches(passMatches)) {
                    compositeDisposable.add(myAPI.change_userPassword(
                        UserDTO.email,
                        editText_password.text.toString()
                    )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { message ->
                            Toast.makeText(
                                this,
                                "비밀번호를 수정하였습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            // 키보드를 내린다.
                            val imm =
                                this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(editText_password.windowToken, 0)
                            finish()

                        })
                } else {
                    Toast.makeText(this, "비밀번호 형식을 지켜주세요.", Toast.LENGTH_SHORT).show()
                    editText_password.requestFocus()
                    return@setOnClickListener
                }

            } else {
                Toast.makeText(this, "변경하실 비밀번호를 작성해주세요.", Toast.LENGTH_SHORT).show()
                editText_password.requestFocus()
                return@setOnClickListener
            }
        }
    }
}
