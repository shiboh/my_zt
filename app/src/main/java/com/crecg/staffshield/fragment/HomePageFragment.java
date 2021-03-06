package com.crecg.staffshield.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.crecg.crecglibrary.RemoteFactory;
import com.crecg.crecglibrary.network.CommonObserverAdapter;
import com.crecg.crecglibrary.network.CommonRequestProxy;
import com.crecg.crecglibrary.network.UrlRoot;
import com.crecg.crecglibrary.network.model.CommonResultModel;
import com.crecg.crecglibrary.network.model.HomeAndFinancialDataModel;
import com.crecg.crecglibrary.network.model.HomeAndFinancialProductItemDataModel;
import com.crecg.crecglibrary.network.model.HomePicListItemData;
import com.crecg.crecglibrary.network.model.HomePicListModelData;
import com.crecg.crecglibrary.utils.ToastUtil;
import com.crecg.crecglibrary.utils.encrypt.DESUtil;
import com.crecg.staffshield.R;
import com.crecg.staffshield.activity.MyFinancialManagementListActivity;
import com.crecg.staffshield.activity.RegularFinancialManagementListActivity;
import com.crecg.staffshield.activity.TestActivity1;
import com.crecg.staffshield.activity.WageTreasureBuyingActivity;
import com.crecg.staffshield.activity.WebActivity;
import com.crecg.staffshield.widget.MyRollViewPager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 首页
 */
public class HomePageFragment extends Fragment implements View.OnClickListener {

    private View mView;
    private Context context;
    private SwipeRefreshLayout swipe_refresh;
    private ScrollView scrollView;
    private LinearLayout ll_vp; // 顶部轮播图
    private LinearLayout ll_point_container; // 轮播图小点
    private LinearLayout ll_home_salary_treasure; // 工资宝
    private LinearLayout ll_home_manage_money; //  理财
    private LinearLayout ll_home_enterprise_dynamics; // 企业动态
    private LinearLayout ll_home_insurance; // 保险
    private LinearLayout ll_go_to_salary_treasure; // 活期 工资宝布局
    private TextView home_btn_transfer_immediately; // 立即转入
    private TextView tv_fund_name; // 基金名称
    private TextView tv_annualized_return; // （基金）近七日年化收益率
    private TextView tv_home_more; // 更多

    private LinearLayout ll_container; // 加载首页定期产品
    //    private List<ProductModelTestData> list;
//    private List<String> picList; // 滑动的图片集合
//    private int[] imageResId; // 图片ID
    private MyRollViewPager rollViewPager;

    private HomeAndFinancialDataModel homeData;
    private List<HomeAndFinancialProductItemDataModel> productList; // 定期产品数据
    private List<HomePicListItemData> picList;


