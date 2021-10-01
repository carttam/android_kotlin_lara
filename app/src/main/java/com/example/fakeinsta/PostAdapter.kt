package com.example.fakeinsta

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.collection.LruCache
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.NetworkImageView
import com.android.volley.toolbox.Volley


class PostAdapter(private val dataSet: MutableList<Post>, private val q : RequestQueue, private val imageLoader: ImageLoader) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: NetworkImageView
        val title: TextView
        val description: TextView

        init {
            // Define click listener for the ViewHolder's View.
            image = view.findViewById(R.id.post_image)
            title = view.findViewById(R.id.title_text_view)
            description = view.findViewById(R.id.description_text_view)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.single_post, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
//        viewHolder.textView.text = dataSet[position]

//        val req = ImageRequest(
//            "http://192.168.1.101/lara/public/home/getImage/3",
//            Response.Listener<Bitmap> { response ->
//                viewHolder.image.setImageBitmap(response)
//            },0,0,null,
//        )

        viewHolder.image.setImageUrl(
            "http://192.168.1.101/lara/public/home/getImage/"+dataSet[position].id.toString(),
            imageLoader
        )
        viewHolder.title.text = dataSet[position].full_name
        viewHolder.description.text = if (dataSet[position].description === "null") "" else dataSet[position].description
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}

