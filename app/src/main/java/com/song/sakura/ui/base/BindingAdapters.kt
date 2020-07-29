package com.song.sakura.ui.base

import androidx.core.view.GravityCompat
import androidx.databinding.BindingAdapter
import androidx.drawerlayout.widget.DrawerLayout

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

    @BindingAdapter(value = ["openDrawer"], requireAll = false)
    @JvmStatic
    fun openDrawer(drawerLayout: DrawerLayout, isOpenDrawer: Boolean) {
        if (isOpenDrawer && !drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START)
        } else {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }
}