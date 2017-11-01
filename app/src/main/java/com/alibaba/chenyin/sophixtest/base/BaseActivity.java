package com.alibaba.chenyin.sophixtest.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.chenyin.sophixtest.R;
import com.alibaba.chenyin.sophixtest.util.LayoutUtil;
import com.alibaba.chenyin.sophixtest.util.ObjectUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by chenyin on 17/10/31.
 */

public abstract class BaseActivity extends AppCompatActivity implements IBaseActivity, Toolbar.OnMenuItemClickListener {

    private FragmentManager fragmentManager;//Fragment管理器

    private Bundle savedInstanceState;//保存的Activity状态

    private boolean isStarted;//记录是否已经调用过onStart方法

    private List<Fragment> addedFragments;//保存通过addFragment方法添加的Fragment

    private Toolbar toolbar;//Design风格Toolbar控件

    private View toolbarMenuView;//Toolbar添加的自定义布局

    public TextView toolbarTitle;//标题

    private View view;//当前activity的view

    protected String TAG = getClass().getSimpleName();//类名

    protected abstract int getContentViewLayoutResId();

    @Override
    final protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG,"onCreate.......创建");
        //当系统版本为4.4或者4.4以上时可以使用沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        if (getContentViewLayoutResId() > 0) {
            view = LayoutInflater.from(this).inflate(getContentViewLayoutResId(), null);
            setContentView(view);
            ButterKnife.bind(this);
            //设置ToolBar
            View v = findViewById(getToolbarResId());
