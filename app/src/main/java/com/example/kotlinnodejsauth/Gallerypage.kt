package com.example.kotlinnodejsauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlinnodejsauth.data.Photo
import kotlinx.android.synthetic.main.activity_gallerypage.*
import kotlinx.android.synthetic.main.fragment_feed.*

class Gallerypage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallerypage)




        pic_upload_page_button.setOnClickListener {
            val editpic = Intent(this, UploadPicPage::class.java)
            startActivity(editpic)
        }

        video_gallery_btn.setOnClickListener {
            val videopage = Intent(this, FeedFragment::class.java)
            startActivity(videopage)
        }






    }
}
