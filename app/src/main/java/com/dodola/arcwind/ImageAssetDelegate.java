package com.dodola.arcwind;

import android.graphics.Bitmap;

public interface ImageAssetDelegate {
  Bitmap fetchBitmap(LottieImageAsset asset);
}