    @Override

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.i("hh", "HomePageFragment --- " + getClass());
            // 获取轮播图数据
            requestHomePicListData();
            // 获取首页数据
            requestHomeData();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        initData();
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_home_page, container, false);
            try {
                initView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (mView.getParent() != null) {
                ((ViewGroup) mView.getParent()).removeView(mView);
            }
        }
        return mView;
    }

    private void initData() {
        // 模拟定期产品数据
//        list = DataUtil.getProductList();

        // 模拟轮播图数据
//        auto_Image();

//        freshVP();
    }

    private void auto_Image() {
//        picList = new ArrayList<String>();
//        picList.add("https://csdnimg.cn/feed/20190712/9bb6aa25f82b061230d08a65a1b534c5.png");
//        picList.add("https://ss.csdn.net/p?https://mmbiz.qpic.cn/mmbiz_jpg/Pn4Sm0RsAujxlx50uwaMWjpghsyVBYjZsgibRQlSTg5V28wWfNOz6QDwrIWHGolXJqFBoA3rWYlv6tnLwbDFQ8g/640?wx_fmt=jpeg");
//        picList.add("https://ss.csdn.net/p?https://mmbiz.qpic.cn/mmbiz_jpg/Rm6KfjqOnWwDkSZUHmIjUAZpc1QXd43BgpEI45JKS7qJA2kghDYBr0CgwhJHTtat7RVawRcSiaFp8PgHm9q0YZA/640?wx_fmt=jpeg");
//
//        imageResId = new int[]{R.mipmap.bg_banner_default1, R.mipmap.bg_banner_default2, R.mipmap.bg_banner_default3};
    }

    private void initView() {
        context = getContext();
        ll_container = mView.findViewById(R.id.ll_container);
//        for (ProductModelTestData product : list) {
//            ll_container.addView(setItemData(product));
//        }

        swipe_refresh = mView.findViewById(R.id.swipe_refresh);
        swipe_refresh.setColorSchemeResources(R.color.colorPrimary);

        scrollView = mView.findViewById(R.id.scrollView);
        ll_vp = mView.findViewById(R.id.ll_vp);
        ll_point_container = mView.findViewById(R.id.ll_point_container);
        ll_home_salary_treasure = mView.findViewById(R.id.ll_home_salary_treasure);
        ll_home_manage_money = mView.findViewById(R.id.ll_home_manage_money);
        ll_home_enterprise_dynamics = mView.findViewById(R.id.ll_home_enterprise_dynamics);
        ll_home_insurance = mView.findViewById(R.id.ll_home_insurance);
        tv_fund_name = mView.findViewById(R.id.tv_fund_name);
        tv_annualized_return = mView.findViewById(R.id.tv_annualized_return);
        ll_go_to_salary_treasure = mView.findViewById(R.id.ll_go_to_salary_treasure);
        home_btn_transfer_immediately = mView.findViewById(R.id.tv_transfer_immediately);
        tv_home_more = mView.findViewById(R.id.tv_home_more);


        ll_home_salary_treasure.setOnClickListener(this);
        ll_home_manage_money.setOnClickListener(this);
        ll_home_enterprise_dynamics.setOnClickListener(this);
        ll_home_insurance.setOnClickListener(this);
        ll_go_to_salary_treasure.setOnClickListener(this);
        home_btn_transfer_immediately.setOnClickListener(this);
        tv_home_more.setOnClickListener(this);
    }

    /**
     * 设置定期产品数据
     */
    private View setItemData(final HomeAndFinancialProductItemDataModel product) {
        final String status = product.status;
        View ll_item;
        // 加载 item 布局
        ll_item = LayoutInflater.from(context).inflate(R.layout.item_regular_products2, null);

        if (status.equals("init") || status.equals("tender")) { // 即将开售、募集中
            LinearLayout ll_container1 = ll_item.findViewById(R.id.ll_container1);
            ll_container1.setVisibility(View.VISIBLE);

            TextView tv_regular_product_name1 = ll_item.findViewById(R.id.tv_regular_product_name); // 产品名称
            TextView tv_product_annualized_return1 = ll_item.findViewById(R.id.tv_product_annualized_return); // 年化收益率
            TextView tv_product_cycle1 = ll_item.findViewById(R.id.tv_product_cycle); // 产品周期（例：31天）
            TextView tv_initial_investment_amount1 = ll_item.findViewById(R.id.tv_initial_investment_amount); // 起投金额
            TextView tv_start_sale_time1 = ll_item.findViewById(R.id.tv_start_sale_time);  // 产品开售时间
            ProgressBar progressbar = ll_item.findViewById(R.id.progressbar); // 产品进度条
            TextView tv_surplus_money1 = ll_item.findViewById(R.id.tv_surplus_money); // 产品剩余可投金额
            ImageView iv_will_sell_state1 = ll_item.findViewById(R.id.iv_will_sell_state); // 产品即将开售状态
            FrameLayout fl_start_sell1 = ll_item.findViewById(R.id.fl_start_sell);
            LinearLayout ll_best_sell1 = ll_item.findViewById(R.id.ll_best_sell);

            tv_regular_product_name1.setText(product.name);
            tv_product_annualized_return1.setText(product.annualRate + "%");
            tv_product_cycle1.setText(product.timeLimit.toString() + "天");
            tv_initial_investment_amount1.setText(product.tenderInitAmount.toString() + "元起投");
            tv_start_sale_time1.setText(product.tenderStartTime);

            if (status.equals("tender")) {
                //募集中
                ll_best_sell1.setVisibility(View.VISIBLE);
                float currentProgress = Float.parseFloat(product.bfbAmount)*100;
                progressbar.setProgress((int)currentProgress);
                tv_surplus_money1.setText(product.syAmount);
            } else if (status.equals("init")) {
                //即将开售
                iv_will_sell_state1.setVisibility(View.VISIBLE);
                iv_will_sell_state1.setBackgroundResource(R.mipmap.img_regular_product_on_sale);
                fl_start_sell1.setVisibility(View.VISIBLE);
            }
        } else { // 募集失败、已满标、计息中、已回款
            RelativeLayout rl_container2 = ll_item.findViewById(R.id.rl_container2);
            rl_container2.setVisibility(View.VISIBLE);

            TextView tv_regular_product_name2 = ll_item.findViewById(R.id.tv_regular_product_name2); // 产品名称
            TextView tv_product_annualized_return2 = ll_item.findViewById(R.id.tv_product_annualized_return2); // 年化收益率
            TextView tv_product_cycle2 = ll_item.findViewById(R.id.tv_product_cycle2); // 产品周期（例：31天）
            TextView tv_initial_investment_amount2 = ll_item.findViewById(R.id.tv_initial_investment_amount2); // 起投金额
            ImageView iv_product_state2 = ll_item.findViewById(R.id.iv_product_state2); // 产品状态图片

            tv_regular_product_name2.setText(product.name);
            tv_product_annualized_return2.setText(product.annualRate);
            tv_product_cycle2.setText(product.timeLimit + "天");
            tv_initial_investment_amount2.setText(product.tenderInitAmount.toString());

            if (status.equals("success")) { // success：融资成功--已满标
                //已满标
                iv_product_state2.setBackground(getResources().getDrawable(R.mipmap.img_regular_product_sell_out));
            } else if (status.equals("fail")) { // fail：融资失败---募集失败
                iv_product_state2.setBackground(getResources().getDrawable(R.mipmap.img_regular_product_sell_fail));
            } else {
                if (status.equals("repaying")) {
                    //计息中
                    iv_product_state2.setBackground(getResources().getDrawable(R.mipmap.img_regular_product_interest_bearing));
                } else if (status.equals("repayed") || status.equals("prepayed")) {
                    //已回款
                    iv_product_state2.setBackground(getResources().getDrawable(R.mipmap.img_regular_product_payment_returned));
                }
            }
        }

        // item 点击跳转定期理财详情页
        ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i("hh", "当前点击的view = " + v);
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra("type",WebActivity.WEB_TYPE_REGULAR_FINANCING_DETAIL);
                intent.putExtra("url",UrlRoot.URL_REGULAR_FINANCING_DETAIL + product.id);
                intent.putExtra("productId",product.id);
                intent.putExtra("title",product.name);
                startActivity(intent);
            }
        });
        return ll_item;
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.i("hh", "首页 onResume方法：" + getClass());
//        initData();
    }

    /**
     * 获取轮播图数据
     */
    private void requestHomePicListData() {
        HashMap<String, Object> param = new HashMap<>();
        String data = DESUtil.encMap(param);

        HashMap<String, Object> paramWrapper = new HashMap<>();
        paramWrapper.put("requestKey", data);
        RemoteFactory.getInstance().getProxy(CommonRequestProxy.class)
                .getHomePicListByPost(paramWrapper)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserverAdapter<String>() {
            @Override
            public void onMyError() {
                ToastUtil.showCustom("轮播图获取数据失败");
            }

            @Override
            public void onMySuccess(String result) {
                if (result == null) {
                    return;
                }
                CommonResultModel<HomePicListModelData> picListModel = new Gson().fromJson(result, new TypeToken<CommonResultModel<HomePicListModelData>>() {
                }.getType());
                HomePicListModelData picListData = picListModel.data;
                picList = picListData.advertiseList;
                if (picList == null) {
                    return;
                }
                freshVP();
            }
        });
    }

    /**
     * 获取首页数据
     */
    private void requestHomeData() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("pageNum", ""); // 页码不传默认为1
        param.put("pageSize", ""); // 页码不传默认为3条
        param.put("listType", "home"); // 首页传
        String data = DESUtil.encMap(param);

        HashMap<String, Object> paramWrapper = new HashMap<>();
        paramWrapper.put("requestKey", data);
        RemoteFactory.getInstance().getProxy(CommonRequestProxy.class)
                .requestHomeAndFinancialData(paramWrapper)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserverAdapter<String>() {
            @Override
            public void onMyError() {
                ToastUtil.showCustom("首页获取数据失败");
            }

            @Override
            public void onMySuccess(String result) {
                if (result == null) {
                    return;
                }
                CommonResultModel<HomeAndFinancialDataModel> homeDataModel = new Gson().fromJson(result, new TypeToken<CommonResultModel<HomeAndFinancialDataModel>>() {
                }.getType());
                homeData = homeDataModel.data;
                if (homeData == null) {
                    return;
                }

                setFundData();
                productList = homeData.productList;

                if (productList == null) {
                    return;
                }
                for (HomeAndFinancialProductItemDataModel productItem : productList) {
                    ll_container.addView(setItemData(productItem));
                }
            }
        });
    }

    // （工资宝）设置基金产品数据
    private void setFundData() {
        tv_fund_name.setText(homeData.prodName);
        tv_annualized_return.setText(homeData.annualReturnBys + "%");
    }

    /**
     * 请求轮播图数据
     */
    private void freshVP() {
        if (context == null) {
            return;
        }
        if (rollViewPager == null) {
            //第一次从后台获取到数据
            rollViewPager = new MyRollViewPager(context, picList, ll_point_container);
            rollViewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_MOVE:
//                            swipe_refresh.setEnabled(false);
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
//                            swipe_refresh.setEnabled(true);
                            break;
                    }
                    return false;
                }
            });
            rollViewPager.setCycle(true);
            rollViewPager.setOnMyListener(new MyRollViewPager.MyClickListener() {
                @Override
                public void onMyClick(int position) {
                    if (picList != null && picList.size() != 0) {
                        ToastUtil.showCustom("当前的position = " + position);
                    }

                }
            });
            rollViewPager.startRoll();
            ll_vp.addView(rollViewPager);
        } else {
            //第二或之后获取到数据，刷新vp
            rollViewPager.setPicList(picList);
            rollViewPager.reStartRoll();
        }
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_home_salary_treasure:  // 工资宝
                ToastUtil.showCustom("工资宝被点击了");
