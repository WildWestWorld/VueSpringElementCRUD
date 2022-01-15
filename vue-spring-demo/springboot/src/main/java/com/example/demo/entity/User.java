package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sun.javafx.beans.IDProperty;
import lombok.Data;

import java.util.List;
import java.util.Set;

@TableName("user")
@Data
public class User {
    @TableId (type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private String nikeName;
    private Integer age;
    private String sex;
    private String address;

    private String avatar;

    @TableField(exist = false)
    private List<Book> bookList;

    @TableField(exist = false)
    private String token;

    @TableField(exist = false)
    private Set<Permission> permissions;


}
