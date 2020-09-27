package com.song.sakura.extension

import android.content.Context
import androidx.core.content.ContextCompat
import com.song.sakura.app.App

fun Float.dp2IntPx(context: Context): Int = (this.dp2FloatPx(context) + 0.5f).toInt()

fun Float.dp2FloatPx(context: Context): Float = context.resources.displayMetrics.density * this

fun Int.dp2IntPx(context: Context): Int = this.toFloat().dp2IntPx(context)

fun Int.toColor() = ContextCompat.getColor(App.getApplication(), toInt())