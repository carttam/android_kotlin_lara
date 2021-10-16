package com.example.fakeinsta

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class AddPostActivity : AppCompatActivity(), View.OnClickListener {
    private var startForResult: ActivityResultLauncher<Intent>? = null
    private var image: ImageView? = null
    private var imageB: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)
        setUp()
    }

    private fun setUp() {
        // set buttons onclickListener
        findViewById<Button>(R.id.select_image_from).setOnClickListener(this)
        findViewById<Button>(R.id.send_button).setOnClickListener(this)
        image = findViewById(R.id.selected_image)
        startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val intent = result.data
                    if (intent != null) {
                        imageB = BitmapFactory.decodeStream(contentResolver.openInputStream(intent.data!!))
                        image!!.setImageBitmap(imageB)
                    }
//                    image!!.setImageURI(intent!!.data)

                }
            }
    }

    private fun sendPost() {
        val token = applicationContext.getSharedPreferences("login", 0).getString("token", "null")
        val byteArrayOutputStream = ByteArrayOutputStream()
        imageB!!.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream)
        val req = object : JsonObjectRequest(
            Method.POST,
            intent.extras!!.getString("url") + "api/addPost",
            JSONObject().put(
                "description",
                findViewById<EditText>(R.id.description_text_view).text.toString()
            ).put("file",Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT)),
            Response.Listener { response ->
                Snackbar.make(
                    findViewById(R.id.main_linearLayout),
                    response.getString("message"),
                    6000
                ).show()
                finish()
            },
            Response.ErrorListener { error ->
                Snackbar.make(
                    findViewById(R.id.main_linearLayout),
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
        Volley.newRequestQueue(this).add(req)
    }
    override fun onClick(v: View?) {
        when (v!!.id) {
            // Select image
            R.id.select_image_from -> {
                startForResult!!.launch(
                    Intent.createChooser(
                        Intent().setType("image/").setAction(Intent.ACTION_GET_CONTENT),
                        "تصویر مورد نظر را انتخاب کنید"
                    )
                )
            }
            // Send post
            R.id.send_button -> sendPost()
            else -> {
            }
        }
    }

}