package com.randomappsinc.catpix.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.randomappsinc.catpix.R

open class SettingsAdapter(
        private var context: Context,
        protected var itemSelectionListener: ItemSelectionListener)
    : RecyclerView.Adapter<SettingsAdapter.SettingViewHolder>() {

    protected var options: Array<String> = context.resources.getStringArray(R.array.settings_options)
    protected var icons: Array<String> = context.resources.getStringArray(R.array.settings_icons)

    interface ItemSelectionListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingViewHolder {
        val itemView = LayoutInflater.from(context).inflate(
                R.layout.settings_item_cell,
                parent,
                false)
        return SettingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SettingViewHolder, position: Int) {
        holder.loadSetting(position)
    }

    override fun getItemCount(): Int {
        return options.size
    }

    inner class SettingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @JvmField @BindView(R.id.settings_icon) var icon: TextView? = null
        @JvmField @BindView(R.id.settings_option) var option: TextView? = null

        init {
            ButterKnife.bind(this, view)
        }

        fun loadSetting(position: Int) {
            option!!.text = options[position]
            icon!!.text = icons[position]
        }

        @OnClick(R.id.parent)
        fun onSettingSelected() {
            itemSelectionListener.onItemClick(adapterPosition)
        }
    }
}
