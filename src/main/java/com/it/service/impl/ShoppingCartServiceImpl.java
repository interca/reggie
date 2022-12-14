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
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    /**
     * 添加购物车
     * @param shoppingCart
     * @return
     */
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

    /**
     * 查询全部购物车
     * @param id
     * @return
     */
    @Override
    public SystemJsonResponse getList(Long id) {
        LambdaQueryWrapper<ShoppingCart> lq = new LambdaQueryWrapper<>();
        lq.eq(ShoppingCart::getUserId,id);
        lq.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.selectList(lq);
        return SystemJsonResponse.success(shoppingCarts);
    }

    /**
     * 减少购物车
     * @param shoppingCart
     * @return
     */
    @Override
    public SystemJsonResponse sub(ShoppingCart shoppingCart) {
        shoppingCart.setCreateTime(LocalDateTime.now());
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        LambdaQueryWrapper<ShoppingCart> lq = new LambdaQueryWrapper<>();
        lq.eq(ShoppingCart::getUserId,shoppingCart.getUserId());
        if(dishId != null){
            lq.eq(ShoppingCart::getDishId,dishId);
        }else {
            lq.eq(ShoppingCart::getSetmealId,setmealId);
        }
        ShoppingCart cart = shoppingCartMapper.selectOne(lq);
        if(cart.getNumber() == 1){
            shoppingCartMapper.delete(lq);
        }else {
            cart.setNumber(cart.getNumber() - 1);
            shoppingCartMapper.update(cart,lq);
            shoppingCart = cart;
        }
        return SystemJsonResponse.success(shoppingCart);
    }

}
