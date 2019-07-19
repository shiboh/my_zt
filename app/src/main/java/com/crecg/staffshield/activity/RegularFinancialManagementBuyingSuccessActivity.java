package com.crecg.staffshield.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crecg.staffshield.R;
import com.crecg.staffshield.common.BaseActivity;

/**
 * 定期理财买入状态详情页
 */

public class RegularFinancialManagementBuyingSuccessActivity extends BaseActivity {

    private ImageView iv_back;
    private TextView tv_common_title;
    private TextView tv_buying_amount; // 买入金额
    private TextView tv_buying_date; // 买入时间
    private TextView tv_start_calculating_earnings_date; // 开始计算收益时间（周三）
    private TextView tv_start_calculating_earnings_week; // 开始计算收益时间（周三）
    private TextView tv_maturity_time; // 理财到期时间
    private TextView tv_maturity_time_week; // 理财到期时间
    private TextView tv_principal_and_interest_date; // 本息到账时间
    private TextView tv_principal_and_interest_week; // 本息到账时间
    private Button btn_complete; //完成
    private String whereToEnterFlag; //  1:首页进   2：工资宝详情页进

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_wage_treasure_turn_success);

        initView();
    }

    private void initView() {
        whereToEnterFlag = getIntent().getStringExtra("whereToEnterFlag");
        iv_back = findViewById(R.id.iv_back);
        tv_common_title = findViewById(R.id.tv_common_title);
        iv_back.setVisibility(View.GONE);
        tv_common_title.setText("定基理财买入详情");

        tv_buying_amount = findViewById(R.id.tv_buying_amount);
        tv_buying_date = findViewById(R.id.tv_buying_date);
        tv_start_calculating_earnings_date = findViewById(R.id.tv_start_calculating_earnings_date);
        tv_start_calculating_earnings_week = findViewById(R.id.tv_start_calculating_earnings_week);
        tv_maturity_time = findViewById(R.id.tv_maturity_time);
        tv_maturity_time_week = findViewById(R.id.tv_maturity_time_week);
        tv_principal_and_interest_date = findViewById(R.id.tv_principal_and_interest_date);
        tv_principal_and_interest_week = findViewById(R.id.tv_principal_and_interest_week);
        btn_complete = findViewById(R.id.btn_complete);

        btn_complete.setOnClickListener(new View.OnClickListener() {
            Intent intent;

            @Override
            public void onClick(View v) {
//                if ("1".equals(whereToEnterFlag)) {
//                    intent = new Intent(RegularFinancialManagementBuyingSuccessActivity.this, MainActivity.class);
//                    intent.putExtra("homeFlag", "1");
//                    intent.putExtra("homeFlag", "1");
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                } else if ("2".equals(whereToEnterFlag)) {
//                    intent = new Intent(RegularFinancialManagementBuyingSuccessActivity.this, SalaryTreasureDetailActivity.class);
//                    intent.putExtra("homeFlag", "1");
//                }
                startActivity(intent);
//                overridePendingTransition(R.anim.activity_in_from_right,
//                        R.anim.activity_out_to_left);
//
//                AppManager.getAppManager().finishAllActivity();

            }
        });

    }
}