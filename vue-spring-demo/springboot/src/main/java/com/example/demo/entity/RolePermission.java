package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("rolePermission")
public class RolePermission  implements Serializable {
    private Integer roleId;
    private  Integer permissionId;
}
