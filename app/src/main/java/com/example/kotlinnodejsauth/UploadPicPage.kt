package com.example.kotlinnodejsauth

import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_upload_pic_page.*


class UploadPicPage : AppCompatActivity() {

    private val PICTURE_REQUEST_CODE:Int = 100





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_pic_page)

        pic_image_view.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            //사진을 여러개 선택할수 있도록 한다
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.setType("image/*")
            startActivityForResult(Intent.createChooser(intent, "Select Picture"),  PICTURE_REQUEST_CODE)

        }


        pic_btn_upload.setOnClickListener {
            val pic_upload = Intent(this, Gallerypage::class.java)
            startActivity(pic_upload)
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if(requestCode == PICTURE_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {

                //기존 이미지 지우기
                image1.setImageResource(0)
                image2.setImageResource(0)
                image3.setImageResource(0)

                //ClipData 또는 Uri를 가져온다
                val uri = data?.getData()
                val clipData = data?.getClipData()

                //이미지 URI 를 이용하여 이미지뷰에 순서대로 세팅한다.
                if (clipData != null) {
                    for (i in 0..2) {
                        if (i < clipData.getItemCount()) {
                            val urione: Uri = clipData.getItemAt(i).getUri()
                            when (i) {
                                0 -> image1.setImageURI(urione)
                                1 -> image2.setImageURI(urione)
                                2 -> image3.setImageURI(urione)
                            }
                        }
                    }
                }
                else if(uri != null)
                {
                    image1.setImageURI(uri)
                }
            }
        }
    }






}










