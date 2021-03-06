package com.crecg.staffshield.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crecg.crecglibrary.network.model.CheckVersionModelData;
import com.crecg.staffshield.R;
import com.crecg.staffshield.common.BaseActivity;
import com.crecg.staffshield.fragment.HomePageFragment;
import com.crecg.staffshield.fragment.FoundFragment;
import com.crecg.staffshield.fragment.MeFragment;
import com.crecg.staffshield.update.UpdateManager;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import static android.content.ContentValues.TAG;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private SwipeRefreshLayout swipe_refresh;
    private ViewPager vp;

    private LinearLayout ll_tab_home;
    private LinearLayout ll_tab_found;
    private LinearLayout ll_tab_me;

    private ImageView iv_tab_home_page;
    private ImageView iv_tab_found;
    private ImageView iv_tab_me;

    private TextView tv_tab_home_page; // 首页
    private TextView tv_tab_found; // 发现
    private TextView tv_tab_me; // 我的

    private ArrayList<Fragment> tabFragments;
    private HomePageFragment homePageFragment;
    private FoundFragment foundFragment;
    private MeFragment meFragment;
    private FragmentPagerAdapter fragmentAdapter;
    private int selectPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initJpush();
        initView();
        initVP();
        setSelect(0);
        CheckVersionUpdates();
    }

    /**
     * 检查版本更新
     */
    private void CheckVersionUpdates() {
        CheckVersionModelData version = new CheckVersionModelData();
        version.display = "修复了bug";
        version.isForce = 0;
        version.url = "";
        new UpdateManager().checkVersion(this, version);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
//            selectPage = getIntent().getIntExtra("selectIndex", 0);
//            setSelect(selectPage);
            String homeFlag = getIntent().getStringExtra("homeFlag");
            if (!TextUtils.isEmpty(homeFlag)) {
                if ("1".equals(homeFlag)) {
                    setSelect(0);
                }
            }
        }
    }

    private void initView() {
//        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        vp = findViewById(R.id.vp);

        ll_tab_home = findViewById(R.id.ll_tab_home);
        ll_tab_found = findViewById(R.id.ll_tab_found);
        ll_tab_me = findViewById(R.id.ll_tab_me);

        iv_tab_home_page = findViewById(R.id.iv_tab_home_page);
        iv_tab_found = findViewById(R.id.iv_tab_found);
        iv_tab_me = findViewById(R.id.iv_tab_me);

        tv_tab_home_page = findViewById(R.id.tv_tab_home_page);
        tv_tab_found = findViewById(R.id.tv_tab_found);
        tv_tab_me = findViewById(R.id.tv_tab_me);

        ll_tab_home.setOnClickListener(this);
        ll_tab_found.setOnClickListener(this);
        ll_tab_me.setOnClickListener(this);
    }

    private void initVP() {
        tabFragments = new ArrayList<>();
        homePageFragment = new HomePageFragment();
        foundFragment = new FoundFragment();
        meFragment = new MeFragment();

        tabFragments.add(homePageFragment);
        tabFragments.add(foundFragment);
        tabFragments.add(meFragment);

        fragmentAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
//                Log.i("aa","tabFragments个数是："+tabFragments.size());
                return tabFragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return tabFragments.get(position);
            }
        };

        vp.setAdapter(fragmentAdapter);

        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                int currentItem = vp.getCurrentItem();
                setTab(currentItem);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_tab_home: // 首页
                setSelect(0);
                break;
            case R.id.ll_tab_found: // 发现
                setSelect(1);
                break;
            case R.id.ll_tab_me: // 我的
                setSelect(2);
                break;
        }
    }

    public void setSelect(int i) {
//        if (PreferenceUtil.isLogin()) {
//            Log.i("hh", this + "判断当前用户是否登录：" + PreferenceUtil.isLogin());
//            if (i == 3) {
//                tab_mine.requestData();
//            }
//            if (i==0){
//                tab_home.requestAppData();
//            }
//        }
        setTab(i);
        vp.setCurrentItem(i);
    }

    private void setTab(int pos) {
        setTabTextUnselectedColor();
        resetImages();

        switch (pos) {
            case 0:
                tv_tab_home_page.setTextColor(getResources().getColor(R.color.main_blue_4A67F5));
                iv_tab_home_page.setImageResource(R.mipmap.tab_home_selected);
                break;
            case 1:
                tv_tab_found.setTextColor(getResources().getColor(R.color.main_blue_4A67F5));
                iv_tab_found.setImageResource(R.mipmap.tab_found_selected);
                break;
            case 2:
                tv_tab_me.setTextColor(getResources().getColor(R.color.main_blue_4A67F5));
                iv_tab_me.setImageResource(R.mipmap.tab_mine_selected);
                break;

        }
    }

    // 底部tab未选中时文字颜色
    private void setTabTextUnselectedColor() {
        tv_tab_home_page.setTextColor(Color.parseColor("#999999"));
        tv_tab_found.setTextColor(Color.parseColor("#999999"));
        tv_tab_me.setTextColor(Color.parseColor("#999999"));
    }

    // 给底部tab设置 未选中时的背景图片
    private void resetImages() {
        iv_tab_home_page.setImageResource(R.mipmap.tab_home_normal);
        iv_tab_found.setImageResource(R.mipmap.tab_found_normal);
        iv_tab_me.setImageResource(R.mipmap.tab_mine_normal);
    }


    private void initJpush() {
        JPushInterface.init(getApplicationContext());
        JPushInterface.resumePush(getApplicationContext());
        Set<String> tagSet = new LinkedHashSet<String>();
        tagSet.add("djkfir");
        JPushInterface.setAliasAndTags(getApplicationContext(), null, tagSet, mTagsCallback);
    }

    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i("aaa", logs);
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e("aaa", logs);
            }

        }

    };

    private long lastTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();
        }
        return false;
    }

    // 双击退出函数
    private void exitBy2Click() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime < 2000) {
            finish();
        } else {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            lastTime = currentTime;
        }
    }
}
