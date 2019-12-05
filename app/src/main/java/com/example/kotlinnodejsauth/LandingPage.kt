package com.example.kotlinnodejsauth

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.kotlinnodejsauth.Retrofit.IUploadAPI
import com.example.kotlinnodejsauth.Retrofit.RetrofitClient
import com.example.kotlinnodejsauth.util.ProgressRequestBody
import kotlinx.android.synthetic.main.activity_landing_page.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_detail.*
import okhttp3.Callback
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import java.util.jar.Manifest
import kotlin.concurrent.thread



class LandingPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)



    }
}
