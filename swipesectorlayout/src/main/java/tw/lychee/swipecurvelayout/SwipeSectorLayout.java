package tw.lychee.swipecurvelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class SwipeSectorLayout extends RelativeLayout {
    private ViewPager mViewPager;
    private ImageView mCoverImage;
    private ArrayList<ImageView> mViews;
    private Context mContext;
    private ArrayList<ImagePair> mItems;
    private OnPageChangeListener mListener;
    private int ITEM_VIEW_WIDTH;
    private int DEGREE_UNIT;
    private int SECTOR_COLOR;
    private int SECTOR_Height;
    private boolean childViewInit = false;

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
        DEGREE_UNIT = ta.getInteger(R.styleable.SwipeSectorLayout_sector_degree, 90) / 2;
        ITEM_VIEW_WIDTH = ta.getDimensionPixelSize(R.styleable.SwipeSectorLayout_item_width, 200);
        SECTOR_COLOR = ta.getColor(R.styleable.SwipeSectorLayout_sector_color, 0xFFFFFFFF);
        SECTOR_Height = ta.getDimensionPixelSize(R.styleable.SwipeSectorLayout_sector_height, 0);
        ta.recycle();
        mContext = context;
        init();

    }

    public void addItem(@NonNull ImagePair item) {
        mItems.add(item);
        mViewPager.getAdapter().notifyDataSetChanged();
    }

    public void addItems(@NonNull ArrayList<ImagePair> items) {
        mItems.addAll(items);
        mViewPager.getAdapter().notifyDataSetChanged();
    }

    private void init() {
        mItems = new ArrayList<>();

        SwipePagerAdapter adapter = new SwipePagerAdapter(mItems);

        LayoutParams pagerParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mViewPager = new ViewPager(mContext);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new PageChangeListener());
        mViewPager.setLayoutParams(pagerParams);
        mViewPager.setPageTransformer(false, new PageTransformer());
        addView(mViewPager);

        mCoverImage = new ImageView(mContext);
        LayoutParams params = new LayoutParams(100, 100);
        params.addRule(ALIGN_PARENT_BOTTOM);
        pagerParams.addRule(CENTER_HORIZONTAL);
        mCoverImage.setImageResource(R.drawable.circle);
        mCoverImage.setLayoutParams(params);
        mCoverImage.setColorFilter(SECTOR_COLOR);
        mCoverImage.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(mCoverImage);

        mViews = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            ImageView imgView = new ImageView(mContext);
            LayoutParams itemParams = new LayoutParams(ITEM_VIEW_WIDTH, ITEM_VIEW_WIDTH);
            itemParams.addRule(ALIGN_PARENT_LEFT);
            itemParams.addRule(ALIGN_PARENT_BOTTOM);
            imgView.setLayoutParams(itemParams);
            mViews.add(imgView);
            addView(imgView);
        }
    }


    class PageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if(!childViewInit){
                LayoutParams params = (LayoutParams)mCoverImage.getLayoutParams();
                int w = mViewPager.getWidth() / 2;
                double r = w / Math.sin(Math.toRadians(DEGREE_UNIT));
                params.width = (int)Math.round(r * 2);
                params.height = (int)Math.round(r * 2);
                params.bottomMargin = - (int)Math.round(r + r * Math.sin(Math.toRadians(DEGREE_UNIT)) - SECTOR_Height );
                params.leftMargin = - (int)Math.round(r - w);
                params.rightMargin = - (int)Math.round(r - w);

                for(int i = 0; i < mViews.size(); i ++) {
                    setLocation(mViews.get(i), positionOffset, i - 2);
                }
                setViewImage(0);
                childViewInit = true;
            }

            if(positionOffset > 0.9999 || positionOffset == 0){
                return;
            }

            if((positionOffset >= 0.9 && positionOffset < 1) ||
                    (positionOffset > 0 && positionOffset <= 0.1)){
                setViewImage(position);
            }

            setViewLocation(positionOffset);


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

    private void setViewLocation(float percentage) {
        for(int i = 0; i < mViews.size(); i ++) {
            setLocation(mViews.get(i), percentage, i - 2);
        }
    }

    private void setLocation(View view, float percentage, int position) {
        final int windowWidth = mViewPager.getWidth();
        final int halfWindowWidth = windowWidth / 2;
        final int radius = (int)Math.round(halfWindowWidth / Math.sin(Math.toRadians(DEGREE_UNIT)));
        final float HORIZON_SCALE = 1.1f;

        final float degree = DEGREE_UNIT * position - DEGREE_UNIT * percentage;
        view.setRotation(degree);
        LayoutParams params = (LayoutParams)view.getLayoutParams();
        int left = (int)Math.round(
                halfWindowWidth + HORIZON_SCALE * radius * Math.sin(Math.toRadians(degree)) - ITEM_VIEW_WIDTH / 2
        );
        int bottom = (int)Math.round(
                radius * Math.cos(Math.toRadians(degree)) - radius * Math.cos(Math.toRadians(DEGREE_UNIT))
        );

        params.leftMargin = left;
        if(left + ITEM_VIEW_WIDTH > windowWidth){
            params.rightMargin = -(left + ITEM_VIEW_WIDTH - windowWidth);
        }
        params.bottomMargin = bottom;
        view.setLayoutParams(params);

        //Log.d("setting", String.format("degree: %.2f, (%d, %d)", degree, left, bottom));
    }

    private void setViewImage(int currIdx) {
        int start, itemIdx, end;
        if(currIdx == 0) {
            start = 2; itemIdx = currIdx; end = 4;
        } else if(currIdx == 1) {
            start = 1; itemIdx = currIdx - 1; end = 4;
        } else if(currIdx == mItems.size() - 1) {
            start = 0; itemIdx = currIdx - 2; end = 2;
        } else if(currIdx == mItems.size() - 2) {
            start = 0; itemIdx = currIdx - 2; end = 3;
        } else {
            start = 0; itemIdx = currIdx - 2; end = 4;
        }
        for(int i = start, j = itemIdx; i <= end && j < mItems.size(); i++, j++) {
            mViews.get(i).setImageResource(mItems.get(j).getImage());
        }
        for(int i = 0; i < start; i++){
            mViews.get(i).setImageResource(0);
        }
        for(int i = end + 1; i < mViews.size(); i++){
            mViews.get(i).setImageResource(0);
        }
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mListener = listener;
    }

    public interface OnPageChangeListener {
        void onPageSelected(int index);
    }
}
