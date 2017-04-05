package com.dodola.arcwind;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;
import android.view.animation.LinearInterpolator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kurt on 17-4-5.
 */

public class PictureView extends TextureView implements TextureView.SurfaceTextureListener {


    public PictureView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setOpaque(false);
        this.setSurfaceTextureListener(this);
        initBitmapFromAsset();
    }

    public PictureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PictureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @Nullable
    private ImageAssetBitmapManager imageAssetBitmapManager;
    @Nullable private String imageAssetsFolder = "images";
    @Nullable private Map<String, LottieImageAsset> images = new HashMap<>();

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    private final Rect src = new Rect();
    private final Rect dst = new Rect();
    private final float density = 1;
    private Bitmap bitmap;

    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private ValueAnimator valueAnimator;
    private int animatedValue;

    private void initBitmapFromAsset() {
        images.put("image_0", LottieImageAsset.Factory.newInstance() );

        if (imageAssetBitmapManager == null) {
            imageAssetBitmapManager = new ImageAssetBitmapManager(this,
                    imageAssetsFolder, null, images);
            bitmap = imageAssetBitmapManager.bitmapForId("image_0");
        }

    }

    private void startAnimation() {
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofInt(60);
            valueAnimator.setDuration(1000);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    if (animatedValue != (int) animation.getAnimatedValue()) {
                        animatedValue = (int) animation.getAnimatedValue();
                        drawMe(getWidth(), getHeight());

                    }
                }
            });
            valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
            valueAnimator.setRepeatCount(-1);
            valueAnimator.start();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        startAnimation();

    }

    private void drawMe(int width, int height) {
        Log.d(L.TAG, " w " + width + " h :" +height );
        Canvas canvas = lockCanvas();

        if (canvas != null) {
            canvas.save();
            src.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
            dst.set(0, 0, (int) (bitmap.getWidth() * density), (int) (bitmap.getHeight() * density));
            canvas.drawBitmap(bitmap, src, dst , paint);
            canvas.restore();

        }
        unlockCanvasAndPost(canvas);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }
}
