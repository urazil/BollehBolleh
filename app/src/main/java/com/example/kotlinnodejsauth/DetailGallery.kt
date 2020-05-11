package com.example.kotlinnodejsauth

import android.app.Service
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinnodejsauth.Common.Common
import com.example.kotlinnodejsauth.Retrofit.CommentAPI
import com.example.kotlinnodejsauth.Retrofit.INodeJS
import com.example.kotlinnodejsauth.Retrofit.RetrofitClient
import com.example.kotlinnodejsauth.Retrofit.ServiceApi
import com.example.kotlinnodejsauth.adapter.CommentAdapter
import com.example.kotlinnodejsauth.adapter.GalleryCommentAdapter
import com.example.kotlinnodejsauth.data.GalleryCommentItem
import com.example.kotlinnodejsauth.data.User
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail_gallery.*
import kotlinx.android.synthetic.main.activity_detail_video.*
import java.lang.StringBuilder

class DetailGallery : AppCompatActivity() {
    var compositeDisposable = CompositeDisposable()
    lateinit var photoapi: ServiceApi
    lateinit var CommentAPI: CommentAPI




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_gallery)
        //Init API
        CommentAPI = Common.apii
        photoapi = Common.photoapi


       //picasso를 사용하여 image 크기 조정
        Picasso.get().load(Common.select_photo?.photo1).resize(700, 700).into(detail_photo1)
        Picasso.get().load(Common.select_photo?.photo2).resize(700, 700).into(detail_photo2)
        Picasso.get().load(Common.select_photo?.photo3).resize(700, 700).into(detail_photo3)

        //사진볼래 제목과 내용 가져오기
        photo_title.text = Common.select_photo?.pictitle
        photo_destination.text = Common.select_photo?.piccontents


        //preference 로그인 정보 가져오기
        val pref = this!!.getSharedPreferences("UserId", Context.MODE_PRIVATE)
        var userID = pref.getString("id", 0.toString())
        val pref1 = this!!.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        var userEmail = pref1.getString(userID, null)
        var gson = Gson()
        var UserDTO = gson.fromJson(userEmail, User::class.java)

        //사진볼래 댓글작성 시
        gallery_datgle_btn.setOnClickListener {
            galleryuploadcomment(
                Common.select_photo?.id,
                gallery_datgle.text.toString(),
                UserDTO.email,
                UserDTO.name
            )
        }

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


    override fun onResume() {
        //댓글 작성한후 가져오기
        recyclerviewcomment(Common.select_photo?.id)
        super.onResume()

        //사진 게시물 수정 클릭시
        photo_modify.setOnClickListener {
            val intent = Intent(this, EditPhotoPage::class.java)
            startActivity(intent)
            finish()
        }

        //사진 게시물 삭제 클릭시
        photo_delete.setOnClickListener {
            Toast.makeText(this,Common.select_photo?.id.toString(), Toast.LENGTH_SHORT).show()
            deletePhotofile(Common.select_photo?.id)
            val intent = Intent(this,GalleryPage::class.java)
            startActivity(intent)
        }
//        Picasso.get().load(Common.select_photo?.photo1).resize(700, 700).into(detail_photo1)
//        Picasso.get().load(Common.select_photo?.photo2).resize(700, 700).into(detail_photo2)
//        Picasso.get().load(Common.select_photo?.photo3).resize(700, 700).into(detail_photo3)
    }

    //서버와 통신하여 사진게시물을 삭제한다.
    private fun deletePhotofile(id: Int?) {
        compositeDisposable.add(
            photoapi.deletePhotoFile(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ message ->

                    Toast.makeText(this, "파일을 삭제하였습니다.", Toast.LENGTH_SHORT)
                        .show()
                },
                    { thr ->
                        Toast.makeText(this, "" + thr.message, Toast.LENGTH_SHORT).show()
                    })
        )
    }

    //댓글 작성시 현재 댓글의 수가 표시 되고 댓글이 보여진다.
    private fun recyclerviewcomment(id: Int?) {
        compositeDisposable.add(
            CommentAPI.gallerycommentdata(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ GalleryCommentItem ->
                    Log.d("댓글", GalleryCommentItem.toString())
                    gallery_txt_comment.text = StringBuilder("댓글(")
                        .append(GalleryCommentItem.size)
                        .append(")")
                    gallery_comment_recyclerview.layoutManager = LinearLayoutManager(this)
                    gallery_comment_recyclerview.adapter = GalleryCommentAdapter(this, GalleryCommentItem)
                    gallery_comment_recyclerview.setHasFixedSize(true)


                },
                    { thr ->
                        Toast.makeText(this, "" + thr.message, Toast.LENGTH_SHORT).show()

                    })
        )

    }
    //서버와 통신하여 작성한 댓글을 DB에 저장한다.
    private fun galleryuploadcomment(
        id: Int?,
        Comment: String?,
        email: String?,
        name: String?
    ) {
        compositeDisposable.add(
            CommentAPI.GalleryCommentFile(id, Comment, email, name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { message ->
                gallery_datgle.setText("")
                Toast.makeText(this, "댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                recyclerviewcomment(Common.select_photo?.id)
            })
    }

}
