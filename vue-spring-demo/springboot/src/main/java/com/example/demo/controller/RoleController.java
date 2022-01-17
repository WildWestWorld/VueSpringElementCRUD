package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Role;
import com.example.demo.entity.RolePermission;
import com.example.demo.entity.User;
import com.example.demo.entity.UserRole;
import com.example.demo.mapper.PermissionMapper;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/role")
public class RoleController  {

    @Resource
    RoleMapper RoleMapper;

    @Resource
    PermissionMapper permissionMapper;

    @PostMapping
    public Result<?> save(@RequestBody Role Role) {
        RoleMapper.insert(Role);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Role Role) {
        RoleMapper.updateById(Role);
        return Result.success();
    }

    // 改变权限接口
    @PutMapping("/changePermission")
    public Result<?> changePermission(@RequestBody Role role) {
        // 1.先根据角色id删除所有的角色跟权限的绑定关系

        //在role_permission表中 根据role的id 删除对应的permission的id
        permissionMapper.deletePermissionsByRoleId(role.getId());

        // 2.再新增 新的绑定关系
       //role.getPermissions()是前端传过来的角色对应的资源路径，然后进行for循环
        //role.getPermissions()得到的是PermissionID的集合，
        //role.getId(),是固定的，一直是传过来role的id
        for (Integer permissionId : role.getPermissions()) {
            permissionMapper.insertRoleAndPermission(role.getId(), permissionId);
        }

        // 判断当前登录的用户角色是否包含了当前操作行的角色id，如果包含，则返回true，需要重新登录。
        //获得当前登陆的user信息
        User user = TokenUtils.getUser();
        //利用user的id获取对应的roleID，使用的表是user_role,用户可以有多个角色，所以是返回的是List
        List<UserRole> userRoles = RoleMapper.getRoleIdByUserId(user.getId());
        //当前用户的中的RoleID中有任何一个和前端传过来的role里面的ID一样
        //也就是更改的是当前用户相关的角色， 返回true前端有个if判断，如果res.data=true就会跳回到login界面
        if (userRoles.stream().anyMatch(item -> item.getRoleId().equals(role.getId()))) {
            return Result.success(true);
        }
//        如果不包含，则返回false，不需要重新登录。
        return Result.success(false);
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        RoleMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        return Result.success(RoleMapper.selectById(id));
    }

    @GetMapping("/all")
    public Result<?> all() {
        return Result.success(RoleMapper.selectList(null));
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<Role> wrapper = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(Role::getName, search);
        }


        Page<Role> RolePage = RoleMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);

        //查询当前角色拥有的访问权限
        //获得页面的分类的结果 数据来源于role表
        List<Role> records = RolePage.getRecords();
        // 给角色设置绑定的权限id数组
        for (Role record : records) {
            //获取当前role表每条数据的id，也就是roleId
            Integer roleId = record.getId();
            //getPermissionByRoleId(roleId)，根据roleId获取可用的PermissionId，数据来自role_permission表
            List<RolePermission> permissionId = permissionMapper.getPermissionByRoleId(roleId);
            //遍历permissionId 数列，获取里面的id
            //把遍历的值变成一个数组collect(Collectors.toList())
            //Collectors.toList()=用来结束Stream流
            //这里的目的是把泛型从RolePermission变成Integer类型
            List<Integer> permissions = permissionId.stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
            //在结果中放入这个数列
            record.setPermissions(permissions);
        }
        return Result.success(RolePage);
    }
}