//                intent = new Intent(context, SalaryTreasureDetailWebActivity.class);
//                startActivity(intent);
                break;
            case R.id.tv_transfer_immediately:  // (工资宝)立即转入 （已经开户跳转工资宝买入页）
                // Todo 点立即转入时，需要先判断用户是否开通联名卡账户
                intent = new Intent(context, WageTreasureBuyingActivity.class);
                intent.putExtra("whereToEnterFlag", "1");
                startActivity(intent);
                break;
            case R.id.ll_home_manage_money:  // 理财
                intent = new Intent(context, MyFinancialManagementListActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_home_enterprise_dynamics:  // 企业动态
//                intent = new Intent(context, TestActivity1.class);
//                startActivity(intent);
                break;
            case R.id.ll_home_insurance:  // 保险
                intent = new Intent(context, TestActivity1.class);
                startActivity(intent);
                break;
            case R.id.ll_go_to_salary_treasure:  // 活期 工资宝布局 (跳转工资宝详情页H5)
                intent = new Intent(context, WebActivity.class);
                intent.putExtra("type", WebActivity.WEB_TYPE_SALARY_TREASURE_DETAIL);
                intent.putExtra("url", UrlRoot.URL_SALARY_TREASURE_DETAIL + "8");
                intent.putExtra("title", "我的资宝");
                startActivity(intent);

                break;
            case R.id.tv_home_more: // 更多 （跳转到定期理财列表页）
                intent = new Intent(context, RegularFinancialManagementListActivity.class);
                startActivity(intent);
        }
    }
}
