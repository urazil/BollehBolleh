package com.example.kotlinnodejsauth.Common

import com.example.kotlinnodejsauth.Retrofit.*
import com.example.kotlinnodejsauth.data.CommentItem
import com.example.kotlinnodejsauth.data.GalleryCommentItem
import com.example.kotlinnodejsauth.data.Photo
import com.example.kotlinnodejsauth.data.Video

object Common {


    var select_video: Video? = null
    var select_comment: CommentItem? = null
    var select_photo: Photo? = null
    var select_gallery_comment : GalleryCommentItem? = null
    // SDK버젼에 따른 네트워크 연결상태 확인
//    fun isConnectedToInternet(context: FragmentActivity?): Boolean {
//        val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        if (cm != null) {
//            if (Build.VERSION.SDK_INT < 23) {
//                val ni = cm.activeNetworkInfo
//                if (ni != null)
//                    return ni.isConnected && (ni.type == ConnectivityManager.TYPE_WIFI ||
//                            ni.type == ConnectivityManager.TYPE_MOBILE)
//
//            } else {
//                val n = cm.activeNetwork
//                if (n != null) {
//                    val nc = cm.getNetworkCapabilities(n)
//                    return nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
//                            nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
//                }
//            }
//        }
//        return false
//    }

    val api: VideoAPI
        get() {
            val retrofit = RetrofitClient.instance
            return retrofit.create(VideoAPI::class.java)
        }

    val apii: CommentAPI
        get() {
            val retrofit = RetrofitClient.instance
            return retrofit.create(CommentAPI::class.java)
        }

    val photoapi: ServiceApi
    get(){
        val retrofit = RetrofitClient.instance
        return retrofit.create(ServiceApi::class.java)
    }



}