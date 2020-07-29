package com.ui.util;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.appcompat.content.res.AppCompatResources;
import com.ui.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.TypedValue;

@SuppressWarnings("deprecation")
public class DrawableHelper {
    public static final boolean M_GT = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;

    public static final boolean LOLLIPOP_GE = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    public static final boolean KITKAT_EQ = Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT;
    public static final boolean KITKAT_GE = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    public static final boolean LOLLIPOP_EQ = Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP;


    private static  Context mContext;

    private static Context getContent() {
        return mContext;
    }

    public static void init(Context context){
        mContext = context;
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Drawable getDrawable(Context context, int resId) {
        Drawable d;
        try {
            d = AppCompatResources.getDrawable(context, resId);
            DrawableUtils.fixDrawable(d);
        } catch (Resources.NotFoundException e) {
            d = context.getResources().getDrawable(resId);
        }
        return d;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Drawable getDrawableWithBounds(Context context, int resId) {
        Drawable d;
        try {
            d = AppCompatResources.getDrawable(context, resId);
            DrawableUtils.fixDrawable(d);
        } catch (Resources.NotFoundException e) {
            d = context.getResources().getDrawable(resId);
        }
        d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());
        return d;
    }

    public static GradientDrawable createShapeDrawable(int color, int corner) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(color);
        gd.setCornerRadius(Utils.dip2px(getContent(), corner));
        return gd;
    }
    public static GradientDrawable createShapeDrawableResource(@ColorRes int color, int corner) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(getContent().getResources().getColor(color));
        gd.setCornerRadius(Utils.dip2px(getContent(), corner));
        return gd;
    }

    public static GradientDrawable createLineDrawable(int color, int height) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(color);
        gd.setStroke(height, color);
        gd.setShape(GradientDrawable.LINE);
        return gd;
    }

    public static GradientDrawable createShapeStrokeDrawable(@ColorRes int color, @ColorRes int strokeColor, int corner) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(getContent().getResources().getColor(color));
        gd.setStroke(2, getContent().getResources().getColor(strokeColor));
        gd.setCornerRadius(Utils.dip2px(getContent(), corner));
        return gd;
    }

    public static GradientDrawable createShapeStrokeDrawable(Context context, @ColorRes int color, @ColorRes int strokeColor, int corner) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(context.getResources().getColor(color));
        gd.setStroke(2, context.getResources().getColor(strokeColor));
        gd.setCornerRadius(Utils.dip2px(context, corner));
        return gd;
    }

    public static GradientDrawable createShapeStrokeDrawable(@ColorRes int color, @ColorRes int strokeColor, int width, int corner) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(getContent().getResources().getColor(color));
        gd.setStroke(width, getContent().getResources().getColor(strokeColor));
        gd.setCornerRadius(Utils.dip2px(getContent(), corner));
        return gd;
    }


    public static GradientDrawable createShapeWithStrokeDrawable(int color, int strokeColor, int corner) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(color);
        gd.setStroke(2, strokeColor);
        gd.setCornerRadius(Utils.dip2px(getContent(), corner));
        return gd;
    }

    public static GradientDrawable createCycleShapeWithStrokeDrawable(int color, int strokeColor, int corner) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(color);
        gd.setShape(GradientDrawable.OVAL);
        gd.setSize(
                Utils.dip2px(getContent(), corner) * 2,
                Utils.dip2px(getContent(), corner) * 2);
        gd.setStroke(2, strokeColor);
        gd.setCornerRadius(Utils.dip2px(getContent(), corner));
        return gd;
    }

    public static StateListDrawable newSelector(@ColorRes int normal, @ColorRes int pressed,int corner) {
        Drawable idNormal = createShapeStrokeDrawable(R.color.color_transparent, normal, corner);
        Drawable idPressed = createShapeStrokeDrawable(R.color.color_transparent, pressed, corner);
        StateListDrawable bg = new StateListDrawable();
        //Drawable disable = getDrawable(R.drawable.shape_btn_background_disable);
        bg.addState(new int[]{android.R.attr.state_selected, android.R.attr.state_enabled}, idPressed);
        bg.addState(new int[]{android.R.attr.state_checked, android.R.attr.state_enabled}, idPressed);

        bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, idPressed);
        //bg.addState(new int[]{-android.R.attr.state_enabled}, disable);
        bg.addState(new int[]{}, idNormal);
        return bg;
    }


    public static StateListDrawable newSelector(Drawable idNormal, Drawable idPressed) {
        StateListDrawable bg = new StateListDrawable();
        //Drawable disable = getDrawable(R.drawable.shape_btn_background_disable);
        bg.addState(new int[]{android.R.attr.state_selected, android.R.attr.state_enabled}, idPressed);
        bg.addState(new int[]{android.R.attr.state_checked, android.R.attr.state_enabled}, idPressed);

        bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, idPressed);
        //bg.addState(new int[]{-android.R.attr.state_enabled}, disable);
        bg.addState(new int[]{}, idNormal);
        return bg;
    }

