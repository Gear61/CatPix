package com.randomappsinc.catpix.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.randomappsinc.catpix.R
import com.randomappsinc.catpix.models.CatPicture
import com.squareup.picasso.Picasso

class FavoritesAdapter : BaseAdapter() {

    val pictures: ArrayList<CatPicture> = ArrayList()

    fun setPictures(catPictures: List<CatPicture>) {
        this.pictures.addAll(catPictures)
        notifyDataSetChanged()
    }

    fun addFavorite(catPicture: CatPicture) {
        this.pictures.add(0, catPicture)
        notifyDataSetChanged()
    }

    fun removeFavorite(catPicture: CatPicture) {
        for (picture in pictures) {
            if (picture.id.equals(catPicture.id)) {
                pictures.remove(picture)
                break
            }
        }
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return pictures.size
    }

    override fun getItem(position: Int): CatPicture {
        return pictures[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    internal inner class CatPictureViewHolder(view: View) {
        @BindView(R.id.favorite_image) lateinit var picture: ImageView

        init {
            ButterKnife.bind(this, view)
        }

        fun loadItem(position: Int) {
            val catPicture = getItem(position)
            Picasso.get()
                    .load(catPicture.getThumbnailUrlWithFallback())
                    .fit()
                    .centerCrop()
                    .into(picture)
        }
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var convertView = view
        val holder: CatPictureViewHolder
        if (view == null) {
            val vi = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = vi.inflate(R.layout.cat_picture_grid_cell, parent, false)
            holder = CatPictureViewHolder(convertView!!)
            convertView.tag = holder
        } else {
            holder = convertView!!.tag as CatPictureViewHolder
        }
        holder.loadItem(position)
        return convertView
    }
}
