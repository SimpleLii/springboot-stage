package com.simplelii.app.common.dao.table;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author SimpleLii
 * @description 表信息
 * @date 14:12 2020/6/24
 */
@Data
public class TableInfo implements Serializable {

    private static final long serialVersionUID = 7988187026023138738L;
    // 表名
    private String tableName;
    // id字段名
    private String idColumn;
    // 字段集合(包含id)
    private List<ColumnInfo> columns;
    // 默认字段集合
    private List<ColumnInfo> baseColumns;

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setIdColumn(String idColumn) {
        this.idColumn = idColumn;
    }

    public void setColumns(List<ColumnInfo> columns) {
        this.columns = columns;
    }

    public void setBaseColumns(List<ColumnInfo> baseColumns) {
        this.baseColumns = baseColumns;
    }


    public ColumnInfo getColumnInfo(String property) {
        if ((this.columns == null) && (this.baseColumns == null)) {
            return null;
        }
        if (this.columns != null) {
            for (ColumnInfo column : this.columns) {
                if ((column.getColumn().equals(property)) || (column.getProperty().equals(property))) {
                    return column;
                }
            }
        }
        if (this.baseColumns != null) {
            for (ColumnInfo column : this.baseColumns) {
                if ((column.getColumn().equals(property)) || (column.getProperty().equals(property))) {
                    return column;
                }
            }
        }
        return null;
    }
}
