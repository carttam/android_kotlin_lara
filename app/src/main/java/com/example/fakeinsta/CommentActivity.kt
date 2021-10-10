package com.example.fakeinsta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import org.json.JSONArray
import org.json.JSONObject

class CommentActivity : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var q: RequestQueue? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
    }

    override fun onResume() {
        super.onResume()
        setUpViews()
    }

    private fun setUpViews() {
        recyclerView = findViewById(R.id.comment_recycler_view)
        val cm_btn_linearL: LinearLayout = findViewById(R.id.do_comment)
        val comment_text_view: TextView = findViewById(R.id.comment_edit_text)
        val comment_button: Button = findViewById(R.id.add_comment_button)
        val url = this.intent.extras?.getString("url")
        val post_id = intent.extras!!.getInt("id")
        val token = applicationContext.getSharedPreferences("login", 0).getString("token", "null")
        q = Volley.newRequestQueue(this)
        Login.login.isLogin(
            token!!,
            url!!,
            cm_btn_linearL,
            q!!
        ) { response ->
            cm_btn_linearL.isVisible = true
            comment_button.setOnClickListener {
                addCm(
                    post_id,
                    comment_text_view.text.toString(),
                    url,
                    token
                )
                comment_text_view.text = ""
            }
        }
        sendReq(url, post_id)
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
            q!!.add(req)
        }
    }

    private fun addCm(post_id: Int, comment: String, url: String, token: String) {
        val req = object : JsonObjectRequest(
            Request.Method.POST,
            url + "api/addComment",
            JSONObject().put("comment", comment).put("post_id", post_id),
            { response ->
                Snackbar.make(
                    recyclerView!!,
                    response.getString("message"),
                    6000
                ).show()
                sendReq(url, intent.extras?.getInt("id"))
            },
            Response.ErrorListener { error ->
                Snackbar.make(
                    recyclerView!!,
                    error.message.toString(),
                    6000
                ).show()
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Authorization"] = "Bearer $token"
                return headers
            }
        }
        q!!.add(req)
    }
}