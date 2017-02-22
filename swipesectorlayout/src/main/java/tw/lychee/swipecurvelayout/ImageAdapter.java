package tw.lychee.swipecurvelayout;

import android.widget.ImageView;

public abstract class ImageAdapter {
    private CustomPagerAdapter mCustomAdapter;

    public abstract void setItemView(int position, ImageView view);
    public abstract void setBackgroundView(int position, ImageView view);
    public abstract int getCount();
    public void notifyDataSetChanged() {
        mCustomAdapter.notifyDataSetChanged();
    }

    void setCustomAdapter(CustomPagerAdapter adapter) {
        mCustomAdapter = adapter;
    }
}
