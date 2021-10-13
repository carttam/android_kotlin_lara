package com.example.fakeinsta

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val url = "http://192.168.1.101/lara/public/"
    private var recyclerview: RecyclerView? = null
    private var q: RequestQueue? = null
    private var token: String? = null
    private var nav: NavigationView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        q = Volley.newRequestQueue(this)
        recyclerview = findViewById(R.id.recyclerview)
        setUpToolbar()
        setUpRecyclerView()

    }

    override fun onResume() {
        super.onResume()

        recyclerview = findViewById(R.id.recyclerview)
        q = Volley.newRequestQueue(this)
        setUpToolbar()

    }

    private fun setUpRecyclerView() {
        token = applicationContext.getSharedPreferences("login", 0).getString("token", "null")
        val postLoader: PostLoader = PostLoader(
            this,
            this.recyclerview!!,
            q!!,
            url,
        )
        postLoader.Load()
    }

    private fun setUpLoginToolbar(full_name: String) {
        nav!!.menu.findItem(R.id.account_menu).title = full_name
        nav!!.menu.findItem(R.id.login_menu).isVisible = false
        nav!!.menu.findItem(R.id.logout_menu).isVisible = true
        nav!!.menu.findItem(R.id.share_post).isVisible = true
    }

    private fun setUpLogOutToolbar() {
        nav!!.menu.findItem(R.id.account_menu).title =
            applicationContext.resources.getString(R.string.account)
        nav!!.menu.findItem(R.id.login_menu).isVisible = true
        nav!!.menu.findItem(R.id.logout_menu).isVisible = false
        nav!!.menu.findItem(R.id.share_post).isVisible = false
    }

    private fun setUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        nav = findViewById<NavigationView>(R.id.navigation)
        nav!!.setNavigationItemSelectedListener(this)
        token = applicationContext.getSharedPreferences("login", 0).getString("token", "null")
        Login.login.isLogin(token!!, url, recyclerview!!, q!!) { response ->
            setUpLoginToolbar(response.getString("full_name"))
        }
        setSupportActionBar(toolbar)
         // TODO : fix back from AddPostActivity home button
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar?.setHomeButtonEnabled(true)
        // Set up Drawer
        val drawer = findViewById<DrawerLayout>(R.id.drawer)
        ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            0,
            0
        ).syncState()
    }

    private fun logOut() {
        val req: JsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST,
            url + "api/logout",
            null,
            { response ->
                Snackbar.make(
                    recyclerview!!,
                    if (response.isNull("status")) response.getString("message") else response.getString(
                        "status"
                    ),
                    6000
                ).show()
                applicationContext.getSharedPreferences("login", 0).edit().remove("token").apply()
                setUpLogOutToolbar()
            },
            { error -> Snackbar.make(recyclerview!!, error.message.toString(), 6000).show() }
        ) {

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Authorization"] = "Bearer " + token!!
                return headers
            }

        }

        q!!.add(req)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // login menu item
            R.id.login_menu -> {
                startActivity(Intent(this, LoginActivity::class.java))
                true
            }
            // log out from account
            R.id.logout_menu -> {
                logOut()
                true
            }
            // add post
            R.id.share_post -> {
                startActivity(Intent(this, AddPostActivity::class.java).putExtra("url",url))
                true
            }
            else -> false
        }
    }

}