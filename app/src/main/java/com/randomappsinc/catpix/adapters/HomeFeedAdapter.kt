package com.randomappsinc.catpix.adapters

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindColor
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnTouch
import com.randomappsinc.catpix.R
import com.randomappsinc.catpix.models.CatPicture
import com.randomappsinc.catpix.persistence.database.FavoritesDataManager
import com.randomappsinc.catpix.utils.Constants
import com.randomappsinc.catpix.utils.animateFavoriteToggle
import com.randomappsinc.catpix.utils.loadThumbnailImage

class HomeFeedAdapter(var context: Context, private var listener: Listener)
    : RecyclerView.Adapter<HomeFeedAdapter.PictureViewHolder>() {

    private var canFetchMore = true

    interface Listener {
        fun onItemClick(position: Int)

        fun onItemDoubleTap(catPicture: CatPicture)

        fun onLastItemSeen()
    }

    val pictures = ArrayList<CatPicture>()
    var placeholder : Drawable = ColorDrawable(ContextCompat.getColor(context, R.color.gray_300))
    var favoritesDataManager = FavoritesDataManager.instance
    private var idToPosition = HashMap<String, Int>()

    fun addPicturesUrls(newPictures: List<CatPicture>) {
        val wasShowingSpinner = pictures.isNotEmpty()
        val prevSize = itemCount

        // Maintain picture ID -> position mapping
        var currentPosition = pictures.size
        for (catPicture in newPictures) {
            idToPosition[catPicture.id] = currentPosition
            currentPosition++
        }

        if (!newPictures.isEmpty()) {
            if (newPictures.size < Constants.EXPECTED_PAGE_SIZE) {
                canFetchMore = false
            }
            pictures.addAll(newPictures)
            if (wasShowingSpinner) {
                notifyItemChanged(prevSize - 1)
            }
            if (newPictures.size > 1) {
                notifyItemRangeInserted(prevSize, newPictures.size - 1)
            }
        } else {
            canFetchMore = false
            if (wasShowingSpinner) {
                notifyItemRemoved(prevSize - 1)
            }
        }
    }

    fun onFavoriteStatusChanged(catPicture: CatPicture) {
        val positionToUpdate = idToPosition[catPicture.id]
        if (positionToUpdate != null) {
            notifyItemChanged(positionToUpdate)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.home_feed_cell,
                parent,
                false)
        return PictureViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        holder.loadContent(position)
    }

    override fun getItemCount(): Int {
        val coreSize = pictures.size
        return if (canFetchMore) coreSize + 1 else coreSize
    }

    fun isPositionASpinner(position: Int): Boolean {
        return position == itemCount - 1 && canFetchMore
    }

    inner class PictureViewHolder(view: View) : RecyclerView.ViewHolder(view),
            GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
        @BindView(R.id.cat_picture) lateinit var pictureView: ImageView
        @BindView(R.id.favorite_status) lateinit var favoriteStatus: TextView
        @BindView(R.id.pagination_spinner_stub) lateinit var loadingSpinnerStub: ViewStub
        private var loadingSpinner : View? = null
        private var gestureDetector: GestureDetectorCompat

        @JvmField @BindColor(R.color.light_red) var lightRed: Int = 0
        @JvmField @BindColor(R.color.dark_gray) var darkGray: Int = 0

        init {
            ButterKnife.bind(this, view)
            gestureDetector = GestureDetectorCompat(context, this)
        }

        fun loadContent(position: Int) {
            if (isPositionASpinner(position)) {
                listener.onLastItemSeen()
                pictureView.visibility = View.GONE
                favoriteStatus.visibility = View.GONE
                maybeInflateSpinnerAndMakeVisible()
            } else {
                maybeHideLoadingSpinner()
                pictureView.visibility = View.VISIBLE
                loadThumbnailImage(pictures[position], pictureView, placeholder)
                favoriteStatus.visibility = View.VISIBLE

                val isFavorited = favoritesDataManager.isPictureFavorited(pictures[position])
                if (isFavorited) {
                    favoriteStatus.setText(R.string.heart_filled_icon)
                    favoriteStatus.setTextColor(lightRed)
                } else {
                    favoriteStatus.setText(R.string.heart_icon)
                    favoriteStatus.setTextColor(darkGray)
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

        @OnTouch(R.id.cat_picture)
        fun onCatPictureTouched(motionEvent: MotionEvent) : Boolean {
            return gestureDetector.onTouchEvent(motionEvent)
        }

        override fun onDoubleTap(p0: MotionEvent?): Boolean {
            animateFavoriteToggle(
                    favoriteStatus,
                    !favoritesDataManager.isPictureFavorited(pictures[adapterPosition]),
                    R.color.dark_gray,
                    R.color.light_red)
            listener.onItemDoubleTap(pictures[adapterPosition])
            return true
        }

        override fun onDoubleTapEvent(p0: MotionEvent?): Boolean {
            return true
        }

        override fun onSingleTapConfirmed(p0: MotionEvent?): Boolean {
            listener.onItemClick(adapterPosition)
            return false
        }

        override fun onShowPress(p0: MotionEvent?) {}

        override fun onSingleTapUp(p0: MotionEvent?): Boolean {
            return true
        }

        override fun onDown(p0: MotionEvent?): Boolean {
            return true
        }

        override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
            return false
        }

        override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
            return false
        }

        override fun onLongPress(p0: MotionEvent?) {}
    }
}
