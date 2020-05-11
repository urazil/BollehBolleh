package com.example.kotlinnodejsauth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinnodejsauth.Common.Common
import com.example.kotlinnodejsauth.Retrofit.VideoAPI
import com.example.kotlinnodejsauth.Service.PicassoImageLoadingService
import com.example.kotlinnodejsauth.adapter.MyVideoAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_video_page.*
import ss.com.bannerslider.Slider
import java.lang.StringBuilder

class VideoPage : Fragment() {

    var compositeDisposable = CompositeDisposable()
    lateinit var VideoAPI: VideoAPI

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    override fun onResume() {
        super.onResume()
        fetchVideo()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_video_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //영상볼래 게시물 등록하러가기 버튼
        upload_Video.setOnClickListener {
            val Intent = Intent(context, UploadPage::class.java)
            startActivity(Intent)
        }

        //Init API
        VideoAPI = Common.api
        Slider.init(PicassoImageLoadingService(this))
        return
    }

    private fun fetchVideo() {
        compositeDisposable.add(
            VideoAPI.videoList
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ videoList ->
                    Log.d("메인 비디오", videoList.toString())
                    txt_video2.text = StringBuilder("New Video(")
                        .append(videoList.size)
                        .append(")")
                    recycler_video.layoutManager = LinearLayoutManager(context)
                    recycler_video.adapter = MyVideoAdapter(context, videoList)
                },
                    { thr ->
//                        Toast.makeText(context, "" + thr.message, Toast.LENGTH_SHORT).show()
                    })
        )
    }
}
