package com.song.sakura.ui.mine

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.PointF
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.ViewAnimationUtils
import androidx.constraintlayout.helper.widget.Flow.*
import androidx.constraintlayout.helper.widget.Layer
import androidx.constraintlayout.widget.ConstraintHelper
import androidx.constraintlayout.widget.ConstraintLayout
import com.song.sakura.R
import com.song.sakura.ui.base.IBaseActivity
import com.song.sakura.ui.base.IBaseViewModel
import com.ui.action.ClickAction
import kotlinx.android.synthetic.main.activity_flow.*

class FlowActivity : IBaseActivity<IBaseViewModel>(), ClickAction {

    override fun isImmersionBarEnabled(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow)
        mToolbar?.title = "Flow"

        setOnClickListener(btn)
        setOnClickListener(btnLayer)
    }

    private var currentMode = WRAP_CHAIN

    override fun onClick(v: View?) {
        if (v == btn) {
            currentMode = when (currentMode) {
                WRAP_CHAIN -> {
                    flow.setWrapMode(WRAP_ALIGNED)
                    WRAP_ALIGNED
                }
                WRAP_ALIGNED -> {
                    flow.setWrapMode(WRAP_NONE)
                    WRAP_NONE
                }
                else -> {
                    flow.setWrapMode(WRAP_CHAIN)
                    WRAP_CHAIN
                }
            }
        } else if (v == btnLayer) {
            val anim = ValueAnimator.ofFloat(0f, 360f)
            anim.addUpdateListener { animation ->
                val angle = animation.animatedValue as Float
                layer.rotation = angle
                layer.scaleX = 1 + (180 - Math.abs(angle - 180)) / 20f
                layer.scaleY = 1 + (180 - Math.abs(angle - 180)) / 20f

                val shift_x = 500 * Math.sin(Math.toRadians((angle * 5).toDouble())).toFloat()
                val shift_y = 500 * Math.sin(Math.toRadians((angle * 7).toDouble())).toFloat()
                layer.translationX = shift_x
                layer.translationY = shift_y
            }
            anim.duration = 4000
            anim.start()
        }
    }

}

class CircularRevealHelper @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintHelper(context, attrs, defStyleAttr) {

    override fun updatePostLayout(container: ConstraintLayout) {
        super.updatePostLayout(container)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val views = getViews(container)
            for (view in views) {
                val anim = ViewAnimationUtils.createCircularReveal(
                    view, view.width / 2,
                    view.height / 2, 0f,
                    Math.hypot((view.height / 2).toDouble(), (view.width / 2).toDouble()).toFloat()
                )
                anim.duration = 3000
                anim.start()
            }
        }
    }
}

class Flyin2Helper @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Layer(context, attrs, defStyleAttr) {

    override fun updatePostLayout(container: ConstraintLayout) {
        super.updatePostLayout(container)
        val centerPoint = PointF(((left + right) / 2).toFloat(), ((top + bottom) / 2).toFloat())

        val animator = ValueAnimator.ofFloat(0f, 1f).setDuration(1000)
        animator.addUpdateListener { animation ->
            val animatedFraction = animation.animatedFraction
            updateTranslation(centerPoint, animatedFraction, container)
        }
        animator.start()
    }

    private fun updateTranslation(centerPoint: PointF, animatedFraction: Float, container: ConstraintLayout) {
        val views = getViews(container)
        for (view in views) {

            val viewCenterX = (view.left + view.right) / 2
            val viewCenterY = (view.top + view.bottom) / 2

            val startTranslationX = if (viewCenterX < centerPoint.x) -2000f else 2000f
            val startTranslationY = if (viewCenterY < centerPoint.y) -2000f else 2000f

            view.translationX = (1 - animatedFraction) * startTranslationX
            view.translationY = (1 - animatedFraction) * startTranslationY
        }
    }
}