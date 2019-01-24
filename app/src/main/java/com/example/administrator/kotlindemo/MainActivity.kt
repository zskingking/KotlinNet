package com.example.administrator.kotlindemo


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var list = ArrayList<Bean>()
        list.add(Bean("投篮",0.6f))
        list.add(Bean("突破",0.9f))
        list.add(Bean("篮板",0.8f))
        list.add(Bean("助攻",0.9f))
        list.add(Bean("抢断",0.8f))
        list.add(Bean("盖帽",0.6f))
        netView.start(list)
    }

    private fun showToast(content:String) =
            Toast.makeText(this,content,Toast.LENGTH_LONG).show()
}