//            if (v != null) {
//                toolbar = (Toolbar) v;
//                toolbar.setPadding(0, statusHeight, 0, 0);
//                setSupportActionBar(toolbar);
//                toolbarTitle = (TextView) v.findViewById(R.id.toolbar_title);
//                if (toolbarTitle != null) {
//                    getSupportActionBar().setDisplayShowTitleEnabled(false);
//                }
//            }
        }
        this.savedInstanceState = savedInstanceState;
        isStarted = true;
        fragmentManager = getSupportFragmentManager();
        // 解决当APP发生错误Activity被重启时Fragment重叠问题
        if (hasFragment()) {
            // 清空已经添加的Fragment
            addedFragments.clear();
            // 将已经添加的Fragment从Activity中分离
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            for (Fragment fragment : addedFragments) {
                fragmentTransaction.remove(fragment);
                fragmentTransaction.commit();
            }
        }
        initData(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG,"onStart.......启动");
        if (isStarted) {
            initView(savedInstanceState);
            // 添加Fragment到Activity中
            addFragments();
            initDialog(savedInstanceState);
            initListener(savedInstanceState);
            isStarted = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG,"onResume.......完成");
        // 竖屏显示
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG,"onPause.......休眠");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG,"onStop.......停止");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG,"onRestart.......重启");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"onDestroy.......销毁");
        if (hasFragment()) {
//            LogUtil.d("原来有添加过Fragment...");
            // 清空已经添加的Fragment
            addedFragments.clear();
            // 将已经添加的Fragment从Activity中分离
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            for (Fragment fragment : addedFragments) {
                fragmentTransaction.detach(fragment);
                fragmentTransaction.commit();
            }
        }
        fragmentManager = null;
        addedFragments = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 解决Fragment嵌套onActivityResult不回调问题，这是FragmentActivity的bug
        if (hasFragment()) {
            for (Fragment fragment : addedFragments) {
                if (!fragment.isDetached() && ObjectUtil.notNull(fragment))
                    fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final int menuId = getToolbarMenuId();
        if (menuId > 0) {
            getMenuInflater().inflate(menuId, menu);
            // 自定义ToolBar menu布局
            final int layoutResId = getToolbarMenuLayoutResId();
            if (layoutResId > 0) {
                toolbarMenuView = LayoutUtil.inflate(this, layoutResId);
                // 需要API 11无法兼容低版本
                // menu.findItem(0).setActionView(toolbarMenuView);
                // 解决低版本兼容问题，使用v4包的MenuItemCompat类
                final int menuResId = getToolbarMenuResId();
                MenuItem menuItem = menu.findItem(menuResId);
                MenuItemCompat.setActionView(menuItem, toolbarMenuView);
                initToolbarMenuActionView(toolbarMenuView);
            }
        }
        return true;
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return onToolbarMenuItemClick(item.getItemId(), item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                onBackKeyClick(1);
                return false;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 跳转到指定的Activity
     *
     * @param targetActivity 要跳转的目标Activity
     */
    public final void startActivity(@NonNull Class<?> targetActivity) {
        startActivity(new Intent(this, targetActivity));
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    /**
     * 跳转到指定的Activity
     *
     * @param flags          intent flags
     * @param targetActivity 要跳转的目标Activity
     */
    public final void startActivity(int flags, @NonNull Class<?> targetActivity) {
        final Intent intent = new Intent(this, targetActivity);
        intent.setFlags(flags);
        startActivity(new Intent(this, targetActivity));
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }
//
//    /**
//     * 跳转到指定的Activity
//     *
//     * @param data           Activity之间传递数据，Intent的Extra key为GlobalSettings.EXTRA_NAME.DATA
//     * @param targetActivity 要跳转的目标Activity
//     */
//    public final void startActivity(@NonNull Bundle data, @NonNull Class<?> targetActivity) {
//        final Intent intent = new Intent();
//        intent.putExtra(GlobalSettings.EXTRA_NAME.DATA, data);
//        intent.setClass(this, targetActivity);
//        startActivity(intent);
//        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
//    }

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
        final Intent intent = new Intent(getApplicationContext(), targetActivity);
        intent.putExtra(extraName, extraValue);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    /**
     * 跳转到指定的Activity
     *
     * @param extraName      要传递的值的键名称
     * @param extraValue     要传递的String类型值
     * @param targetActivity 要跳转的目标Activity
     */
    public final void startActivity(@NonNull String extraName, @NonNull String extraValue, @NonNull String extraName2, @NonNull int extraValue2, @NonNull Class<?> targetActivity) {
        if (TextUtils.isEmpty(extraName))
            throw new NullPointerException("传递的值的键名称为null或空");
        final Intent intent = new Intent(getApplicationContext(), targetActivity);
        intent.putExtra(extraName, extraValue);
        intent.putExtra(extraName2, extraValue2);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }


    /**
     * 跳转到指定的Activity
     *
     * @param extraName      要传递的值的键名称
     * @param extraValue     要传递的String类型值
     * @param targetActivity 要跳转的目标Activity
     */
    public final void startActivity(@NonNull String extraName, @NonNull int extraValue, @NonNull Class<?> targetActivity) {
        if (TextUtils.isEmpty(extraName))
            throw new NullPointerException("传递的值的键名称为null或空");
        final Intent intent = new Intent(getApplicationContext(), targetActivity);
        intent.putExtra(extraName, extraValue);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    /**
     * 获取通过Intent对象传递过来的参数
     *
     * @param name 参数名称
     * @return 从intent和bundle中都没有取到返回null，否则返回传递过来的参数值
     */
    public final String getStringExtra(@NonNull String name) {
        final Intent intent = getIntent();
        if (ObjectUtil.notNull(intent)) {
            // 如果intent不为空，则先从intent中取，如果没有再从bundle中取，如果都没有就返回null
            final String extra = intent.getStringExtra(name);
            if (ObjectUtil.isNull(extra)) {
                final Bundle data = intent.getExtras();
                if (ObjectUtil.notNull(data)) {
                    return data.getString(name);
                }
            } else
                return extra;
        }
        return null;
    }

    /**
     * 获取通过Intent对象传递过来的参数
     *
     * @param name         Extra参数名称
     * @param defaultValue 没有在intent或bundle对象中找到时设置的默认值
     * @return 从intent和bundle中都没有取到返回设置的默认值，否则返回传递过来的参数值
     */
    public final String getStringExtra(@NonNull String name, String defaultValue) {
        final String extra = getStringExtra(name);
        if (ObjectUtil.isNull(extra))
            return defaultValue;
        return extra;
    }

    /**
     * 获取通过Intent对象传递过来的参数
     *
     * @param name 参数名称
     * @return 从intent和bundle中都没有取到返回null，否则返回传递过来的参数值
     */
    public final boolean getBooleanExtra(@NonNull String name) {
        final Intent intent = getIntent();
        if (ObjectUtil.notNull(intent)) {
            // 如果intent不为空，则先从intent中取，如果没有再从bundle中取，如果都没有就返回false
            final boolean extra = intent.getBooleanExtra(name, false);
            if (!extra) {
                final Bundle data = intent.getExtras();
                if (ObjectUtil.notNull(data)) {
                    return data.getBoolean(name);
                }
            }
            return extra;
        }
        return false;
    }

    /**
     * 获取通过Intent对象传递过来的参数
     *
     * @param name         Extra参数名称
     * @param defaultValue 没有在intent或bundle对象中找到时设置的默认值
     * @return 从intent和bundle中都没有取到返回设置的默认值，否则返回传递过来的参数值
     */
    public final boolean getBooleanExtra(@NonNull String name, boolean defaultValue) {
        final boolean extra = getBooleanExtra(name);
        if (extra)
            return defaultValue;
        return extra;
    }

    /**
     * 获取通过Intent对象传递过来的参数
     *
     * @param name 参数名称
     * @return 从intent和bundle中都没有取到返回null，否则返回传递过来的参数值
     */
    public final int getIntExtra(@NonNull String name) {
        final Intent intent = getIntent();
        if (ObjectUtil.notNull(intent)) {
            // 如果intent不为空，则先从intent中取，如果没有再从bundle中取，如果都没有就返回-1
            final int extra = intent.getIntExtra(name, -1);
            if (extra == -1) {
                final Bundle data = intent.getExtras();
                if (ObjectUtil.notNull(data)) {
                    return data.getInt(name);
                }
            }
            return extra;
        }
        return -1;
    }

    /**
     * 获取通过Intent对象传递过来的参数
     *
     * @param name         Extra参数名称
     * @param defaultValue 没有在intent或bundle对象中找到时设置的默认值
     * @return 从intent和bundle中都没有取到返回设置的默认值，否则返回传递过来的参数值
     */
    public final int getIntExtra(@NonNull String name, int defaultValue) {
        final int extra = getIntExtra(name);
        if (extra == -1)
            return defaultValue;
        return extra;
    }

    public final short getShortExtra(@NonNull String name) {
        final Intent intent = getIntent();
        if (ObjectUtil.isNull(intent))
            return 0;
        return intent.getShortExtra(name, Short.valueOf("0"));
    }

    public final int getShortExtra(@NonNull String name, short defaultValue) {
        final Intent intent = getIntent();
        if (ObjectUtil.isNull(intent))
            return 0;
        return intent.getShortExtra(name, defaultValue);
    }

    public final long getLongExtra(@NonNull String name) {
        final Intent intent = getIntent();
        if (ObjectUtil.isNull(intent))
            return 0L;
        return intent.getLongExtra(name, 0L);
    }

    public final long getLongExtra(@NonNull String name, long defaultValue) {
        final Intent intent = getIntent();
        if (ObjectUtil.isNull(intent))
            return 0L;
        return intent.getLongExtra(name, defaultValue);
    }

    /**
     * 获取通过Intent对象传递过来的参数
     *
     * @param name 参数名称
     * @return 从intent和bundle中都没有取到返回0.0f，否则返回传递过来的参数值
     */
    public final float getFloatExtra(@NonNull String name) {
        final Intent intent = getIntent();
        if (ObjectUtil.notNull(intent)) {
            // 如果intent不为空，则先从intent中取，如果没有再从bundle中取，如果都没有就返回0.0f
            final float extra = intent.getFloatExtra(name, 0.0f);
            if (extra == 0.0f) {
                final Bundle data = intent.getExtras();
                if (ObjectUtil.notNull(data)) {
                    return data.getFloat(name);
                }
            }
            return extra;
        }
        return 0.0f;
    }

    /**
     * 获取通过Intent对象传递过来的参数
     *
     * @param name         Extra参数名称
     * @param defaultValue 没有在intent或bundle对象中找到时设置的默认值
     * @return 从intent和bundle中都没有取到返回设置的默认值，否则返回传递过来的参数值
     */
    public final float getFloatExtra(@NonNull String name, float defaultValue) {
        final float extra = getFloatExtra(name);
        if (extra == 0.0f)
            return defaultValue;
        return extra;
    }

    /**
     * 获取通过Intent对象传递过来的参数
     *
     * @param name 参数名称
     * @return 从intent和bundle中都没有取到返回0.0，否则返回传递过来的参数值
     */
    public final double getDoubleExtra(@NonNull String name) {
        final Intent intent = getIntent();
        if (ObjectUtil.notNull(intent)) {
            // 如果intent不为空，则先从intent中取，如果没有再从bundle中取，如果都没有就返回0.0
            final double extra = intent.getDoubleExtra(name, 0.0);
            if (extra == 0.0) {
                final Bundle data = intent.getExtras();
                if (ObjectUtil.notNull(data)) {
                    return data.getDouble(name);
                }
            }
            return extra;
        }
        return 0.0;
    }

    /**
     * 获取通过Intent对象传递过来的参数
     *
     * @param name         Extra参数名称
     * @param defaultValue 没有在intent或bundle对象中找到时设置的默认值
     * @return 从intent和bundle中都没有取到返回0.0，否则返回传递过来的参数值
     */
    public final double getDoubleExtra(@NonNull String name, double defaultValue) {
        final double extra = getDoubleExtra(name);
        if (extra == 0.0)
            return defaultValue;
        return extra;
    }

    @Override
    public int getFragmentContainerViewId() {
        return -1;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {

    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        if (ObjectUtil.notNull(toolbar)) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onToolbarNavigationClick();
                }
            });
            toolbar.setOnMenuItemClickListener(this);
        }
    }

    @Override
    public void initListener(@NonNull Bundle savedInstanceState) {

    }

    @Override
    public void initDialog(@NonNull Bundle savedInstanceState) {

    }

    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    public Fragment[] getFragments() {
        return new Fragment[0];
    }

    @Override
    public List<Fragment> getFragmentList() {
        return null;
    }

    @Override
    public String getFragmentTag() {
        return null;
    }

    @Override
    public int getToolbarResId() {
        return 0;
    }

    @Override
    public int getToolbarMenuLayoutResId() {
        return 0;
    }

    @Override
    public int getToolbarMenuResId() {
        return 0;
    }

    @Override
    public int getToolbarMenuId() {
        return 0;
    }

    @Override
    public void onToolbarNavigationClick() {

    }

    /**
     * 判断Activity中是否有Fragment
     *
     * @return 有返回true，否则返回false
     */
    protected final boolean hasFragment() {
        return addedFragments != null && !addedFragments.isEmpty();
    }

    /**
     * 添加Fragment到Activity中
     */
    private void addFragments() {
        // 添加Fragment到Activity中
        final Fragment fragment = getFragment();
        final Fragment[] fragments = getFragments();
        final List<Fragment> fragmentList = getFragmentList();
        if (ObjectUtil.notNull(fragment) || (ObjectUtil.notNull(fragments) && fragments.length > 0) || (ObjectUtil.notNull(fragmentList) && !fragmentList.isEmpty())) {
            if (ObjectUtil.isNull(addedFragments))
                addedFragments = new ArrayList<>();
        }
        if (fragment != null) {
            addedFragments.add(fragment);
//            LogUtil.d("添加了一个Fragment：" + fragment);
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentManager.beginTransaction();
            if (getFragmentContainerViewId() > 0) {
                fragmentTransaction.add(getFragmentContainerViewId(), fragment);
            } else if (getFragmentTag() != null)
                fragmentTransaction.add(fragment, getFragmentTag());
            else if (getFragmentContainerViewId() > 0 && getFragmentTag() != null)
                fragmentTransaction.add(getFragmentContainerViewId(), fragment, getFragmentTag());
            // 默认显示添加的Fragment
            fragmentTransaction.show(fragment);
            fragmentTransaction.commit();
        } else if (fragments != null && fragments.length > 0) {
//            LogUtil.d("添加了" + fragments.length + "个Fragment");
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentManager.beginTransaction();
            if (getFragmentContainerViewId() > 0) {
                for (Fragment item : fragments) {
                    if (item == null)
                        throw new NullPointerException("当前Fragment为null");
                    addedFragments.add(item);
                    fragmentTransaction.add(getFragmentContainerViewId(), item);
                    // 添加后默认隐藏Fragment
                    fragmentTransaction.hide(item);
                }
            } else if (getFragmentTag() != null) {
                for (Fragment item : fragments) {
                    if (item == null)
                        throw new NullPointerException("当前Fragment为null");
                    addedFragments.add(item);
                    fragmentTransaction.add(item, getFragmentTag());
                    // 添加后默认隐藏Fragment
                    fragmentTransaction.hide(item);
                }
            } else if (getFragmentContainerViewId() > 0 && getFragmentTag() != null) {
                for (Fragment item : fragments) {
                    if (item == null)
                        throw new NullPointerException("当前Fragment为null");
                    addedFragments.add(item);
                    fragmentTransaction.add(getFragmentContainerViewId(), item, getFragmentTag());
                    // 添加后默认隐藏Fragment
                    fragmentTransaction.hide(item);
                }
            }
            // 默认显示第一个添加的Fragment
            fragmentTransaction.show(fragments[0]);
            fragmentTransaction.commit();
        } else if (fragmentList != null && !fragmentList.isEmpty()) {
//            LogUtil.d("添加了" + fragmentList.size() + "个Fragment");
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentManager.beginTransaction();
            if (getFragmentContainerViewId() > 0) {
                for (Fragment item : fragmentList) {
                    if (item == null)
                        throw new NullPointerException("当前Fragment为null");
                    addedFragments.add(item);
                    fragmentTransaction.add(getFragmentContainerViewId(), item);
                    // 添加后默认隐藏Fragment
                    fragmentTransaction.hide(item);
                }
            } else if (getFragmentTag() != null) {
                for (Fragment item : fragmentList) {
                    if (item == null)
                        throw new NullPointerException("当前Fragment为null");
                    addedFragments.add(item);
                    fragmentTransaction.add(item, getFragmentTag());
                    // 添加后默认隐藏Fragment
                    fragmentTransaction.hide(item);
                }
            } else if (getFragmentContainerViewId() > 0 && getFragmentTag() != null) {
                for (Fragment item : fragmentList) {
                    if (item == null)
                        throw new NullPointerException("当前Fragment为null");
                    addedFragments.add(item);
                    fragmentTransaction.add(getFragmentContainerViewId(), item, getFragmentTag());
                    // 添加后默认隐藏Fragment
                    fragmentTransaction.hide(item);
                }
            }
            // 默认显示第一个添加的Fragment
            fragmentTransaction.show(fragmentList.get(0));
            fragmentTransaction.commit();
        }
    }

    public void initToolbarMenuActionView(@NonNull View actionView) {

    }

    /**
     * Toolbar Menu item点击事件回调
     *
     * @param menuId menu资源ID
     * @param item   MenuItem对象
     * @return
     */
    public boolean onToolbarMenuItemClick(@IdRes int menuId, @NonNull MenuItem item) {
        return false;
    }

    public void onBackKeyClick(int flag) {
        this.finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }


}
