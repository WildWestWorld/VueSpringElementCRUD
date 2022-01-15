package com.example.demo.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;

import com.example.demo.entity.*;
import com.example.demo.mapper.PermissionMapper;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.mapper.UserMapper;

import com.example.demo.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserMapper userMapper;

    @Resource
    RoleMapper roleMapper;

    @Resource
    PermissionMapper permissionMapper;

    @PostMapping("/login")
    public Result<?> login (@RequestBody User user){
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername())   .eq(User::getPassword, user.getPassword()));
        if (res == null){
            return Result.error("-1","用户名或密码错误");
        }
        //通过userId查询用户的角色信息
        Integer userId =res.getId();
        List<UserRole> roleIds = roleMapper.getRoleIdByUserId(userId);

        HashSet<Permission> permissionSet = new HashSet<>();


        //为什么使用循环因为用户是可以有多个角色的
        for (UserRole roleId : roleIds) {

            //根据角色id查询所有的可访问的资源/路径的id
            List<RolePermission> RolePermissions = permissionMapper.getPermissionByRoleId(roleId.getRoleId());
            //查询所有可以访问的资源/路径的id
            for (RolePermission RolePermission : RolePermissions) {
                Integer permissionId = RolePermission.getPermissionId();
                Permission permission = permissionMapper.selectById(permissionId);
                permissionSet.add(permission);
            }
        }

        res.setPermissions(permissionSet);



        String token = TokenUtils.getToken(res);
        res.setToken(token);
//        User userInfo = TokenUtils.getUser();
//        System.out.println(userInfo);
//
//        System.out.println(token);
        System.out.println(res);
        return Result.success(res);
    }

    @PostMapping("/register")
    public Result<?> register (@RequestBody User user){
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()));
        if (res != null){
            return Result.error("-1","用户名重复");
        }
        if(user.getPassword()==null){
            user.setPassword("123456");
        }
        userMapper.insert(user);
        return Result.success();
    }

    @PostMapping
    public Result<?> save (@RequestBody User user){
        if(user.getPassword()==null){
            user.setPassword("123456");
        }
        userMapper.insert(user);
        return Result.success();
    }

    @PutMapping
    public Result<?> update (@RequestBody User user){
        userMapper.updateById(user);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete (@PathVariable Integer id){
        userMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> findById (@PathVariable Integer id){
        User res = userMapper.selectById(id);
        if (res == null){
            return Result.error("-1","该ID不存在");
        }

        User userInfo = TokenUtils.getUser();
        System.out.println(userInfo);

        return Result.success(res);
    }


    /**
     * 统计数据
     *
     * @return
     */
    @GetMapping("/count")
    public Result<?> count() {
//        User user = getUser(); // 当前登录的用户信息
        return Result.success(userMapper.countAddress());
    }


    @GetMapping
    public Result<?> findPage (@RequestParam(defaultValue = "1") Integer pageNum,
                               @RequestParam(defaultValue = "10") Integer pageSize,
                               @RequestParam(defaultValue = "")String searchWord){
        //用于识别null不然为昵称为null的用户查不出来
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();

        if (StrUtil.isNotBlank(searchWord)){
            wrapper.like(User::getNikeName,searchWord);
        }

        Page<User> userPage = userMapper.findPage(new Page<>(pageNum, pageSize), searchWord);
        return Result.success(userPage);
    }
}
