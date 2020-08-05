package com.sxw.baseui.view.verticalindicator;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.sxw.baseui.R;
import com.sxw.baseui.utils.ViewUtils;
import com.sxw.baseui.view.verticalindicator.adapter.TabAdapter;
import com.sxw.baseui.view.verticalindicator.util.TabFragmentManager;
import com.sxw.baseui.view.verticalindicator.widget.QTabView;
import com.sxw.baseui.view.verticalindicator.widget.TabView;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.view.ViewPager.SCROLL_STATE_IDLE;
import static android.support.v4.view.ViewPager.SCROLL_STATE_SETTLING;

/**
 * @author chqiu
 * Email:qstumn@163.com
 */
public class VerticalTabLayout extends ScrollView {
    private Context mContext;
    private TabStrip mTabStrip;
    private int mColorIndicator;
    private TabView mSelectedTab;
    private int mTabMargin;
    private int mIndicatorWidth;
    private int mIndicatorGravity;
    private float mIndicatorCorners;
    private float mRoundrCorners;
    private int mTabMode;
    private int mTabHeight;

    private int firstSelectPosition = 0;
    //lineIndicator
    private int mLineIndicatorWidth;
    private int mLineIndicatorColor;
    private int mLineIndicatorGravity = 10;//left-->10   right-->20
    private boolean mLineIndicatorSHow = false;
    private float mLineIndicatorHeightRatio = 1.0f;
    private int mLineIndicatorMargin = 0;


    public static int TAB_MODE_FIXED = 10;
    public static int TAB_MODE_SCROLLABLE = 11;

    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;

    private OnTabClickListener mOnTabClickListener;
    private List<OnTabSelectedListener> mTabSelectedListeners;
    private OnTabPageChangeListener mTabPageChangeListener;
    private DataSetObserver mPagerAdapterObserver;
    private TabFragmentManager mTabFragmentManager;
    private int aniDuration = 100;
    private Path mPath = new Path();
    private float[] mRids;

    private int[] gradientColors;

    public void setGradientColors(int[] gradientColors) {
        this.gradientColors = gradientColors;
    }

    public VerticalTabLayout(Context context) {
        this(context, null);
    }

