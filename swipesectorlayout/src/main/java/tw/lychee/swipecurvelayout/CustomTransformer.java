package tw.lychee.swipecurvelayout;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

class CustomTransformer implements ViewPager.PageTransformer{
    private static final float MIN_ALPHA = 0.3f;
    private static final float DEFAULT_SCALE = 1.2f;

    @Override
    public void transformPage(View view, float position) {

        int pageWidth = view.getWidth();
        ImageView imgView = (ImageView)view.findViewById(R.id.img);
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
        } else if (position <= 0) { // [-1,0]

            view.setAlpha((1 - MIN_ALPHA) * (1 - Math.abs(position)) + MIN_ALPHA);
            float transX = pageWidth * -position;
            view.setTranslationX(transX);
            float scale = (DEFAULT_SCALE - 1.0f) * (1 - Math.abs(position)) + 1;
            imgView.setScaleX(scale);
            imgView.setScaleY(scale);

        } else if (position <= 1) { // (0,1]
            imgView.setAlpha(1.0f);
            imgView.setScaleX(DEFAULT_SCALE);
            imgView.setScaleY(DEFAULT_SCALE);

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
        }
    }
}
