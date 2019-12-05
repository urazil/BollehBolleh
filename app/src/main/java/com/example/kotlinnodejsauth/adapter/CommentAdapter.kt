package com.example.kotlinnodejsauth.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinnodejsauth.Interface.IRecyclerOnClick
import com.example.kotlinnodejsauth.R
import com.example.kotlinnodejsauth.data.CommentItem
import com.example.kotlinnodejsauth.data.Video

class CommentAdapter (internal val context: Context?, internal val commentList:List<CommentItem>): RecyclerView.Adapter<CommentAdapter.MyCommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): MyCommentViewHolder {
      val view = LayoutInflater.from(context)
          .inflate(R.layout.item_comment,parent,false)
        return MyCommentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }
    override fun onBindViewHolder(p0: MyCommentViewHolder, p1: Int) {
        p0.Comment_user.text = commentList[p1].name.toString()
        p0.Comment.text = commentList[p1].Comment.toString()
        p0.date.text = commentList[p1].date.toString()
    }



    inner class MyCommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var Comment_user: TextView
        internal var Comment: TextView
        internal var date: TextView
        init {
            Comment_user = itemView.findViewById(R.id.comment_id) as TextView
            Comment = itemView.findViewById(R.id.text_comment) as TextView
            date = itemView.findViewById(R.id.comment_time) as TextView
        }


    }
}