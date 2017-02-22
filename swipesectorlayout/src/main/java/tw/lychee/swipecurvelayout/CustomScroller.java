package tw.lychee.swipecurvelayout;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

class CustomScroller extends Scroller {

    private double mScrollFactor = 1;

    public CustomScroller(Context context) {
        super(context);
    }

    public CustomScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }


    public CustomScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    public void setScrollDurationFactor(double scrollFactor) {
        mScrollFactor = scrollFactor;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, (int) (duration * mScrollFactor));
    }

}