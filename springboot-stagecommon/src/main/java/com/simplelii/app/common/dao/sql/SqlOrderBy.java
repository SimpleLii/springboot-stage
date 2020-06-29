package com.simplelii.app.common.dao.sql;

import lombok.Data;

/**
 * @author SimpleLii
 * @description sql查询排序条件封装
 * @date 11:07 2020/6/24
 */
@Data
public class SqlOrderBy {

    // 定义排序枚举
    public enum OrderBy {
        DESC(" DESC"),
        ASC(" ASC");

        OrderBy(String sqlOrderBy) {
            this.sqlOrderBy = sqlOrderBy;
        }

        public String getSqlOrderBy() {
            return sqlOrderBy;
        }

        public void setSqlOrderBy(String sqlOrderBy) {
            this.sqlOrderBy = sqlOrderBy;
        }

        private String sqlOrderBy;

        public static OrderBy fromString(String value) {
            return valueOf(value.toLowerCase());
        }

    }


    private String property;


    private OrderBy order;

    public SqlOrderBy(String property, OrderBy order) {
        this.property = property;
        this.order = order;
    }


    public static SqlOrderBy DESC(String property) {
        return new SqlOrderBy(property, OrderBy.DESC);
    }

    public static SqlOrderBy ASC(String property) {
        return new SqlOrderBy(property, OrderBy.ASC);
    }
}
