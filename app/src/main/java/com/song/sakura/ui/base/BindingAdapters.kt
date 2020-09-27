package com.song.sakura.ui.base

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.databinding.BindingAdapter
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide


/**
 * Title: com.song.sakura.ui.base
 * Description:
 * Copyright:Copyright(c) 2020
 * CreateTime:2020/03/26 11:23
 *
 * @author SogZiw
 * @version 1.0
 */
object BindingAdapters {

    @BindingAdapter(value = ["openDrawer"])
    @JvmStatic
    fun openDrawer(drawerLayout: DrawerLayout, isOpenDrawer: Boolean) {
        if (isOpenDrawer && !drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START)
        } else {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    /**
     * requireAll 属性为false，表示在XML中，属性可以不用全部赋值，
     * 若是true，则attribute_name_1 ~ attribute_name_n 属性都要赋值，如果不设置，默认是true。
     */
    @BindingAdapter(value = ["imageUrl", "placeholder"], requireAll = false)
    @JvmStatic
    fun setImageUrl(imageView: ImageView, url: String?, placeHolder: Drawable) {
        if (url == null) {
            imageView.setImageDrawable(placeHolder)
        } else {
            Glide.with(imageView).load(url).into(imageView)
        }
    }
}