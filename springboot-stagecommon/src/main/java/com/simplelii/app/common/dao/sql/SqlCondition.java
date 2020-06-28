package com.simplelii.app.common.dao.sql;

import lombok.Data;

/**
 * @author SimpleLii
 * @description 单表多条件查询封装对象
 * @date 11:07 2020/6/24
 */
@Data
public class SqlCondition {


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

        public SqlCondition setProperty(String property, Object value) {
            SqlCondition sqlCondition = new SqlCondition();
            sqlCondition.setValue(value);
            sqlCondition.setProperty(property);
            sqlCondition.setOperator(this);
            return sqlCondition;
        }
    }


    private Operator operator;

    // 条件对应字段
    private String property;
    // 对应字段的值
    private Object value;

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


