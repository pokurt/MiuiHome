package com.yuk.miuihome.module

import de.robv.android.xposed.XposedHelpers
import android.widget.GridView
import android.view.ViewGroup
import com.yuk.miuihome.view.utils.OwnSP
import com.yuk.miuihome.view.utils.ktx.hookAfterMethod

class ModifyFolderColumnsCount {

    fun init() {
        val value = OwnSP.ownSP.getInt("folderColumns", -1)
        if (value == -1 || value == 3) return
        "com.miui.home.launcher.Folder".hookAfterMethod("onFinishInflate"
        ) {
            val columns: Int = value
            val mContent = XposedHelpers.getObjectField(it.thisObject, "mContent") as GridView
            mContent.numColumns = columns
            if (OwnSP.ownSP.getBoolean("folderWidth", false) && (columns > 3)) {
                val lp = mContent.layoutParams
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT
                mContent.layoutParams = lp
            }
            if (columns > 3) {
                val mBackgroundView = XposedHelpers.getObjectField(it.thisObject, "mBackgroundView") as ViewGroup
                mBackgroundView.setPadding(mBackgroundView.paddingLeft / 3, mBackgroundView.paddingTop, mBackgroundView.paddingRight / 3, mBackgroundView.paddingBottom)
            }
        }
    }
}