package com.example.kotlinnodejsauth.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinnodejsauth.Common.Common
import com.example.kotlinnodejsauth.DetailGallery
import com.example.kotlinnodejsauth.Interface.IRecyclerOnClick
import com.example.kotlinnodejsauth.R
import com.example.kotlinnodejsauth.UploadPicPage
import com.example.kotlinnodejsauth.data.Photo
import com.squareup.picasso.Picasso

class MyPhotoAdapter(internal val context: Context?, internal val photoList:List<Photo>):RecyclerView.Adapter<MyPhotoAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int):MyViewHolder {
        val view  = LayoutInflater.from(context)
            .inflate(R.layout.item_picture,p0,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
        Picasso.get().load(photoList[p1].photo1).into(p0.picture_image)
        p0.picture_name.text = photoList[p1].pictitle
        Log.e("에러",photoList[p1].pictitle.toString())

        //사진볼래 게시물 클릭시
        p0.setClick(object : IRecyclerOnClick {
                    override fun onClcik(view: View, position: Int) {
                        Common.select_photo = photoList[position]
                        val intent = Intent(context,DetailGallery::class.java)
                       view.context.startActivity(intent)

            }

            override fun onLongClick(view: View, Position: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    inner class MyViewHolder (itemView:View) : RecyclerView.ViewHolder(itemView),View.OnClickListener{

        internal var picture_image : ImageView
        internal var picture_name : TextView
        internal lateinit var iRecyclerOnClick: IRecyclerOnClick

        fun setClick(iRecyclerOnClick: IRecyclerOnClick) {
            this.iRecyclerOnClick = iRecyclerOnClick
        }

        init{
            picture_image = itemView.findViewById(R.id.picture_image) as ImageView
            picture_name = itemView.findViewById(R.id.picture_name)  as TextView
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            iRecyclerOnClick.onClcik(v!!,adapterPosition)
        }


    }
}