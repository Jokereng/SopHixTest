package com.alibaba.chenyin.sophixtest.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.chenyin.sophixtest.R;

import butterknife.ButterKnife;

/**
 * Created by chenyin on 17/10/31.
 */

public abstract class BaseFragment extends Fragment implements IBaseFragment {

    private View inflateView;//获取View对象

    private Activity activity;//所属Activity

    public boolean viewCreated;//记录是否已经创建了,防止重复创建

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 防止重复调用onCreate方法，造成在initData方法中adapter重复初始化问题
        if (!viewCreated) {
            viewCreated = true;
            initData();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewCreated = true;
        if (null == inflateView) {
            // 强制竖屏显示
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            int layoutResId = getCreateViewLayoutId();
            if (layoutResId > 0) {
                inflateView = inflater.inflate(getCreateViewLayoutId(), container, false);
            }
            // 解决点击穿透问题
            inflateView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            ButterKnife.bind(this, inflateView);
        }
        return inflateView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (viewCreated == true) {
            initView(view, savedInstanceState);
            initDialog();
            initListener();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 解决ViewPager中的问题
        if (null != inflateView) {
            ((ViewGroup) inflateView.getParent()).removeView(inflateView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public final void startActivity(@NonNull Class<?> targetActivity) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).startActivity(targetActivity);
        }
    }

    /**
     * 跳转到指定的Activity
     *
     * @param extraName      要传递的值的键名称
     * @param extraValue     要传递的String类型值
     * @param targetActivity 要跳转的目标Activity
     */
    public final void startActivity(@NonNull String extraName, @NonNull String extraValue, @NonNull Class<?> targetActivity) {
        if (TextUtils.isEmpty(extraName))
            throw new NullPointerException("传递的值的键名称为null或空");
        final Intent intent = new Intent(getActivity(), targetActivity);
        intent.putExtra(extraName, extraValue);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    /**
     * 跳转到指定的Activity
     *
     * @param extraName      要传递的值的键名称
     * @param extraValue     要传递的int类型值
     * @param targetActivity 要跳转的目标Activity
     */
    public final void startActivity(@NonNull String extraName, @NonNull int extraValue, @NonNull Class<?> targetActivity) {
        if (TextUtils.isEmpty(extraName))
            throw new NullPointerException("传递的值的键名称为null或空");
        final Intent intent = new Intent(getActivity(), targetActivity);
        intent.putExtra(extraName, extraValue);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    /**
     * 跳转到指定的Activity
     *
     * @param extraName      要传递的值的键名称
     * @param value          要传递的序列化后的值
     * @param targetActivity 要跳转的目标Activity
     */
    public final void startActivity(@NonNull String extraName, @NonNull Parcelable value, @NonNull Class<?> targetActivity) {
        if (TextUtils.isEmpty(extraName))
            throw new NullPointerException("传递的值的键名称为null或空");
        final Intent intent = new Intent(getActivity(), targetActivity);
        intent.putExtra(extraName, value);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }


    @Override
    public void initData() {

    }

    @Override
    public void initView(View inflateView, Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initDialog() {

    }
}
