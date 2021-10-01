package com.example.fakeinsta

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.Toast
import androidx.collection.LruCache
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.google.android.material.snackbar.Snackbar
import org.json.JSONArray
import org.json.JSONObject

class PostLoader(private val context: Context,private val recyclerview: RecyclerView,private val q : RequestQueue) {

        public fun Load()
        {
            val req = JsonObjectRequest(
                Request.Method.POST,
                "http://192.168.1.101/lara/public/api",
                null,
                this::onResponse,
                Response.ErrorListener { error -> Snackbar.make(recyclerview,error.message.toString(),5000).show() }
            )

            q.add(req)
        }

        private fun onResponse(response: JSONObject)
        {
            val dataSet = mutableListOf<Post>()
            val data = response.getJSONArray("data")
            for (i in 0 until data.length())
            {
                val post = data.getJSONObject(i)

                dataSet.add(Post(
                    post.getInt("id"),
                    post.getString("description"),
                    post.getInt("user_id"),
                    post.getString("file_name"),
                    post.getString("file_type"),
                    post.getString("full_name")
                ))

            }

            val imageLoader = ImageLoader(q,
                object : ImageLoader.ImageCache {
                    private val cache: LruCache<String, Bitmap> = LruCache<String, Bitmap>(20)
                    override fun getBitmap(url: String): Bitmap? {
                        return cache.get(url)
                    }

                    override fun putBitmap(url: String, bitmap: Bitmap) {
                        cache.put(url, bitmap)
                    }
                })

            val postAdapter : PostAdapter = PostAdapter(dataSet,q,imageLoader)
            recyclerview.adapter = postAdapter
            recyclerview.layoutManager = LinearLayoutManager(context)
        }
}