package tw.lychee.swipecurvelayout;

import android.support.annotation.DrawableRes;

public class ImagePair {
    private int mBackground;
    private int mImage;

    public ImagePair (@DrawableRes int image) {
        this(image , 0);
        mImage = image;
    }

    public ImagePair (@DrawableRes int image, @DrawableRes int background) {
        mImage = image;
        mBackground = background;
    }

    public int getBackground() {
        return mBackground;
    }

    public int getImage() {
        return mImage;
    }
}
