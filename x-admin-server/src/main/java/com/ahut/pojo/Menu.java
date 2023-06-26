package com.ahut.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 
 * @TableName x_menu
 */
@TableName(value ="x_menu")
@Data
public class Menu implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer menuId;
    private String component;
    private String path;
    private String redirect;
    private String name;
    private String title;
    private String icon;
    private Integer parentId;
    private String isLeaf;
    private Integer hidden;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Menu> children;

    @TableField(exist = false)
    private Map<String,Object> meta;

    public  Map<String,Object> getMeta(){
        meta = new HashMap<>();
        meta.put("title",title);
        meta.put("icon",icon);
        return meta;
    }
}