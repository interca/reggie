package com.it.controller;

import com.it.entity.ShoppingCart;
import com.it.entity.User;
import com.it.service.ShoppingCartService;
import com.it.utli.SystemJsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 购物车
 * @since  2022 - 10 -20
 * @version  1.0
 * @author  hyj
 */
@RestController
@RequestMapping("/shoppingCart")
public class shoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;



    /**
     * 返回购物车所有信息
     * @return
     */
    @GetMapping("/list")
    public SystemJsonResponse getList(HttpServletRequest request){
        long id= (long ) request.getSession().getAttribute("user");
        return shoppingCartService.getList(id);
    }

    /**
     * 添加购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public  SystemJsonResponse add(@RequestBody ShoppingCart shoppingCart, HttpServletRequest request){
        long id= (long ) request.getSession().getAttribute("user");
        shoppingCart.setUserId(id);
        return shoppingCartService.add(shoppingCart);
    }
}
