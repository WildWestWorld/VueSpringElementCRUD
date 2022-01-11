package com.example.demo.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Book;
import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import com.example.demo.mapper.BookMapper;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@RestController
@RequestMapping("/order")
public class OrderController  {

    @Resource
    OrderMapper OrderMapper;

    @Resource
    BookMapper bookMapper;

    @PostMapping
    public Result<?> save(@RequestBody Order Order) {
        OrderMapper.insert(Order);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Order Order) {
        OrderMapper.updateById(Order);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        OrderMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        return Result.success(OrderMapper.selectById(id));
    }


    //购买书的接口
    @GetMapping("/buy/{bookId}")
    public Result<?> buy(@PathVariable Long bookId) {

        //IdUtil.getSnowflake() =调用HUtool中的IDUtil中的雪花算法
        //雪花算法:64位的二进制
        //1位，不用最高位固定是0
        //2-42 41位，用来记录时间戳（毫秒）。
        //43-52 10位，用来记录工作机器id。
        //53-64 12位，序列号，用来记录同毫秒内产生的不同id。
        //默认第一位0 + 时间戳 + 机器码 + 12位的随机ID   SnowFlake可以保证： 所有生成的id按时间趋势递增 整个分布式系统内不会产生重复id

        //IdUtil.getSnowflake().nextIdStr =把雪花算法输出的ID变成字符串
        String orderNo = IdUtil.getSnowflake().nextIdStr();

        //获取当前用户的信息
        User user = TokenUtils.getUser();
        //查询传入ID的Book信息，也就是要购买的书的信息
        Book book = bookMapper.selectById(bookId);

        //封装一个新的订单 Order
        Order order = new Order();
        //设置订单的编号为我们刚刚设置的雪花算法生成的ID
        order.setOrderNo(orderNo);
        //名字设置为购买的书的名字
        order.setName(book.getName());
        //总价设置为购买的书的价格
        order.setTotalPrice(book.getPrice());
        //支付金额设置为购买的书的价格
        order.setPayPrice(book.getPrice());
        //运费设置为 0
        order.setTransportPrice(BigDecimal.ZERO);
        //运费设置为 0
        order.setDiscount(BigDecimal.ZERO);
        //设置订单的UserID为当前的UserId
        order.setUserId(user.getId());
        //设置订单的Username为当前的Username
        order.setUsername(user.getUsername());
        //设置订单的创建时间
        order.setCreateTime(DateUtil.offsetSecond(new Date(),0));
        //设置订单的状态
        order.setState(0);

        //调用save方法将我们封装好的Order上传到数据库
        save(order);

        //支付链接
        String payUrl = "http://localhost:9090/alipay/pay?subject=" + book.getName() + "&traceNo=" + orderNo + "&totalAmount=" + book.getPrice();

        // 新建订单，扣减库存
        return Result.success(payUrl);
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(Order::getOrderNo, search);
        }
        Page<Order> OrderPage = OrderMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(OrderPage);
    }

}
