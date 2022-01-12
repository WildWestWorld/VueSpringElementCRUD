package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;


@Data
@TableName("message")
public class Message extends Model<Message> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
      * 内容
      */
    private String content;

    /**
      * 评论人
      */
    private String username;

    /**
      * 评论时间
      */
    private String time;

    /**
      * 父ID
      */
    private Integer parentId;
    /**
     * 关联id
     */
    private Integer foreignId;

    @TableField(exist = false)
    private Message parentMessage;


    @TableField(exist = false)
    private String avatar;


}
