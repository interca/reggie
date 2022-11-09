package com.it.service;

import com.it.entity.ShoppingCart;
import com.it.utli.SystemJsonResponse;

public interface ShoppingCartService {
     SystemJsonResponse add(ShoppingCart shoppingCart);

}
