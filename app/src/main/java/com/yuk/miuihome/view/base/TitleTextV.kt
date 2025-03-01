package com.yuk.miuihome.view.base

import android.content.Context
import android.graphics.Typeface
import android.view.View
import com.yuk.miuihome.R
import com.yuk.miuihome.view.data.Padding
import com.yuk.miuihome.view.utils.ktx.dp2px

class TitleTextV(
    val text: String? = null,
    val resId: Int? = null,
    private val onClickListener: View.OnClickListener? = null
) : BaseView() {

    override fun getType(): BaseView = this

    override fun create(context: Context): View {
        return TextV(text, resId, 13f, context.getColor(R.color.title), Padding(dp2px(25f), dp2px(13f), 0, dp2px(13f)), Typeface.defaultFromStyle(Typeface.NORMAL), onClickListener).create(context)
    }
}