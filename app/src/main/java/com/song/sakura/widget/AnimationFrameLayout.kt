package com.song.sakura.widget

import android.content.Context
import android.util.AttributeSet
import android.view.animation.Animation
import android.widget.FrameLayout

class AnimationFrameLayout : FrameLayout {

    private lateinit var mListener: Animation.AnimationListener

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)


    fun setAnimationListener(listener: Animation.AnimationListener) {
        mListener = listener
    }

    override fun onAnimationStart() {
        super.onAnimationStart()
        mListener.onAnimationStart(animation)
    }

    override fun onAnimationEnd() {
        super.onAnimationEnd()
        mListener.onAnimationEnd(animation)
    }

}