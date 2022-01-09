package com.example.demo.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Book;
import com.example.demo.mapper.BookMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {
    @Resource
    BookMapper bookMapper;


    @PostMapping
    public Result<?> save (@RequestBody Book book){

        bookMapper.insert(book);
        return Result.success();
    }

    @PutMapping
    public Result<?> update (@RequestBody Book book){
        bookMapper.updateById(book);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete (@PathVariable Integer id){
        bookMapper.deleteById(id);
        return Result.success();
    }

    @PostMapping("/deleteBatch")
    public Result<?> deleteBatch (@RequestBody List<Integer> ids){
        bookMapper.deleteBatchIds(ids);
        return Result.success();
    }


    @GetMapping
    public Result<?> findPage (@RequestParam(defaultValue = "1") Integer pageNum,
                               @RequestParam(defaultValue = "10") Integer pageSize,
                               @RequestParam(defaultValue = "")String searchWord){
        //用于识别null不然为昵称为null的用户查不出来
        LambdaQueryWrapper<Book> wrapper = Wrappers.<Book>lambdaQuery();

        if (StrUtil.isNotBlank(searchWord)){
            wrapper.like(Book::getName,searchWord);
        }

        Page<Book> bookPage = bookMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(bookPage);
    }
}
