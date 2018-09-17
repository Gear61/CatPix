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

class SettingsAdapter(context: Context, private var itemSelectionListener: ItemSelectionListener)
    : RecyclerView.Adapter<SettingsAdapter.SettingViewHolder>() {

    private var options: Array<String> = context.resources.getStringArray(R.array.settings_options)
    private var icons: Array<String> = context.resources.getStringArray(R.array.settings_icons)

    interface ItemSelectionListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
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
        @BindView(R.id.settings_icon) lateinit var icon: TextView
        @BindView(R.id.settings_option) lateinit var option: TextView

        init {
            ButterKnife.bind(this, view)
        }

        fun loadSetting(position: Int) {
            icon.text = icons[position]
            option.text = options[position]
        }

        @OnClick(R.id.parent)
        fun onSettingSelected() {
            itemSelectionListener.onItemClick(adapterPosition)
        }
    }
}
