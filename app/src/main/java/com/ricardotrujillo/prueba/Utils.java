package com.ricardotrujillo.prueba;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.telecom.Call;
import android.view.Display;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.squareup.picasso.Callback;

/**
 * Created by froger_mcs on 05.11.14.
 */
public class Utils {
    private static int screenWidth = 0;
    private static int screenHeight = 0;

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int getScreenHeight(Context c) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }

        return screenHeight;
    }

    public static int getScreenWidth(Context c) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        return screenWidth;
    }

    public static boolean isAndroid5() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    @TargetApi(21)
    public static RippleDrawable getPressedColorRippleDrawable(int normalColor, int pressedColor) {

        return new RippleDrawable(getPressedColorSelector(normalColor, pressedColor), getColorDrawableFromColor(normalColor), null);
    }

    public static ColorStateList getPressedColorSelector(int normalColor, int pressedColor) {

        return new ColorStateList(
                new int[][]
                        {
                                new int[]{}
                        },
                new int[]
                        {
                                pressedColor
                        }
        );
    }

    public static ColorDrawable getColorDrawableFromColor(int color) {

        return new ColorDrawable(color);
    }

    @TargetApi(17)
    public static Bitmap blur(Context context, Bitmap image, float blurRadius) {
        if (null == image) return null;

        Bitmap outputBitmap = Bitmap.createBitmap(image);
        final RenderScript renderScript = RenderScript.create(context);
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);

        //Intrinsic Gausian blur filter
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        theIntrinsic.setRadius(blurRadius);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }

    @TargetApi(21)
    public static void enterReveal(View view) {

        // get the center for the clipping circle
        int cx = view.getMeasuredWidth() / 2;
        int cy = view.getMeasuredHeight() - view.getMeasuredHeight();

        // get the final radius for the clipping circle
        int finalRadius = Math.max(view.getWidth(), view.getHeight()) / 2;

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, view.getWidth());

        // make the view visible and start the animation
        view.setVisibility(View.VISIBLE);
        anim.setDuration(Constants.REVEAL_ANIMATION);
        anim.setInterpolator(new LinearInterpolator());
        anim.start();
    }

    public static void animateAvatar(final View v, final Callback callback) {

        v.animate()
                .scaleXBy(0.3f)
                .setDuration(Constants.AVATAR_ANIMATION_DURATION_IN)
                .setInterpolator(new LinearInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                        v.setRotation(0);

                        v.animate()
                                .scaleXBy(-0.3f);

                        v.animate()
                                .scaleYBy(-0.3f)
                                .setDuration(Constants.AVATAR_ANIMATION_DURATION_OUT)
                                .setInterpolator(new OvershootInterpolator(7f))
                                .setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {

                                        callback.onSuccess();
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                });
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

        v.animate()
                .scaleYBy(0.3f);

        v.animate()
                .rotation(360)
                .setInterpolator(new AnticipateOvershootInterpolator())
                .setDuration(Constants.AVATAR_ANIMATION_ROTATION);
    }

    public static int alterColor(int color, float factor) {

        int a = (color & (0xFF << 24)) >> 24;
        int r = (int) (((color & (0xFF << 16)) >> 16) * factor);
        int g = (int) (((color & (0xFF << 8)) >> 8) * factor);
        int b = (int) ((color & 0xFF) * factor);

        return Color.argb(a, r, g, b);
    }
}
