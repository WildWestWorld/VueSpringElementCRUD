package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("userRole")
public class UserRole {
    private Integer userId;
    private Integer roleId;
}
