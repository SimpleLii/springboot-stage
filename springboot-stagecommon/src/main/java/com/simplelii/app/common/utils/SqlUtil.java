package com.simplelii.app.common.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author liXin
 * @description sql工具类
 * @date 2020/6/28
 */
public class SqlUtil {
    // 参数异常判定
    private static String[] badParamArray = new String[]{"drop", "delete", "update", "truncate", "table", "--"};
    private static String[] badSqlArray = new String[]{"drop", "delete", "update", "truncate", "--"};

    public static boolean isSpiteSql(String sql) {
        return sqlValidate(sql, badSqlArray);
    }

    /**
     * 判断sql注入
     *
     * @param sqlParams where 条件sql String
     * @return 出现恶意sql注入 返回 true
     */
    public static boolean isSpiteParams(String sqlParams) {
        return sqlValidate(sqlParams, badParamArray);
    }

    private static boolean sqlValidate(String sqlParams, String[] badStrArray) {
        if (StringUtils.isNotEmpty(sqlParams)) {
            String sqlLowerCase = sqlParams.toLowerCase();
            for (String badString : badStrArray) {
                if (sqlLowerCase.contains(badString)) {
                    return false;
                }
            }
        }
        return true;
    }

}
