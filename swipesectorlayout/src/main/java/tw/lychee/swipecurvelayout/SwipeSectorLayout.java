package tw.lychee.swipecurvelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SwipeSectorLayout extends RelativeLayout {
    private CustomPager mViewPager;
    private ImageView mCoverImage;
    private ImageView mViews[];
    private Context mContext;
    private OnPageChangeListener mListener;
    private int mItemViewWidth;
    private int mInnerDegreeUnit;
    private int mOuterDegreeUnit;
    private int mSectorColor;
    private int mSectorHeight;
    private boolean childViewInit = false;
    private ImageAdapter mAdapter;
    private final float DELAY = 0.2f;
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

        mViews = new ImageView[ITEM_VIEW_COUNTS];
        for(int i = 0; i < mViews.length; i++){
            mViews[i] = new ImageView(mContext);
            LayoutParams itemParams = new LayoutParams(mItemViewWidth, mItemViewWidth);
            itemParams.addRule(ALIGN_PARENT_LEFT);
            itemParams.addRule(ALIGN_PARENT_BOTTOM);
            mViews[i].setLayoutParams(itemParams);
            addView(mViews[i]);
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

        for(int i = 0; i < mViews.length; i ++) {
            setLocation(mViews[i], 0, i - 2);
        }
        setViewImage(0);
        childViewInit = true;
    }

    private void setViewLocation(float percentage) {
        for(int i = 0; i < mViews.length; i ++) {
            setLocation(mViews[i], percentage, i - 2);
        }
    }

    private void setLocation(View view, float percentage, int position) {
        final int windowWidth = mViewPager.getWidth();
        final int halfWindowWidth = windowWidth / 2;
        final int radius = (int)Math.round(halfWindowWidth / Math.sin(Math.toRadians(mInnerDegreeUnit)));
        final float HORIZON_SCALE = 1.1f;

        final float degree = mInnerDegreeUnit * position - mInnerDegreeUnit * percentage;
        view.setRotation(degree);
        LayoutParams params = (LayoutParams)view.getLayoutParams();
        int left = (int)Math.round(
                halfWindowWidth + HORIZON_SCALE * radius * Math.sin(Math.toRadians(degree)) - mItemViewWidth / 2
        );
        int bottom = (int)Math.round(
                radius * Math.cos(Math.toRadians(degree)) - radius * Math.cos(Math.toRadians(mInnerDegreeUnit))
        );

        params.leftMargin = left;
        if(left + mItemViewWidth > windowWidth){
            params.rightMargin = -(left + mItemViewWidth - windowWidth);
        }
        params.bottomMargin = bottom;
        view.setLayoutParams(params);

        //Log.d("setting", String.format("degree: %.2f, (%d, %d)", degree, left, bottom));
    }

    private void setViewImage(int currIdx) {
        //This rule is base on ITEM_VIEW_COUNTS = 5
        int start, itemIdx, end;
        if(currIdx == 0) {
            start = 2; itemIdx = currIdx; end = 4;
        } else if(currIdx == 1) {
            start = 1; itemIdx = currIdx - 1; end = 4;
        } else if(currIdx == mAdapter.getCount() - 1) {
            start = 0; itemIdx = currIdx - 2; end = 2;
        } else if(currIdx == mAdapter.getCount() - 2) {
            start = 0; itemIdx = currIdx - 2; end = 3;
        } else {
            start = 0; itemIdx = currIdx - 2; end = 4;
        }
        for(int i = start, j = itemIdx; i <= end && j < mAdapter.getCount(); i++, j++) {
            mAdapter.setItemView(j, mViews[i]);
        }
        for(int i = 0; i < start; i++){
            mViews[i].setImageResource(0);
        }
        for(int i = end + 1; i < mViews.length; i++){
            mViews[i].setImageResource(0);
        }
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mListener = listener;
    }

    class PageChangeListener implements ViewPager.OnPageChangeListener {
        private float mPreOffset = 0;
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if(!childViewInit){
                initChildView();
            }

            if(positionOffset > 0.9999 || positionOffset == 0){
                mPreOffset = 0;
                return;
            }

            if((positionOffset >= 0.9 && positionOffset < 1) ||
                    (positionOffset > 0 && positionOffset <= 0.1)){
                setViewImage(position);
            }

            if(mPreOffset == 0){

            } else if(mPreOffset < positionOffset) {
                //right
                float percentage = positionOffset / (1 - DELAY);
                percentage = percentage >= 1 ? 1 : percentage;
                setViewLocation(percentage);
            } else {
                //left
                float percentage = (positionOffset - DELAY) / (1 - DELAY);
                percentage = percentage <= 0 ? 0 : percentage;
                setViewLocation(percentage);
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
