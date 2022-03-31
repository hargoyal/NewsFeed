package com.harshit.newsfeed.adapters


import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.harshit.newsfeed.models.NewsFeedModel
import com.harshit.newsfeed.databinding.NewsFeedCardViewBinding


class NewsFeedRecyclerViewAdapter(private val mValues: List<NewsFeedModel.Article>, val context: FragmentActivity?) :
        RecyclerView.Adapter<NewsFeedRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = NewsFeedCardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.binding.textViewNewsTitle.text = item.title
        holder.binding.textViewNewsDescription.text = item.description
        Glide.with(context!!).asBitmap().load(Uri.parse(item.urlToImage)).override(300, 200).centerInside().thumbnail(0.2f).into(holder.binding.imageViewNewsThumbnail)
//        holder.imageViewNewsThumbnail.setImageURI(Uri.parse(item.urlToImage))
        holder.itemView.setOnClickListener {
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val binding: NewsFeedCardViewBinding) : RecyclerView.ViewHolder(binding.root)
}