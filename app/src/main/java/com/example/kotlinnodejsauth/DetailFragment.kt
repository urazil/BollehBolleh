package com.example.kotlinnodejsauth


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinnodejsauth.Common.Common
import com.example.kotlinnodejsauth.Retrofit.ServiceApi
//import com.example.kotlinnodejsauth.Retrofit.CommentAPI
import com.example.kotlinnodejsauth.adapter.CommentAdapter
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_detail.*
import java.lang.StringBuilder


const val VIDEO_ID = "videoID"

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {
    var compositeDisposable = CompositeDisposable()
    lateinit var myAPI: ServiceApi
    private lateinit var videoID: String
    private lateinit var controller: android.widget.MediaController
    private var mCurrentPosition = 0
    private val PLAYBACK_TIME = "play_time"

    private val VIDEO_SAMPLE = Common.select_video?.urivideo.toString()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        myAPI = Common.serviceapi
        arguments?.let {
            videoID = it.getString(VIDEO_ID).toString()
        }

        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME);
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myAPI = Common.serviceapi

        controller = android.widget.MediaController(context)
        controller.setPadding(0, 0, 0, 1000)
        controller.setMediaPlayer(vv_video)


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onResume() {
        super.onResume()
        val pref = this.activity!!.getSharedPreferences("UserId", Context.MODE_PRIVATE)
        var userID = pref.getString("id", 0.toString())
        val pref1 = this.activity!!.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        var userEmail = pref1.getString(userID, null)
        var gson = Gson()
        var UserDTO = gson.fromJson(userEmail, User::class.java)



        tv_destination.text = Common.select_video?.contents
        recyclerviewcomment(Common.select_video?.id)
        video_delete.setOnClickListener {
            Toast.makeText(activity, Common.select_video?.id.toString(), Toast.LENGTH_SHORT).show()
            deleteVideofile(Common.select_video?.id)
            view!!.findNavController().navigate(R.id.action_detailFragment_to_FeedFragment)
        }

        video_modify.setOnClickListener {
            val intent = Intent(context, EditVideoPage::class.java)
            startActivity(intent)
        }











        comment_upload.setOnClickListener {
            uploadcomment(
                Common.select_video?.id,
                edit_comment.text.toString(),
                UserDTO.email,
                UserDTO.name
            )
        }


    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    override fun onPause() {
        super.onPause()
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
            Toast.makeText(context, "재생 끝", Toast.LENGTH_SHORT).show()
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

    private fun uploadcomment(
        id: Int?,
        Comment: String?,
        email: String?,
        name: String?
    ) {
        compositeDisposable.add(myAPI.CommentFile(id, Comment, email, name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { message ->
                edit_comment.setText("")
                Toast.makeText(this.context, "댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                recyclerviewcomment(Common.select_video?.id)
            })
    }

    private fun recyclerviewcomment(id: Int?) {

        compositeDisposable.add(
            myAPI.commentdata(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ CommentItem ->
                    Log.d("댓글", CommentItem.toString())
                    txt_comment.text = StringBuilder("댓글(")
                        .append(CommentItem.size)
                        .append(")")
                    comment_recyclerview.layoutManager = LinearLayoutManager(context)
                    comment_recyclerview.adapter = CommentAdapter(activity, CommentItem)
                    comment_recyclerview.setHasFixedSize(true)


                },
                    { thr ->
                        Toast.makeText(activity, "" + thr.message, Toast.LENGTH_SHORT).show()

                    })
        )

    }


    private fun deleteVideofile(id: Int?) {
        compositeDisposable.add(
            myAPI.deleteFile(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ message ->

                    Toast.makeText(activity, "파일을 삭제하였습니다.", Toast.LENGTH_SHORT)
                        .show()

                },

                    { thr ->
                        Toast.makeText(activity, "" + thr.message, Toast.LENGTH_SHORT).show()

                    })
        )


    }


}
