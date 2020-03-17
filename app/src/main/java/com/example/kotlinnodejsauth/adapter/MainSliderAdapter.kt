package com.example.kotlinnodejsauth.adapter

import com.example.kotlinnodejsauth.data.Video
import ss.com.bannerslider.adapters.SliderAdapter
import ss.com.bannerslider.viewholder.ImageSlideViewHolder

class MainSliderAdapter(private val VideoList: List<Video>) : SliderAdapter() {
    override fun getItemCount(): Int {
        return VideoList.size
    }

    override fun onBindImageSlide(position: Int, imageSlideViewHolder: ImageSlideViewHolder?) {
        imageSlideViewHolder!!.bindImageSlide(VideoList[position].uriimage)
    }
}