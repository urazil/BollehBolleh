package com.example.kotlinnodejsauth.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinnodejsauth.Common.Common
import com.example.kotlinnodejsauth.DetailFragment
import com.example.kotlinnodejsauth.Interface.IRecyclerOnClick
import com.example.kotlinnodejsauth.R
import com.example.kotlinnodejsauth.data.Video
import com.squareup.picasso.Picasso

class MyVideoAdapter( internal val context: Context?, internal val videoList:List<Video>):RecyclerView.Adapter<MyVideoAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int):MyViewHolder {
        val view  = LayoutInflater.from(context)
            .inflate(R.layout.item_feed,p0,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
     Picasso.get().load(videoList[p1].uriimage).into(p0.video_image)
        p0.video_name.text = videoList[p1].title
        p0.setClick(object :IRecyclerOnClick{
            override fun onClcik(view: View, position: Int) {
                view.findNavController().navigate(R.id.action_feedFragment_to_detailFragment)
                Common.select_video = videoList[position]

            }

            override fun onLongClick(view: View, Position: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    inner class MyViewHolder (itemView:View) : RecyclerView.ViewHolder(itemView),View.OnClickListener{

       internal var video_image : ImageView
       internal var video_name : TextView
       internal lateinit var iRecyclerOnClick: IRecyclerOnClick

        fun setClick(iRecyclerOnClick: IRecyclerOnClick) {
            this.iRecyclerOnClick = iRecyclerOnClick
        }

       init{
           video_image = itemView.findViewById(R.id.video_image) as ImageView
           video_name = itemView.findViewById(R.id.video_name)  as TextView
           itemView.setOnClickListener(this)
        }

       override fun onClick(v: View?) {
      iRecyclerOnClick.onClcik(v!!,adapterPosition)
       }


   }
}