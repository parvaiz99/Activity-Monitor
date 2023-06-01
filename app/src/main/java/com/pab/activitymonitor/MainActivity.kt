package com.pab.activitymonitor

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.os.SystemClock
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var appInfoAdapter: AppInfoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val appInfoList = getAppInfoList()

        appInfoAdapter = AppInfoAdapter(appInfoList)
        recyclerView.adapter = appInfoAdapter
    }

    private fun getAppInfoList(): List<AppInfo> {
        val appInfoList = mutableListOf<AppInfo>()

        val packageManager = packageManager
        val activities = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        for (activity in activities) {
            val appName = packageManager.getApplicationLabel(activity).toString()
            val packageName = activity.packageName
            val cpuUsage = getCpuUsage(packageName) // Implement this function to get CPU usage for the app
            val ramUsage = getRamUsage(packageName) // Implement this function to get RAM usage for the app

            appInfoList.add(AppInfo(appName, packageName, cpuUsage, ramUsage))
        }

        return appInfoList
    }

    // Function to get CPU usage for the app
    private fun getCpuUsage(packageName: String): String {
        val pid = getProcessId(packageName)
        val processCpuUsage = getProcessCpuUsage(pid)
        return "${processCpuUsage}%"
    }

    // Function to get RAM usage for the app
    private fun getRamUsage(packageName: String): String {
        val pid = getProcessId(packageName)
        val memoryInfo = ActivityManager.MemoryInfo()
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getMemoryInfo(memoryInfo)
        val totalMemory = memoryInfo.totalMem
        val appMemory = Debug.getPss() * 1024 // Get the app's memory in bytes
        val ramUsagePercentage = (appMemory.toFloat() / totalMemory.toFloat()) * 100
        return "${ramUsagePercentage}%" // Return RAM usage as a string
    }

    private fun getProcessCpuUsage(pid: Int): Float {
        val processStatFile = "/proc/$pid/stat"
        var cpuUsage: Float = 0f

        try {
            val reader = BufferedReader(FileReader(processStatFile))
            val statLine = reader.readLine()
            reader.close()

            val fields = statLine.split(" ")
            val utime = fields[13].toLong() // CPU time spent in user mode
            val stime = fields[14].toLong() // CPU time spent in kernel mode
            val cutime = fields[15].toLong() // CPU time of waited-for children in user mode
            val cstime = fields[16].toLong() // CPU time of waited-for children in kernel mode
            val totalCpuTime = utime + stime + cutime + cstime // Total CPU time
            val elapsedTime = SystemClock.elapsedRealtime() * 10 // Device uptime in jiffies
            cpuUsage = (totalCpuTime.toFloat() / elapsedTime.toFloat()) * 100
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return cpuUsage
    }
    // Function to get the process ID (PID) for the app based on the package name
    private fun getProcessId(packageName: String): Int {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val processes = activityManager.runningAppProcesses
        for (processInfo in processes) {
            if (processInfo.processName == packageName) {
                return processInfo.pid
            }
        }
        return -1 // Return -1 if the process ID is not found
    }

}
