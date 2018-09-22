package com.randomappsinc.catpix.adapters

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.randomappsinc.catpix.R
import com.randomappsinc.catpix.models.CatPicture
import com.randomappsinc.catpix.utils.Constants
import com.randomappsinc.catpix.utils.loadThumbnailImage
import java.util.*

class HomeFeedAdapter(var context: Context, private var listener: Listener)
    : RecyclerView.Adapter<HomeFeedAdapter.PicturesRowViewHolder>() {

    private var canFetchMore = true

    interface Listener {
        fun onItemClick(position: Int)

        fun onLastItemSeen()
    }

    val pictures = ArrayList<CatPicture>()
    var placeholder : Drawable = ColorDrawable(ContextCompat.getColor(context, R.color.gray_300))

    fun addPicturesUrls(newPictures: List<CatPicture>) {
        val wasShowingSpinner = !pictures.isEmpty()
        val prevSize = itemCount
        if (!newPictures.isEmpty()) {
            if (newPictures.size < Constants.EXPECTED_PAGE_SIZE) {
                canFetchMore = false
            }
            pictures.addAll(newPictures)
            if (wasShowingSpinner) {
                notifyItemChanged(prevSize - 1)
            }
            if (newPictures.size > 3) {
                notifyItemRangeInserted(prevSize, newPictures.size / 3)
            }
        } else {
            canFetchMore = false
            if (wasShowingSpinner) {
                notifyItemRemoved(prevSize - 1)
            }
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
        val coreSize = pictures.size / 3
        val numRows = if (pictures.size % 3 > 0) coreSize + 1 else coreSize
        return if (canFetchMore) numRows + 1 else numRows
    }

    inner class PicturesRowViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.pictures_row) lateinit var picturesRow: View
        @BindView(R.id.picture_1) lateinit var picture1: ImageView
        @BindView(R.id.picture_2) lateinit var picture2: ImageView
        @BindView(R.id.picture_3) lateinit var picture3: ImageView
        @BindView(R.id.pagination_spinner_stub) lateinit var loadingSpinnerStub: ViewStub
        var loadingSpinner : View? = null

        init {
            ButterKnife.bind(this, view)
        }

        fun loadContent(position: Int) {
            if (position == itemCount - 1 && canFetchMore) {
                listener.onLastItemSeen()
                picturesRow.visibility = View.GONE
                maybeInflateSpinnerAndMakeVisible()
            } else {
                maybeHideLoadingSpinner()
                picturesRow.visibility = View.VISIBLE
                val firstPosition = position * 3
                if (firstPosition < pictures.size) {
                    loadThumbnailImage(pictures[firstPosition], picture1, placeholder)
                    if (firstPosition + 1 < pictures.size) {
                        loadThumbnailImage(pictures[firstPosition + 1], picture2, placeholder)
                        if (firstPosition + 2 < pictures.size) {
                            loadThumbnailImage(pictures[firstPosition + 2], picture3, placeholder)
                        }
                    }
                }
            }
        }

        private fun maybeInflateSpinnerAndMakeVisible() {
            if (loadingSpinner == null) {
                loadingSpinner = loadingSpinnerStub.inflate()
            }
            loadingSpinner!!.visibility = View.VISIBLE
        }

        private fun maybeHideLoadingSpinner() {
            if (loadingSpinner != null) {
                loadingSpinner!!.visibility = View.GONE
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
