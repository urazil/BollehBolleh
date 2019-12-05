package com.example.kotlinnodejsauth

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.kotlinnodejsauth.Retrofit.IUploadAPI
import com.example.kotlinnodejsauth.Retrofit.RetrofitClient
import com.example.kotlinnodejsauth.util.ProgressRequestBody
import com.ipaulpro.afilechooser.utils.FileUtils
import kotlinx.android.synthetic.main.activity_upload_page.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response

class UploadPage : AppCompatActivity(), ProgressRequestBody.UploadCallbacks {
    override fun onProgressUpdate(percentage: Int) {
        dialog.progress = percentage
    }


    val BASE_URL = "http://10.0.2.2:3000/"

    val apiUpload: IUploadAPI
    get() = RetrofitClient.getClient(BASE_URL).create(IUploadAPI::class.java)

    private val PERMISSION_REQUEST: Int = 1000
    private val PICK_IMAGE_REQUEST: Int = 1001
    private  val PICK_VIDEO_REQUEST: Int = 1002

    lateinit var mService:IUploadAPI

    private var selectedFileUri: Uri? = null
    private var selectedvideo: Uri? = null

    lateinit var dialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_page)


        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),PERMISSION_REQUEST)

       // 서비스
        mService = apiUpload

       image_view.setOnClickListener{chooseImage()

       }

        video_view.setOnClickListener {chooseVideo()

        }




        btn_upload.setOnClickListener{uploadFile()}


    }

    private fun uploadFile() {
       if(selectedFileUri != null){
           dialog = ProgressDialog(this)
           dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
           dialog.setMessage("업로딩중")
           dialog.isIndeterminate = false
           dialog.setCancelable(false)
           dialog.max = 100
           dialog.show()

           val file = FileUtils.getFile(this@UploadPage,selectedFileUri)
           val file1 = FileUtils.getFile(this,selectedvideo)
           val requestFile = ProgressRequestBody(file,this@UploadPage)
           val requestFile1 = ProgressRequestBody(file1,this@UploadPage)

           val body = MultipartBody.Part.createFormData("file",file.name,requestFile)
           val body2 = MultipartBody.Part.createFormData("file",file1.name,requestFile1)

        Thread(Runnable {

            mService.uploadFile(ssumnail_title.text.toString(),body,body2,video_contents.text.toString())
                .enqueue(object:retrofit2.Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        dialog.dismiss()
                        Toast.makeText(this@UploadPage,t!!.message,Toast.LENGTH_SHORT).show()
                        finish()
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        Toast.makeText(this@UploadPage,"upload 성공",Toast.LENGTH_SHORT).show()
                        dialog.dismiss()

                        var intent = Intent(this@UploadPage,LandingPage::class.java)
                        startActivity(intent)
                        finish()
                    }

                })
        }).start()

       }
        else{
           Toast.makeText(this,"파일을 선택하세요", Toast.LENGTH_SHORT).show()
       }
    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        try{
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }catch (e: NumberFormatException){

        }
//        val getContentIntent = com.ipaulpro.afilechooser.utils.FileUtils.createGetContentIntent()
//        val intent = Intent.createChooser(getContentIntent,"Select a file")
//        startActivityForResult(intent,PICK_IMAGE_REQUEST)
        }

    private fun chooseVideo() {
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
                    selectedFileUri = data.data
                    if (selectedFileUri != null && !selectedFileUri!!.path!!.isEmpty())
                        image_view.setImageURI(selectedFileUri)
                }
            } else if (requestCode == PICK_VIDEO_REQUEST) {
                if (data != null) {
                    selectedvideo = data.data
                    if (selectedvideo != null && !selectedvideo!!.path!!.isEmpty())
                        video_view.setVideoURI(selectedvideo)
                }

            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_REQUEST -> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Granted",Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this, "Denied",Toast.LENGTH_SHORT).show()
            }
        }
    }
}