package com.example.kotlinnodejsauth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.kotlinnodejsauth.Retrofit.INodeJS
import com.example.kotlinnodejsauth.Retrofit.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Init retrofit
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)

        //로그인 버튼 클릭시
        login_button.setOnClickListener {
            login(edt_email.text.toString(), edt_password.text.toString())
        }

        //회원가입 버튼 클릭시
        register_btn.setOnClickListener {
            val registerintent = Intent(this, Register::class.java)
            startActivity(registerintent)
        }
        //툴바 생성하기
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
    }



    private fun login(email: String, password: String) {
        compositeDisposable.add(myAPI.loginUser(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { message ->
                if (message.contains("encrypted_password")) {
                    SaveId("id", edt_email.text.toString())
                    UserInfo(edt_email.text.toString())
                    Toast.makeText(this@MainActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LandingPage::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    // 사용자의 ID 를 쉐어드 프리퍼런스에 저장한다.
    fun SaveId(Key: String, Value: String) {
        val pref = getSharedPreferences("UserId", Context.MODE_PRIVATE)
        val ed = pref.edit()
        ed.putString(Key, Value)
        ed.apply()
    }

    // 사용자의 정보를 쉐어드 프리퍼런스에 저장한다.
    private fun UserInfo(email: String) {
        compositeDisposable.add(myAPI.userInfo(email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { message ->
//                Log.d("userinfo", message.toString())
                // 로그인한 아이디를 네비게이션 드로어 아이디에 입력한다.
                val pref = getSharedPreferences("UserId", Context.MODE_PRIVATE)
                var userID = pref.getString("id", 0.toString())
                val pref1 = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
                val ed = pref1.edit()
                ed.putString(userID, message.toString())
                ed.apply()

            })
    }

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }

    override fun isDestroyed(): Boolean {
        compositeDisposable.clear()
        return super.isDestroyed()
    }
}
