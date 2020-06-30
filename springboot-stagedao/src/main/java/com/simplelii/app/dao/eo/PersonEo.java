package com.simplelii.app.dao.eo;

import com.simplelii.app.common.dao.base.BaseEo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author liXin
 * @description
 * @date 2020/6/10
 */
@Data
@Table(name = "person")
@Entity
public class PersonEo extends BaseEo {


    private Long id;
    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

}
