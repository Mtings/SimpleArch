package com.song.sakura.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.song.sakura.R

class AlignedTextView : AppCompatTextView {

    companion object {
        const val TYPE_TOP = 0x01
        const val TYPE_BOTTOM = 0x02
    }

    private var alignment = 0
    private val textRect = Rect()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AlignedTextView)
            if (typedArray.hasValue(R.styleable.AlignedTextView_alignment)) {
                setAlignment(typedArray.getInt(R.styleable.AlignedTextView_alignment, 0))
            }
            typedArray.recycle()
        }
    }

    private fun setAlignment(attr: Int) {
        when (attr) {
            TYPE_TOP -> alignment = TYPE_TOP
            TYPE_BOTTOM -> alignment = TYPE_BOTTOM
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            if (isChineseOrLowerCase(this.text.toString())) {
                canvas.getClipBounds(textRect)
                paint.getTextBounds(this.text.toString(), 0, this.text.length, textRect)
                paint.textAlign = Paint.Align.CENTER
                paint.color = this.currentTextColor
                val drawX = width / 2f
                var drawY = 0f
                val offset = paint.textSize / 10
                if (alignment == TYPE_TOP) {
                    drawY = height / 2f - offset
                } else if (alignment == TYPE_BOTTOM) {
                    drawY = height * 3 / 2f - offset
                }
                canvas.drawText(this.text.toString(), drawX, drawY, paint)

            } else {
                canvas.getClipBounds(textRect)
                val cHeight = textRect.height()
                paint.getTextBounds(this.text.toString(), 0, this.text.length, textRect)
                val bottom = textRect.bottom
                textRect.offset(-textRect.left, -textRect.top)
                paint.textAlign = Paint.Align.CENTER
                paint.color = this.currentTextColor
                val drawX = width / 2f
                var drawY = 0f
                if (alignment == TYPE_TOP) {
                    drawY =
                        (textRect.bottom - bottom).toFloat() - ((textRect.bottom - textRect.top) / 2)
                } else if (alignment == TYPE_BOTTOM) {
                    drawY = top + cHeight.toFloat() + ((textRect.bottom - textRect.top) / 2)
                }

                canvas.drawText(this.text.toString(), drawX, drawY, paint)
            }

        }
    }

    // 判断一个字符是否是中文
    private fun isChinese(c: Char): Boolean {
        return c.toInt() in 0x4E00..0x9FA5 // 根据字节码判断
    }

    // 判断一个字符串是否含有中文或小写字母
    private fun isChineseOrLowerCase(str: String?): Boolean {
        if (str == null) return false
        for (c in str.toCharArray()) {
            if (isChinese(c) || c.isLowerCase()) return true
        }
        return false
    }


}