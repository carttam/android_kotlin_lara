package com.example.fakeinsta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val postLoader: PostLoader = PostLoader(
            this,
            this.findViewById(R.id.recyclerview),
            Volley.newRequestQueue(this),
            "http://192.168.1.101/lara/public/"
        )
        postLoader.Load()

    }
}