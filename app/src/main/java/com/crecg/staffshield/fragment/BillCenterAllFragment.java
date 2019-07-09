package com.crecg.staffshield.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewSwitcher;

import com.crecg.crecglibrary.network.model.BillCenterListData;
import com.crecg.crecglibrary.network.model.BillCenterModelData;
import com.crecg.staffshield.R;
import com.crecg.staffshield.adapter.BillCenterAllRecycleAdapter;
import com.crecg.staffshield.adapter.BillCenterAllRecycleAdapter2;

import java.util.ArrayList;

/**
 * 账单中心 --- 全部
 */

public class BillCenterAllFragment extends Fragment {
    private static final String KEY = "param1";
    private String mParam1;
    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recycler_view;
    private int currentPage = 1;    //当前页
    private Context context;
    private int currentPosition; // 当前tab位置（0：提问，1：话题）
    private String userId;
    private ViewSwitcher vs;
    private ArrayList<BillCenterListData> list;
    private BillCenterAllRecycleAdapter billCenterAllRecycleAdapter;

    public static BillCenterAllFragment newInstance(String param1) {
        BillCenterAllFragment fragment = new BillCenterAllFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY, param1);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            initData();
        } else {
//            Log.i("hh", this + " -- setUserVisibleHint --" + isVisibleToUser);
//            if (myFinaciaHoldAdapter != null) {
//                list.clear();
//                currentPage = 1;
//                myFinaciaHoldAdapter.changeMoreStatus(myFinaciaHoldAdapter.NO_LOAD_MORE);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initData();
        View view = inflater.inflate(R.layout.fragment_recycle_layout, container, false); // Todo need modify
        initView(view);
        initListener();

        return view;
    }

    private void initData() {
        // 模拟本月数据
        BillCenterModelData data11 = new BillCenterModelData();
        data11.cardType = "bank";
        data11.title = "勘设联名卡 - 银行卡（ 2331）";
        data11.income = "3000.00 ";
        data11.time = "今天  18:05";

        BillCenterModelData data12 = new BillCenterModelData();
        data12.cardType = "salaryTreasure";
        data12.title = "工资宝收益";
        data12.income = "+64.3";
        data12.time = "昨天 11:30";

        BillCenterModelData data13 = new BillCenterModelData();
        data13.cardType = "regularFinancial";
        data13.title = "理财- 勘设联名卡";
        data13.income = "600.00 ";
        data13.time = "07-07  18:05";

        ArrayList<BillCenterModelData> thisMonth = new ArrayList<BillCenterModelData>();
        thisMonth.add(data11);
        thisMonth.add(data12);
        thisMonth.add(data13);

        BillCenterListData billCenterData1 = new BillCenterListData();
        billCenterData1.type = 1;
        billCenterData1.time = "本月";
        billCenterData1.jsonData = thisMonth;


        //模拟06数据
        BillCenterModelData data21 = new BillCenterModelData();
        data21.cardType = "bank";
        data21.title = "勘设联名卡 - 银行卡（ 2331）";
        data21.income = "400.00 ";
        data21.time = "06-06  18:05";

        BillCenterModelData data22 = new BillCenterModelData();
        data22.cardType = "salaryTreasure";
        data22.title = "工资宝收益";
        data22.income = "+14.3";
        data22.time = "06-07 11:30";

        ArrayList<BillCenterModelData> preMonth = new ArrayList<BillCenterModelData>();
        preMonth.add(data21);
        preMonth.add(data22);

        BillCenterListData billCenterData2 = new BillCenterListData();
        billCenterData2.type = 1;
        billCenterData2.time = "07月";
        billCenterData2.jsonData = preMonth;


        // 模拟05数据
        BillCenterModelData data31 = new BillCenterModelData();
        data31.cardType = "bank";
        data31.title = "勘设联名卡 - 银行卡（ 2331）";
        data31.income = "333.00 ";
        data31.time = "05-06  18:05";

        BillCenterModelData data32 = new BillCenterModelData();
        data32.cardType = "salaryTreasure";
        data32.title = "工资宝收益";
        data32.income = "+24.3";
        data32.time = "05-07 11:30";

        BillCenterModelData data33 = new BillCenterModelData();
        data33.cardType = "regularFinancial";
        data33.title = "理财- 勘设联名卡";
        data33.income = "56.00 ";
        data33.time = "05-08  18:05";

        BillCenterModelData data34 = new BillCenterModelData();
        data34.cardType = "salaryTreasure";
        data34.title = "工资宝收益";
        data34.income = "+34.3";
        data34.time = "05-09 11:30";

        BillCenterModelData data35 = new BillCenterModelData();
        data35.cardType = "regularFinancial";
        data35.title = "理财- 勘设联名卡";
        data35.income = "2345.00 ";
        data35.time = "05-10  18:05";

        ArrayList<BillCenterModelData> prepreMonth = new ArrayList<BillCenterModelData>();
        prepreMonth.add(data31);
        prepreMonth.add(data32);
        prepreMonth.add(data33);
        prepreMonth.add(data34);
        prepreMonth.add(data35);

        BillCenterListData billCenterData3 = new BillCenterListData();
        billCenterData3.type = 1;
        billCenterData3.time = "06月";
        billCenterData3.jsonData = prepreMonth;

        list = new ArrayList<>();
        list.add(billCenterData1);
        list.add(billCenterData2);
        list.add(billCenterData3);
    }

    private void initView(View view) {
        context = getActivity();

//        vs = (ViewSwitcher) view.findViewById(R.id.vs);
//        swipe_refresh = view.findViewById(R.id.swipe_refresh);
        recycler_view = view.findViewById(R.id.recycler_view);

//        TextView tv_empty = view.findViewById(R.id.tv_empty);
//        tv_empty.setText("测试...");

        initRecyclerView();
    }

    private void initRecyclerView() {
        recycler_view.setLayoutManager(new LinearLayoutManager(context));
        billCenterAllRecycleAdapter = new BillCenterAllRecycleAdapter(context, list);
        recycler_view.setAdapter(billCenterAllRecycleAdapter);
//        //添加动画
//        recycler_view.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 获取“全部”列表数据
     */
    public void requestBillCenterAllData() {
    }

    private void initListener() {
//        initPullRefresh();
//        initLoadMoreListener();
    }

    private void initPullRefresh() {
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {  // 下拉刷新
                currentPage = 1;
//                requestBillCenterAllData();
            }
        });
    }

    private void initLoadMoreListener() {
        recycler_view.setOnScrollListener(new RecyclerView.OnScrollListener() {
            private int firstVisibleItem;
            private int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //判断RecyclerView的状态 是空闲时，同时，是最后一个可见的ITEM时才加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == billCenterAllRecycleAdapter.getItemCount() && firstVisibleItem != 0) {
                    if (list.size() == 0) {
                        return;
                    }
                    currentPage++;
                    requestBillCenterAllData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    public void getCurrentTab(int position) {
        this.currentPosition = position;
    }

//    public String setUserId(String userId) {
//        this.userId = userId;
//        return userId;
//    }
}
