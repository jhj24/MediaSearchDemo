package com.example.mediasearchdemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.mediasearchdemo.fileSelector.LoadManagerUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_search.setOnClickListener {
            val intent = Intent(this, FileSelectorActivity::class.java)
            intent.putExtra("type",0)
            startActivity(intent)
        }

        btn_search2.setOnClickListener {
            val intent = Intent(this, FileSelectorActivity::class.java)
            intent.putExtra("type",1)
            startActivity(intent)
        }
    }
}
