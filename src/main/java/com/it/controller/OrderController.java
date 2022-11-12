package com.it.controller;

import com.it.entity.Orders;
import com.it.service.OrderService;
import com.it.utli.SystemJsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 订单业务
 */
@RequestMapping("/order")
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public SystemJsonResponse submit(@RequestBody Orders orders, HttpServletRequest request){
        long id= (long ) request.getSession().getAttribute("user");
        orderService.submit(orders,id);
      return SystemJsonResponse.success("下单成功");
    }
}
