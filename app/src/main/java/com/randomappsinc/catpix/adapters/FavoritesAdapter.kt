package com.randomappsinc.catpix.adapters

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.randomappsinc.catpix.R
import com.randomappsinc.catpix.models.CatPicture
import com.randomappsinc.catpix.utils.loadThumbnailImage

class FavoritesAdapter(var context: Context, private var listener: Listener)
    : RecyclerView.Adapter<FavoritesAdapter.CatPictureViewHolder>() {

    interface Listener {
        fun onItemClick(position: Int)
    }

    val pictures: ArrayList<CatPicture> = ArrayList()
    var placeholder : Drawable = ColorDrawable(ContextCompat.getColor(context, R.color.gray_300))

    fun setPictures(catPictures: List<CatPicture>) {
        this.pictures.addAll(catPictures)
        notifyDataSetChanged()
    }

    fun addFavorite(catPicture: CatPicture) {
        this.pictures.add(0, catPicture)
        notifyItemInserted(0)
    }

    fun removeFavorite(catPicture: CatPicture) {
        for (i in 0 until pictures.size) {
            if (pictures[i].id.equals(catPicture.id)) {
                pictures.removeAt(i)
                notifyItemRemoved(i)
                break
            }
        }
    }

    fun getItem(position: Int): CatPicture {
        return pictures[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatPictureViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.cat_picture_grid_cell,
                parent,
                false)
        return CatPictureViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return pictures.size
    }

    override fun onBindViewHolder(holder: CatPictureViewHolder, position: Int) {
        holder.loadItem(position)
    }

    inner class CatPictureViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.favorite_image) lateinit var picture: ImageView

        init {
            ButterKnife.bind(this, view)
        }

        fun loadItem(position: Int) {
            val catPicture = getItem(position)
            loadThumbnailImage(catPicture, picture, placeholder)
        }

        @OnClick(R.id.favorite_image)
        fun onImageClicked() {
            listener.onItemClick(adapterPosition)
        }
    }
}
