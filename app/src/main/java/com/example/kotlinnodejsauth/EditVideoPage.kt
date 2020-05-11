package com.example.kotlinnodejsauth

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.example.kotlinnodejsauth.Common.Common
import com.example.kotlinnodejsauth.Retrofit.RetrofitClient
import com.example.kotlinnodejsauth.Retrofit.VideoAPI
import com.example.takenotes.Utils.ProgressRequestBody
import com.ipaulpro.afilechooser.utils.FileUtils
import kotlinx.android.synthetic.main.activity_edit_video_page.*
import kotlinx.android.synthetic.main.activity_upload_page.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response

class EditVideoPage : AppCompatActivity(), ProgressRequestBody.UploadCallbacks {
    override fun onProgressUpdate(percentage: Int) {
        dialog.progress = percentage
    }

    val BASE_URL = "http://10.0.2.2:3000/"

    val apiUpload: VideoAPI
        get() = RetrofitClient.getClient(BASE_URL).create(VideoAPI::class.java)

    private val PERMISSION_REQUEST: Int = 1000
    private val PICK_IMAGE_REQUEST: Int = 1001
    private val PICK_VIDEO_REQUEST: Int = 1002

    lateinit var mService: VideoAPI

    private var selectedEditFileUri: Uri? = null
    private var selectedEditvideo: Uri? = null

    lateinit var dialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_video_page)
        // 사용자 휴대폰의 저장소를 사용할수 있는 퍼미션 동의를 했을 경우 비디오저장소 사용 가능
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        )
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST
            )
        //Init API
        mService = apiUpload

        //영상볼래 썸네일 사진등록 버튼
        edit_image_view.setOnClickListener { chooseEditImage() }

        //영상볼래 영상등록 버튼 클릭시
        edit_video_view.setOnClickListener { chooseEditVideo() }

        //영상 볼래 게시물등록 버튼 클릭시
        edit_btn_upload.setOnClickListener { uploadFile(Common.select_video?.id) }

        //영상내용쓰는 edittext 여러줄 기능
        video_contents.setHorizontallyScrolling(false)

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

    private fun uploadFile(id: Int?) {
        if (selectedEditFileUri != null) {
            //게시물 등록시 업로드 되는 상황을 dialog로 표시
            dialog = ProgressDialog(this)
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            dialog.setMessage("업로딩중")
            dialog.isIndeterminate = false
            dialog.setCancelable(false)
            dialog.max = 100
            dialog.show()
            val file = FileUtils.getFile(this@EditVideoPage, selectedEditFileUri)
            val file1 = FileUtils.getFile(this, selectedEditvideo)
            val requestFile = ProgressRequestBody(file, this@EditVideoPage)
            val requestFile1 = ProgressRequestBody(file1, this@EditVideoPage)
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
            val body2 = MultipartBody.Part.createFormData("file", file1.name, requestFile1)
            Thread(Runnable {
                if (id != null) {
                    mService.editFile(
                        edit_ssumnail_title.text.toString(),
                        body,
                        body2,
                        edit_video_contents.text.toString(),
                        id
                    )
                        .enqueue(object : retrofit2.Callback<String> {
                            override fun onFailure(call: Call<String>, t: Throwable) {
                                dialog.dismiss()
                                Toast.makeText(this@EditVideoPage, t!!.message, Toast.LENGTH_SHORT)
                                    .show()
                                finish()
                            }

                            override fun onResponse(
                                call: Call<String>,
                                response: Response<String>
                            ) {
                                Toast.makeText(this@EditVideoPage, "upload 성공", Toast.LENGTH_SHORT)
                                    .show()
                                dialog.dismiss()

                                var intent = Intent(this@EditVideoPage, LandingPage::class.java)
                                startActivity(intent)
                                finish()
                            }
                        })
                }
            }).start()
        } else {
            Toast.makeText(this, "파일을 선택하세요", Toast.LENGTH_SHORT).show()
        }
    }

    private fun chooseEditImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        try {
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        } catch (e: NumberFormatException) {

        }

    }

    private fun chooseEditVideo() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setType("video/*")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        try {
            startActivityForResult(intent, PICK_VIDEO_REQUEST);
        } catch (e: NumberFormatException) {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                if (data != null) {
                    selectedEditFileUri = data.data
                    if (selectedEditFileUri != null && selectedEditFileUri!!.path!!.isNotEmpty())
                        edit_image_view.setImageURI(selectedEditFileUri)
                }
            } else if (requestCode == PICK_VIDEO_REQUEST) {
                if (data != null) {
                    selectedEditvideo = data.data
                    if (selectedEditvideo != null && selectedEditvideo!!.path!!.isNotEmpty())
                        edit_video_view.setVideoURI(selectedEditvideo)
                }

            }
        }
    }

    //요청 결과 수신
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST -> {
                //카메라 권한이 허용된 경우
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "허용", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this, "거부", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
