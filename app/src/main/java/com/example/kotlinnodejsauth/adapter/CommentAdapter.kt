package com.example.kotlinnodejsauth.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.*
import android.view.LayoutInflater

import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinnodejsauth.CommentEditBox
import com.example.kotlinnodejsauth.Common.Common
import com.example.kotlinnodejsauth.Interface.IRecyclerOnClick
import com.example.kotlinnodejsauth.R

import com.example.kotlinnodejsauth.User
import com.example.kotlinnodejsauth.data.CommentItem
import com.google.gson.Gson


class CommentAdapter(internal val context: Context?, internal val commentList: List<CommentItem>) :
    RecyclerView.Adapter<CommentAdapter.MyCommentViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCommentViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_comment, parent, false)
        return MyCommentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    override fun onBindViewHolder(p0: MyCommentViewHolder, p1: Int) {


        p0.Comment_user.text = commentList[p1].name.toString()
        p0.Comment.text = commentList[p1].Comment.toString()
        p0.date.text = commentList[p1].date.toString()

        p0.setClick(object : IRecyclerOnClick {
            override fun onClcik(view: View, position: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onLongClick(view: View, Position: Int) {
                Common.select_comment = commentList[Position]


                val pref = view.context!!.getSharedPreferences("UserId", Context.MODE_PRIVATE)
                var userID = pref.getString("id", 0.toString())
                val pref1 = view.context!!.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
                var userEmail = pref1.getString(userID, null)
                var gson = Gson()
                var UserDTO = gson.fromJson(userEmail, User::class.java)


                if (commentList[p1].email == UserDTO.email) {
                    val intent = Intent(view.context, CommentEditBox::class.java)
                    view.context.startActivity(intent)
                } else {
                    Toast.makeText(context, "작성자가 아닙니다.", Toast.LENGTH_SHORT).show()
                }

            }

        })

    }


    inner class MyCommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnLongClickListener {


        override fun onLongClick(v: View?): Boolean {
            iRecyclerOnClick.onLongClick(v!!, adapterPosition)
            return true
        }

        fun setClick(iRecyclerOnClick: IRecyclerOnClick) {
            this.iRecyclerOnClick = iRecyclerOnClick
        }

        internal var Comment_user: TextView
        internal var Comment: TextView
        internal var date: TextView
        internal lateinit var iRecyclerOnClick: IRecyclerOnClick

        init {
            Comment_user = itemView.findViewById(R.id.comment_id) as TextView
            Comment = itemView.findViewById(R.id.text_comment) as TextView
            date = itemView.findViewById(R.id.comment_time) as TextView
            itemView.setOnLongClickListener(this)


        }


    }


}