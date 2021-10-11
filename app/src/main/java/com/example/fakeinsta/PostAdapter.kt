package com.example.fakeinsta

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.NetworkImageView


class PostAdapter(
    private val context: Context,
    val dataSet: MutableList<Post>,
    var nextPage: String?,
    private val imageLoader: ImageLoader,
    private val url: String,
    private val postLoader: PostLoader
) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: NetworkImageView
        val title: TextView
        val description: TextView
        val comment_button: ImageButton

        init {
            // Define click listener for the ViewHolder's View.
            image = view.findViewById(R.id.post_image)
            title = view.findViewById(R.id.title_text_view)
            description = view.findViewById(R.id.description_text_view)
            comment_button = view.findViewById(R.id.comment_button)
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
        // set view Data's
        viewHolder.image.setImageUrl(
            url + "home/getImage/" + dataSet[position].id.toString(),
            imageLoader
        )
        viewHolder.title.text = dataSet[position].full_name
        viewHolder.description.text =
            if (dataSet[position].description === "null") "" else dataSet[position].description
        // set onClickListener for comment button
        viewHolder.comment_button.setOnClickListener {
            val intent: Intent = Intent(context, CommentActivity::class.java).apply {
                putExtra("id", dataSet[position].id)
                putExtra("url", url)
            }
            context.startActivity(intent)
        }
        // set pagination
        if ((position + 1) == dataSet.size && nextPage != "null")
        {
            postLoader.loadNextPage(nextPage!!,this)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}

