package com.it.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.it.Exception.CustomException;
import com.it.entity.*;
import com.it.mapper.*;
import com.it.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.ParameterResolutionDelegate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;
    /**
     * 用户下单
     * @param orders
     * @param id
     */
    @Override
    public void submit(Orders orders, long id) {
      //查询用户购物车数据
        LambdaQueryWrapper<ShoppingCart> lq = new LambdaQueryWrapper<>();
        lq.eq(ShoppingCart::getUserId,id);
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.selectList(lq);
        if(shoppingCarts == null || shoppingCarts.size() == 0)throw  new CustomException("购物车为空");
        //查询用户和地址数据
        User user = userMapper.selectById(id);
        AddressBook addressBook = addressBookMapper.selectById(orders.getAddressBookId());
        //计算金额
        AtomicInteger atomicInteger =new AtomicInteger(0);
        List<OrderDetail>list =new ArrayList<>();
        //生成订单号
        long orderId = IdWorker.getId();
        for(ShoppingCart s:shoppingCarts){
            atomicInteger.addAndGet(s.getAmount().multiply((new BigDecimal(s.getNumber()))).intValue());
            OrderDetail orderDetail  = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(s.getNumber());
            orderDetail.setDishFlavor(s.getDishFlavor());
            orderDetail.setDishId(s.getDishId());
            orderDetail.setSetmealId(s.getSetmealId());
            orderDetail.setName(s.getName());
            orderDetail.setImage(s.getImage());
            orderDetail.setNumber(s.getNumber());
            orderDetail.setAmount(s.getAmount());
            orderDetailMapper.insert(orderDetail);
        }

        orders.setNumber(String.valueOf(orderId));
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(atomicInteger.get()));
        orders.setUserId(id);
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(user.getPhone());
        String add = addressBook.getProvinceName()==null?"":addressBook.getProvinceName()+
                     addressBook.getCityName() == null?"":addressBook.getCityName()+
                     addressBook.getDistrictName()==null?"":addressBook.getDistrictName()+
                     addressBook.getDetail()==null?"" : addressBook.getDetail();
        orders.setAddress(add);
        //向订单表插入数据
        orderMapper.insert(orders);
        //向订单明细表插入数据
        //清空购物车
        shoppingCartMapper.delete(lq);
    }
}
