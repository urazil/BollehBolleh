package com.example.kotlinnodejsauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.kotlinnodejsauth.Retrofit.INodeJS
import com.example.kotlinnodejsauth.Retrofit.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register.*

class Register : AppCompatActivity() {

    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //Init Api
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)

        //회원가입 완료 버튼 클릭시
        register_complete_btn.setOnClickListener {
            register(
                edt_email1.text.toString(),
                edt_name1.text.toString(),
                edt_password1.text.toString()
            )
        }
        //툴바 생성
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        ab.setDisplayHomeAsUpEnabled(true)
    }
        //툴바 뒤로가기
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun register(email: String, name: String, password: String) {

        compositeDisposable.add(myAPI.registerUser(email, name, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
                .subscribe { message ->
                if (message.contains("encrypted_password")) {
                    Toast.makeText(this, "회원가입 성공!!!!!!!", Toast.LENGTH_SHORT).show()
                    val loginIntent = Intent(this, MainActivity::class.java)
                    startActivity(loginIntent)
                    finish()
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }


            })

    }



}
