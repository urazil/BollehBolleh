package com.example.kotlinnodejsauth.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinnodejsauth.R
import com.example.kotlinnodejsauth.VIDEO_ID
import com.example.kotlinnodejsauth.data.DummyItem
import kotlinx.android.synthetic.main.item_feed.view.*

class FeedAdapter(private val dataSet:MutableList <DummyItem>) : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, position:Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent,false)
        return  ViewHolder(view)
    }

    override fun getItemCount(): Int {
     return dataSet.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
      val item = dataSet[position]
        viewHolder.bind(item)
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item : DummyItem) = with(view){
       view.setOnClickListener{
           var data = Bundle()
           data.putString(VIDEO_ID, item.videoID)
           view.findNavController().navigate(R.id.action_feedFragment_to_detailFragment, data)
       }
        }
    }
}