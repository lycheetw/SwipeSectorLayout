package tw.lychee.swipecurvelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SwipeSectorLayout extends RelativeLayout {
    private CustomPager mViewPager;
    private ImageView mCoverImage;
    private CircularArrayList<ImageView> mReusableViews;
    private Context mContext;
    private OnPageChangeListener mListener;
    private int mItemViewWidth;
    private int mInnerDegreeUnit;
    private int mOuterDegreeUnit;
    private int mSectorColor;
    private int mSectorHeight;
    private boolean childViewInit = false;
    private ImageAdapter mAdapter;
    private final float DELAY = 0.05f;
    private final int ITEM_VIEW_COUNTS = 5;


    public SwipeSectorLayout(Context context) {
        this(context, null);
    }

    public SwipeSectorLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeSectorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.SwipeSectorLayout, 0, 0);
        int inner_degree = ta.getInteger(R.styleable.SwipeSectorLayout_sector_inner_degree, 90);
        mInnerDegreeUnit = inner_degree / 2;
        mOuterDegreeUnit = ta.getInteger(R.styleable.SwipeSectorLayout_sector_outer_degree, inner_degree) / 2;
        mItemViewWidth = ta.getDimensionPixelSize(R.styleable.SwipeSectorLayout_item_width, 200);
        mSectorColor = ta.getColor(R.styleable.SwipeSectorLayout_sector_color, 0xFFFFFFFF);
        mSectorHeight = ta.getDimensionPixelSize(R.styleable.SwipeSectorLayout_sector_height, 0);
        ta.recycle();
        mContext = context;
        init();

    }

    private void init() {

        LayoutParams pagerParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mViewPager = new CustomPager(mContext);

        mViewPager.addOnPageChangeListener(new PageChangeListener());
        mViewPager.setLayoutParams(pagerParams);
        mViewPager.setPageTransformer(false, new CustomTransformer());
        mViewPager.setScrollDurationFactor(1.8);
        addView(mViewPager);


        mCoverImage = new ImageView(mContext);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(ALIGN_PARENT_BOTTOM);
        pagerParams.addRule(CENTER_HORIZONTAL);
        mCoverImage.setImageResource(R.drawable.circle);
        mCoverImage.setLayoutParams(params);
        mCoverImage.setColorFilter(mSectorColor);
        mCoverImage.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(mCoverImage);

        mReusableViews = new CircularArrayList<>();
        for(int i = 0; i < ITEM_VIEW_COUNTS; i++){
            ImageView view = new ImageView(mContext);
            LayoutParams itemParams = new LayoutParams(mItemViewWidth, mItemViewWidth);
            itemParams.addRule(ALIGN_PARENT_LEFT);
            itemParams.addRule(ALIGN_PARENT_BOTTOM);
            view.setLayoutParams(itemParams);
            addView(view);
            mReusableViews.add(view);
        }

    }

    public void setAdapter(ImageAdapter adapter) {
        mAdapter = adapter;
        CustomPagerAdapter customAdapter = new CustomPagerAdapter(mAdapter);
        mViewPager.setAdapter(customAdapter);
        adapter.setCustomAdapter(customAdapter);
    }

    public ImageAdapter getAdapter() {
        return mAdapter;
    }

    private void initChildView() {
        LayoutParams params = (LayoutParams)mCoverImage.getLayoutParams();
        int w = mViewPager.getWidth() / 2;
        double r = w / Math.sin(Math.toRadians(mOuterDegreeUnit));
        params.width = (int)Math.round(r * 2);
        params.height = (int)Math.round(r * 2);
        params.bottomMargin = - (int)Math.round(r + r * Math.cos(Math.toRadians(mOuterDegreeUnit)) - mSectorHeight);
        params.leftMargin = - (int)Math.round(r - w);
        params.rightMargin = - (int)Math.round(r - w);

        setViewLocation(0);
        setViewImage(0);
        childViewInit = true;
    }

    private void setViewLocation(float percentage) {
        final int windowWidth = mViewPager.getWidth();
        final int halfWindowWidth = windowWidth / 2;
        final int radius = (int)Math.round(halfWindowWidth / Math.sin(Math.toRadians(mInnerDegreeUnit)));
        final float HORIZON_SCALE = 1.1f;

        for(int i = 0; i < mReusableViews.size(); i++) {
            View view = mReusableViews.get(i);
            float degree = mInnerDegreeUnit * i - mInnerDegreeUnit * percentage;
            int threshold = mInnerDegreeUnit * (mReusableViews.size() - 1);

            while(Math.abs(degree) >= threshold) {
                if(degree < 0)
                    degree += (mReusableViews.size())* mInnerDegreeUnit;
                else
                    degree -= (mReusableViews.size()) * mInnerDegreeUnit;
            }

            view.setRotation(degree);
            int left = (int)Math.round(
                    halfWindowWidth + HORIZON_SCALE * radius * Math.sin(Math.toRadians(degree)) - mItemViewWidth / 2
            );
            int bottom = (int)Math.round(
                    radius * Math.cos(Math.toRadians(degree)) - radius * Math.cos(Math.toRadians(mInnerDegreeUnit))
            );
            view.setTranslationX(left);
            view.setTranslationY(-bottom);

            //Log.d("setting", String.format("(%d) degree: %.4f, position: (%d, %d)", i, degree, left, bottom));
        }
    }

    private void setViewImage(int currIdx) {
        int t = (mReusableViews.size() - 1) / 2;
        for(int i = currIdx - t; i <= currIdx + t; i++) {
            if(i < 0 || i > mAdapter.getCount() - 1) {
                mReusableViews.get(i).setImageResource(0);
            } else {
                mAdapter.setItemView(i, mReusableViews.get(i));
            }
        }
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mListener = listener;
    }

    class PageChangeListener implements ViewPager.OnPageChangeListener {
        private float mPreOffset = 0;
        private int mPrePosition = 0;
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if(!childViewInit){
                initChildView();
            }

            if(positionOffset > 0.9999 || positionOffset == 0){
                mPreOffset = 0;
                return;
            }

            if(mPrePosition != position) {
                setViewImage(position);
                mPrePosition = position;
            }

            if(mPreOffset == 0){

            } else if(mPreOffset < positionOffset) {
                //right
                float percentage = positionOffset / (1 - DELAY);
                percentage = percentage >= 1 ? 1 : percentage;
                setViewLocation(position + percentage);
            } else {
                //left
                float percentage = (positionOffset - DELAY) / (1 - DELAY);
                percentage = percentage <= 0 ? 0 : percentage;
                setViewLocation(position + percentage);
            }
            mPreOffset = positionOffset;
        }

        @Override
        public void onPageSelected(int position) {
            if(mListener != null)
                mListener.onPageSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    public interface OnPageChangeListener {
        void onPageSelected(int position);
    }
}
