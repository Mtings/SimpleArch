package com.song.sakura.ui.base

import android.app.Activity
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

open class BaseViewHolder(itemView: View) : com.chad.library.adapter.base.viewholder.BaseViewHolder(itemView) {

    init {
        val displayMetrics = itemView.resources.displayMetrics
    }

    fun inflater(parent: ViewGroup, layoutRes: Int): View? {
        return LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
    }

    fun inflater(layoutRes: Int, parent: ViewGroup): View? {
        return LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
    }

    open fun <T : View> findViewById(@IdRes resId: Int): T {
        return getView(resId)
    }

    open fun getDrawable(res: Int): Drawable? {
        val drawable = ContextCompat.getDrawable(itemView.context, res)
        drawable?.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        return drawable
    }

    fun getColors(@ColorRes resId: Int): Int {
        return ContextCompat.getColor(itemView.context, resId)
    }

    fun getString(@StringRes resId: Int, s: String): String? {
        return itemView.context.resources.getString(resId) + s
    }

    fun getString(@StringRes resId: Int): String? {
        return itemView.context.resources.getString(resId)
    }

    fun setViewDrawableRight(view: TextView, resId: Int) {
        view.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(resId), null)
    }

    fun setTextViewSize(@IdRes resId: Int, size: Int) {
        val textView: TextView = getView<TextView>(resId)
        textView.textSize = size.toFloat()
    }

    fun setViewSize(@IdRes resId: Int, w: Int, h: Int) {
        val view: View = getView<View>(resId)
        val lp = view.layoutParams
        lp.width = w
        lp.height = h
        view.requestLayout()
    }

    fun setViewSize(parent: View, @IdRes resId: Int, w: Int, h: Int) {
        val view = parent.findViewById<View>(resId)
        if (view != null) {
            val lp = view.layoutParams
            lp.width = w
            lp.height = h
            view.requestLayout()
        }
    }

    fun setViewVisible(@IdRes resId: Int, visible: Int) {
        val view: View = getView(resId)
        view.visibility = visible
    }

    fun getActivity(): Activity {
        return itemView.context as Activity
    }

}