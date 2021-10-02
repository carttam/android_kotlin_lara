package com.example.fakeinsta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import org.json.JSONArray

class CommentActivity : AppCompatActivity() {
    var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
        recyclerView = findViewById(R.id.comment_recycler_view)
        sendReq(this.intent.extras?.getString("url"), this.intent.extras?.getInt("id"))
    }

    private fun onResponse(response: JSONArray) {
        val list = mutableListOf<Comment>()
        for (i in 0 until response.length()) {
            val cm = response.getJSONObject(i)
            list.add(
                Comment(
                    cm.getInt("id"),
                    cm.getString("comment"),
                    cm.getString("user_full_name")
                )
            )
        }

        recyclerView?.adapter = CommentAdapter(list)
        recyclerView?.layoutManager = LinearLayoutManager(this)
    }

    private fun sendReq(url: String?, id: Int?) {
        if (url !== null && id !== null) {
            val q = Volley.newRequestQueue(this)
            val req = JsonArrayRequest(
                Request.Method.GET,
                url + "home/getComments/" + id.toString(),
                null,
                this::onResponse,
                Response.ErrorListener { error ->
                    Snackbar.make(
                        recyclerView!!,
                        error.message.toString(),
                        6000
                    ).show()
                }
            )
            q.add(req)
        }
    }

}