package com.example.qhsj.guidedialog.view;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.qhsj.guidedialog.R;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

/**
 * DialogFragment
 */
public class MyDialogFragment extends DialogFragment {
    public int[] mImages;
    private ArrayList<View> pageViews;
    private int mPosition;
    private boolean isDragging;
    private ViewPager.PageTransformer mPageTransformer;
    public static MyDialogFragment instance = null;
    private boolean mIsCancel;
    private boolean mIsTransparent;

    public MyDialogFragment() {
    }

    /*
     * 可以防止快速点击的时候报错
     */
    public static MyDialogFragment getInstance() {
        if (instance == null) {
            synchronized (MyDialogFragment.class) {
                if (instance == null) {
                    instance = new MyDialogFragment();
                }
            }
        }
        return instance;
    }

    /**
     * 设置图片列表
     */
    public MyDialogFragment setImages(int[] images) {
        mImages = images;
        return this;
    }

    /**
     * 设置ViewPager切换动画方式
     */
    public MyDialogFragment setPageTransformer(ViewPager.PageTransformer pageTransformer) {
        mPageTransformer = pageTransformer;
        return this;
    }

    /*
     * 显示 DialogFragment
     */
    public MyDialogFragment show(android.app.FragmentManager fragmentManager) {
        if (instance != null) {
            instance.show(fragmentManager, "ZqgDialogFragment");
        }
        return this;
    }

    /**
     * 点击四周是否取消dialog,默认取消
     */
    public MyDialogFragment setCanceledOnTouchOutside(boolean isCancel) {
        mIsCancel = isCancel;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
        pageViews = new ArrayList<>();
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (mIsCancel) {
            getDialog().setCanceledOnTouchOutside(mIsCancel);
        }

        View view = inflater.inflate(R.layout.fragment_dialog, container);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        for (int image : mImages) {
             // SimpleDraweeView
            View inflate = inflater.inflate(R.layout.guide_pager_four, null);
            ImageView imageView = (ImageView) inflate.findViewById(R.id.sdv_item_guide_img);
            Uri uri = new Uri.Builder().scheme("res").path(String.valueOf(image)).build();
            imageView.setImageURI(uri); // 一行代码绑定 uri
            pageViews.add(imageView);
        }

         // 设置 Vp
        viewPager.setPageTransformer(true, mPageTransformer);
        viewPager.setAdapter(new ZqgPagerAdapter());
        indicator.setViewPager(viewPager);

         // 设置监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        isDragging = true;
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        isDragging = false;
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                         // 最后一张再继续拖动消失 Dialog
                        if (mPosition == pageViews.size() - 1 && isDragging) {
                            getDialog().dismiss();
                        }
                        break;
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mIsTransparent) {
            Window window = getDialog().getWindow();
            WindowManager.LayoutParams windowParams = window.getAttributes();
            windowParams.dimAmount = 0.0f;
            window.setAttributes(windowParams);
        }
    }

    class ZqgPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(pageViews.get(position));
            return pageViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(pageViews.get(position));
        }
    }
}
