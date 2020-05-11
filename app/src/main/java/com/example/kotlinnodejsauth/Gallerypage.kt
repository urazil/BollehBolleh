package com.example.kotlinnodejsauth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kotlinnodejsauth.Common.Common
import com.example.kotlinnodejsauth.Retrofit.ServiceApi
import com.example.kotlinnodejsauth.adapter.MyPhotoAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_gallery_page.*
import java.lang.StringBuilder

class GalleryPage : Fragment() {

    var compositeDisposable = CompositeDisposable()
    lateinit var myAPI: ServiceApi

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }
    override fun onResume() {
        super.onResume()
        fetchPhoto()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gallery_page, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       //Init API
        myAPI = Common.photoapi

        //사진볼래 게시물 작성하러가기 버튼 클릭시
        pic_upload_page_button.setOnClickListener {
            val editpic = Intent(context, UploadPicPage::class.java)
            startActivity(editpic)
        }
        return
    }
     // MySQL 에 저장된 DB를 확인한 후, 불러와서 사용자에게 리스트를 보여주고 게시물의 갯수에 맞게 숫자가 표시된다.
    private fun fetchPhoto() {
        compositeDisposable.add(
            myAPI.photoList
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ photoList ->
                    Log.d("메인 사진첩", photoList.toString())
                    txt_photo.text = StringBuilder("NEW PHOTO(")
                        .append(photoList.size)
                        .append(")")
                    recycler_photo.layoutManager = GridLayoutManager(context,2)
                    recycler_photo.adapter = MyPhotoAdapter(context, photoList)
                },
                    { thr ->
//                        Toast.makeText(context, "" + thr.message, Toast.LENGTH_SHORT).show()
                    })
        )

    }


}


