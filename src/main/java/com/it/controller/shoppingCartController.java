package com.it.controller;

import com.it.utli.SystemJsonResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 购物车
 * @since  2022 - 10 -20
 * @version  1.0
 * @author  hyj
 */
@RestController
@RequestMapping("/shoppingCart")
public class shoppingCartController {

    @GetMapping("/list")
    public SystemJsonResponse getList(){
          return null;
    }
}