    public VerticalTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mTabSelectedListeners = new ArrayList<>();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerticalTabLayout);
        mColorIndicator = typedArray.getColor(R.styleable.VerticalTabLayout_indicator_color,
                context.getResources().getColor(R.color.colorAccent));
        mIndicatorWidth = (int) typedArray.getDimension(R.styleable.VerticalTabLayout_indicator_width, ViewUtils.dp2px(3));
        mIndicatorCorners = typedArray.getDimension(R.styleable.VerticalTabLayout_indicator_corners, 0);
        mRoundrCorners = typedArray.getDimension(R.styleable.VerticalTabLayout_round_corners, 0);
        mIndicatorGravity = typedArray.getInteger(R.styleable.VerticalTabLayout_indicator_gravity, Gravity.LEFT);
        if (mIndicatorGravity == 3) {
            mIndicatorGravity = Gravity.LEFT;
        } else if (mIndicatorGravity == 5) {
            mIndicatorGravity = Gravity.RIGHT;
        } else if (mIndicatorGravity == 119) {
            mIndicatorGravity = Gravity.FILL;
        }
        mTabMargin = (int) typedArray.getDimension(R.styleable.VerticalTabLayout_tab_margin, 0);
        mTabMode = typedArray.getInteger(R.styleable.VerticalTabLayout_tab_mode, TAB_MODE_FIXED);
        int defaultTabHeight = LinearLayout.LayoutParams.WRAP_CONTENT;
        mTabHeight = (int) typedArray.getDimension(R.styleable.VerticalTabLayout_tab_height, defaultTabHeight);


        mLineIndicatorColor = typedArray.getColor(R.styleable.VerticalTabLayout_lineindicator_color
                , context.getResources().getColor(R.color.colorAccent));
        mLineIndicatorSHow = typedArray.getBoolean(R.styleable.VerticalTabLayout_lineindicator_show, false);
        mLineIndicatorWidth = (int) typedArray.getDimension(R.styleable.VerticalTabLayout_lineindicator_width, 4);
        mLineIndicatorHeightRatio = typedArray.getFloat(R.styleable.VerticalTabLayout_lineIndicator_height_ratio, 1.0f);
        mLineIndicatorMargin = (int) typedArray.getDimension(R.styleable.VerticalTabLayout_lineIndicator_margin, 0);
        mLineIndicatorGravity = typedArray.getInteger(R.styleable.VerticalTabLayout_lineindicator_gravity, Gravity.LEFT);
        if (mLineIndicatorGravity == 10) {
            mLineIndicatorGravity = Gravity.LEFT;
        } else {
            mLineIndicatorGravity = Gravity.RIGHT;
        }

        typedArray.recycle();

        mRids = new float[]{mRoundrCorners, mRoundrCorners, mRoundrCorners, mRoundrCorners
                , mRoundrCorners, mRoundrCorners, mRoundrCorners, mRoundrCorners,};

        setWillNotDraw(false);
    }

    public void setOnTabClickListener(OnTabClickListener onTabClickListener) {
        mOnTabClickListener = onTabClickListener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            removeAllViews();
        }
        initTabStrip();
    }

    public void setAniDuration(int aniDuration) {
        this.aniDuration = aniDuration;
    }

    private void initTabStrip() {
        mTabStrip = new TabStrip(mContext);
        addView(mTabStrip, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public void removeAllTabs() {
        mTabStrip.removeAllViews();
        mSelectedTab = null;
    }

    public TabView getTabAt(int position) {
        return (TabView) mTabStrip.getChildAt(position);
    }

    public int getTabCount() {
        return mTabStrip.getChildCount();
    }

    public int getSelectedTabPosition() {
        int index = mTabStrip.indexOfChild(mSelectedTab);
        return index == -1 ? 0 : index;
    }

    private void addTabWithMode(TabView tabView) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        initTabWithMode(params);
        mTabStrip.addView(tabView, params);

        DividerBean divider = tabView.getDividerBean();
        if (divider != null) {
            View dView = new View(mContext);
            dView.setBackgroundResource(divider.getColor());
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, divider.getHeight());
            layoutParams.gravity = Gravity.BOTTOM;
            tabView.addView(dView, layoutParams);
        }

        if (mTabStrip.indexOfChild(tabView) == firstSelectPosition) {
            tabView.setChecked(true);
            params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            tabView.setLayoutParams(params);
            mSelectedTab = tabView;
            setTabSelected(firstSelectPosition);
            mTabStrip.post(new Runnable() {
                @Override
                public void run() {
                    mTabStrip.moveIndicator(firstSelectPosition);
                }
            });
        }
    }

    private void initTabWithMode(LinearLayout.LayoutParams params) {
        if (mTabMode == TAB_MODE_FIXED) {
            params.height = 0;
            params.weight = 1.0f;
            params.setMargins(0, 0, 0, 0);
            setFillViewport(true);
        } else if (mTabMode == TAB_MODE_SCROLLABLE) {
            params.height = mTabHeight;
            params.weight = 0f;
            params.setMargins(0, mTabMargin, 0, 0);
            setFillViewport(false);
        }
    }

    private void scrollToTab(int position) {
        final TabView tabView = getTabAt(position);
        int y = getScrollY();
        int tabTop = tabView.getTop() + tabView.getHeight() / 2 - y;
        int target = getHeight() / 2;
        if (tabTop > target) {
            smoothScrollBy(0, tabTop - target);
        } else if (tabTop < target) {
            smoothScrollBy(0, tabTop - target);
        }
    }

    private float mLastPositionOffset;

    private void scrollByTab(int position, final float positionOffset) {
        final TabView tabView = getTabAt(position);
        int y = getScrollY();
        int tabTop = tabView.getTop() + tabView.getHeight() / 2 - y;
        int target = getHeight() / 2;
        int nextScrollY = tabView.getHeight() + mTabMargin;
        if (positionOffset > 0) {
            float percent = positionOffset - mLastPositionOffset;
            if (tabTop > target) {
                smoothScrollBy(0, (int) (nextScrollY * percent));
            }
        }
        mLastPositionOffset = positionOffset;
    }

    public VerticalTabLayout setFirstSelectPosition(int firstSelectPosition) {
        this.firstSelectPosition = firstSelectPosition;
        return this;
    }

    public void addTab(TabView tabView) {
        if (tabView != null) {
            addTabWithMode(tabView);
            tabView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = mTabStrip.indexOfChild(view);
                    setTabSelected(position);
                    if (mOnTabClickListener != null) {
                        mOnTabClickListener.onTabClick((TabView) view, position);
                    }
                }
            });
        } else {
            throw new IllegalStateException("tabview can't be null");
        }
    }

    public void setTabSelected(final int position) {
        setTabSelected(position, true, true);
    }

    public void setTabSelected(final int position, final boolean callListener) {
        setTabSelected(position, true, callListener);
    }

    private void setTabSelected(final int position, final boolean updataIndicator, final boolean callListener) {
        post(new Runnable() {
            @Override
            public void run() {
                setTabSelectedImpl(position, updataIndicator, callListener);
            }
        });
    }

    private void setTabSelectedImpl(final int position, boolean updataIndicator, boolean callListener) {
        TabView view = getTabAt(position);
        if (view == null) {
            return;
        }
        boolean selected;
        if (selected = (view != mSelectedTab)) {
            if (mSelectedTab != null) {
                mSelectedTab.setChecked(false);
            }
            if (updataIndicator) {
                mTabStrip.moveIndicatorWithAnimator(position);
            }
            view.setChecked(true);
            mSelectedTab = view;
            scrollToTab(position);
        }
        if (callListener) {
            for (int i = 0; i < mTabSelectedListeners.size(); i++) {
                OnTabSelectedListener listener = mTabSelectedListeners.get(i);
                if (listener != null) {
                    if (selected) {
                        listener.onTabSelected(view, position);
                    } else {
                        listener.onTabReselected(view, position);
                    }
                }
            }
        }
    }

    public void setTabBadge(int tabPosition, int badgeNum) {
        getTabAt(tabPosition).getBadgeView().setBadgeNumber(badgeNum);
    }

    public void setTabBadge(int tabPosition, String badgeText) {
        getTabAt(tabPosition).getBadgeView().setBadgeText(badgeText);
    }

    public void setTabMode(int mode) {
        if (mode != TAB_MODE_FIXED && mode != TAB_MODE_SCROLLABLE) {
            throw new IllegalStateException("only support TAB_MODE_FIXED or TAB_MODE_SCROLLABLE");
        }
        if (mode == mTabMode) {
            return;
        }
        mTabMode = mode;
        for (int i = 0; i < mTabStrip.getChildCount(); i++) {
            View view = mTabStrip.getChildAt(i);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
            initTabWithMode(params);
            if (i == 0) {
                params.setMargins(0, 0, 0, 0);
            }
            view.setLayoutParams(params);
        }
        mTabStrip.invalidate();
        mTabStrip.post(new Runnable() {
            @Override
            public void run() {
                mTabStrip.updataIndicator();
            }
        });
    }

    /**
     * only in TAB_MODE_SCROLLABLE mode will be supported
     *
     * @param margin margin
     */
    public void setTabMargin(int margin) {
        if (margin == mTabMargin) {
            return;
        }
        mTabMargin = margin;
        if (mTabMode == TAB_MODE_FIXED) {
            return;
        }
        for (int i = 0; i < mTabStrip.getChildCount(); i++) {
            View view = mTabStrip.getChildAt(i);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
            params.setMargins(0, i == 0 ? 0 : mTabMargin, 0, 0);
            view.setLayoutParams(params);
        }
        mTabStrip.invalidate();
        mTabStrip.post(new Runnable() {
            @Override
            public void run() {
                mTabStrip.updataIndicator();
            }
        });
    }

    /**
     * only in TAB_MODE_SCROLLABLE mode will be supported
     *
     * @param height height
     */
    public void setTabHeight(int height) {
        if (height == mTabHeight) {
            return;
        }
        mTabHeight = height;
        if (mTabMode == TAB_MODE_FIXED) {
            return;
        }
        for (int i = 0; i < mTabStrip.getChildCount(); i++) {
            View view = mTabStrip.getChildAt(i);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
            params.height = mTabHeight;
            view.setLayoutParams(params);
        }
        mTabStrip.invalidate();
        mTabStrip.post(new Runnable() {
            @Override
            public void run() {
                mTabStrip.updataIndicator();
            }
        });
    }

    public void setIndicatorColor(int color) {
        mColorIndicator = color;
        mTabStrip.invalidate();
    }

    public void setIndicatorWidth(int width) {
        mIndicatorWidth = width;
        mTabStrip.setIndicatorGravity();
    }

    public void setIndicatorCorners(int corners) {
        mIndicatorCorners = corners;
        mTabStrip.invalidate();
    }

    /**
     * @param gravity only support Gravity.LEFT,Gravity.RIGHT,Gravity.FILL
     */
    public void setIndicatorGravity(int gravity) {
        if (gravity == Gravity.LEFT || gravity == Gravity.RIGHT || Gravity.FILL == gravity) {
            mIndicatorGravity = gravity;
            mTabStrip.setIndicatorGravity();
        } else {
            throw new IllegalStateException("only support Gravity.LEFT,Gravity.RIGHT,Gravity.FILL");
        }
    }

    public void addOnTabSelectedListener(OnTabSelectedListener listener) {
        if (listener != null) {
            mTabSelectedListeners.add(listener);
        }
    }

    public void removeOnTabSelectedListener(OnTabSelectedListener listener) {
        if (listener != null) {
            mTabSelectedListeners.remove(listener);
        }
    }

    public void setTabAdapter(TabAdapter adapter) {
        removeAllTabs();
        if (adapter != null) {
            for (int i = 0; i < adapter.getCount(); i++) {
                addTab(new QTabView(mContext).setIcon(adapter.getIcon(i))
                        .setTitle(adapter.getTitle(i))
                        .setBadge(adapter.getBadge(i))
                        .setDividerBean(adapter.getDividerBean(i))
                        .setBackground(adapter.getBackground(i)));
            }
        }
    }

    public void setupWithFragment(FragmentManager manager, int containerResid, List<Fragment> fragments) {
        if (mTabFragmentManager != null) {
            mTabFragmentManager.detach();
        }
        if (containerResid != 0) {
            mTabFragmentManager = new TabFragmentManager(manager, containerResid, fragments, this);
        } else {
            mTabFragmentManager = new TabFragmentManager(manager, fragments, this);
        }
    }

    public void setupWithFragment(FragmentManager manager, int containerResid, List<Fragment> fragments, TabAdapter adapter) {
        setTabAdapter(adapter);
        setupWithFragment(manager, containerResid, fragments);
    }

    public void setupWithViewPager(@Nullable ViewPager viewPager) {
        if (mViewPager != null && mTabPageChangeListener != null) {
            mViewPager.removeOnPageChangeListener(mTabPageChangeListener);
        }

        if (viewPager != null) {
            final PagerAdapter adapter = viewPager.getAdapter();
            if (adapter == null) {
                throw new IllegalArgumentException("ViewPager does not have a PagerAdapter set");
            }

            mViewPager = viewPager;

            if (mTabPageChangeListener == null) {
                mTabPageChangeListener = new OnTabPageChangeListener();
            }
            viewPager.addOnPageChangeListener(mTabPageChangeListener);

            addOnTabSelectedListener(new OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabView tab, int position) {
                    if (mViewPager != null && mViewPager.getAdapter().getCount() >= position) {
                        mViewPager.setCurrentItem(position);
                    }
                }

                @Override
                public void onTabReselected(TabView tab, int position) {
                }
            });

            setPagerAdapter(adapter, true);
        } else {
            mViewPager = null;
            setPagerAdapter(null, true);
        }
    }

    private void setPagerAdapter(@Nullable final PagerAdapter adapter, final boolean addObserver) {
        if (mPagerAdapter != null && mPagerAdapterObserver != null) {
            mPagerAdapter.unregisterDataSetObserver(mPagerAdapterObserver);
        }

        mPagerAdapter = adapter;

        if (addObserver && adapter != null) {
            if (mPagerAdapterObserver == null) {
                mPagerAdapterObserver = new PagerAdapterObserver();
            }
            adapter.registerDataSetObserver(mPagerAdapterObserver);
        }

        populateFromPagerAdapter();
    }

    private void populateFromPagerAdapter() {
        removeAllTabs();
        if (mPagerAdapter != null) {
            final int adapterCount = mPagerAdapter.getCount();
            if (mPagerAdapter instanceof TabAdapter) {
                setTabAdapter((TabAdapter) mPagerAdapter);
            } else {
                for (int i = 0; i < adapterCount; i++) {
                    String title = mPagerAdapter.getPageTitle(i) == null ? "tab" + i : mPagerAdapter.getPageTitle(i).toString();
                    addTab(new QTabView(mContext).setTitle(
                            new QTabView.TabTitle.Builder().setContent(title).build()));
                }
            }

            // Make sure we reflect the currently set ViewPager item
            if (mViewPager != null && adapterCount > 0) {
                final int curItem = mViewPager.getCurrentItem();
                if (curItem != getSelectedTabPosition() && curItem < getTabCount()) {
                    setTabSelected(curItem);
                }
            }
        } else {
            removeAllTabs();
        }
    }

    private class TabStrip extends LinearLayout {
        private float mIndicatorTopY;
        private float mIndicatorX;
        private float mIndicatorBottomY;
        private int mLastWidth;
        private Paint mIndicatorPaint;
        private RectF mIndicatorRect;
        private RectF mLineIndicatorRedRect;
        private Paint mLineIndicatorPaint;
        private AnimatorSet mIndicatorAnimatorSet;

        public TabStrip(Context context) {
            super(context);
            setWillNotDraw(false);
            setOrientation(LinearLayout.VERTICAL);
            mIndicatorPaint = new Paint();
            mIndicatorPaint.setAntiAlias(true);
            mIndicatorGravity = mIndicatorGravity == 0 ? Gravity.LEFT : mIndicatorGravity;
            mIndicatorRect = new RectF();
            mLineIndicatorRedRect = new RectF();

            mLineIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mLineIndicatorPaint.setColor(Color.RED);
            mLineIndicatorPaint.setStyle(Paint.Style.FILL);
            setIndicatorGravity();
        }

        protected void setIndicatorGravity() {
            if (mIndicatorGravity == Gravity.LEFT) {
                mIndicatorX = 0;
                if (mLastWidth != 0) {
                    mIndicatorWidth = mLastWidth;
                }
                setPadding(mIndicatorWidth, 0, 0, 0);
            } else if (mIndicatorGravity == Gravity.RIGHT) {
                if (mLastWidth != 0) {
                    mIndicatorWidth = mLastWidth;
                }
                setPadding(0, 0, mIndicatorWidth, 0);
            } else if (mIndicatorGravity == Gravity.FILL) {
                mIndicatorX = 0;
                setPadding(0, 0, 0, 0);
            }
            post(new Runnable() {
                @Override
                public void run() {
                    if (mIndicatorGravity == Gravity.RIGHT) {
                        mIndicatorX = getWidth() - mIndicatorWidth;
                    } else if (mIndicatorGravity == Gravity.FILL) {
                        mLastWidth = mIndicatorWidth;
                        mIndicatorWidth = getMeasuredWidth();
                    }
                    invalidate();
                }
            });

            if (mLineIndicatorGravity == Gravity.LEFT) {
                mLineIndicatorRedRect.left = mLineIndicatorMargin;
                mLineIndicatorRedRect.right = mLineIndicatorWidth + mLineIndicatorMargin;

                int paddingLeft = getPaddingLeft() + mLineIndicatorWidth + mLineIndicatorMargin;
                setPadding(paddingLeft, 0, 0, 0);
            } else {
                mLineIndicatorRedRect.left = getWidth() - mLineIndicatorWidth - mLineIndicatorMargin;
                mLineIndicatorRedRect.right = getWidth() - mLineIndicatorMargin;
                int paddingRight = getPaddingLeft() + mLineIndicatorWidth + mLineIndicatorMargin;
                setPadding(0, 0, paddingRight, 0);
            }

        }

        private void calcIndicatorY(float offset) {
            int index = (int) Math.floor(offset);
            View childView = getChildAt(index);
            if (Math.floor(offset) != getChildCount() - 1 && Math.ceil(offset) != 0) {
                View nextView = getChildAt(index + 1);
                mIndicatorTopY = childView.getTop() + (nextView.getTop() - childView.getTop()) * (offset - index);
                mIndicatorBottomY = childView.getBottom() + (nextView.getBottom() -
                        childView.getBottom()) * (offset - index);
            } else {
                mIndicatorTopY = childView.getTop();
                mIndicatorBottomY = childView.getBottom();
            }
            /*fragment中可能会出现此mIndicatorBottomY=0的问题（Fragment延迟加载导致）*/
            if (mIndicatorBottomY == 0) {
                mIndicatorBottomY = mTabHeight;
            }
        }

        protected void updataIndicator() {
            if (getChildCount() > 0) {
                moveIndicatorWithAnimator(getSelectedTabPosition());
            }
        }

        protected void moveIndicator(float offset) {
            calcIndicatorY(offset);
            invalidate();
        }

        /**
         * move indicator to a tab location
         *
         * @param index tab location's index
         */
        protected void moveIndicatorWithAnimator(int index) {
            final int direction = index - getSelectedTabPosition();
            View childView = getChildAt(index);
            if (childView == null) {
                return;
            }
            final float targetTop = childView.getTop();
            final float targetBottom = childView.getBottom();
            if (mIndicatorTopY == targetTop && mIndicatorBottomY == targetBottom) {
                return;
            }
            if (mIndicatorAnimatorSet != null && mIndicatorAnimatorSet.isRunning()) {
                mIndicatorAnimatorSet.end();
            }
            post(new Runnable() {
                @Override
                public void run() {
                    ValueAnimator startAnime = null;
                    ValueAnimator endAnime = null;
                    if (direction > 0) {
                        startAnime = ValueAnimator.ofFloat(mIndicatorBottomY, targetBottom)
                                .setDuration(aniDuration);
                        startAnime.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                mIndicatorBottomY = Float.parseFloat(animation.getAnimatedValue().toString());
                                invalidate();
                            }
                        });
                        endAnime = ValueAnimator.ofFloat(mIndicatorTopY, targetTop).setDuration(aniDuration);
                        endAnime.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                mIndicatorTopY = Float.parseFloat(animation.getAnimatedValue().toString());
                                invalidate();
                            }
                        });
                    } else if (direction < 0) {
                        startAnime = ValueAnimator.ofFloat(mIndicatorTopY, targetTop).setDuration(aniDuration);
                        startAnime.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                mIndicatorTopY = Float.parseFloat(animation.getAnimatedValue().toString());
                                invalidate();
                            }
                        });
                        endAnime = ValueAnimator.ofFloat(mIndicatorBottomY, targetBottom).setDuration(aniDuration);
                        endAnime.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                mIndicatorBottomY = Float.parseFloat(animation.getAnimatedValue().toString());
                                invalidate();
                            }
                        });
                    }
                    if (startAnime != null) {
                        mIndicatorAnimatorSet = new AnimatorSet();
//                        mIndicatorAnimatorSet.playTogether(startAnime,startAnime);
                        mIndicatorAnimatorSet.play(startAnime).with(endAnime);
//                        mIndicatorAnimatorSet.play(endAnime).after(startAnime);
                        mIndicatorAnimatorSet.start();
                    }
                }
            });
        }


        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (mRoundrCorners > 0) {
                canvas.clipPath(mPath);
            }

            mIndicatorRect.left = mIndicatorX;
            mIndicatorRect.top = mIndicatorTopY;
            mIndicatorRect.right = mIndicatorX + mIndicatorWidth;
            mIndicatorRect.bottom = mIndicatorBottomY;

            if (gradientColors != null && gradientColors.length >= 2) {
                float[] position = {0f, 0.8f, 1.0f};
                LinearGradient linearGradient = new LinearGradient(mIndicatorRect.left, mIndicatorRect.left
                        , mIndicatorRect.left, mIndicatorRect.bottom, gradientColors, position, Shader.TileMode.CLAMP);
                mIndicatorPaint.setShader(linearGradient);
            } else {
                mIndicatorPaint.setColor(mColorIndicator);
            }

            if (mIndicatorCorners != 0) {
                canvas.drawRoundRect(mIndicatorRect, mIndicatorCorners, mIndicatorCorners, mIndicatorPaint);
            } else {
                canvas.drawRect(mIndicatorRect, mIndicatorPaint);
            }

            if (mLineIndicatorSHow) {
                if (mLineIndicatorHeightRatio >= 1.0f) {
                    mLineIndicatorRedRect.top = mIndicatorTopY;
                    mLineIndicatorRedRect.bottom = mIndicatorBottomY;
                } else {
                    float height = mIndicatorBottomY - mIndicatorTopY;
                    mLineIndicatorRedRect.top = mIndicatorTopY + height * (1 - mLineIndicatorHeightRatio) / 2;
                    mLineIndicatorRedRect.bottom = mIndicatorBottomY - height * (1 - mLineIndicatorHeightRatio) / 2;
                }

                mLineIndicatorPaint.setColor(mLineIndicatorColor);
                canvas.drawRect(mLineIndicatorRedRect, mLineIndicatorPaint);
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            if (mIndicatorGravity == Gravity.FILL) {
                mIndicatorWidth = getMeasuredWidth();
            }
            mPath.addRoundRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()), mRids, Path.Direction.CW);
        }
    }

    private class OnTabPageChangeListener implements ViewPager.OnPageChangeListener {
        private int mPreviousScrollState;
        private int mScrollState;
        boolean mUpdataIndicator;

        public OnTabPageChangeListener() {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mPreviousScrollState = mScrollState;
            mScrollState = state;
            mUpdataIndicator = !(mScrollState == SCROLL_STATE_SETTLING
                    && mPreviousScrollState == SCROLL_STATE_IDLE);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            if (mUpdataIndicator) {
                mTabStrip.moveIndicator(positionOffset + position);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (position != getSelectedTabPosition()) {
                setTabSelected(position, !mUpdataIndicator, true);
            }
        }
    }

    private class PagerAdapterObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            populateFromPagerAdapter();
        }

        @Override
        public void onInvalidated() {
            populateFromPagerAdapter();
        }
    }

    public interface OnTabSelectedListener {

        void onTabSelected(TabView tab, int position);

        void onTabReselected(TabView tab, int position);
    }

    public interface OnTabClickListener {
        void onTabClick(TabView tabView, int position);
    }
}
