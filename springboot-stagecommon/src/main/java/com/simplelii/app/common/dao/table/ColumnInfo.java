package com.simplelii.app.common.dao.table;

import java.io.Serializable;

/**
 * @author SimpleLii
 * @description 表字段信息
 * @date 14:14 2020/6/24
 */
public class ColumnInfo implements Serializable {

    private static final long serialVersionUID = 8550904383009293852L;
    // 表字段名
    private String column;

    // 映射eo字段（驼峰）
    private String property;

    // 是否分片
    private boolean isShardColumn;

    // 映射eo字段类型
    private Class<?> propertyClass;

    public String getColumn() {
        return this.column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getProperty() {
        return this.property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public boolean isShardColumn() {
        return this.isShardColumn;
    }

    public void setShardColumn(boolean isShardColumn) {
        this.isShardColumn = isShardColumn;
    }

    public Class<?> getPropertyClass() {
        return this.propertyClass;
    }

    public void setPropertyClass(Class<?> propertyClass) {
        this.propertyClass = propertyClass;
    }
}
