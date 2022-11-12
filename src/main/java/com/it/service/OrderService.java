package com.it.service;

import com.it.entity.Orders;

public interface OrderService {
    /**
     * 用户下单
     * @param orders
     * @param id
     */
    void submit(Orders orders, long id);
}
