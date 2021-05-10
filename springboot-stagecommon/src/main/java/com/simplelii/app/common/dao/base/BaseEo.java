package com.simplelii.app.common.dao.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.simplelii.app.common.dao.sql.SqlCondition;
import com.simplelii.app.common.dao.sql.SqlOrderBy;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * @author liXin
 * @description mysql 表基础eo
 * @date 2020/6/10
 */
@Data
public class BaseEo {

    private static final long serialVersionUID = 1906675943768391743L;

    @Id
    protected Long id;

    @Column(name = "create_person")
    protected String createPerson;

    @Column(name = "create_time")
    protected Date createTime;

    @Column(name = "update_person")
    protected String updatePerson;

    @Column(name = "update_time")
    protected Date updateTime;

    @Column(name = "dr")
    protected Integer dr = 0;

    @JsonIgnore
    @Transient
    // 多个字段使用逗号分隔
    protected transient String orderByDesc;

    @JsonIgnore
    @Transient
    // 多个字段使用逗号分隔
    protected transient String orderBy;

    @JsonIgnore
    @Transient
    // 条件顺序与list顺序一致
    protected transient List<SqlOrderBy> sqlOrderBys;

    @JsonIgnore
    @Transient
    // where 条件封装对象 ，条件顺序与list顺序一致
    protected transient List<SqlCondition> sqlConditions;


}