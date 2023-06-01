package com.pab.activitymonitor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AppInfoAdapter(private val appInfoList: List<AppInfo>) :
    RecyclerView.Adapter<AppInfoAdapter.AppInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppInfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app_info, parent, false)
        return AppInfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppInfoViewHolder, position: Int) {
        val appInfo = appInfoList[position]
        holder.bind(appInfo)
    }

    override fun getItemCount(): Int {
        return appInfoList.size
    }

    inner class AppInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(appInfo: AppInfo) {
            // Bind appInfo properties to the UI elements in the item layout
            itemView.findViewById<TextView>(R.id.txtAppName).text = appInfo.appName
            itemView.findViewById<TextView>(R.id.txtPackageName).text = appInfo.packageName
            itemView.findViewById<TextView>(R.id.txtCpuUsage).text = appInfo.cpuUsage
            itemView.findViewById<TextView>(R.id.txtRamUsage).text = appInfo.ramUsage
        }
    }
}
