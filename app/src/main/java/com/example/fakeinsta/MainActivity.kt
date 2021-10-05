package com.example.fakeinsta

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {
    private val url = "http://192.168.1.101/lara/public/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpToolbar()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val postLoader: PostLoader = PostLoader(
            this,
            this.findViewById(R.id.recyclerview),
            Volley.newRequestQueue(this),
            url
        )
        // TODO : Add Paginate to App.
        postLoader.Load()
    }

    private fun setUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
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

    private fun isLogin(): Boolean {
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.main_menu, menu)

//        if (isLogin())
//        {
//            menu?.findItem(R.id.logout_menu)?.isVisible = true
//            menu?.findItem(R.id.login_menu)?.isVisible = false
//        }
//        else
//        {
//            menu?.findItem(R.id.logout_menu)?.isVisible = false
//            menu?.findItem(R.id.login_menu)?.isVisible = true
//        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }

}