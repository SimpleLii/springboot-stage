package com.simplelii.app.common.dao.sql;

import lombok.Data;

/**
 * @author SimpleLii
 * @description 单表多条件查询封装对象
 * @date 11:07 2020/6/24
 */
@Data
public class SqlCondition {

    // 比较符
    private Operator operator;

    // 条件对应字段
    private String property;

    // 对应字段的值
    private Object value;

    // 枚举定义,where比较符
    public enum Operator {
        eq,
        ne,
        gt,
        lt,
        ge,
        le,
        like,
        in,
        isNull,
        isNotNull;

        public static Operator fromString(String value) {
            return valueOf(value.toLowerCase());
        }
    }

    public SqlCondition(String property, Operator operator, Object value) {
        this.property = property;
        this.operator = operator;
        this.value = value;
    }

    public static SqlCondition eq(String property, Object value) {
        return new SqlCondition(property, Operator.eq, value);
    }

    public static SqlCondition ne(String property, Object value) {
        return new SqlCondition(property, Operator.ne, value);
    }

    public static SqlCondition gt(String property, Object value) {
        return new SqlCondition(property, Operator.gt, value);
    }

    public static SqlCondition lt(String property, Object value) {
        return new SqlCondition(property, Operator.lt, value);
    }

    public static SqlCondition ge(String property, Object value) {
        return new SqlCondition(property, Operator.ge, value);
    }

    public static SqlCondition le(String property, Object value) {
        return new SqlCondition(property, Operator.le, value);
    }

    public static SqlCondition like(String property, Object value) {
        return new SqlCondition(property, Operator.like, value);
    }

    public static SqlCondition in(String property, Object value) {
        return new SqlCondition(property, Operator.in, value);
    }

    public static SqlCondition isNull(String property, Object value) {
        return new SqlCondition(property, Operator.isNull, value);
    }

    public static SqlCondition isNotNull(String property, Object value) {
        return new SqlCondition(property, Operator.isNotNull, value);
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}


;


