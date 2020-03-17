package com.example.kotlinnodejsauth


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinnodejsauth.Common.Common
import com.example.kotlinnodejsauth.Retrofit.ServiceApi
import com.example.kotlinnodejsauth.Service.PicassoImageLoadingService
//import com.example.kotlinnodejsauth.adapter.FeedAdapter
import com.example.kotlinnodejsauth.adapter.MyVideoAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_feed.*
import ss.com.bannerslider.Slider
import java.lang.StringBuilder

/**
 * A simple [Fragment] subclass.
 */
class FeedFragment : Fragment() {

    var compositeDisposable = CompositeDisposable()
    lateinit var myAPI: ServiceApi

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Init API
        myAPI = Common.serviceapi





        Slider.init(PicassoImageLoadingService(this))


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onResume() {
        super.onResume()





        swipe_refresh.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_orange_dark,
            android.R.color.background_dark
        )
        swipe_refresh.setOnRefreshListener {
            if (Common.isConnectedToInternet(activity)) {
                fetchVideo()
            } else {
                Toast.makeText(context, "pleas check your connection", Toast.LENGTH_SHORT).show()
            }

        }


        upload_page_button.setOnClickListener {
            val Intent = Intent(context, UploadPage::class.java)
            startActivity(Intent)
        }

        pic_gallery_btn.setOnClickListener {
            val gallerypageintet = Intent(context, UploadPage::class.java)
            startActivity(gallerypageintet)
        }



    }


    private fun fetchVideo() {
        compositeDisposable.add(
            myAPI.videoList
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ videoList ->
                    Log.d("메인 비디오", videoList.toString())
                    txt_video.text = StringBuilder("New Video(")
                        .append(videoList.size)
                        .append(")")
                    recycler_video.layoutManager = LinearLayoutManager(context)
                    recycler_video.adapter = MyVideoAdapter(activity, videoList)
                },
                    { thr ->
                        Toast.makeText(activity, "" + thr.message, Toast.LENGTH_SHORT).show()

                    })
        )

    }

}

