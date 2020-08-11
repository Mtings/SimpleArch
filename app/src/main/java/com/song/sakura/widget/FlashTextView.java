package com.song.sakura.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.song.sakura.R;
import com.ui.util.Lists;

import java.util.ArrayList;

public class FlashTextView extends View {

    private float radius = 5;
    private Paint mPaint;
    private Paint textPaint;
    private String text = "";
    private boolean isPause = false;
    String[] letter = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
            "X", "Y", "Z"};
    String[] number = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    String[] symbol = {"_", "{", "}", "[", "]", "^", "`", "@", "?", "/", ">", "<", "=", ":", "\\", ";", ""};
    private int textType;
    public final static int TYPE_LETTER = 0x01; //字母
    public final static int TYPE_NUMBER = 0x02; //数字
    public final static int TYPE_SYMBOL = 0x03; //符号
    public final static int TYPE_CHINESE = 0x04; //中文


    private ArrayList<String> textList;
    private int currentIndex = 0;
    private int targetIndex = 0;

    private OnPauseListener onPauseListener;

    public FlashTextView(Context context) {
        super(context);
    }

    public FlashTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FlashTextView);
        radius = ta.getDimension(R.styleable.FlashTextView_corner_float, 5);
        text = ta.getString(R.styleable.FlashTextView_text);
        if (ta.hasValue(R.styleable.FlashTextView_textType)) {
            setType(ta.getInt(R.styleable.FlashTextView_textType, 0));
        }
        ta.recycle();
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(50);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
    }

    private void setType(int textType) {
        switch (textType) {
            case TYPE_LETTER:
                textList = Lists.newArrayList(letter);
                break;
            case TYPE_NUMBER:
                textList = Lists.newArrayList(number);
                break;
            case TYPE_SYMBOL:
                textList = Lists.newArrayList(symbol);
                break;
            case TYPE_CHINESE:
                String[] strings = {"", text};
                textList = Lists.newArrayList(strings);
                break;
            default:
                textList = Lists.newArrayList();
                break;
        }
        targetIndex = textList.indexOf(text);
    }

    private Bitmap drawRect(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.BLACK);
        c.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), radius, radius, p);
        return bm;
    }

    private void drawText(Canvas canvas) {
        if (targetIndex == -1) {
            return;
        }
        if (currentIndex != targetIndex) {
            float x = getWidth() / 2f - textPaint.measureText(textList.get(currentIndex)) / 2;
            float y = getHeight() / 2f + (textPaint.getFontMetrics().descent - textPaint.getFontMetrics().ascent) / 2 - textPaint.getFontMetrics().descent;
            canvas.drawText(textList.get(currentIndex), x, y, textPaint);
            currentIndex++;
        } else {
            float x = getWidth() / 2f - textPaint.measureText(text) / 2;
            float y = getHeight() / 2f + (textPaint.getFontMetrics().descent - textPaint.getFontMetrics().ascent) / 2 - textPaint.getFontMetrics().descent;
            canvas.drawText(text, x, y, textPaint);
            stopChange();
        }
        mPaint.setStrokeWidth(4f);
        canvas.drawLine(0, getHeight() / 2f, getWidth(), getHeight() / 2f, mPaint);
    }

    public int getTargetIndex() {
        return targetIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public void changeType(int textType) {
        this.textType = textType;
        setType(textType);
    }

    public void changeText(String text) {
        this.text = text;
        setType(textType);
    }

    public void changeTextAndType(String text) {
        if (null == text) {
            textList = Lists.newArrayList();
            targetIndex = -1;
            return;
        }
        this.text = text;
        if (isChineseStr(text)) {
            textType = TYPE_CHINESE;
            setType(textType);
            return;
        }
        if (Lists.newArrayList(letter).contains(text)) {
            textType = TYPE_LETTER;
            setType(textType);
            return;
        }
        if (Lists.newArrayList(number).contains(text)) {
            textType = TYPE_NUMBER;
            setType(textType);
            return;
        }
        if (Lists.newArrayList(symbol).contains(text)) {
            textType = TYPE_SYMBOL;
            setType(textType);
            return;
        }
    }

    // 判断一个字符是否是中文
    private boolean isChinese(char c) {
        return c >= 0x4E00 && c <= 0x9FA5;// 根据字节码判断
    }

    // 判断一个字符串是否含有中文
    public boolean isChineseStr(String str) {
        if (str == null)
            return false;
        for (char c : str.toCharArray()) {
            if (isChinese(c))
                return true;
        }
        return false;
    }

    /**
     * 中止遮罩动画
     */
    public void stopChange() {
        if (onPauseListener != null) {
            onPauseListener.onPause(true);
        }
        isPause = true;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }

    public boolean getIsPause() {
        return isPause;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(drawRect(getWidth(), getHeight()), 0, 0, mPaint);
        drawText(canvas);
    }

    public void setOnPauseListener(OnPauseListener onPauseListener) {
        this.onPauseListener = onPauseListener;
    }

    public interface OnPauseListener {
        public void onPause(boolean isPause);
    }
}
