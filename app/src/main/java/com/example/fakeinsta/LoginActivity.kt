package com.example.fakeinsta

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    private val url = "http://192.168.1.101/lara/public/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val email = findViewById<AutoCompleteTextView>(R.id.email_auto_complete_text)
        val password = findViewById<EditText>(R.id.password_edit_text)

        findViewById<Button>(R.id.sign_in_button).setOnClickListener {
            logIn(email.text.toString(), password.text.toString())
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun logIn(email: String, password: String) {
        val q = Volley.newRequestQueue(this)

        val view = this.findViewById<RecyclerView>(R.id.recyclerview)

        val jobj = JSONObject()
        jobj.put("email", email)
        jobj.put("password", password)
        val req = JsonObjectRequest(
            Request.Method.POST,
            url + "api/login",
            jobj,
            { response ->
                val status = response.getString("status")
                val message = response.getString("message")
                if (status == "success") {
                    val editor = applicationContext.getSharedPreferences("login", 0).edit()
                    editor.putString("token", response.getString("token"))
                    editor.apply()
                    finish()
                } else {
                    Snackbar.make(findViewById(R.id.login_activity_main), message, 6000).show()
                }
            },
            { error -> Snackbar.make(view, error.message.toString(), 6000).show() }
        )

        q.add(req)
    }
}