package com.crecg.crecglibrary.network.model;


/**
 * 模拟定期产品数据
 */
public class ProductModelTestData {

    //模拟首页定期产品数据
    public String name; // 产品名字
    public String day; // 产品募集天数
    public String date; // 产品发标时间
    public String annualizedReturn; // 产品年化收益率
    public String investmentAmount; // 产品起投金额
    public int progressBar; // 产品售卖进度条
    public String surplusMoney; // 剩余可投的钱
    public String percentName;
    public int flag; // 定期产品状态  1：热卖中  2：即将开售   3：已售罄   4：计息中   5：已回款

    //模拟我的理财产品数据
    public String holdingShare; // 持有份额（元）
    public String expectedEarnings; // 预计收益（元）
    public String investmentCycle; // 产品投资周期
    public String state; // 产品当前状态 one:募集中  two:计息中  three:回款中   four:已回款


    // 模拟首页轮播图数据

}
