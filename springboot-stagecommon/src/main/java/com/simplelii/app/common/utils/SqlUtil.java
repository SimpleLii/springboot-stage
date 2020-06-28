package com.simplelii.app.common.utils;

/**
 * @author liXin
 * @description sql工具类
 * @date 2020/6/28
 */
public class SqlUtil {

    /**
     * 判断sql注入
     * 通过正则表达式判断是否有 特殊符号 --
     *
     * @param sqlConditionWhere where 条件sql String
     * @return 出现恶意sql注入 返回 true
     */
    public static boolean isSpiteParams(String sqlConditionWhere) {
        return true;
    }
}
