package com.example.fakeinsta

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class CommentAdapter(
    private val dataSet: MutableList<Comment>,
) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val user_full_name: TextView
        val comment: TextView

        init {
            // Define click listener for the ViewHolder's View.
            user_full_name = view.findViewById(R.id.user_full_name)
            comment = view.findViewById(R.id.comment_text_view)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.single_comment, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.user_full_name.text = dataSet[position].user_full_name
        viewHolder.comment.text = dataSet[position].comment
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}

