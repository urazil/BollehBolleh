package com.example.kotlinnodejsauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_startpage.*

class Startpage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startpage)

        //Handler를 사용해서 화면이 3초 딜레이후 자동으로 이동
        Handler().postDelayed({
            startActivity(Intent(this@Startpage, MainActivity::class.java))
        }, 3000)
    }
}
