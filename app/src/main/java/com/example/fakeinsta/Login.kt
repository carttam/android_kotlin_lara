package com.example.fakeinsta

import android.view.View
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject

class Login {
    companion object login {
        public fun isLogin(
            token: String,
            url: String,
            view: View,
            q: RequestQueue,
            funDo: (response: JSONObject) -> Unit
        ) {
            if (token === "null")
                return
            val req: JsonObjectRequest = object : JsonObjectRequest(
                Request.Method.POST,
                url + "api/isLogin",
                null,
                Response.Listener { response ->
                    funDo(response)
                },
                Response.ErrorListener { error ->
//                    Snackbar.make(
//                        view,
//                        "Login Error Happened !",
//                        6000
//                    ).show()
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
}