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
    private CircularArrayList<ImageView> mReusableViews;
    private Context mContext;
    private OnPageChangeListener mListener;
    private int mItemViewWidth;
    private int mDegreeUnit;
    private boolean childViewInit = false;
    private ImageAdapter mAdapter;
    private final float DELAY = 0.05f;
    private final int ITEM_VIEW_COUNTS = 5;

    private final int PREVIEW_ITEM_COLOR = 0xFFAAAAAA;
    private final int PREVIEW_BG_COLOR = 0x60AAAAAA;


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
        int degree = ta.getInteger(R.styleable.SwipeSectorLayout_degree, 90);
        if(degree < 20){
            throw new IllegalStateException("degree attribute cannot be less than 20");
        } else if (degree > 180){
            throw new IllegalStateException("degree attribute cannot be greater than 180");
        }
        mDegreeUnit = degree / 2;
        mItemViewWidth = ta.getDimensionPixelSize(R.styleable.SwipeSectorLayout_item_width, 200);
        ta.recycle();
        mContext = context;
        addViews();
        setChildrenDrawingOrderEnabled(true);
        if(isInEditMode())
            setPreviewColor(true);
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int n = getChildCount();
        if(n > ITEM_VIEW_COUNTS + 2){
            throw new IllegalStateException("SwipeSectorLayout can not have more than one child");
        }
        //for preview in layout editor
        if(!childViewInit)
            setViewLocation(0);

    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if(i == 0)
            return 0;
        if(i == 1)
            return childCount - 1;
        return i - 1;
    }

    private void setPreviewColor(boolean isPreview) {
        if(isPreview){
            for(View view: mReusableViews.getAll()){
                view.setBackgroundColor(PREVIEW_ITEM_COLOR);
            }
            setBackgroundColor(PREVIEW_BG_COLOR);
        } else {
            for(View view: mReusableViews.getAll()){
                view.setBackgroundColor(0);
            }
            setBackgroundColor(0);
        }

    }

    private void addViews() {
        LayoutParams pagerParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mViewPager = new CustomPager(mContext);
        mViewPager.setLayoutParams(pagerParams);
        mViewPager.setPageTransformer(false, new CustomTransformer());
        mViewPager.setScrollDurationFactor(1.8);
        mViewPager.addOnPageChangeListener(new PageChangeListener());
        addView(mViewPager);

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
        setViewLocation(0);
        setViewImage(0);
        childViewInit = true;
    }

    private void setViewLocation(float percentage) {
        final int windowWidth = getWidth();
        final int halfWindowWidth = windowWidth / 2;
        final int radius = (int)Math.round(halfWindowWidth / Math.sin(Math.toRadians(mDegreeUnit)));
        final float HORIZON_SCALE = 1.1f;

        for(int i = 0; i < mReusableViews.size(); i++) {
            View view = mReusableViews.get(i);
            float degree = mDegreeUnit * i - mDegreeUnit * percentage;
            int threshold = mDegreeUnit * (mReusableViews.size() - 1);

            while(Math.abs(degree) >= threshold) {
                if(degree < 0)
                    degree += (mReusableViews.size())* mDegreeUnit;
                else
                    degree -= (mReusableViews.size()) * mDegreeUnit;
            }

            view.setRotation(degree);
            int left = (int)Math.round(
                    halfWindowWidth + HORIZON_SCALE * radius * Math.sin(Math.toRadians(degree)) - mItemViewWidth / 2
            );
            int bottom = (int)Math.round(
                    radius * Math.cos(Math.toRadians(degree)) - radius * Math.cos(Math.toRadians(mDegreeUnit))
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
