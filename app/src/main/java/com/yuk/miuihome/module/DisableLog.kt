
package com.yuk.miuihome.module

import com.yuk.miuihome.XposedInit
import com.yuk.miuihome.view.utils.ktx.setReturnConstant

class DisableLog {

    fun init() {
        try {
            if (XposedInit().checkVersionCode() <= 426004312L) "com.miui.home.launcher.MiuiHomeLog".setReturnConstant("setDebugLogState", Boolean::class.java, result = false)
            "com.miui.home.launcher.MiuiHomeLog".setReturnConstant("log", String::class.java, String::class.java, result = null)
            "com.xiaomi.onetrack.OneTrack".setReturnConstant("isDisable", result = true)
        } catch (e: Throwable) {
        }
    }
}