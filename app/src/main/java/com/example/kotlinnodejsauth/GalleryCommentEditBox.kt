package com.example.kotlinnodejsauth

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.kotlinnodejsauth.Common.Common
import com.example.kotlinnodejsauth.Retrofit.CommentAPI
import com.example.kotlinnodejsauth.data.User
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_comment_edit_box.*
import kotlinx.android.synthetic.main.activity_gallery_comment_edit_box.*

class GalleryCommentEditBox : AppCompatActivity() {

    lateinit var commentapi: CommentAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_comment_edit_box)

       //iit API
        commentapi = Common.apii

        //사진볼래 댓글 수정버튼 클릭시
        gallery_comment_edit.setOnClickListener {
            galleryeditcomment(Common.select_gallery_comment?.idxx, Common.select_gallery_comment?.id)
        }
        //사진볼래 댓글 삭제버튼 클릭시
        gallery_comment_delete.setOnClickListener {
            gallerydeletecomment(Common.select_gallery_comment?.idxx)

        }
    }

    @SuppressLint("CheckResult")
    fun galleryeditcomment(idxx: Int?, id: Int?) {
        val pref = this.getSharedPreferences("UserId", Context.MODE_PRIVATE)
        var userID = pref.getString("id", 0.toString())
        val pref1 = this.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        var userEmail = pref1.getString(userID, null)
        var gson = Gson()
        var UserDTO = gson.fromJson(userEmail, User::class.java)

        commentapi.galleryeditcomment(idxx, id, gallery_edittext_comment.text.toString(), UserDTO.email,Common.select_gallery_comment?.name, Common.select_gallery_comment?.date)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { message ->
                Toast.makeText(this, "수정하였습니다.", Toast.LENGTH_SHORT).show()
                // 키보드를 내린다.
                val imm =
                    this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(edittext_comment.windowToken, 0)
                finish()


            }
    }

    @SuppressLint("CheckResult")
    fun gallerydeletecomment(idxx: Int?) {
        commentapi.gallerydeletecomment(idxx)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { message ->
                Toast.makeText(this,"삭제하였습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }

    }
    }

