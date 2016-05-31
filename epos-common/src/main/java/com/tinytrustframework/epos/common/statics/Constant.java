package com.tinytrustframework.epos.common.statics;

/**
 * 常量类
 *
 * @author Owen
 * @version [版本号, 2010-11-6]
 */
public class Constant {

    ////////////////////////////////////////////////////
    ////////////////////字符集////////////////////////////
    ////////////////////////////////////////////////////
    /**
     * UTF8
     */
    public static final String CHARSET_UTF8 = "UTF-8";

    /**
     * GBK
     */
    public static final String CHARSET_GBK = "GBK";

    ////////////////////////////////////////////////////
    ////////////////////日期格式///////////////////////////
    ////////////////////////////////////////////////////
    /**
     * 4位长度日期时间格式 'yyyy'
     */
    public static final String DATE_FORMAT_4 = "yyyy";

    /**
     * 6位长度日期时间格式 'yyyyMM'
     */
    public static final String DATE_FORMAT_6 = "yyyyMM";

    /**
     * 8位长度日期时间格式 'yyyyMMdd'
     */
    public static final String DATE_FORMAT_8 = "yyyyMMdd";

    /**
     * 10位长度日期时间格式 'yyyy-MM-dd'
     */
    public static final String DATE_FORMAT_10 = "yyyy-MM-dd";

    /**
     * 17位长度日期时间格式 'yyyyMMddHHmmssSSS'
     */
    public static final String DATE_FORMAT_17 = "yyyyMMddHHmmssSSS";

    /**
     * 19位长度日期时间格式 'yyyy-MM-dd HH:mm:ss'
     */
    public static final String DATE_FORMAT_19 = "yyyy-MM-dd HH:mm:ss";

    ////////////////////////////////////////////////////
    ////////////////////会话用户存储KEY/////////////////////
    ////////////////////////////////////////////////////
    /**
     * 当前session存储用户信息KEY
     */
    public static final String CURRENT_USER_IN_SESSION = "current_user_in_session";

    ////////////////////////////////////////////////////
    ////////////////////角色类型///////////////////////////
    ////////////////////////////////////////////////////
    /**
     * 角色类型-管理员
     */
    public static final int ROLE_TYPE_ADMIN = 1;

    /**
     * 角色类型-高级经销商
     */
    public static final int ROLE_TYPE_SENIOR_DISTRIBUTOR = 2;

    /**
     * 角色类型-初级经销商
     */
    public static final int ROLE_TYPE_JUNIOR_DISTRIBUTOR = 3;

    ////////////////////////////////////////////////////
    ////////////////////用户状态///////////////////////////
    ////////////////////////////////////////////////////
    /**
     * 用户状态-等待审核
     */
    public static final int USER_STATE_WAITING_VALIDATE = 1;

    /**
     * 用户状态-正常
     */
    public static final int USER_STATE_OK = 2;

    /**
     * 用户状态-锁定
     */
    public static final int USER_STATE_STOP = 3;

    ////////////////////////////////////////////////////
    ////////////////////菜单级别///////////////////////////
    ////////////////////////////////////////////////////
    /**
     * 菜单级别 - 一级菜单
     */
    public static final int MENU_LEVEL_FIRST = 1;

    /**
     * 菜单级别 - 二级菜单
     */
    public static final int MENU_LEVEL_SECOND = 2;

    ////////////////////////////////////////////////////
    ////////////////////菜单类型///////////////////////////
    ////////////////////////////////////////////////////
    /**
     * 菜单类型-菜单
     */
    public static final int MENU_TYPE_MENU = 1;

    /**
     * 菜单类型-功能
     */
    public static final int MENU_TYPE_FUNCTION = 2;

    /**
     * 菜单类型-链接
     */
    public static final int MENU_TYPE_LINKED = 3;


    ////////////////////////////////////////////////////
    ////////////////////到账时间///////////////////////////
    ////////////////////////////////////////////////////
    /**
     * 到账时间 T+1
     */
    public static final int USER_TRANFER_TYPE_T1 = 2;

    /**
     * 到账时间 T+0
     */
    public static final int USER_TRANFER_TYPE_T0 = 1;

    ////////////////////////////////////////////////////
    ////////////////////订单来源///////////////////////////
    ////////////////////////////////////////////////////
    /**
     * 订单来源 POS
     */
    public static final int ORDER_SOURCE_POS = 1;

    /**
     * 订单来源  线下（手动）
     */
    public static final int ORDER_SOURCE_OFFLINE = 2;

    ////////////////////////////////////////////////////
    ////////////////////订单状态///////////////////////////
    ////////////////////////////////////////////////////
    /**
     * 订单状态 待处理
     */
    public static final int ORDER_STATE_WAITING = 1;

    /**
     * 订单状态  处理中
     */
    public static final int ORDER_STATE_DEALING = 2;

    /**
     * 订单状态 处理失败
     */
    public static final int ORDER_STATE_FIAL = 3;

    /**
     * 订单状态 处理成功
     */
    public static final int ORDER_STATE_SUCCESS = 4;

    ////////////////////////////////////////////////////
    ////////////////////订单类型///////////////////////////
    ////////////////////////////////////////////////////

    /**
     * 订单类型  普通订单
     */
    public static final int ORDER_TYPE_COMMON = 1;

    /**
     * 订单类型 返点订单
     */
    public static final int ORDER_TYPE_RETURN = 2;


    ////////////////////////////////////////////////////
    ////////////////////重置开关///////////////////////////
    ////////////////////////////////////////////////////

    /**
     * 充值开关:开启
     */
    public static final int RECHARGE_OPEN_ON = 1;

    /**
     * 充值开关:关闭
     */
    public static final int RECHARGE_OPEN_OFF = 2;

}
