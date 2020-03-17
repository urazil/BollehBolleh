package com.example.kotlinnodejsauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kotlinnodejsauth.Retrofit.RetrofitClient
import com.example.kotlinnodejsauth.Retrofit.ServiceApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register.*

class register : AppCompatActivity() {

    lateinit var myAPI: ServiceApi
    var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(ServiceApi::class.java)

        register_complete_btn.setOnClickListener {
            register(
                edt_email1.text.toString(),
                edt_name1.text.toString(),
                edt_password1.text.toString()
            )


        }
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
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }


            })

    }



}
