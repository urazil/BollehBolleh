package com.example.kotlinnodejsauth

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinnodejsauth.Common.Common
import com.example.kotlinnodejsauth.Retrofit.CommentAPI
import com.example.kotlinnodejsauth.Retrofit.INodeJS
import com.example.kotlinnodejsauth.Retrofit.VideoAPI
import com.example.kotlinnodejsauth.adapter.CommentAdapter
import com.example.kotlinnodejsauth.data.User
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail_video.*
import java.lang.StringBuilder

class DetailVideo : AppCompatActivity() {

    var compositeDisposable = CompositeDisposable()
    lateinit var VideoAPI: VideoAPI
    lateinit var CommentAPI: CommentAPI
    private lateinit var controller: android.widget.MediaController
    private var mCurrentPosition = 0
    private val PLAYBACK_TIME = "play_time"
    private val VIDEO_SAMPLE = Common.select_video?.urivideo.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_video)

        //Init API
        VideoAPI = Common.api
        CommentAPI = Common.apii

        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME);
        }

        //MediaController의 위치지정 및 표시
        controller = android.widget.MediaController(this)
        controller.setPadding(0, 0, 0, 1000)
        controller.setMediaPlayer(vv_video)

        //SharePreference에 저장한 로그인 정보 가져오기
        val pref = this!!.getSharedPreferences("UserId", Context.MODE_PRIVATE)
        var userID = pref.getString("id", 0.toString())
        val pref1 = this!!.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        var userEmail = pref1.getString(userID, null)
        var gson = Gson()
        var UserDTO = gson.fromJson(userEmail, User::class.java)

        //댓글 작성 등록시
        comment_upload.setOnClickListener {
            uploadcomment(
                Common.select_video?.id,
                edit_comment.text.toString(),
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
        //댓글 등록시 보여진다.
        recyclerviewcomment(Common.select_video?.id)
        super.onResume()

        //영상볼래 제목과 내용불러오기
        video_title.text = Common.select_video?.title
        tv_destination.text = Common.select_video?.contents

        //영상볼래 게시물 삭제 클릭시
        video_delete.setOnClickListener {
            Toast.makeText(this, Common.select_video?.id.toString(), Toast.LENGTH_SHORT).show()
            deleteVideofile(Common.select_video?.id)
            val intent = Intent(this, VideoPage::class.java)
            startActivity(intent)
        }

        //영상볼래 게시물 수정 클릭시
        video_modify.setOnClickListener {
            val intent = Intent(this, EditVideoPage::class.java)
            startActivity(intent)

        }
    }

    override fun onStart() {
        super.onStart()
        //영상 재생
        initializePlayer()
    }

    override fun onPause() {
        super.onPause()
        //SDK VERSION이 낮을시 영상정지
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            vv_video.pause()
        }
        mCurrentPosition = vv_video.currentPosition
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(PLAYBACK_TIME, mCurrentPosition)
    }

    override fun onStop() {
        super.onStop()
        //다른 화면으로 넘어갈시 영상중지
        releasePlayer()
    }

    //raw폴더의 위치를 가져다가 만들어준다.
    private fun getMedia(mediaName: String): Uri {
        return Uri.parse(Common.select_video?.urivideo)
    }

    private fun initializePlayer() {
        val videoUri = getMedia(VIDEO_SAMPLE)
        vv_video.setVideoURI(videoUri)
        vv_video.setMediaController(controller)
        checkIfVideoWasPlaying()
        vv_video.start()
        vv_video.setOnCompletionListener {
            Toast.makeText(this, "재생 끝", Toast.LENGTH_SHORT).show()
            //동영상 끝나고 재시작
            vv_video.seekTo(1)
            vv_video.start()
        }
    }

    private fun checkIfVideoWasPlaying() {
        if (mCurrentPosition > 0) {
            vv_video.seekTo(mCurrentPosition)
        } else {
            vv_video.seekTo(1)
        }
    }

    private fun releasePlayer() {
        vv_video.stopPlayback()
    }
    //서버와 통신하여 작성한 댓글을 DB에 저장한다.
    private fun uploadcomment(
        id: Int?,
        Comment: String?,
        email: String?,
        name: String?
    ) {
        compositeDisposable.add(CommentAPI.CommentFile(id, Comment, email, name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { message ->
                edit_comment.setText("")
                Toast.makeText(this, "댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                recyclerviewcomment(Common.select_video?.id)
            })
    }
    //댓글 작성시 현재 댓글의 수가 표시 되고 댓글이 보여진다.
    private fun recyclerviewcomment(id: Int?) {
        compositeDisposable.add(
            CommentAPI.commentdata(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ CommentItem ->
                    Log.d("댓글", CommentItem.toString())
                    txt_comment.text = StringBuilder("댓글(")
                        .append(CommentItem.size)
                        .append(")")
                    comment_recyclerview.layoutManager = LinearLayoutManager(this)
                    comment_recyclerview.adapter = CommentAdapter(this, CommentItem)
                    comment_recyclerview.setHasFixedSize(true)


                },
                    { thr ->
                        Toast.makeText(this, "" + thr.message, Toast.LENGTH_SHORT).show()

                    })
        )

    }
    //서버와 통신하여 영상 게시물을 삭제한다.
    private fun deleteVideofile(id: Int?) {
        compositeDisposable.add(
            VideoAPI.deleteFile(id)
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



}

