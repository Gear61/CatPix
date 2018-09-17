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
import com.randomappsinc.catpix.utils.Constants
import com.squareup.picasso.Picasso
import java.util.*

class HomeFeedAdapter(private var listener: Listener)
    : RecyclerView.Adapter<HomeFeedAdapter.PicturesRowViewHolder>() {

    private var canFetchMore = true

    interface Listener {
        fun onItemClick(position: Int)
    }

    val pictureUrls = ArrayList<String>()

    fun addPicturesUrls(newUrls: List<String>) {
        if (!newUrls.isEmpty()) {
            if (newUrls.size < Constants.EXPECTED_PAGE_SIZE) {
                canFetchMore = false
            }
            pictureUrls.addAll(newUrls)
            notifyDataSetChanged()
        } else {
            canFetchMore = false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PicturesRowViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.cat_pictures_row,
                parent,
                false)
        return PicturesRowViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PicturesRowViewHolder, position: Int) {
        holder.loadContent(position)
    }

    override fun getItemCount(): Int {
        val coreSize = pictureUrls.size / 3
        val numRows = if (pictureUrls.size % 3 > 0) coreSize + 1 else coreSize
        return if (canFetchMore) numRows + 1 else numRows
    }

    inner class PicturesRowViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.pictures_row) lateinit var picturesRow: View
        @BindView(R.id.picture_1) lateinit var picture1: ImageView
        @BindView(R.id.picture_2) lateinit var picture2: ImageView
        @BindView(R.id.picture_3) lateinit var picture3: ImageView
        @BindView(R.id.pagination_spinner) lateinit var loadingSpinner: View

        init {
            ButterKnife.bind(this, view)
        }

        fun loadContent(position: Int) {
            if (position == itemCount - 1 && canFetchMore) {
                picturesRow.visibility = View.GONE
                loadingSpinner.visibility = View.VISIBLE
            } else {
                loadingSpinner.visibility = View.GONE
                picturesRow.visibility = View.VISIBLE
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
        }

        @OnClick(R.id.picture_1)
        fun onFirstClicked() {
            listener.onItemClick(adapterPosition * 3)
        }

        @OnClick(R.id.picture_2)
        fun onSecondClicked() {
            listener.onItemClick(adapterPosition * 3 + 1)
        }

        @OnClick(R.id.picture_3)
        fun onThirdClicked() {
            listener.onItemClick(adapterPosition * 3 + 2)
        }
    }
}
