package com.example.kotlinnodejsauth

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlinnodejsauth.util.ProgressRequestBody3
import kotlinx.android.synthetic.main.activity_gallerypage.*
import kotlinx.android.synthetic.main.activity_upload_pic_page.*

class UploadPicPage : AppCompatActivity(),ProgressRequestBody3.UploadCallbacks {

    override fun onProgressUpdate(percentage: Int) {
        dialog.progress = percentage
    }

    lateinit var dialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_pic_page)

        pic_btn_upload.setOnClickListener {
            val pic_upload = Intent(this, Gallerypage::class.java)
            startActivity(pic_upload)
        }


    }

}
