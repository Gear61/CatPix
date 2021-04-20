package com.randomappsinc.catpix.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
        private var icon: TextView = view.findViewById(R.id.settings_icon)
        private var option: TextView = view.findViewById(R.id.settings_option)

        init {
            view.setOnClickListener {
                itemSelectionListener.onItemClick(adapterPosition)
            }
        }

        fun loadSetting(position: Int) {
            icon.text = icons[position]
            option.text = options[position]
        }
    }
}
