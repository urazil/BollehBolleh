package com.example.kotlinnodejsauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_startpage.*
import kotlinx.coroutines.MainScope

class Startpage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startpage)

        Handler().postDelayed({
            startActivity(Intent(this@Startpage, MainActivity::class.java))

        }, 2000)
    }
}
