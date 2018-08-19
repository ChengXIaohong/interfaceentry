package com.interfaceentry.interfaceentry.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 基类
 *
 * @author chengxiaohong coax@outlook.it
 * @create 2018-08-16 17:48
 **/

@MappedSuperclass
@Data
public class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建时间
     */
    private Long createAt;

    /**
     * 创建用户
     */
    private Long createBy;
    /**
     * 修改时间
     */
    private Long updateAt;
    /**
     * 修改用户
     */
    private Long updateBy;

    /**
     * 版本号
     */
    @Version
    private Long version;
}
