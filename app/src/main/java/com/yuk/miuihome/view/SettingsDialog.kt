package com.yuk.miuihome.view

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.yuk.miuihome.R

class SettingsDialog(context: Context) : Dialog(context, R.style.CustomDialog) {
    var view: View

    init {
        window!!.setGravity(Gravity.BOTTOM)
        view = createView(context, R.layout.dialog_layout)
    }

    private fun createView(context: Context, dialog_layout: Int): View {
        val inflate: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflate.inflate(dialog_layout, null)
        setContentView(view)
        return view
    }

    fun addView(mView: View) {
        view.findViewById<LinearLayout>(R.id.View).addView(mView)
    }

    fun addView(mView: View, index: Int) {
        view.findViewById<LinearLayout>(R.id.View).addView(mView, index)
    }
    fun addView(mView: View, width: Int, height: Int) {
        view.findViewById<LinearLayout>(R.id.View).addView(mView, width, height)
    }
    fun addView(mView: View, params: ViewGroup.LayoutParams) {
        view.findViewById<LinearLayout>(R.id.View).addView(mView, params)
    }
    fun addView(mView: View, index: Int, params: ViewGroup.LayoutParams) {
        view.findViewById<LinearLayout>(R.id.View).addView(mView, index, params)
    }

    override fun setTitle(title: CharSequence?) {
        view.findViewById<TextView>(R.id.Title).text = title
    }

    override fun setTitle(titleId: Int) {
        view.findViewById<TextView>(R.id.Title).setText(titleId)
    }

    fun setRButton(text: CharSequence?, callBacks: (it: View) -> Unit) {
        view.findViewById<Button>(R.id.RButton).apply {
            setText(text)
            setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) it.performHapticFeedback(HapticFeedbackConstants.CONFIRM, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
                else it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
                callBacks(it)
            }
        }
    }

    fun setRButton(textId: Int, callBacks: () -> Unit) {
        view.findViewById<Button>(R.id.RButton).apply {
            setText(textId)
            setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) it.performHapticFeedback(HapticFeedbackConstants.CONFIRM, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
                else it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
                callBacks()
            }
        }
    }

    fun setLButton(text: CharSequence?, callBacks: (it: View) -> Unit) {
        view.findViewById<Button>(R.id.LButton).apply {
            setText(text)
            setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) it.performHapticFeedback(HapticFeedbackConstants.REJECT, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
                else it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
                callBacks(it)
            }
            visibility = View.VISIBLE
        }
    }

    fun setLButton(textId: Int, callBacks: () -> Unit) {
        view.findViewById<Button>(R.id.LButton).apply {
            setText(textId)
            setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) it.performHapticFeedback(HapticFeedbackConstants.REJECT, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
                else it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
                callBacks()
            }
            visibility = View.VISIBLE
        }
    }

    fun setMessage(textId: Int) {
        view.findViewById<TextView>(R.id.Message).apply {
            setText(textId)
            visibility = View.VISIBLE
        }
    }

    fun setMessage(text: CharSequence?) {
        view.findViewById<TextView>(R.id.Message).apply {
            this.text = text
            visibility = View.VISIBLE
        }
    }

    fun setEditText(text: String, hint: String) {
        view.findViewById<EditText>(R.id.EditText).apply {
            setText(text.toCharArray(), 0, text.length)
            this.hint = hint
            visibility = View.VISIBLE
        }
    }

    fun getEditText(): String = view.findViewById<EditText>(R.id.EditText).text.toString()

    override fun show() {
        super.show()
        val lp = window!!.attributes
        lp.dimAmount = 0.3f
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window!!.attributes = lp
    }
}