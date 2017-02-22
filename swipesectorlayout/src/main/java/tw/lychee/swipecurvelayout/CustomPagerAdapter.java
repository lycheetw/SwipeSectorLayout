package tw.lychee.swipecurvelayout;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

class CustomPagerAdapter extends PagerAdapter {
    private ArrayList<ImagePair> mItems;

    CustomPagerAdapter(ArrayList<ImagePair> items) {
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return o == view;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return "Item " + (position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = ((Activity)container.getContext()).getLayoutInflater().inflate(R.layout.pager, null);
        ImageView imgView = (ImageView)view.findViewById(R.id.img);
        imgView.setImageResource(mItems.get(position).getBackground());
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}