package com.yuk.miuihome.view

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuk.miuihome.R
import com.yuk.miuihome.XposedInit
import com.yuk.miuihome.view.adapter.ItemAdapter
import com.yuk.miuihome.view.data.DataHelper
import com.yuk.miuihome.view.data.Item
import com.yuk.miuihome.view.utils.HomeContext
import com.yuk.miuihome.view.utils.LogUtil
import com.yuk.miuihome.view.utils.OwnSP
import com.yuk.miuihome.view.utils.ktx.dp2px
import de.robv.android.xposed.XposedBridge
import kotlin.concurrent.thread
import kotlin.system.exitProcess

class HookSettingsActivity: TransferActivity() {

    private val itemList = arrayListOf<Item>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter
    private lateinit var back: ImageView
    private lateinit var title: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        DataHelper.currentActivity = this
        itemList.addAll(DataHelper.getItems())
        initBack()
        recyclerView = findViewById(R.id.settings_recycler)
        title = findViewById(R.id.title)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = ItemAdapter(itemList)
        recyclerView.adapter = adapter
        if (OwnSP.ownSP.getBoolean("isFirstUse", true)) showFirstUseDialog()
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val y = recyclerView.computeVerticalScrollOffset()
                val o = dp2px(100f)-y*2
                if (o > dp2px(0f)) title.setPadding(0, o,0,0)
                else title.setPadding(0, dp2px(0f),0,0)
            }
        })
    }

    private fun initBack() {
        back = findViewById(R.id.settings_back)
        back.setOnClickListener {
            if (DataHelper.thisItems == DataHelper.main) DataHelper.currentActivity.finish()
            else DataHelper.setItems(DataHelper.main)
        }
    }

    override fun onBackPressed() {
        if (DataHelper.thisItems != DataHelper.main) DataHelper.setItems(DataHelper.main)
        else super.onBackPressed()
    }

    fun getDensityDpi(): Int {
        val dm = DisplayMetrics()
        HomeContext.Activity.windowManager.defaultDisplay.getMetrics(dm)
        return (dm.density * 160).toInt()
    }

    private fun showFirstUseDialog() {
        SettingsDialog(this).apply {
            setTitle(XposedInit.moduleRes.getString(R.string.Welcome))
            setMessage(XposedInit.moduleRes.getString(R.string.Tips))
            setRButton(XposedInit.moduleRes.getString(R.string.Yes)) {
                OwnSP.clear()
                OwnSP.set("isFirstUse",false)
                OwnSP.set("animationLevel", 1.0f)
                OwnSP.set("blurLevel", "CompleteBlur")
                OwnSP.set("blurWhenOpenFolder", true)
                OwnSP.set("smoothAnimation", true)
                OwnSP.set("mamlDownload", true)
                OwnSP.set("dockRadius", 2.5f)
                OwnSP.set("dockHeight", 7.9f)
                OwnSP.set("dockSide", 3.0f)
                OwnSP.set("dockBottom", 2.3f)
                OwnSP.set("dockIconBottom", 3.5f)
                OwnSP.set("dockMarginTop", 0.6f)
                OwnSP.set("dockMarginBottom", 11.0f)
                OwnSP.set("task_vertical", 1000.0f)
                OwnSP.set("task_horizontal1", 1.0f)
                OwnSP.set("task_horizontal2", 1.0f)
                OwnSP.set("folderColumns", 3)
                dismiss()
                thread {
                    LogUtil.toast(XposedInit.moduleRes.getString(R.string.Reboot2))
                    Thread.sleep(1000)
                    exitProcess(0)
                }
            }
            setLButton(XposedInit.moduleRes.getString(R.string.Cancel)) { this@HookSettingsActivity.finish() }
            show()
        }
    }
}