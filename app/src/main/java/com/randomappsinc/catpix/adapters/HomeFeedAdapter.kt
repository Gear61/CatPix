package com.randomappsinc.catpix.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.randomappsinc.catpix.R
import com.squareup.picasso.Picasso
import java.util.*

class HomeFeedAdapter(private var itemSelectionListener: ItemSelectionListener)
    : RecyclerView.Adapter<HomeFeedAdapter.PicturesRowViewHolder>() {

    interface ItemSelectionListener {
        fun onItemClick(position: Int)
    }

    val pictureUrls = ArrayList<String>()

    fun addPicturesUrls(newUrls: List<String>) {
        pictureUrls.addAll(newUrls)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PicturesRowViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.cat_pictures_row,
                parent,
                false)
        return PicturesRowViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PicturesRowViewHolder, position: Int) {
        holder.loadPictures(position)
    }

    override fun getItemCount(): Int {
        val coreSize = pictureUrls.size / 3
        return if (coreSize % 3 > 0) coreSize + 1 else coreSize
    }

    inner class PicturesRowViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.picture_1) lateinit var picture1: ImageView
        @BindView(R.id.picture_2) lateinit var picture2: ImageView
        @BindView(R.id.picture_3) lateinit var picture3: ImageView

        init {
            ButterKnife.bind(this, view)
        }

        fun loadPictures(position: Int) {
            val firstPosition = position * 3
            if (firstPosition < pictureUrls.size) {
                Picasso.get()
                        .load(pictureUrls[firstPosition])
                        .fit()
                        .centerCrop()
                        .into(picture1)

                if (firstPosition + 1 < pictureUrls.size) {
                    Picasso.get()
                            .load(pictureUrls[firstPosition + 1])
                            .fit()
                            .centerCrop()
                            .into(picture2)

                    if (firstPosition + 2 < pictureUrls.size) {
                        Picasso.get()
                                .load(pictureUrls[firstPosition + 2])
                                .fit()
                                .centerCrop()
                                .into(picture3)
                    }
                }
            }
        }

        @OnClick(R.id.picture_1)
        fun onFirstClicked() {
            itemSelectionListener.onItemClick(adapterPosition * 3)
        }

        @OnClick(R.id.picture_2)
        fun onSecondClicked() {
            itemSelectionListener.onItemClick(adapterPosition * 3 + 1)
        }

        @OnClick(R.id.picture_3)
        fun onThirdClicked() {
            itemSelectionListener.onItemClick(adapterPosition * 3 + 2)
        }
    }
}
