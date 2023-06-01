package com.pab.activitymonitor

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class ActivityMonitorService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString()
            val activityName = event.className?.toString()

            // Display the package name and activity name in your desired way (e.g., log, toast, UI update)
            Log.d("ActivityMonitor", "Package: $packageName, Activity: $activityName")
        }
    }

    override fun onInterrupt() {
        // This method is called when the accessibility service is interrupted or disabled.
    }

    override fun onServiceConnected() {
        super.onServiceConnected()

        // Configure your service settings here (if required)
    }
}
