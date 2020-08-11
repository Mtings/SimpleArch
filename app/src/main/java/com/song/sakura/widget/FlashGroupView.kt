package com.song.sakura.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.children
import com.song.sakura.R
import kotlinx.android.synthetic.main.layout_flash_group_view.view.*


public class FlashGroupView : FrameLayout {

    private var string = ""
    private var list: MutableList<String> = ArrayList()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    init {
        inflate(context, R.layout.layout_flash_group_view, this)

    }

    public fun setString(text: String) {
        this.string = text
        list = string.toCharArray().toMutableList().map {
            it.toString()
        }.toMutableList()

        list.forEach {
            addView(it)
        }
    }

    private fun addView(text: String) {
        val view =
            View.inflate(context, R.layout.view_flash_frame, null) as FlashFrameView
        view.changeChildTextAndType(text)
        layout.addView(view)
    }


    public fun startAnim() {
        layout.children.forEach {
            if (it is FlashFrameView) {
                it.startAnim()
            }
        }
    }

}
