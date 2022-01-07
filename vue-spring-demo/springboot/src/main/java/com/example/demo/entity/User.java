package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sun.javafx.beans.IDProperty;
import lombok.Data;

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
    private String role;





}
