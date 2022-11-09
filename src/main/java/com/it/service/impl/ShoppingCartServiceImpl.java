package com.it.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.it.entity.ShoppingCart;
import com.it.mapper.ShoppingCartMapper;
import com.it.service.ShoppingCartService;
import com.it.utli.GlobalResponseCode;
import com.it.utli.SystemJsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Override
    public SystemJsonResponse add(ShoppingCart shoppingCart) {
        //查询菜品或者套餐是否在数据库 是的话就数量加一
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        LambdaQueryWrapper<ShoppingCart>lq   = new LambdaQueryWrapper<>();
        ShoppingCart cart = null;
        lq.eq(ShoppingCart::getUserId,shoppingCart.getUserId());
        if(dishId != null){
            //添加到购物车是菜品
            lq.eq(ShoppingCart::getDishId,dishId);
            //如果有这个数据  就数量加一
        }else {
           //否则是套餐
            lq.eq(ShoppingCart::getSetmealId,setmealId);
        }
        cart = shoppingCartMapper.selectOne(lq);
        if(cart != null){
            cart.setCreateTime(LocalDateTime.now());
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.update(cart,lq);
        }else {
            //否则就新增
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
            cart = shoppingCart;
        }
        return SystemJsonResponse.success(cart);
    }
}
