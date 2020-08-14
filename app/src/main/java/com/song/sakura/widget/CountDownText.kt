package com.song.sakura.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.CountDownTimer
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.song.sakura.R
import kotlinx.android.synthetic.main.view_countdown.view.*
import kotlinx.android.synthetic.main.view_countdown_digit.view.*

class CountDownText : LinearLayout {

    private var countDownTimer: CountDownTimer? = null
    private var resetSymbol: String = ""

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        View.inflate(context, R.layout.view_countdown, this)
        attrs?.let {
            val typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.CountDownText)

            val resetSymbol = typedArray.getString(R.styleable.CountDownText_resetSymbol)
            if (resetSymbol != null) {
                setResetSymbol(resetSymbol)
            }

            val digitTopDrawable =
                typedArray.getDrawable(R.styleable.CountDownText_digitTopDrawable)
            setDigitTopDrawable(digitTopDrawable)
            val digitBottomDrawable =
                typedArray.getDrawable(R.styleable.CountDownText_digitBottomDrawable)
            setDigitBottomDrawable(digitBottomDrawable)
            val digitDividerColor =
                typedArray.getColor(R.styleable.CountDownText_digitDividerColor, 0)
            setDigitDividerColor(digitDividerColor)

            val digitTextColor = typedArray.getColor(R.styleable.CountDownText_digitTextColor, 0)
            setDigitTextColor(digitTextColor)

            val digitTextSize = typedArray.getDimension(R.styleable.CountDownText_digitTextSize, 0f)
            setDigitTextSize(digitTextSize)

            val digitPadding = typedArray.getDimension(R.styleable.CountDownText_digitPadding, 0f)
            setDigitPadding(digitPadding.toInt())

            val halfDigitHeight =
                typedArray.getDimensionPixelSize(R.styleable.CountDownText_halfDigitHeight, 0)
            val digitWidth =
                typedArray.getDimensionPixelSize(R.styleable.CountDownText_digitWidth, 0)
            setHalfDigitHeightAndDigitWidth(halfDigitHeight, digitWidth)

            val animationDuration =
                typedArray.getInt(R.styleable.CountDownText_animationDuration, 600)
            setAnimationDuration(animationDuration)

            typedArray.recycle()
        }
    }

    fun startCountDown(text: String) {
        countDownTimer?.cancel()
        if (resetSymbol == text) {
            return
        }
        countDownTimer = object : CountDownTimer(1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                setCountDownTime(text)
            }

            override fun onFinish() {
                digit.setNewText(text)
            }
        }
        countDownTimer?.start()
        resetSymbol = text
    }


    fun resetCountdownTimer() {
        countDownTimer?.cancel()
        digit.setNewText(resetSymbol)
    }

    private fun setCountDownTime(text: String) {
        digit.animateTextChange(text)
    }

    private fun setResetSymbol(resetSymbol: String?) {
        resetSymbol?.let {
            if (it.isNotEmpty()) {
                this.resetSymbol = resetSymbol
            } else {
                this.resetSymbol = ""
            }
        } ?: kotlin.run {
            this.resetSymbol = ""
        }
    }


    private fun setDigitTopDrawable(digitTopDrawable: Drawable?) {
        if (digitTopDrawable != null) {
            digit.frontUpper.background = digitTopDrawable
            digit.backUpper.background = digitTopDrawable
        } else {
            setTransparentBackgroundColor()
        }
    }

    private fun setDigitBottomDrawable(digitBottomDrawable: Drawable?) {
        if (digitBottomDrawable != null) {
            digit.frontLower.background = digitBottomDrawable
            digit.backLower.background = digitBottomDrawable
        } else {
            setTransparentBackgroundColor()
        }
    }

    private fun setDigitDividerColor(digitDividerColor: Int) {
        var dividerColor = digitDividerColor
        if (dividerColor == 0) {
            dividerColor = ContextCompat.getColor(context, R.color.color_transparent)
        }
        digit.digitDivider.setBackgroundColor(dividerColor)
    }

    private fun setDigitPadding(digitPadding: Int) {
        digit.setPadding(digitPadding, digitPadding, digitPadding, digitPadding)
    }

    private fun setDigitTextColor(digitsTextColor: Int) {
        var textColor = digitsTextColor
        if (textColor == 0) {
            textColor = ContextCompat.getColor(context, R.color.color_transparent)
        }
        digit.frontUpperText.setTextColor(textColor)
        digit.backUpperText.setTextColor(textColor)

        digit.frontLowerText.setTextColor(textColor)
        digit.backLowerText.setTextColor(textColor)
    }

    private fun setDigitTextSize(digitsTextSize: Float) {
        digit.frontUpperText.setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        digit.backUpperText.setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)

        digit.frontLowerText.setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        digit.backLowerText.setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
    }

    private fun setTransparentBackgroundColor() {
        val transparent = ContextCompat.getColor(context, R.color.color_transparent)
        digit.frontLower.setBackgroundColor(transparent)
        digit.backLower.setBackgroundColor(transparent)
    }

    private fun setHalfDigitHeightAndDigitWidth(halfDigitHeight: Int, digitWidth: Int) {
        setHeightAndWidthToView(digit.frontUpper, halfDigitHeight, digitWidth)
        setHeightAndWidthToView(digit.backUpper, halfDigitHeight, digitWidth)

        // Lower
        setHeightAndWidthToView(digit.frontLower, halfDigitHeight, digitWidth)
        setHeightAndWidthToView(digit.backLower, halfDigitHeight, digitWidth)

        // Dividers
        digit.digitDivider.layoutParams.width = digitWidth

    }

    private fun setHeightAndWidthToView(view: View, halfDigitHeight: Int, digitWidth: Int) {
        val digitFrontUpperLayoutParams = view.layoutParams
        digitFrontUpperLayoutParams.height = halfDigitHeight
        digitFrontUpperLayoutParams.width = digitWidth
        digit.frontUpper.layoutParams = digitFrontUpperLayoutParams
    }

    private fun setAnimationDuration(animationDuration: Int) {
        digit.setAnimationDuration(animationDuration.toLong())
    }


}