//    public static StateListDrawable newSelector(Context context, int idNormal, int idPressed) {
//        StateListDrawable bg = new StateListDrawable();
//        Drawable normal = idNormal == -1 ? null : getDrawable(context, idNormal);
//        Drawable pressed = idPressed == -1 ? null : getDrawable(context, idPressed);
//        int color = PreferenceUtils.getInstance(context).getThemeColor();
//        pressed.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
//        //Drawable disable = getDrawable(R.drawable.shape_btn_background_disable);
//        bg.addState(new int[]{android.R.attr.state_selected, android.R.attr.state_enabled}, pressed);
//
//        bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
//        //bg.addState(new int[]{-android.R.attr.state_enabled}, disable);
//        bg.addState(new int[]{}, normal);
//        return bg;
//    }

    public static StateListDrawable newSelector(Context context, Drawable idNormal, Drawable idPressed) {
        StateListDrawable bg = new StateListDrawable();
        //Drawable disable = getDrawable(R.drawable.shape_btn_background_disable);
        bg.addState(new int[]{android.R.attr.state_selected, android.R.attr.state_enabled}, idPressed);
        bg.addState(new int[]{android.R.attr.state_checked, android.R.attr.state_enabled}, idPressed);

        bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, idPressed);
        //bg.addState(new int[]{-android.R.attr.state_enabled}, disable);
        bg.addState(new int[]{}, idNormal);
        return bg;
    }

    public static StateListDrawable newSelector(Drawable idNormal, Drawable idPressed, Drawable disable) {
        StateListDrawable bg = new StateListDrawable();
        bg.addState(new int[]{android.R.attr.state_selected, android.R.attr.state_enabled}, idPressed);
        bg.addState(new int[]{android.R.attr.state_checked, android.R.attr.state_enabled}, idPressed);

        bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, idPressed);
        bg.addState(new int[]{-android.R.attr.state_enabled}, disable);
        bg.addState(new int[]{}, idNormal);
        return bg;
    }

    public static StateListDrawable newListSelector(Context context, int idNormal, int idPressed) {
        StateListDrawable bg = new StateListDrawable();
        //Drawable disable = getDrawable(R.drawable.shape_btn_background_disable);
        Drawable normal = idNormal == -1 ? null : getDrawable(context, idNormal);
        Drawable pressed = idPressed == -1 ? null : getDrawable(context, idPressed);

        bg.addState(new int[]{android.R.attr.state_selected, android.R.attr.state_enabled}, pressed);

        bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        //bg.addState(new int[]{-android.R.attr.state_enabled}, disable);
        bg.addState(new int[]{}, normal);
        return bg;
    }

    public static ColorStateList newListSelector(int idNormal, int idPressed) {
        Context context = getContent();

        int[] colors = new int[]{idPressed, idPressed, idPressed, idNormal, idPressed, idPressed,
                idNormal};
        int[][] states = new int[7][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_selected, android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_enabled};
        states[4] = new int[]{android.R.attr.state_focused};
        states[5] = new int[]{android.R.attr.state_window_focused};
        states[6] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;

    }

    public static ColorStateList newColorStateList(@ColorRes int isNormal, @ColorRes int isPressed) {
        Context context = getContent();
        int idPressed = context.getResources().getColor(isPressed);
        int idNormal = context.getResources().getColor(isNormal);
        int[] colors = new int[]{idPressed, idPressed, idPressed, idPressed, idNormal, idPressed, idPressed,
                idNormal};
        int[][] states = new int[8][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_selected, android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_checked, android.R.attr.state_enabled};

        states[4] = new int[]{android.R.attr.state_enabled};
        states[5] = new int[]{android.R.attr.state_focused};
        states[6] = new int[]{android.R.attr.state_window_focused};
        states[7] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;

    }

    private static GradientDrawable createDotDrawable(int color, int corner) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(color);
        gd.setShape(GradientDrawable.OVAL);
        gd.setSize(
                Utils.dip2px(getContent(), corner) / 2,
                Utils.dip2px(getContent(), corner) / 2);
        gd.setCornerRadius(Utils.dip2px(getContent(), corner) / 2);
        return gd;
    }

//    public static Drawable createDrawerDrawable() {
//        Resources res = getContent().getResources();
//        Context context = getContent();
//        Drawable drawable1 = DrawableHelper.getDrawable(context, R.drawable.ic_dehaze_black_24dp);
//        Drawable drawable2 = createDotDrawable(Color.RED, 1);
//        Drawable[] drawables = {drawable1, drawable2};
//        LayerDrawable layerDrawable = new LayerDrawable(drawables);
//        layerDrawable.setLayerInset(0, 0, Utils.dip2px(1), Utils.dip2px(1), 0);
//        layerDrawable.setLayerInset(1,
//                Utils.dip2px(16), 0, 0, Utils.dip2px(16));
//        return layerDrawable;
//    }

    @DrawableRes
    public static int getDefaultBackground(Context context){
        TypedValue typedValue = new TypedValue();
        context.getTheme()
                .resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
        return typedValue.resourceId;
    }


}
