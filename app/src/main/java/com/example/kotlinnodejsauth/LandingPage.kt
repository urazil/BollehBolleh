package com.example.kotlinnodejsauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_landing_page.*

class LandingPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

         //툴바 아이콘 색상설정
        val icon = AppCompatResources.getDrawable(this, R.drawable.ic_account_circle_black_24dp)!!
        DrawableCompat.setTint(icon, ContextCompat.getColor(this,  R.color.typing))

        //툴바 생성하기
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)

        val adapter = MyViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(VideoPage(),"영상 볼래?")
        adapter.addFragment(GalleryPage(),"사진 볼래?")
        adapter.addFragment(ChatPage(),"채팅 볼래?")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
     }


    class MyViewPagerAdapter(manager: FragmentManager): FragmentPagerAdapter(manager){

        private val fragmentList : MutableList<Fragment> = ArrayList()
        private val titleList : MutableList<String> = ArrayList()


         override fun getItem(position: Int): Fragment {
             return fragmentList[position]
          }

        override fun getCount(): Int {
            return fragmentList.size
        }

        fun addFragment(fragment : Fragment,title: String){
             fragmentList.add(fragment)
            titleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titleList[position]

        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main,menu)
        return true
    }
    //툴바 오른쪽 내 정보버튼 클릭시
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId) {
            R.id.my_info -> {
                val intent = Intent(this, My_info::class.java )
                startActivity(intent)
                 }
        }
        return super.onOptionsItemSelected(item)
    }
}
