package com.it.service;

import com.it.entity.Category;
import com.it.utli.SystemJsonResponse;
import org.springframework.stereotype.Service;


public interface CategoryService {
     SystemJsonResponse getPage(int page, int pageSize, String name) ;


    boolean save(Category category);
}
