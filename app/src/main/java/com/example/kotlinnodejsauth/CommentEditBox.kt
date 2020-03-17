package com.example.kotlinnodejsauth

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.kotlinnodejsauth.Common.Common
import com.example.kotlinnodejsauth.Retrofit.ServiceApi
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_comment_edit_box.*

class CommentEditBox : AppCompatActivity() {

    lateinit var commentapi: ServiceApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_edit_box)

        commentapi = Common.serviceapi


        comment_edit.setOnClickListener {
            editcomment(Common.select_comment?.idxx, Common.select_comment?.id)
        }
        comment_delete.setOnClickListener {
            deletecomment(Common.select_comment?.idxx)
            finish()
        }


    }

    @SuppressLint("CheckResult")
    fun editcomment(idxx: Int?, id: Int?) {
        val pref = this.getSharedPreferences("UserId", Context.MODE_PRIVATE)
        var userID = pref.getString("id", 0.toString())
        val pref1 = this.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        var userEmail = pref1.getString(userID, null)
        var gson = Gson()
        var UserDTO = gson.fromJson(userEmail, User::class.java)

        commentapi.editcomment(
            idxx, id, edittext_comment.text.toString(), UserDTO.email,
            Common.select_comment?.name, Common.select_comment?.date
        )
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
    fun deletecomment(idxx: Int?) {
        commentapi.deletecomment(idxx)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { message ->
                Toast.makeText(
                    this,
                    "삭제하였습니다.",
                    Toast.LENGTH_SHORT
                ).show()

            }
    }


}
