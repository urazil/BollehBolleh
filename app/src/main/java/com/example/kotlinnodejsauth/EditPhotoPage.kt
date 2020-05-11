package com.example.kotlinnodejsauth

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.ClipData
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.example.kotlinnodejsauth.Common.Common
import com.example.kotlinnodejsauth.Retrofit.RetrofitClient
import com.example.kotlinnodejsauth.Retrofit.ServiceApi
import com.example.takenotes.Utils.ProgressRequestBody
import com.ipaulpro.afilechooser.utils.FileUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_photo_page.*
import kotlinx.android.synthetic.main.activity_upload_pic_page.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response


class EditPhotoPage : AppCompatActivity(), ProgressRequestBody.UploadCallbacks {

    override fun onProgressUpdate(percentage: Int) {
        dialog.progress = percentage
    }

    val Base_URL = "http://10.0.2.2:3000/"
    val mPhoto: ServiceApi
        get() = RetrofitClient.getClient(Base_URL).create(ServiceApi::class.java)
    lateinit var mService: ServiceApi
    lateinit var dialog: ProgressDialog
    private val PERMISSION_REQUEST: Int = 1000
    private val PICTURE_REQUEST_CODE: Int = 100
    var imageList = ArrayList<Uri>()
    var images = ArrayList<MultipartBody.Part>()
    var uri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_photo_page)

        // 사용자 휴대폰의 저장소를 사용할수 있는 퍼미션 동의를 했을 경우 갤러리 사용 가능
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        )
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST
            )

        //Init API
        mService = mPhoto

        //사진 등록 클릭시
        edit_pic_image_view.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            // multiple 사진선택
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) // 이미지를 다중선택 할수있는 안드로이드 버전만 다중선택 가능.
            {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.setType("image/*")
                startActivityForResult(intent, PICTURE_REQUEST_CODE)
            }
            //사진 1장선택
            else {
                startActivityForResult(intent, PICTURE_REQUEST_CODE)
            }
        }
        //사진볼래 게시물등록 버튼 클릭시
        edit_pic_btn_upload.setOnClickListener { editpicFile(Common.select_photo?.id) }
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

    fun editpicFile(id: Int?) {
        images.clear()
        for (i in 0 until imageList.size) {
            images.add(uploadPicFile("files", imageList[i]))
            Log.d("images", images.toString())
        }
        //게시물 등록시 업로드 되는 상황을 dialog로 표시
        dialog = ProgressDialog(this)
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        dialog.setMessage("업로딩중")
        dialog.isIndeterminate = false
        dialog.setCancelable(false)
        dialog.max = 100
        dialog.show()
        if (id != null) {
            mPhoto.editPicFile(
                edit_picture_title.text.toString(),
                images,
                edit_picture_contents.text.toString(),
                id
            )
                .enqueue(object : retrofit2.Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.d("t", t.message.toString())
                        Toast.makeText(this@EditPhotoPage, t!!.message, Toast.LENGTH_SHORT).show()

                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        Toast.makeText(this@EditPhotoPage, "upload 성공", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                        finish()
                    }
                })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICTURE_REQUEST_CODE) {
                if (data != null) {
                    //기존 이미지 지우기
                    edit_image1.setImageResource(0)
                    edit_image1.visibility = View.GONE
                    edit_image2.setImageResource(0)
                    edit_image2.visibility = View.GONE
                    edit_image3.setImageResource(0)
                    edit_image3.visibility = View.GONE
                }

                //ClipData 또는 Uri를 가져온다
                val uri = data?.data
                val clipData = data?.clipData

                Log.e("uri", data?.data.toString())
                Log.e("clipData", data?.clipData.toString())

                //이미지 URI 를 이용하여 이미지뷰에 순서대로 세팅한다.
                if (data?.clipData == null) {
                    imageList.add(Uri.parse(data?.clipData.toString()))
                } else {
                    val clipData: ClipData = data?.clipData!!
                    if (clipData.itemCount > 3) {
                        Toast.makeText(
                            this,
                            "사진은 3개까지 선택가능 합니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    } else if (clipData.itemCount == 1) {
                        val dataStr = clipData.getItemAt(0).uri
                        edit_image1.setImageURI(clipData.getItemAt(0).uri)
                        edit_image1.visibility = View.VISIBLE
                        imageList.add(dataStr)
                    } else if (clipData.itemCount in 2..3) {
                        var i = 0
                        while (i < clipData.itemCount) {
                            when (i) {
                                0 -> {
                                    Picasso.get().load(clipData.getItemAt(i).uri)
                                        .resize(300, 300).into(edit_image1)
                                    edit_image1.visibility = View.VISIBLE
                                }
                                1 -> {
                                    Picasso.get().load(clipData.getItemAt(i).uri)
                                        .resize(300, 300).into(edit_image2)
                                    edit_image2.visibility = View.VISIBLE
                                }
                                2 -> {
                                    Picasso.get().load(clipData.getItemAt(i).uri)
                                        .resize(300, 300).into(edit_image3)
                                    edit_image3.visibility = View.VISIBLE
                                }
                            }
                            imageList.add(clipData.getItemAt(i).uri)
                            i++

                        }
                    }
                }
            }
        } else {
            Toast.makeText(this, "파일 선택을 다시 해주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    @NonNull
    private fun uploadPicFile(partName: String, fileUri: Uri): MultipartBody.Part {
        val file = FileUtils.getFile(this, fileUri)
        val requestFile = ProgressRequestBody(file, this)
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

}
