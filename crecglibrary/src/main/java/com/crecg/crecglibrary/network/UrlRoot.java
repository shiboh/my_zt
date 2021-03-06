package com.crecg.crecglibrary.network;


/**
 *  服务器地址
 */
public class UrlRoot {

    private final static int ENV_DEV = 1; // 测试环境
    private final static int ENV_PRODUCT = 2; // 正式环境
    private final static int ENV_LVJ = 3; // 吕剑
    private final static int ENV_LHB = 4; // 李红彬

    // 主地址
    public static String ROOT_URL;

    // 短信类型

    public static final String REGISTER = "register"; // 用户注册
    public static final String RETPWD = "retpwd"; // 登录密码找回
    public static final String RESETPWD = "resetpwd"; // 重置交易密码

    private static final int mEnvironment = 3;  //1测试环境，2正式环境 ,3和4均为个人服务器地址

    static {
        switch (mEnvironment) {
            case ENV_DEV: // 测试环境 1
                ROOT_URL = "http://192.168.1.38:83/";
                break;
            case ENV_PRODUCT: // 正式环境 2
                ROOT_URL = "http://v.juhe.cn/";
                break;
            case ENV_LVJ: // 吕剑 3
                ROOT_URL = "http://192.168.1.246:83/";
                break;
            case ENV_LHB: // 李红彬 4
                ROOT_URL = "http://192.168.1.241:83/";
                break;
            default:
                break;
        }
    }

    // 以下均为H5 页面地址

    // 工资宝详情
    public static final String URL_SALARY_TREASURE_DETAIL = ROOT_URL + "myGongZiBao/";

    // 工资宝详情
    public static final String URL_REGULAR_FINANCING_DETAIL = ROOT_URL + "finaProductKanSe/";
}
