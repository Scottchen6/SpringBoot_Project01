package com.ahut.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName x_role_menu
 */
@TableName(value ="x_role_menu")
@Data
public class RoleMenu implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private Integer roleId;

    /**
     * 
     */
    private Integer menuId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}