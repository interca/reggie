package com.it.service;

import com.it.entity.ShoppingCart;
import com.it.utli.SystemJsonResponse;

public interface ShoppingCartService {

    /**
     * 添加购物车
     * @param shoppingCart
     * @return
     */
     SystemJsonResponse add(ShoppingCart shoppingCart);

    /**
     * 获取购物车信息
     * @return
     */
    SystemJsonResponse getList(Long id);
}
